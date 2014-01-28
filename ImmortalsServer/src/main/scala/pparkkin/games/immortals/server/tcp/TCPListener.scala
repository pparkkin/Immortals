package pparkkin.games.immortals.server.tcp

import java.net.InetSocketAddress
import akka.actor._
import pparkkin.games.immortals.server.game.{Start, ImmortalsGame}

class TCPListener(name: String, address: InetSocketAddress) extends TCPConnection {
  val game = ImmortalsGame.newInstance(context, self, name)
  val connection = TCPServer.newInstance(context, address, self)
  game ! Start

}

object TCPListener {
  def newInstance(system: ActorRefFactory, name: String, address: InetSocketAddress): ActorRef = {
    system.actorOf(Props(new TCPListener(name, address)), "TCPListener")
  }

}
