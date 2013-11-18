package pparkkin.games.immortals.serializers

import akka.util.{ByteString, ByteStringBuilder}
import pparkkin.games.immortals.datatypes.Board

object BoardSerializer {
  implicit val byteOrder = java.nio.ByteOrder.BIG_ENDIAN

  def serialize(board: Board): ByteString = {
    val bb = new ByteStringBuilder()
      .putInt(board.height)
      .putInt(board.width)

    board.iterate((p: Boolean) => bb.putByte(if (p) 1 else 0))

    bb.result()
  }

  def deserialize(bytes: ByteString): Board = {
    val it = bytes.iterator
    val height = it.getInt
    val width = it.getInt

    Board.tabulate(height, width) ((row, col) => {
      if (it.getByte == 1) true else false
    })
  }

}
