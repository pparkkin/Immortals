package pparkkin.games.immortals.server.tcp

import java.net.InetSocketAddress
import akka.actor._
import pparkkin.games.immortals.server.game.{Start, ImmortalsGame}

// Control messages
case class ConnectionReady()
case class NewClientConnection(id: Int)

class TCPServerConnection(name: String, address: InetSocketAddress) extends TCPConnection {
  val game = ImmortalsGame.newInstance(context, self, name)
  val connection = TCPServer.newInstance(context, address, self)
  val id = TCPServer.UNDEFINED_CONN_ID
  game ! Start

  def receiveInternal: Receive = {
    case ConnectionReady =>
      log.debug("Connection ready.")
      game ! ConnectionReady
    case NewClientConnection(id) =>
      log.debug(s"New connection requested for $id.")
      sender ! TCPClientConnection.newInstance(context, game, connection, id)

  }

  def receive = receiveInternal orElse receiveFromGame orElse receiveFromNetwork

}

object TCPServerConnection {
  def newInstance(system: ActorRefFactory, name: String, address: InetSocketAddress): ActorRef = {
    system.actorOf(Props(new TCPServerConnection(name, address)), "TCPListener")
  }

}
