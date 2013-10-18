package pparkkin.games.immortals.server.game

object GameOfLife {
  def tick(board: Array[Array[Boolean]]): Array[Array[Boolean]] = {
    val height = board.size
    val width = board(0).size

    val res = Array.ofDim[Boolean](height, width)

    for (row <- 0 until board.size) {
      for (col <- 0 until board(row).size) {
        val n = liveNeighbors(board, row, col)
        if (isLive(board, row, col)) {
          if (1 < n && n < 4) res(row)(col) = true
          else res(row)(col) = false
        } else {
          if (n == 3) res(row)(col) = true
          else res(row)(col) = false
        }
      }
    }

    res
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
