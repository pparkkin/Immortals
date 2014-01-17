package pparkkin.games.immortals.client.tcp

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
  val UNDEFINED_CONN_ID: Int = -1

  def newInstance(system: ActorRefFactory, address: InetSocketAddress, dp: ActorRef): ActorRef = {
    system.actorOf(Props(new TCPClient(address, dp)), "TCPClient")
  }

}
