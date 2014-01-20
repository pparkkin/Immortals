package pparkkin.games.immortals.server.tcp

import java.net.InetSocketAddress
import akka.actor._

class TCPListener(val controller: ActorRef, address: InetSocketAddress) extends TCPConnection {

  val connection = TCPServer.newInstance(context, address, self)

}

object TCPListener {
  def newServer(factory: ActorRefFactory, address: InetSocketAddress, controller: ActorRef): ActorRef = {
    newInstance(factory, controller, address)
  }

  def newInstance(system: ActorRefFactory, controller: ActorRef, address: InetSocketAddress): ActorRef = {
    system.actorOf(Props(new TCPListener(controller, address)), "TCPListener")
  }

}
