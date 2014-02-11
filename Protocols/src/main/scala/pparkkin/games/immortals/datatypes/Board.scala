package pparkkin.games.immortals.datatypes

import scala.collection.immutable.IndexedSeq

class Board private (val c: IndexedSeq[IndexedSeq[Boolean]]) {
  def height = c.size
  def width = if (height == 0) 0 else c(0).size

  def apply(i: Int, j: Int): Boolean = {
    val maxRow = height-1
    val maxCol = width-1

    if (i < 0 || i > maxRow) return false
    if (j < 0 || j > maxCol) return false

    c(i)(j)
  }

  def ==(other: Board) =
    (this.c.equals(other.c))

  def iterate(f: (Boolean) => _) {
    c.map(_.map(f))
  }

  def updated(i: Int, j: Int, b: Boolean) = {
    Board(c.updated(i, c(i).updated(j, b)))
  }

}

object Board {
  def apply(src: IndexedSeq[IndexedSeq[Boolean]]): Board =
    new Board(IndexedSeq.tabulate(src.size, src(0).size)((i, j) => src(i)(j)))
  def empty(height: Int, width: Int): Board =
    new Board(IndexedSeq.fill[Boolean](height, width)(false))
  def tabulate(height: Int, width: Int)(f: (Int, Int) => Boolean): Board =
    new Board(IndexedSeq.tabulate(height, width)(f))

}
