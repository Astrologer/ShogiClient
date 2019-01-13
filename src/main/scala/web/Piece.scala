package web

import org.scalajs.dom.CanvasRenderingContext2D
import org.scalajs.dom.html.Image
import org.scalajs.dom


class Piece(id: Int, var row: Int, var col: Int) extends GameObject(1, id) {
  setRow(row)
  setCol(col)
  setScale(Positioner.getPieceScale)

  println(s"$id -  $x $y, $row $col")
  def setPos(row: Int, col: Int) {
    setRow(row)
    setCol(col)
  }

  def setRow(row: Int) {
    this.row = row
    setY(Positioner.getPieceY(row))
  }

  def setCol(col: Int) {
    this.col = col
    setX(Positioner.getPieceX(col))
  }
}

object PieceEnum extends Enumeration {
  type PieceEmunType = Value
  val WP = Value("p")
  val WPx = Value("+p")
  val WL = Value("l")
  val WN = Value("n")
  val WS = Value("s")
  val WG = Value("g")
  val WK = Value("k")

  val BP = Value("P")
}

object Piece extends Enumeration {
  import PieceEnum._

  val image, scale, x, y = Value
  val config: Map[PieceEmunType, List[(Value, String)]] = Map(
    WP -> List((image, "images/piece_p.svg")),
    BP -> List((image, "images/piece_p.svg")),
    WPx -> List(),
  )
  var id: Int = 10

  def apply(kind: PieceEmunType, row: Int, col: Int) = {
    id += 1
    var piece = new Piece(id, row, col)

    config(kind).foreach { case (kind, value) => kind match {
      case Piece.scale => piece.setScale(value.toDouble)
      case Piece.image => piece.setImage(value)
      case Piece.x => piece.setX(value.toInt)
      case Piece.y => piece.setY(value.toInt)
    }}
    piece
  }
}
