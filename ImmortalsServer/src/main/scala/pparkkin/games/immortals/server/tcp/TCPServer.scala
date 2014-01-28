package pparkkin.games.immortals.server.tcp

import akka.actor._
import scala.concurrent.duration._
import java.net.InetSocketAddress
import scala.collection.mutable
import akka.actor.IO.SocketHandle
import akka.util.{Timeout, ByteString}

class TCPServer(address: InetSocketAddress, dataProcessor: ActorRef) extends Actor with ActorLogging {
  import akka.pattern.ask
  implicit val timeout = Timeout(500.millis)

  import context.dispatcher

  override def preStart {
    IOManager(context.system) listen address
  }

  var idCount = 0

  val conn2socket = mutable.Map[ActorRef, SocketHandle]()
  val socket2conn = mutable.Map[SocketHandle, ActorRef]()

  def receive = {
    case IO.NewClient(server) =>
      log.debug("New client.")
      val socket = server.accept()
      val id = idCount ; idCount += 1
      val cf = (dataProcessor ? NewClientConnection(id)).mapTo[ActorRef]
      cf.onSuccess {
        case conn =>
          conn2socket.put(conn, socket)
          socket2conn.put(socket, conn)
      }
    case IO.Read(socket, bytes) =>
      socket2conn.get(socket.asSocket)
        .map(_ ! Process(TCPServer.UNDEFINED_CONN_ID, bytes))
        .getOrElse(log.warning("Read from unknown socket."))
    case Send(id, bytes) =>
      log.debug(s"Sending $bytes to socket id $id.")
      id match {
        case TCPServer.UNDEFINED_CONN_ID =>
          conn2socket.map((e) => e._2.write(bytes))
        case i =>
          conn2socket.get(sender)
            .map(_.write(bytes))
            .getOrElse(log.warning(s"Could not find socket for id $id."))
      }
    case IO.Closed(socket, cause) =>
      socket2conn.get(socket.asSocket)
        .map(_ ! Process(TCPServer.UNDEFINED_CONN_ID, ByteString("Q")))
        .getOrElse(log.warning("Unknown player closed socket."))
    case m =>
      log.warning(s"Unknown message $m")
  }
}

object TCPServer {
  val UNDEFINED_CONN_ID: Int = -1

  def newInstance(system: ActorRefFactory, address: InetSocketAddress, dp: ActorRef): ActorRef = {
    system.actorOf(Props(new TCPServer(address, dp)), "TCPServer")
  }
}
