package pparkkin.games.immortals.client.ui

import swing.Frame
import akka.actor._
import pparkkin.games.immortals.client.controller.Quit
import scala.swing.event.WindowClosing

case class DisplayBoard(board: Array[Array[Boolean]])

class GameFrame(controller: ActorRef, board: Array[Array[Boolean]]) extends Actor with ActorLogging {
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
  }
}

object GameFrame {
  def open(factory: ActorRefFactory, controller: ActorRef, board: Array[Array[Boolean]]): ActorRef = {
    factory.actorOf(Props(new GameFrame(controller, board)), "GameFrame")
  }
}
