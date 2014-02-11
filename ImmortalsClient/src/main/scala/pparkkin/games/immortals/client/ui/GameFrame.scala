package pparkkin.games.immortals.client.ui

import swing.Frame
import akka.actor._
import scala.swing.event.{Key, KeyReleased, WindowClosing}
import pparkkin.games.immortals.datatypes.{Players, Board}
import pparkkin.games.immortals.client.controller.{MoveUp, MoveDown, MoveRight, MoveLeft}

case class DisplayBoard(board: Board)
case class DisplayPlayers(players: Players)
case class CloseDisplay()

class GameFrame(controller: ActorRef, board: Board) extends Actor with ActorLogging {
  val panel = new BoardPanel(board)
  val frame = new Frame {
    title = "Game"
    contents = panel

    listenTo(this)
    reactions += {
      case WindowClosing(w) =>
        this.dispose()
        controller ! CloseDisplay
      case KeyReleased(_, key, _, _) =>
        key match {
          case Key.KpDown =>
            controller ! MoveDown
          case Key.KpLeft =>
            controller ! MoveLeft
          case Key.KpRight =>
            controller ! MoveRight
          case Key.KpUp =>
            controller ! MoveUp
          case k =>
            log.info(s"Unknown key press $k.")
        }
    }
  }
  frame.visible = true

  def receive = {
    case DisplayBoard(board) =>
      panel.updateBoard(board)
    case DisplayPlayers(players) =>
      panel.updatePlayers(players)
  }
}

object GameFrame {
  def open(factory: ActorRefFactory, controller: ActorRef, board: Board): ActorRef = {
    factory.actorOf(Props(new GameFrame(controller, board)), "GameFrame")
  }
}
