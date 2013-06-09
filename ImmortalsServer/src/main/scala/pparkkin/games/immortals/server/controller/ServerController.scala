package pparkkin.games.immortals.server.controller

import akka.actor._
import java.net.InetSocketAddress
import pparkkin.games.immortals.server.game.{Start, ImmortalsGame}
import pparkkin.games.immortals.tcp.{TCPServer, End}

case class Listen(address: InetSocketAddress)
case class Exit()

class ServerController(address: InetSocketAddress) extends Actor with ActorLogging {
  val game = ImmortalsGame.newInstance(context, self)
  val listener = TCPServer.newInstance(context, address, self)
  game ! Start

  def receive = {
    case End =>
      System.exit(0)
    case Exit =>
      System.exit(0)
  }

}

object ServerController {
  def newInstance(system: ActorRefFactory, address: InetSocketAddress): ActorRef = {
    system.actorOf(Props(new ServerController(address)), "ServerController")
  }
}
