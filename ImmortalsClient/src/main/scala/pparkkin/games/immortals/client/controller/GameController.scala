package pparkkin.games.immortals.client.controller

import akka.actor._
import pparkkin.games.immortals.client.ui.GameFrame
import pparkkin.games.immortals.tcp.TCPClient
import java.net.InetSocketAddress

case class Quit()

class GameController(address: InetSocketAddress) extends Actor with ActorLogging {
  GameFrame.open(self)
  TCPClient.connect(context, address, self)

  def receive = {
    case Quit => System.exit(0)
  }
}

object GameController {
  def newInstance(factory: ActorRefFactory, address: InetSocketAddress): ActorRef = {
    factory.actorOf(Props(new GameController(address)), "GameController")
  }
}
