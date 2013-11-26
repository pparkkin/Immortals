package pparkkin.games.immortals.tcp

import akka.actor._
import akka.util.ByteString
import java.net.InetSocketAddress
import pparkkin.games.immortals.messages._
import scala.collection.mutable
import pparkkin.games.immortals.serializers.{PlayersSerializer, BoardSerializer}
import pparkkin.games.immortals.messages.Welcome
import pparkkin.games.immortals.messages.Update
import pparkkin.games.immortals.messages.Join

// Control messages
case class ConnectionReady()

// Messages to/from network
case class Process(id: Int, bytes: ByteString)
case class Send(id: Int, bytes: ByteString)

class TCPConnection(controller: ActorRef, connectionFactory: TCPActorFactory, address: InetSocketAddress) extends Actor with ActorLogging {
  val connection = connectionFactory.newActor(context, address, self)

  val id2player = mutable.Map[Int, String]()
  val player2id = mutable.Map[String, Int]()

  def receive = {
    case ConnectionReady =>
      log.debug("Connection ready.")
      controller ! ConnectionReady
    case Process(id, bytes) =>
      log.debug(s"Connection $id sent $bytes.")
      bytes(0) match {
        case 0x4a =>
          val player = bytes.drop(1).decodeString("UTF-8")
          id2player.put(id, player)
          player2id.put(player, id)
          controller ! Join(player)
        case 0x57 =>
          id2player.get(id)
            .map(controller ! Welcome(_, bytes.drop(1).decodeString("UTF-8")))
            .getOrElse(log.warning(s"Could not find player id $id."))
        case 0x55 =>
          log.debug("Received update.")
          controller ! Update(BoardSerializer.deserialize(bytes.drop(1)))
        case 0x50 =>
          log.debug("Received player positions.")
          controller ! PlayerPositions(PlayersSerializer.deserialize(bytes.drop(1)))
        case t =>
          log.info(s"Unknown message type $t.")
      }
    case Join(player) =>
      log.debug(s"Received join from $player.")
      connection ! Send(TCPClient.UNDEFINED_CONN_ID, ByteString("J"+player))
      id2player.put(TCPClient.UNDEFINED_CONN_ID, player)
      player2id.put(player, TCPClient.UNDEFINED_CONN_ID)
    case Welcome(player, game) =>
      log.debug(s"Received welcome $game for $player.")
      player2id.get(player)
        .map(connection ! Send(_, ByteString("W"+game)))
        .getOrElse(log.warning(s"Unknown player $player."))
    case Update(board) =>
      log.debug("Received a board update.")
      connection ! Send(TCPClient.UNDEFINED_CONN_ID,
        ByteString("U") ++ BoardSerializer.serialize(board))
    case PlayerPositions(pp) =>
      log.debug("Received a player position update.")
      connection ! Send(TCPClient.UNDEFINED_CONN_ID,
        ByteString("P") ++ PlayersSerializer.serialize(pp))
    case m =>
      log.warning(s"Unknown message $m.")
  }
}

object TCPConnection {
  def newServer(factory: ActorRefFactory, address: InetSocketAddress, controller: ActorRef): ActorRef = {
    newInstance(factory, controller, TCPServerFactory, address)
  }

  def newClient(factory: ActorRefFactory, address: InetSocketAddress, controller: ActorRef): ActorRef = {
    newInstance(factory, controller, TCPClientFactory, address)
  }

  def newInstance(system: ActorRefFactory, controller: ActorRef,
                  connectionFactory: TCPActorFactory, address: InetSocketAddress): ActorRef = {
    system.actorOf(Props(new TCPConnection(controller, connectionFactory, address)), "TCPConnection")
  }

}
