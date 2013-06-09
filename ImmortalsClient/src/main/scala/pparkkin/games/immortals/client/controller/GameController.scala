package pparkkin.games.immortals.client.controller

import akka.actor._
import pparkkin.games.immortals.client.ui.GameFrame
import pparkkin.games.immortals.tcp.TCPConnection
import java.net.InetSocketAddress

case class Quit()

class GameController(address: InetSocketAddress) extends Actor with ActorLogging {
  GameFrame.open(self)
  TCPConnection.newClient(context, address, self)

  def receive = {
    case Quit => System.exit(0)
  }
}

object GameController {
  def newInstance(factory: ActorRefFactory, address: InetSocketAddress): ActorRef = {
    factory.actorOf(Props(new GameController(address)), "GameController")
  }
}
