package pparkkin.games.immortals.client.ui

import swing.{Label, Frame}
import akka.actor.ActorRef
import javax.swing.SwingUtilities
import swing.event.{WindowClosing}
import pparkkin.games.immortals.client.controller.Quit

class GameFrame(controller: ActorRef) extends Frame {
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

object GameFrame {
  def open(controller: ActorRef) = {
    SwingUtilities.invokeLater(new Runnable {
      def run {
        val frame = new GameFrame(controller)
        frame.visible = true
      }
    })
  }
}
