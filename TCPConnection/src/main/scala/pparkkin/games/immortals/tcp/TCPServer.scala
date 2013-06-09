package pparkkin.games.immortals.tcp

import akka.actor._
import java.net.InetSocketAddress

class TCPServer(address: InetSocketAddress, dataProcessor: ActorRef) extends Actor with ActorLogging {

  override def preStart {
    IOManager(context.system) listen address
  }

  def receive = {
    case IO.NewClient(server) =>
      log.info("New client.")
      server.accept()
    case IO.Read(socket, bytes) =>
      dataProcessor ! Process(bytes)
  }
}

object TCPServer {
//  def newInstance(system: ActorRefFactory, address: InetSocketAddress, controller: ActorRef): ActorRef = {
//    val dp = TCPConnection.newInstance(system, controller)
//    system.actorOf(Props(new TCPServer(address, dp)), "TCPServer")
//  }
}
