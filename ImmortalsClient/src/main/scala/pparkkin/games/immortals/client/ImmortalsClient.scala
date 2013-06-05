package pparkkin.games.immortals.client

import swing.SwingApplication
import ui.ServerSelectorFrame

object ImmortalsClient extends SwingApplication {
  
  def startup(args : Array[String]) {
    val selector = new ServerSelectorFrame
    selector.visible = true
  }

}
