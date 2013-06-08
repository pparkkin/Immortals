package pparkkin.games.immortals.tcp

import akka.actor._
import akka.util.ByteString

// Messages to/from game
case class End()

// Messages to/from network
case class Process(bytes: ByteString)

class TCPDataProcessor(game: ActorRef) extends Actor with ActorLogging {
  def receive = {
    case Process(bytes) =>
      game ! End
  }
}

object TCPDataProcessor {
  def newInstance(system: ActorSystem, game: ActorRef): ActorRef = {
    system.actorOf(Props(new TCPDataProcessor(game)), "TCPDataProcessor")
  }
}
