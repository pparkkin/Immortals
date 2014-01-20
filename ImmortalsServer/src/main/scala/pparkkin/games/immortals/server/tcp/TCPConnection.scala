package pparkkin.games.immortals.server.tcp

import akka.actor._
import akka.util.ByteString
import java.net.InetSocketAddress
import pparkkin.games.immortals.messages._
import scala.collection.mutable
import pparkkin.games.immortals.serializers.{WelcomeSerializer, PlayersSerializer, BoardSerializer}
import pparkkin.games.immortals.messages.Welcome
import pparkkin.games.immortals.messages.Update
import pparkkin.games.immortals.messages.Join
import akka.event.LoggingAdapter

// Control messages
case class ConnectionReady()

// Messages to/from network
case class Process(id: Int, bytes: ByteString)

case class Send(id: Int, bytes: ByteString)

trait TCPConnection extends Actor with ActorLogging {
  val controller: ActorRef
  val connection: ActorRef

  def receive = {
    case ConnectionReady =>
      log.debug("Connection ready.")
      controller ! ConnectionReady
    case Process(id, bytes) =>
      log.debug(s"Connection $id sent $bytes.")
      bytes(0) match {
        case 0x4a =>
          val player = bytes.drop(1).decodeString("UTF-8")
          controller ! Join(player)
        case 0x57 =>
          controller ! WelcomeSerializer.deserialize(bytes.drop(1))
        case 0x41 =>
          val player = bytes.drop(1).decodeString("UTF-8")
          controller ! WelcomeAck(player)
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
      connection ! Send(TCPServer.UNDEFINED_CONN_ID, ByteString("J"+player))
    case w: Welcome =>
      log.debug(s"Received Welcome.")
      // TCPServer.UNDEFINED_CONN_ID below is incorrect. Should be id.
      connection ! Send(TCPServer.UNDEFINED_CONN_ID, ByteString("W") ++ WelcomeSerializer.serialize(w))
    case WelcomeAck(player) =>
      log.debug("Received WelcomeAck.")
      connection ! Send(TCPServer.UNDEFINED_CONN_ID, ByteString("A"+player))
    case Update(board) =>
      log.debug("Received a board update.")
      connection ! Send(TCPServer.UNDEFINED_CONN_ID,
        ByteString("U") ++ BoardSerializer.serialize(board))
    case PlayerPositions(pp) =>
      log.debug("Received a player position update.")
      connection ! Send(TCPServer.UNDEFINED_CONN_ID,
        ByteString("P") ++ PlayersSerializer.serialize(pp))
    case m =>
      log.warning(s"Unknown message $m.")
  }
}

