package pparkkin.games.immortals.server

import akka.actor.ActorSystem
import controller.ServerController
import java.net.InetSocketAddress

object ImmortalsServer extends App {
  args.length match {
    case 1 =>
      val system = ActorSystem("ImmortalsServer")
      ServerController.newInstance(system, new InetSocketAddress("localhost", 1204), args(0))
  }
}
