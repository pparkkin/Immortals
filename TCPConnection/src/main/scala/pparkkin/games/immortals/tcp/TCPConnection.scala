package pparkkin.games.immortals.tcp

import akka.actor._
import akka.util.ByteString
import java.net.InetSocketAddress

// Messages to/from game
case class End()

// Messages to/from network
case class Process(bytes: ByteString)
case class Send(data: String)

class TCPConnection(controller: ActorRef, connectionFactory: TCPActorFactory, address: InetSocketAddress) extends Actor with ActorLogging {
  val connection = connectionFactory.newActor(context, address, self)

  def receive = {
    case Process(bytes) =>
      controller ! End
  }
}

object TCPConnection {
  def newServer(factory: ActorRefFactory, address: InetSocketAddress, controller: ActorRef): ActorRef = {
    newInstance(factory, controller, TCPServerFactory, address)
  }

  def newClient(factory: ActorRefFactory, address: InetSocketAddress, controller: ActorRef): ActorRef = {
    newInstance(factory, controller, TCPClientFactory, address)
  }

  def newInstance(system: ActorRefFactory, controller: ActorRef,
                  connectionFactory: TCPActorFactory, address: InetSocketAddress): ActorRef = {
    system.actorOf(Props(new TCPConnection(controller, connectionFactory, address)), "TCPConnection")
  }
}
