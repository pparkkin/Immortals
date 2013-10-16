package pparkkin.games.immortals.server.controller

import akka.actor._
import java.net.InetSocketAddress
import pparkkin.games.immortals.server.game.{Start, ImmortalsGame}
import pparkkin.games.immortals.tcp.TCPConnection
import pparkkin.games.immortals.messages.{Welcome, Join}

class ServerController(address: InetSocketAddress) extends Actor with ActorLogging {
  val game = ImmortalsGame.newInstance(context, self)
  val listener = TCPConnection.newServer(context, address, self)
  game ! Start

  def receive = {
    case j: Join =>
      game ! j
    case w: Welcome =>
      listener ! w
  }

}

object ServerController {
  def newInstance(system: ActorRefFactory, address: InetSocketAddress): ActorRef = {
    system.actorOf(Props(new ServerController(address)), "ServerController")
  }
}
