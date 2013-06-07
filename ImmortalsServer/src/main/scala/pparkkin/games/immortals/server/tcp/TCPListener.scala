package pparkkin.games.immortals.server.tcp

import akka.actor._
import java.net.InetSocketAddress

class TCPListener(port: Int, dataProcessor: ActorRef) extends Actor with ActorLogging {

  override def preStart {
    IOManager(context.system) listen new InetSocketAddress(port)
  }

  def receive = {
    case IO.NewClient(server) =>
      log.info("New client.")
      server.accept()
    case IO.Read(rHandle, bytes) =>
      dataProcessor ! Process(bytes)
  }
}

object TCPListener {
  def newInstance(system: ActorSystem, port: Int, dataProcessor: ActorRef): ActorRef = {
    system.actorOf(Props(new TCPListener(port, dataProcessor)), "TCPListener")
  }
}
