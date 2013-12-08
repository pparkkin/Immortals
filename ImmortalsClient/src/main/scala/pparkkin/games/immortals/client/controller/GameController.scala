package pparkkin.games.immortals.client.controller

import akka.actor._
import pparkkin.games.immortals.client.ui.{DisplayPlayers, DisplayBoard, GameFrame}
import pparkkin.games.immortals.tcp.{ConnectionReady, TCPConnection}
import java.net.InetSocketAddress
import pparkkin.games.immortals.messages._
import pparkkin.games.immortals.messages.Welcome
import pparkkin.games.immortals.messages.Update
import pparkkin.games.immortals.client.ui.DisplayBoard
import pparkkin.games.immortals.messages.Join
import pparkkin.games.immortals.messages.PlayerPositions
import pparkkin.games.immortals.client.ui.DisplayPlayers
import pparkkin.games.immortals.tcp.ConnectionReady
import scala.Some

case class Quit()

class GameController(address: InetSocketAddress, player: String) extends Actor with ActorLogging {
  var frame: Option[ActorRef] = None
  val server = TCPConnection.newClient(context, address, self)

  def receive = {
    case ConnectionReady =>
      log.debug("Connection ready.")
      server ! Join(player)
    case Welcome(player, game, board) =>
      log.debug(s"Welcome received from $game for $player.")
      frame = Some(GameFrame.open(context, self, board))
      server ! WelcomeAck(player)
    case Update(board) =>
      log.debug("Received updated board.")
      frame.map(_ ! DisplayBoard(board))
    case PlayerPositions(players) =>
      log.debug("Received updated player positions.")
      frame.map(_ ! DisplayPlayers(players))
    case m =>
      log.info(s"Unknown message $m.")
  }
}

object GameController {
  def newInstance(factory: ActorRefFactory, address: InetSocketAddress, player: String): ActorRef = {
    factory.actorOf(Props(new GameController(address, player)), "GameController")
  }
}
