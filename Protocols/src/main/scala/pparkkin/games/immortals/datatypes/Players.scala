package pparkkin.games.immortals.datatypes

import scala.collection.immutable.Map

class Players private (val c: Map[String, (Int, Int)]) {
  def +(kv: (String, (Int, Int))) =
    new Players(c + kv)
  def size = c.size
  def map[A](f: ((String, (Int, Int))) => A): Iterator[A] =
    c.iterator.map[A](f)
  def foreach(f: ((String, (Int, Int))) => Unit) {
    c.foreach(f)
  }
  def ==(other: Players) =
    this.c.equals(other.c)
}

object Players {
  def apply(c: Map[String, (Int, Int)]): Players = new Players(c)
  def empty: Players = new Players(Map.empty[String, (Int, Int)])
}
