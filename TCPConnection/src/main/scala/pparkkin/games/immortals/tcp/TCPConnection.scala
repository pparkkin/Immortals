package pparkkin.games.immortals.tcp

import akka.actor._
import java.net.InetSocketAddress
import akka.actor.IO.{SocketHandle, Connected}
import akka.util.ByteString

case class Send(data: String)

class TCPConnection(address: InetSocketAddress) extends Actor with ActorLogging {
  var listeners: Set[ActorRef] = Set()
  override def preStart = {
    IOManager(context.system) connect address
  }

  def receive = {
    case Connected(h, _) =>
      log.info("Connected to server.")
      context.become(connected(h))
  }

  def connected(handle: SocketHandle): Receive = {
    case Send(s) =>
      handle.write(ByteString(s))
  }
}

object TCPConnection {
  def connect(system: ActorRefFactory, address: InetSocketAddress): ActorRef = {
    system.actorOf(Props(new TCPConnection(address)))
  }
}
