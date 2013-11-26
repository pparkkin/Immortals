package pparkkin.games.immortals.client.ui

import swing.Frame
import akka.actor._
import pparkkin.games.immortals.client.controller.Quit
import scala.swing.event.WindowClosing
import pparkkin.games.immortals.datatypes.{Players, Board}

case class DisplayBoard(board: Board)
case class DisplayPlayers(players: Players)

class GameFrame(controller: ActorRef, board: Board) extends Actor with ActorLogging {
  val panel = new BoardPanel(board)
  val frame = new Frame {
    title = "Game"
    contents = panel

    listenTo(this)
    reactions += {
      case WindowClosing(w) =>
        this.dispose()
        controller ! Quit
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
