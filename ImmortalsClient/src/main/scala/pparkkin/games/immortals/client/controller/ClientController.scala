package pparkkin.games.immortals.client.controller

import akka.actor._
import java.net.InetSocketAddress
import pparkkin.games.immortals.client.ui.ServerSelectorFrame

case class Exit()
case class SelectServer()
case class ConnectServer(server: String)

class ClientController() extends Actor with ActorLogging {
  def receive = {
    case Exit =>
      System.exit(0)
    case SelectServer =>
      ServerSelectorFrame.open(self)
    case ConnectServer(server) =>
      val addr = new InetSocketAddress(server, 1204)
      log.info("Connecting to "+addr.toString)
      GameController.newInstance(context, addr)
  }
}

object ClientController {
  def newInstance(system: ActorSystem): ActorRef = {
    system.actorOf(Props(new ClientController()), "ClientController")
  }
}
