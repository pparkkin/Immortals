package pparkkin.games.immortals.client.ui

import swing.{Label, Frame}
import akka.actor._
import javax.swing.SwingUtilities
import swing.event.{WindowClosing}
import pparkkin.games.immortals.client.controller.Quit
import pparkkin.games.immortals.client.controller.Quit
import scala.swing.event.WindowClosing

case class Joined(game: String)
case class Display(board: Array[Array[Boolean]])

class GameFrame(controller: ActorRef) extends Actor with ActorLogging {
  val frame = new Frame {
    title = "Game"
    contents = new Label {
      text = "This is the game."
    }

    listenTo(this)
    reactions += {
      case WindowClosing(w) =>
        this.dispose()
        controller ! Quit
    }
  }
  frame.visible = true

  def receive = {
    case Joined(game) =>
      frame.title = game
    case Display(board) =>
      frame.contents = new Label(board.toString)
  }
}

object GameFrame {
  def open(factory: ActorRefFactory, controller: ActorRef): ActorRef = {
    factory.actorOf(Props(new GameFrame(controller)), "GameFrame")
  }
}
