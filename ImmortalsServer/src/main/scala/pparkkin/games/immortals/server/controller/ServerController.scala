package pparkkin.games.immortals.server.controller

import akka.actor._
import java.net.InetSocketAddress
import pparkkin.games.immortals.server.game.{Start, ImmortalsGame}
import pparkkin.games.immortals.tcp.TCPConnection
import pparkkin.games.immortals.messages._
import pparkkin.games.immortals.messages.Welcome
import pparkkin.games.immortals.messages.Update
import pparkkin.games.immortals.messages.Join
import pparkkin.games.immortals.messages.PlayerPositions
import pparkkin.games.immortals.server.game.Start

class ServerController(address: InetSocketAddress, name: String) extends Actor with ActorLogging {
  val game = ImmortalsGame.newInstance(context, self, name)
  val listener = TCPConnection.newServer(context, address, self)
  game ! Start

  def receive = {
    case j: Join =>
      game ! j
    case w: Welcome =>
      listener ! w
    case a: WelcomeAck =>
      game ! a
    case u: Update =>
      listener ! u
    case pp: PlayerPositions =>
      listener ! pp
    case m =>
      log.warning(s"Unknown message $m")
  }

}

object ServerController {
  def newInstance(system: ActorRefFactory, address: InetSocketAddress, name: String): ActorRef = {
    system.actorOf(Props(new ServerController(address, name)), "ServerController")
  }
}
