package pparkkin.games.immortals.tcp

import akka.actor._
import java.net.InetSocketAddress
import akka.actor.IO.{SocketHandle, Connected}

class TCPClient(address: InetSocketAddress, dataProcessor: ActorRef) extends Actor with ActorLogging {
  override def preStart = {
    IOManager(context.system) connect address
  }

  def receive = {
    case Connected(h, _) =>
      log.debug("Connected to server.")
      context.become(connected(h))
      log.debug("Became connected.")
      dataProcessor ! ConnectionReady
  }

  def connected(handle: SocketHandle): Receive = {
    case IO.Read(_, bytes) =>
      log.debug(s"Read $bytes from socket.")
      dataProcessor ! Process(TCPClient.UNDEFINED_CONN_ID, bytes)
    case Send(_, s) =>
      log.debug(s"Sending $s")
      handle.write(s)
  }
}

object TCPClient {
//  def connect(system: ActorRefFactory, address: InetSocketAddress, controller: ActorRef): ActorRef = {
//    val dp = TCPConnection.newInstance(system, controller)
//    system.actorOf(Props(new TCPClient(address, dp)), "TCPClient")
//  }
  val UNDEFINED_CONN_ID: Int = -1
}

object TCPClientFactory extends TCPActorFactory {
  def newActor(factory: ActorRefFactory, address: InetSocketAddress, dataProcessor: ActorRef): ActorRef = {
    factory.actorOf(Props(new TCPClient(address, dataProcessor)), "TCPClient")
  }
}
