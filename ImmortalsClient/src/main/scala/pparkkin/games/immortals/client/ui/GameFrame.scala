package pparkkin.games.immortals.client.ui

import swing.Frame
import akka.actor._
import pparkkin.games.immortals.client.controller.Quit
import scala.swing.event.WindowClosing

case class Joined(game: String)
case class Display(board: Array[Array[Boolean]])

class GameFrame(controller: ActorRef) extends Actor with ActorLogging {
  val panel = new BoardPanel
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

  var board: Option[Array[Array[Boolean]]] = None

  def receive = {
    case Joined(game) =>
      frame.title = game
    case Display(board) =>
      panel.updateBoard(board)
  }
}

object GameFrame {
  def open(factory: ActorRefFactory, controller: ActorRef): ActorRef = {
    factory.actorOf(Props(new GameFrame(controller)), "GameFrame")
  }
}
