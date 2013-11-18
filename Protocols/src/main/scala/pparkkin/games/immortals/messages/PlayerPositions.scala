package pparkkin.games.immortals.messages

import scala.collection.immutable.Map

case class PlayerPositions(players: Map[String, (Int, Int)])
