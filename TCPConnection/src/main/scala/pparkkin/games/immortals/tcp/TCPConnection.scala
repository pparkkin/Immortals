package pparkkin.games.immortals.tcp

import akka.actor._
import akka.util.{ByteStringBuilder, ByteString}
import java.net.InetSocketAddress
import pparkkin.games.immortals.messages.{Update, Welcome, Join, End}
import scala.collection.mutable

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
          log.info("Received update.")
        case t =>
          log.info(s"Unknown message type $t.")
      }
      controller ! End
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
        ByteString("U") ++ TCPConnection.serializeBoard(board))
    case m =>
      log.info(s"Unknown message $m.")
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

  implicit val byteOrder = java.nio.ByteOrder.BIG_ENDIAN

  def serializeBoard(board: Array[Array[Boolean]]): ByteString = {
    val height = board.size
    val width = board(0).size

    val bb = new ByteStringBuilder()
      .putInt(height)
      .putInt(width)

    board.map((row) => {
      row.map((p) => bb.putByte(if (p) 1 else 0))
    })

    bb.result()
  }

  def deserializeBoard(bytes: ByteString): Array[Array[Boolean]] = {
    val it = bytes.iterator
    val height = it.getInt
    val width = it.getInt

    Array.tabulate[Boolean](height, width) ((row, col) => {
      if (it.getByte == 1) true else false
    })
  }
}
