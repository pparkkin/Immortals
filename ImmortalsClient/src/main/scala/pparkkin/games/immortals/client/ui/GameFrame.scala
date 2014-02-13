package pparkkin.games.immortals.client.ui

import swing.Frame
import akka.actor._
import scala.swing.event.{Key, WindowClosing}
import pparkkin.games.immortals.datatypes.{Players, Board}
import pparkkin.games.immortals.client.controller.{MoveUp, MoveDown, MoveRight, MoveLeft}

case class DisplayBoard(board: Board)
case class DisplayPlayers(players: Players)
case class KeyPress(key: Key.Value)
case class CloseDisplay()

class GameFrame(controller: ActorRef, board: Board) extends Actor with ActorLogging {
  val panel = new BoardPanel(self, board)
  val frame = new Frame {
    title = "Game"
    contents = panel

    listenTo(this)
    reactions += {
      case WindowClosing(w) =>
        this.dispose()
        controller ! CloseDisplay
    }
  }
  frame.visible = true

  def receive = {
    case DisplayBoard(board) =>
      panel.updateBoard(board)
    case DisplayPlayers(players) =>
      panel.updatePlayers(players)
    case KeyPress(key) =>
      key match {
        case Key.Up =>
          controller ! MoveUp
        case Key.Right =>
          controller ! MoveRight
        case Key.Down =>
          controller ! MoveDown
        case Key.Left =>
          controller ! MoveLeft
      }
  }
}

object GameFrame {
  def open(factory: ActorRefFactory, controller: ActorRef, board: Board): ActorRef = {
    factory.actorOf(Props(new GameFrame(controller, board)), "GameFrame")
  }
}
