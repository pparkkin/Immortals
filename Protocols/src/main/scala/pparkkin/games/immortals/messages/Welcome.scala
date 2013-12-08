package pparkkin.games.immortals.messages

import pparkkin.games.immortals.datatypes.Board

case class Welcome(player: String, game: String, board: Board)
