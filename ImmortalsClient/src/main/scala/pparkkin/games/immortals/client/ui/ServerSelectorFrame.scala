package pparkkin.games.immortals.client.ui

import scala.swing._
import swing.event.{WindowClosing, ButtonClicked}
import akka.actor.ActorRef
import pparkkin.games.immortals.client.controller.{Exit, ConnectServer}
import javax.swing.SwingUtilities
import scala.swing.event.WindowClosing
import pparkkin.games.immortals.client.controller.Exit
import scala.swing.event.ButtonClicked
import pparkkin.games.immortals.client.controller.ConnectServer

class ServerSelectorFrame(controller: ActorRef) extends Frame {
  val nameField = new TextField {
    columns = 12
  }

  val field = new TextField {
    columns = 12
  }

  val button = new Button {
    text = "Connect"
  }

  title = "Select Server"

  contents = new BoxPanel(Orientation.Vertical) {
    contents += new BoxPanel(Orientation.Horizontal) {
      contents += nameField
      contents += new Label { text = "Player name"}
    }
    contents += new BoxPanel(Orientation.Horizontal) {
      contents += field
      contents += new Label { text = "Server host name"}
    }
    contents += new FlowPanel(button)
  }

  listenTo(button, this)
  reactions += {
    case ButtonClicked(b) =>
      this.dispose()
      controller ! ConnectServer(field.text, nameField.text)
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
