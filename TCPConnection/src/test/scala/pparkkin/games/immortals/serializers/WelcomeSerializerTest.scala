package pparkkin.games.immortals.serializers

import pparkkin.games.immortals.messages.Welcome
import pparkkin.games.immortals.datatypes.Board
import org.scalatest.FunSuite

class WelcomeSerializerTest extends FunSuite {
  val welcome = Welcome("Player", "Game", Board.empty(1, 1))

  test("Serialize / deserialize welcome message") {
    val bytes = WelcomeSerializer.serialize(welcome)
    val actual = WelcomeSerializer.deserialize(bytes)

    assert(actual == welcome)
  }


}
