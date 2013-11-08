package pparkkin.games.immortals.client.ui

import scala.swing.{Graphics2D, Panel}
import java.awt.Color
import javax.swing.BorderFactory

class BoardPanel extends Panel {
  val SQUARE_W = 12
  val SQUARE_H = 12

  private var board: Option[Array[Array[Boolean]]] = None

  def updateBoard(board: Array[Array[Boolean]]) {
    this.board = Some(board)
    this.repaint()

  }

  override
  def paintComponent(g: Graphics2D) {
    board.map(drawBoard(_, g))
  }

  def drawBoard(board: Array[Array[Boolean]], g: Graphics2D) {
    val height = board.size
    val width = board(0).size
    for (i <- 0 until height) {
      for (j <- 0 until width) {
        drawSquare(i, j, board(i)(j), g)
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
