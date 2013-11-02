package pparkkin.games.immortals.client.controller

import akka.actor._
import pparkkin.games.immortals.client.ui.{Display, Joined, GameFrame}
import pparkkin.games.immortals.tcp.{ConnectionReady, TCPConnection}
import java.net.InetSocketAddress
import pparkkin.games.immortals.messages.{Update, Welcome, Join}

case class Quit()

class GameController(address: InetSocketAddress, player: String) extends Actor with ActorLogging {
  val frame = GameFrame.open(context, self)
  val server = TCPConnection.newClient(context, address, self)

  def receive = {
    case ConnectionReady =>
      log.debug("Connection ready.")
      server ! Join(player)
    case Welcome(player, game) =>
      log.debug(s"Welcome received from $game for $player.")
      frame ! Joined(game)
    case Update(board) =>
      log.debug("Received updated board.")
      frame ! Display(board)
    case m =>
      log.info(s"Unknown message $m.")
  }
}

object GameController {
  def newInstance(factory: ActorRefFactory, address: InetSocketAddress, player: String): ActorRef = {
    factory.actorOf(Props(new GameController(address, player)), "GameController")
  }
}
