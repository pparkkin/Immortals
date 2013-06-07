package pparkkin.games.immortals.client.controller

import akka.actor._
import java.net.InetSocketAddress
import pparkkin.games.immortals.client.tcp.TCPConnection
import util.{Success, Failure}

case class Exit()
case class ConnectServer(server: String)

class ClientController() extends Actor with ActorLogging {
  var serverConnection: Option[ActorRef] = None

  def receive = {
    case Exit =>
      System.exit(0)
    case ConnectServer(server) =>
      val addr = new InetSocketAddress(server, 1204)
      log.info("Connecting to "+addr.toString)
      serverConnection = TCPConnection.connect(context.system, addr) match {
        case Success(conn) => Some(conn)
        case Failure(t) =>
          log.error(t, "Connection failure.")
          self ! Exit
          None
      }
  }
}

object ClientController {
  def newInstance(system: ActorSystem): ActorRef = {
    system.actorOf(Props(new ClientController()), "ClientController")
  }
}
