package pparkkin.games.immortals.client.ui

import scala.swing.{Graphics2D, Panel}
import java.awt.Color

class BoardPanel extends Panel {
  private var board: Option[Array[Array[Boolean]]] = None

  def updateBoard(board: Array[Array[Boolean]]) {
    this.board = Some(board)
    this.repaint()
  }

  override
  def paintComponent(g: Graphics2D) {
    board match {
      case Some(b) =>
        g.setColor(Color.RED)
      case None =>
        g.setColor(Color.BLUE)
    }
    g.fillRect(0, 0, size.width, size.height)
//    fillPolygons(g, gi.head.seq)
//    debugInfoColor match {
//      case Some(c) => {
//        displayDebugInfo(g, c)
//      }
//      case None => {}
//    }
  }


}
