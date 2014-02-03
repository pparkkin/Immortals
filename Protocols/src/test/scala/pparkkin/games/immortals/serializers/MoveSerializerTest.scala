package pparkkin.games.immortals.serializers

import pparkkin.games.immortals.messages.Move
import org.scalatest.FunSuite

class MoveSerializerTest extends FunSuite {
  val move = Move("Player", (0, -1))

  test("Serialize / deserialize move message") {
    val bytes = MoveSerializer.serialize(move)
    val actual = MoveSerializer.deserialize(bytes)

    assert(actual == move)
  }


}
