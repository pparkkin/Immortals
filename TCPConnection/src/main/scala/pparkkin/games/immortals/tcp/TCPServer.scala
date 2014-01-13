package pparkkin.games.immortals.tcp

import akka.actor._
import java.net.InetSocketAddress
import scala.collection.mutable
import akka.actor.IO.SocketHandle
import akka.util.ByteString

class TCPServer(address: InetSocketAddress, dataProcessor: ActorRef) extends Actor with ActorLogging {

  override def preStart {
    IOManager(context.system) listen address
  }

  var idCount = 0

  val id2socket = mutable.Map[Int, SocketHandle]()
  val socket2id = mutable.Map[SocketHandle, Int]()

  def receive = {
    case IO.NewClient(server) =>
      log.debug("New client.")
      val socket = server.accept()
      val id = idCount ; idCount += 1
      id2socket.put(id, socket)
      socket2id.put(socket, id)
    case IO.Read(socket, bytes) =>
      socket2id.get(socket.asSocket)
        .map(dataProcessor ! Process(_, bytes))
        .getOrElse(log.warning("Read from unknown socket."))
    case Send(id, bytes) =>
      log.debug(s"Sending $bytes to socket id $id.")
      id match {
        case TCPClient.UNDEFINED_CONN_ID =>
          id2socket.map((e) => e._2.write(bytes))
        case i =>
          id2socket.get(i)
            .map(_.write(bytes))
            .getOrElse(log.warning(s"Could not find socket for id $i."))
      }
    case IO.Closed(socket, cause) =>
      socket2id.get(socket.asSocket)
        .map(dataProcessor ! Process(_, ByteString("Q")))
        .getOrElse(log.warning("Unknown player closed socket."))
    case m =>
      log.warning(s"Unknown message $m")
  }
}

object TCPServer {
//  def newInstance(system: ActorRefFactory, address: InetSocketAddress, controller: ActorRef): ActorRef = {
//    val dp = TCPConnection.newInstance(system, controller)
//    system.actorOf(Props(new TCPServer(address, dp)), "TCPServer")
//  }
}

object TCPServerFactory extends TCPActorFactory {
  def newActor(factory: ActorRefFactory, address: InetSocketAddress, dataProcessor: ActorRef): ActorRef = {
    factory.actorOf(Props(new TCPServer(address, dataProcessor)), "TCPServer")
  }
}
