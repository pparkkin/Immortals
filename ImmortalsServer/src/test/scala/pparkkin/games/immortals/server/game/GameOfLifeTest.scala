package pparkkin.games.immortals.server.game

import scala.collection.immutable.IndexedSeq
import org.scalatest.FunSuite
import pparkkin.games.immortals.datatypes.Board

class GameOfLifeTest extends FunSuite {
  // xoo
  // oxx
  // oxo
  val board = Board(IndexedSeq(IndexedSeq(true,  false, false),
                               IndexedSeq(false, true,  true),
                               IndexedSeq(false, true,  false)))

  // oxo
  // xxx
  // oxx
  val boardAfter = Board(IndexedSeq(IndexedSeq(false, true, false),
                                    IndexedSeq(true,  true, true),
                                    IndexedSeq(false, true, true)))

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
    assert(res == boardAfter)
  }

  test("Random board") {
    val height = 20
    val width = 20
    val board = GameOfLife.randomBoard(height, width)
    assert(board.height == height)
    assert(board.width == width)
  }
}
