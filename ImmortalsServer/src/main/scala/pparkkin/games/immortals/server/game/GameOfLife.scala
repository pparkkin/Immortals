package pparkkin.games.immortals.server.game

import scala.util.Random
import pparkkin.games.immortals.datatypes.Board

object GameOfLife {
  def randomBoard(height: Int, width: Int, fill: Double = 0.3): Board = {
    Board.tabulate(height, width) ((_, _) =>
      if (Random.nextDouble() < fill) true else false)
  }

  def tick(board: Board): Board = {
    val height = board.height
    val width = board.width

    Board.tabulate(height, width) ((row, col) => {
      val n = liveNeighbors(board, row, col)
      if (isLive(board, row, col)) {
        if (1 < n && n < 4) true else false
      } else {
        if (n == 3) true else false
      }
    })
  }

  def liveNeighbors(board: Board, row: Int, col: Int): Int = {
    Array((-1, -1), (-1, 0), (-1, +1),
          ( 0, -1),          ( 0, +1),
          (+1, -1), (+1, 0), (+1, +1))
      .map((rc) => isLive(board, row+rc._1, col+rc._2))
      .count((p) => p)
  }

  def isLive(board: Board, row: Int, col: Int): Boolean = {
    board(row, col)
  }
}
