package pparkkin.games.immortals.tcp

import akka.actor._
import akka.util.ByteString

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
  def newInstance(system: ActorRefFactory, controller: ActorRef): ActorRef = {
    system.actorOf(Props(new TCPConnection(controller)), "TCPConnection")
  }
}
