package pparkkin.games.immortals.server.game

import org.scalatest.FunSuite

class GameOfLifeTest extends FunSuite {
  // xoo
  // oxx
  // oxo
  val board = Array(Array(true,  false, false),
                    Array(false, true,  true),
                    Array(false, true,  false))

  val boardAfter = Array(Array(false, true, false),
                         Array(true,  true, true),
                         Array(false, true, true))

  test("Calculate neighbors") {
    assert(GameOfLife.liveNeighbors(board, 0, 0) == 1)
    assert(GameOfLife.liveNeighbors(board, 0, 1) == 3)
    assert(GameOfLife.liveNeighbors(board, 0, 2) == 2)
    assert(GameOfLife.liveNeighbors(board, 1, 0) == 3)
    assert(GameOfLife.liveNeighbors(board, 1, 1) == 3)
    assert(GameOfLife.liveNeighbors(board, 1, 2) == 2)
    assert(GameOfLife.liveNeighbors(board, 2, 0) == 2)
    assert(GameOfLife.liveNeighbors(board, 2, 1) == 2)
    assert(GameOfLife.liveNeighbors(board, 2, 2) == 3)
  }

  test("Tick") {
    val res = GameOfLife.tick(board)
    assert(res.deep == boardAfter.deep)
  }
}
