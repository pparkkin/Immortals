package pparkkin.games.immortals.datatypes

import scala.collection.immutable.Map

class Players private (val c: Map[String, (Int, Int)]) {
  def +(kv: (String, (Int, Int))) =
    new Players(c + kv)
}

object Players {
  def empty = new Players(Map.empty[String, (Int, Int)])
}
