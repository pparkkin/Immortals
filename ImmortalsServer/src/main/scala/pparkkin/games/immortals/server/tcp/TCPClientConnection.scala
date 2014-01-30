package pparkkin.games.immortals.server.tcp

import akka.actor.{Props, ActorRefFactory, ActorRef}

class TCPClientConnection(val game: ActorRef, val connection: ActorRef, val id: Int) extends TCPConnection {

  def receive = receiveFromGame orElse receiveFromNetwork

}

object TCPClientConnection {
  def newInstance(system: ActorRefFactory, game: ActorRef, connection: ActorRef, id: Int): ActorRef = {
    system.actorOf(Props(new TCPClientConnection(game, connection, id)), s"TCPClientConnection-$id")
  }
}
