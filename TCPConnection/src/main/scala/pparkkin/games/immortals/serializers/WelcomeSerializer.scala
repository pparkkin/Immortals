package pparkkin.games.immortals.serializers

import akka.util.{ByteStringBuilder, ByteString}
import pparkkin.games.immortals.messages.Welcome

object WelcomeSerializer {
  implicit val byteOrder = java.nio.ByteOrder.BIG_ENDIAN

  def deserialize(bytes: ByteString): Welcome = {
    val it = bytes.iterator

    val pLen = it.getInt
    val pBuf = Array.fill[Byte](pLen)(0)
    it.getBytes(pBuf)
    val player = ByteString(pBuf)

    val gLen = it.getInt
    val gBuf = Array.fill[Byte](gLen)(0)
    it.getBytes(gBuf)
    val game = ByteString(gBuf)

    val bLen = it.getInt
    val bBuf = Array.fill[Byte](bLen)(0)
    it.getBytes(bBuf)
    val board = ByteString(bBuf)

    Welcome(player.decodeString("UTF-8"), game.decodeString("UTF-8"), BoardSerializer.deserialize(board))
  }

  def serialize(msg: Welcome): ByteString = {
    val player = ByteString(msg.player)
    val game = ByteString(msg.game)
    val board = BoardSerializer.serialize(msg.board)

    val bb = new ByteStringBuilder()

    bb.putInt(player.length)
    bb.putBytes(player.toArray)
    bb.putInt(game.length)
    bb.putBytes(game.toArray)
    bb.putInt(board.length)
    bb.putBytes(board.toArray)

    bb.result()
  }
}
