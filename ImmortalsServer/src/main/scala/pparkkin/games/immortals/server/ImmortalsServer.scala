package pparkkin.games.immortals.server

import akka.actor.ActorSystem
import controller.ServerController
import java.net.InetSocketAddress

object ImmortalsServer extends App {
  val system = ActorSystem("ImmortalsServer")
  var controller = ServerController.newInstance(system, new InetSocketAddress("localhost", 1204))
}
