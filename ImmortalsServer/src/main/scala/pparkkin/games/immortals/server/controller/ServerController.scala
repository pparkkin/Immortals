package pparkkin.games.immortals.server.controller

import akka.actor._
import java.net.InetSocketAddress
import pparkkin.games.immortals.server.game.{Start, ImmortalsGame}
import pparkkin.games.immortals.tcp.TCPConnection
import pparkkin.games.immortals.messages.{Update, Welcome, Join}

class ServerController(address: InetSocketAddress, name: String) extends Actor with ActorLogging {
  val game = ImmortalsGame.newInstance(context, self, name)
  val listener = TCPConnection.newServer(context, address, self)
  game ! Start

  def receive = {
    case j: Join =>
      game ! j
    case w: Welcome =>
      listener ! w
    case u: Update =>
      listener ! u
  }

}

object ServerController {
  def newInstance(system: ActorRefFactory, address: InetSocketAddress, name: String): ActorRef = {
    system.actorOf(Props(new ServerController(address, name)), "ServerController")
  }
}
