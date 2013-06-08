package pparkkin.games.immortals.client.ui

import swing.{Button, FlowPanel, TextField, Frame}
import swing.event.{WindowClosing, ButtonClicked}
import akka.actor.ActorRef
import pparkkin.games.immortals.client.controller.{Exit, ConnectServer}
import javax.swing.SwingUtilities

class ServerSelectorFrame(controller: ActorRef) extends Frame {
  val field = new TextField {
    columns = 12
  }

  val button = new Button {
    text = "Connect"
  }

  title = "Select Server"

  contents = new FlowPanel(field, button)

  listenTo(button, this)
  reactions += {
    case ButtonClicked(b) =>
      this.dispose()
      controller ! ConnectServer(field.text)
    case WindowClosing(w) =>
      this.dispose()
      controller ! Exit
  }

}

object ServerSelectorFrame {
  def open(controller: ActorRef) {
    SwingUtilities.invokeLater(new Runnable {
      def run {
        val selector = new ServerSelectorFrame(controller)
        selector.visible = true
      }
    })
  }
}
