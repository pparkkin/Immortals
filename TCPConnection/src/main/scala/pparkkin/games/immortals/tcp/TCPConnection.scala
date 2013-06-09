package pparkkin.games.immortals.tcp

import akka.actor._
import akka.util.ByteString
import java.net.InetSocketAddress

// Messages to/from game
case class End()

// Messages to/from network
case class Process(bytes: ByteString)
case class Send(data: String)

class TCPConnection(controller: ActorRef) extends Actor with ActorLogging {
  def receive = {
    case Process(bytes) =>
      controller ! End
  }
}

object TCPConnection {
  def newServer(factory: ActorRefFactory, address: InetSocketAddress, controller: ActorRef): ActorRef = {
    val dp = newInstance(factory, controller)
    factory.actorOf(Props(new TCPServer(address, dp)), "TCPServer")
  }

  def newClient(factory: ActorRefFactory, address: InetSocketAddress, controller: ActorRef): ActorRef = {
    val dp = newInstance(factory, controller)
    factory.actorOf(Props(new TCPClient(address, dp)), "TCPClient")
  }

  def newInstance(system: ActorRefFactory, controller: ActorRef): ActorRef = {
    system.actorOf(Props(new TCPConnection(controller)), "TCPConnection")
  }
}
