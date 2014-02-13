package pparkkin.games.immortals.client.controller

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import akka.actor._
import pparkkin.games.immortals.client.ui.GameFrame
import pparkkin.games.immortals.client.tcp.TCPConnection
import java.net.InetSocketAddress
import pparkkin.games.immortals.messages._
import pparkkin.games.immortals.messages.Welcome
import pparkkin.games.immortals.messages.Update
import pparkkin.games.immortals.client.ui.DisplayBoard
import pparkkin.games.immortals.messages.Join
import pparkkin.games.immortals.messages.PlayerPositions
import pparkkin.games.immortals.client.ui.DisplayPlayers
import pparkkin.games.immortals.client.tcp.ConnectionReady
import scala.Some
import scala.util.Random

case class MoveDown()
case class MoveLeft()
case class MoveRight()
case class MoveUp()

class GameController(address: InetSocketAddress, player: String) extends Actor with ActorLogging {
  var frame: Option[ActorRef] = None
  val server = TCPConnection.newClient(context, address, self)

  def receive = {
    case ConnectionReady =>
      log.debug("Connection ready.")
      server ! Join(player)
    case Welcome(player, game, board) =>
      log.debug(s"Welcome received from $game for $player.")
      frame match {
        case None =>
          frame = Some(GameFrame.open(context, self, board))
          server ! WelcomeAck(player)
        case Some(f) => // noop
      }
    case Update(board) =>
      log.debug("Received updated board.")
      frame.map(_ ! DisplayBoard(board))
    case PlayerPositions(players) =>
      log.debug("Received updated player positions.")
      frame.map(_ ! DisplayPlayers(players))
    case MoveDown =>
      server ! Move(player, downMove)
    case MoveLeft =>
      server ! Move(player, leftMove)
    case MoveRight =>
      server ! Move(player, rightMove)
    case MoveUp =>
      server ! Move(player, upMove)
    case m =>
      log.info(s"Unknown message $m.")
  }

  val rightMove = (0, 1)
  val leftMove = (0, -1)
  val upMove = (-1, 0)
  val downMove = (1, 0)

}

object GameController {
  def newInstance(factory: ActorRefFactory, address: InetSocketAddress, player: String): ActorRef = {
    factory.actorOf(Props(new GameController(address, player)), "GameController")
  }
}
