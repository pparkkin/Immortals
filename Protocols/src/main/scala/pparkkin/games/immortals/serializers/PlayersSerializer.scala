package pparkkin.games.immortals.serializers

import pparkkin.games.immortals.datatypes.Players
import akka.util.{ByteStringBuilder, ByteString}

object PlayersSerializer {
  implicit val byteOrder = java.nio.ByteOrder.BIG_ENDIAN

  def serialize(players: Players): ByteString = {
    val bb = new ByteStringBuilder()
      .putInt(players.size)

    players.map[ByteString]((p: (String, (Int, Int))) => serialize(p))
      .foldLeft[ByteStringBuilder](bb)((z, e) => z.putBytes(e.toArray))

    bb.result()
  }

  def serialize(player: (String, (Int, Int))) = {
    val bb = new ByteStringBuilder()
      .putInt(player._1.size)
      .putBytes(player._1.getBytes)
      .putInt(player._2._1)
      .putInt(player._2._2)
    bb.result()
  }

  def deserialize(bytes: ByteString): Players = {
    val it = bytes.iterator
    val size = it.getInt

    val builder = Map.newBuilder[String, (Int, Int)]

    for (i <- 0 until size) {
      val nl = it.getInt
      val na = Array.tabulate[Byte](nl)(_ => it.getByte)
      val name = new String(na)
      val xpos = it.getInt
      val ypos = it.getInt
      builder += (name -> (xpos, ypos))
    }

    Players(builder.result())
  }


}
