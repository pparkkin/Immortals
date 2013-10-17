package pparkkin.games.immortals.server.game

object GameOfLife {
  def tick(board: Array[Array[Boolean]]): Array[Array[Boolean]] = {
    val height = board.size
    val width = board(0).size

    val res = Array.ofDim[Boolean](height, width)

    for (row <- 0 until board.size) {
      for (col <- 0 until board(row).size) {
        val n = liveNeighbors(board, row, col)
        if (board(row)(col)) {
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
    val maxRow = board.size-1
    val maxCol = board(row).size-1

    val n  = if (row <= 0) false else board(row-1)(col)
    val ne = if (row <= 0) false else (if (col >= maxCol) false else board(row-1)(col+1))
    val e  = if (col >= maxCol) false else board(row)(col+1)
    val se = if (row >= maxRow) false else (if (col >= maxCol) false else board(row+1)(col+1))
    val s  = if (row >= maxRow) false else board(row+1)(col)
    val sw = if (row >= maxRow) false else (if (col <= 0) false else board(row+1)(col-1))
    val w  = if (col <= 0) false else board(row)(col-1)
    val nw = if (row <= 0) false else (if (col <= 0) false else board(row-1)(col-1))

    Array(n, ne, e, se, s, sw, w, nw).count((p) => p)
  }
}
