package pparkkin.games.immortals.client.ui

import scala.swing.{Graphics2D, Panel}
import java.awt.Color
import pparkkin.games.immortals.datatypes.{Players, Board}
import scala.swing.event.{Key, KeyReleased}
import akka.actor.ActorRef

class BoardPanel(val parent: ActorRef, var board: Board, var players: Players = Players.empty) extends Panel {
  val SQUARE_W = 12
  val SQUARE_H = 12

  listenTo(keys)
  reactions += {
    case KeyReleased(_, key, _, _) =>
      parent ! KeyPress(key)
  }

  focusable = true
  requestFocus()

  def updateBoard(board: Board) {
    this.board = board
    this.repaint()
  }

  def updatePlayers(players: Players) {
    this.players = players
    this.repaint()
  }

  override
  def paintComponent(g: Graphics2D) {
    drawBoard(board, g)
    drawPlayers(players, g)
  }

  def drawBoard(board: Board, g: Graphics2D) {
    val height = board.height
    val width = board.width
    for (i <- 0 until height) {
      for (j <- 0 until width) {
        drawSquare(i, j, board(i, j), g)
      }
    }
  }

  def drawPlayers(players: Players, g: Graphics2D) {
    players.foreach((p: (String, (Int, Int))) =>
      drawSquare(p._2._1, p._2._2, Color.RED, g: Graphics2D))
  }

  def drawSquare(i: Int, j: Int, alive: Boolean, g: Graphics2D) {
    g.setColor(Color.GRAY)
    g.drawRect(j*SQUARE_W, i*SQUARE_H, SQUARE_W, SQUARE_H)

    alive match {
      case true => g.setColor(Color.BLACK)
      case false => g.setColor(Color.WHITE)
    }
    g.fillRect((j*SQUARE_W)+1, (i*SQUARE_H)+1, SQUARE_W-1, SQUARE_H-1)
  }

  def drawSquare(i: Int, j: Int, color: Color, g: Graphics2D) {
    g.setColor(Color.GRAY)
    g.drawRect(j*SQUARE_W, i*SQUARE_H, SQUARE_W, SQUARE_H)

    g.setColor(color)
    g.fillRect((j*SQUARE_W)+1, (i*SQUARE_H)+1, SQUARE_W-1, SQUARE_H-1)
  }

}
