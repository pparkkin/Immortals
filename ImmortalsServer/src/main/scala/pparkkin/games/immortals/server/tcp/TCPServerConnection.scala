package pparkkin.games.immortals.server.tcp

import java.net.InetSocketAddress
import akka.actor._
import pparkkin.games.immortals.server.game.{Start, ImmortalsGame}

// Control messages
case class ConnectionReady()
case class NewClientConnection()

class TCPServerConnection(name: String, address: InetSocketAddress) extends TCPConnection {
  val game = ImmortalsGame.newInstance(context, self, name)
  val connection = TCPServer.newInstance(context, address, self)
  game ! Start

  def receiveInternal: Receive = {
    case ConnectionReady =>
      log.debug("Connection ready.")
      game ! ConnectionReady
    case NewClientConnection =>
      log.debug("New connection requested.")
      sender ! TCPClientConnection.newInstance(context, game, connection)

  }

  def receive = receiveInternal orElse receiveFromGame orElse receiveFromNetwork

}

object TCPServerConnection {
  def newInstance(system: ActorRefFactory, name: String, address: InetSocketAddress): ActorRef = {
    system.actorOf(Props(new TCPServerConnection(name, address)), "TCPListener")
  }

}
