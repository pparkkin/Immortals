package pparkkin.games.immortals.client.ui

import scala.swing.{Graphics2D, Panel}
import java.awt.Color
import javax.swing.BorderFactory
import pparkkin.games.immortals.datatypes.Board

class BoardPanel(var board: Board) extends Panel {
  val SQUARE_W = 12
  val SQUARE_H = 12

  def updateBoard(board: Board) {
    this.board = board
    this.repaint()
  }

  override
  def paintComponent(g: Graphics2D) {
    drawBoard(board, g)
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

  def drawSquare(i: Int, j: Int, alive: Boolean, g: Graphics2D) {
    g.setColor(Color.GRAY)
    g.drawRect(j*SQUARE_W, i*SQUARE_H, SQUARE_W, SQUARE_H)

    alive match {
      case true => g.setColor(Color.BLACK)
      case false => g.setColor(Color.WHITE)
    }
    g.fillRect((j*SQUARE_W)+1, (i*SQUARE_H)+1, SQUARE_W-1, SQUARE_H-1)
  }

}
