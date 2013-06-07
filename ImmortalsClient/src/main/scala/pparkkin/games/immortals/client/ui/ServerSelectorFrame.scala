package pparkkin.games.immortals.client.ui

import swing.{Button, FlowPanel, TextField, Frame}
import swing.event.{WindowClosing, ButtonClicked}
import akka.actor.ActorRef
import pparkkin.games.immortals.client.controller.ConnectServer

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
      controller ! ConnectServer(field.text)
      this.dispose()
    case WindowClosing(w) =>
      System.exit(1)
  }

}
