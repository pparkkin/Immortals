package pparkkin.games.immortals.serializers

import scala.collection.immutable.Map
import pparkkin.games.immortals.datatypes.Players
import org.scalatest.FunSuite

class PlayersSerializerTest extends FunSuite {
  val players = Players(Map("Paavo" -> (4, 5), "Kalle" -> (23, 0)))

  test("Serialize / deserialize player list") {
    val bytes = PlayersSerializer.serialize(players)
    val actual = PlayersSerializer.deserialize(bytes)

    assert(actual == players)
  }


}
