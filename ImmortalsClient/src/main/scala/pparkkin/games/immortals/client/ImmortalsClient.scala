package pparkkin.games.immortals.client

import controller.ClientController
import swing.SwingApplication
import ui.ServerSelectorFrame
import akka.actor.ActorSystem

object ImmortalsClient extends SwingApplication {
  
  def startup(args : Array[String]) {
    val system = ActorSystem("ImmortalsClient")
    val controller = ClientController.newInstance(system)
    val selector = new ServerSelectorFrame(controller)
    selector.visible = true
  }

}
