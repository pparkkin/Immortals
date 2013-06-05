package pparkkin.games.immortals.client.ui

import swing.{Button, FlowPanel, TextField, Frame}
import swing.event.{WindowClosing, ButtonClicked}

class ServerSelectorFrame extends Frame {
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
      System.exit(0)
    case WindowClosing(w) =>
      System.exit(1)
  }

}
