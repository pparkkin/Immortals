package pparkkin.games.immortals.client.controller

import akka.actor._
import pparkkin.games.immortals.client.ui.{DisplayBoard, GameFrame}
import pparkkin.games.immortals.tcp.{ConnectionReady, TCPConnection}
import java.net.InetSocketAddress
import pparkkin.games.immortals.messages.{Update, Welcome, Join}

case class Quit()

class GameController(address: InetSocketAddress, player: String) extends Actor with ActorLogging {
  var frame: Option[ActorRef] = None
  val server = TCPConnection.newClient(context, address, self)

  def receive = {
    case ConnectionReady =>
      log.debug("Connection ready.")
      server ! Join(player)
    case Welcome(player, game) =>
      log.debug(s"Welcome received from $game for $player.")
    case Update(board) =>
      log.debug("Received updated board.")
      frame
        .map(_ ! DisplayBoard(board))
        .getOrElse(frame = Some(GameFrame.open(context, self, board)))
    case m =>
      log.info(s"Unknown message $m.")
  }
}

object GameController {
  def newInstance(factory: ActorRefFactory, address: InetSocketAddress, player: String): ActorRef = {
    factory.actorOf(Props(new GameController(address, player)), "GameController")
  }
}
