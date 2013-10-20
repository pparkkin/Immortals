package pparkkin.games.immortals.server.game

import scala.util.Random

object GameOfLife {
  def randomBoard(height: Int, width: Int, fill: Double = 0.3): Array[Array[Boolean]] = {
    Array.tabulate[Boolean](height, width) ((_, _) =>
      if (Random.nextDouble() < fill) true else false)
  }

  def tick(board: Array[Array[Boolean]]): Array[Array[Boolean]] = {
    val height = board.size
    val width = board(0).size

    Array.tabulate[Boolean](height, width) ((row, col) => {
      val n = liveNeighbors(board, row, col)
      if (isLive(board, row, col)) {
        if (1 < n && n < 4) true else false
      } else {
        if (n == 3) true else false
      }
    })
  }

  def liveNeighbors(board: Array[Array[Boolean]], row: Int, col: Int): Int = {
    Array((-1, -1), (-1, 0), (-1, +1),
          (0,  -1),          (0,  +1),
          (+1, -1), (+1, 0), (+1, +1))
      .map((rc) => isLive(board, row+rc._1, col+rc._2))
      .count((p) => p)
  }

  def isLive(board: Array[Array[Boolean]], row: Int, col: Int): Boolean = {
    val maxRow = board.size-1
    val maxCol = board(0).size-1

    if (row < 0 || row > maxRow) return false
    if (col < 0 || col > maxCol) return false

    board(row)(col)
  }
}
