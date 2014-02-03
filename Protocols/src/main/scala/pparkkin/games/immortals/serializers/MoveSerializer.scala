package pparkkin.games.immortals.serializers

import pparkkin.games.immortals.messages.Move
import akka.util.{ByteStringBuilder, ByteString}

object MoveSerializer {
  implicit val byteOrder = java.nio.ByteOrder.BIG_ENDIAN

  def serialize(msg: Move): ByteString = {
    val player = ByteString(msg.player)

    val bb = new ByteStringBuilder()

    bb.putInt(player.length)
    bb.putBytes(player.toArray)
    bb.putInt(msg.move._1)
    bb.putInt(msg.move._2)

    bb.result()

  }

  def deserialize(bytes: ByteString): Move = {
    val it = bytes.iterator

    val pLen = it.getInt
    val pBuf = Array.fill[Byte](pLen)(0)
    it.getBytes(pBuf)
    val player = ByteString(pBuf)

    val xMove = it.getInt
    val yMove = it.getInt

    Move(player.decodeString("UTF-8"), (xMove, yMove))

  }

}
