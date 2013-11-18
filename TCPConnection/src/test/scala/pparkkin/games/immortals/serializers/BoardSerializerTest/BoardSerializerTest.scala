package pparkkin.games.immortals.serializers

import scala.collection.immutable.IndexedSeq
import pparkkin.games.immortals.datatypes.Board
import org.scalatest.FunSuite

class BoardSerializerTest extends FunSuite {
  val board = Board(IndexedSeq(IndexedSeq(true,  false, false),
                               IndexedSeq(false, true,  true),
                               IndexedSeq(false, true,  false)))

  test("Serialize / deserialize board") {
    val bytes = BoardSerializer.serialize(board)
    val actual = BoardSerializer.deserialize(bytes)

    assert(actual == board)
  }


}
