package pparkkin.games.immortals.server.tcp

import akka.actor._
import scala.concurrent.duration._
import java.net.InetSocketAddress
import scala.collection.mutable
import akka.actor.IO.{Handle, SocketHandle}
import akka.util.{Timeout, ByteString}

class TCPServer(address: InetSocketAddress, dataProcessor: ActorRef) extends Actor with ActorLogging {
  import akka.pattern.ask
  implicit val timeout = Timeout(500.millis)

  import context.dispatcher

  override def preStart {
    IOManager(context.system) listen address
  }

  var idCount = 0

  val conn2socket = mutable.Map[ActorRef, Handle]()
  val socket2conn = mutable.Map[Handle, ActorRef]()

  def receive = {
    case IO.Listening(server, address) =>
      log.debug("Listening.")
      val socket = server
      conn2socket.put(dataProcessor, socket)
      socket2conn.put(socket, dataProcessor)
    case IO.NewClient(server) =>
      log.debug("New client.")
      val socket = server.accept()
      val cf = (dataProcessor ? NewClientConnection).mapTo[ActorRef]
      cf.onSuccess {
        case conn =>
          conn2socket.put(conn, socket)
          socket2conn.put(socket, conn)
      }
    case IO.Read(socket, bytes) =>
      socket2conn.get(socket)
        .map(_ ! Process(bytes))
        .getOrElse(log.warning("Read from unknown socket."))
    case Send(bytes) =>
      log.debug(s"Sending $bytes to socket.")
      conn2socket.get(sender)
        .map(_.asWritable.write(bytes))
        .getOrElse(log.warning("Could not find socket for data."))
    case IO.Closed(socket, cause) =>
      socket2conn.get(socket)
        .map(_ ! Process(ByteString("Q")))
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
