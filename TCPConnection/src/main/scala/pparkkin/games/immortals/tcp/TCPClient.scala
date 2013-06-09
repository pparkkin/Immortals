package pparkkin.games.immortals.tcp

import akka.actor._
import java.net.InetSocketAddress
import akka.actor.IO.{SocketHandle, Connected}
import akka.util.ByteString

class TCPClient(address: InetSocketAddress, dataProcessor: ActorRef) extends Actor with ActorLogging {
  var listeners: Set[ActorRef] = Set()
  override def preStart = {
    IOManager(context.system) connect address
  }

  def receive = {
    case Connected(h, _) =>
      log.info("Connected to server.")
      context.become(connected(h))
      h.write(ByteString("J:Paavo"))
  }

  def connected(handle: SocketHandle): Receive = {
    case Send(s) =>
      handle.write(ByteString(s))
  }
}

object TCPClient {
//  def connect(system: ActorRefFactory, address: InetSocketAddress, controller: ActorRef): ActorRef = {
//    val dp = TCPConnection.newInstance(system, controller)
//    system.actorOf(Props(new TCPClient(address, dp)), "TCPClient")
//  }
}
