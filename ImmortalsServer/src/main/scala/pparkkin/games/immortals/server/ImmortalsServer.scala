package pparkkin.games.immortals.server

import akka.actor.ActorSystem
import java.net.InetSocketAddress
import pparkkin.games.immortals.server.tcp.TCPServerConnection

object ImmortalsServer extends App {
  args.length match {
    case 1 =>
      val system = ActorSystem("ImmortalsServer")
      TCPServerConnection.newInstance(system, args(0), new InetSocketAddress("localhost", 1204))
  }
}
