package pparkkin.games.immortals.tcp

import org.scalatest.FunSuite

class TCPConnectionTest extends FunSuite {
  val board = Array(Array(true,  false, false),
    Array(false, true,  true),
    Array(false, true,  false))

  test("Serialize / deserialize board") {
    val bytes = TCPConnection.serializeBoard(board)
    val actual = TCPConnection.deserializeBoard(bytes)

    assert(actual.deep == board.deep)
  }

}
