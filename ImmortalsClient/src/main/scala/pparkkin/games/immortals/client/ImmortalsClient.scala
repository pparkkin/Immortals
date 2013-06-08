package pparkkin.games.immortals.client

import controller.{SelectServer, ClientController}
import swing.SwingApplication
import akka.actor.ActorSystem

object ImmortalsClient extends SwingApplication {
  
  def startup(args : Array[String]) {
    val system = ActorSystem("ImmortalsClient")
    val controller = ClientController.newInstance(system)
    controller ! SelectServer
  }

}
