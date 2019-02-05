package web

import org.scalajs.dom.{CanvasRenderingContext2D, MouseEvent}
import org.scalajs.dom.html.Canvas
import scala.collection.mutable.TreeSet
import scala.scalajs.js.timers.setInterval


trait MouseListener {
  this: GameEngine with BussClient with ObjectsStore =>

  register[InitEvent](_ => init)

  private def init() {
    canvas.onclick = (e: MouseEvent) => clickHandler(Positioner.scale(e.clientX), Positioner.scale(e.clientY))
  }

  private def getPieceClicked(x: Int, y: Int): Option[Piece] =
    shapes
      .filter(p => p.isInstanceOf[Piece] && p.containsPoint(x,  y))
      .headOption
      .asInstanceOf[Option[Piece]]

  private def clickHandler(x: Int, y: Int) {
    val piece = getPieceClicked(x, y)
    println(s"${x} ${y} ${piece.isEmpty} ${state.activePiece.isEmpty}")
    (piece.nonEmpty, state.activePiece.nonEmpty) match {
      case (false, true) =>
        state.activePiece.foreach { p =>
          val move = p.getMove(Positioner.getPieceRow(y), Positioner.getPieceCol(x))
          println(move)
          p.setPos(Positioner.getPieceRow(y), Positioner.getPieceCol(x))
          state.activePiece = None
          notify(PlayerMoveEvent(move))
        }
      case (true, false) => state.activePiece = piece
      case _ => println("miss")
    }
  }
}
