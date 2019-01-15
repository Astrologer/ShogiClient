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
  val WPX = Value("+p")
  val WL = Value("l")
  val WLX = Value("+l")
  val WN = Value("n")
  val WNX = Value("+n")
  val WS = Value("s")
  val WSX = Value("+s")
  val WG = Value("g")
  val WK = Value("k")
  val WR = Value("r")
  val WRX = Value("+r")
  val WB = Value("b")
  val WBX = Value("+b")

  val BP = Value("P")
  val BPX = Value("+P")
  val BL = Value("L")
  val BLX = Value("+L")
  val BN = Value("N")
  val BNX = Value("+N")
  val BS = Value("S")
  val BSX = Value("+S")
  val BG = Value("G")
  val BK = Value("K")
  val BR = Value("R")
  val BRX = Value("+R")
  val BB = Value("B")
  val BBX = Value("+B")
}

object Piece extends Enumeration {
  import PieceEnum._

  val image, scale, x, y = Value
  val config: Map[PieceEmunType, List[(Value, String)]] = Map(
    WP -> List((image, "images/piece_wp.svg")),
    WPX -> List((image, "images/piece_wp.svg")),
    WL -> List((image, "images/piece_wp.svg")),
    WLX -> List((image, "images/piece_wp.svg")),
    WN -> List((image, "images/piece_wp.svg")),
    WNX -> List((image, "images/piece_wp.svg")),
    WS -> List((image, "images/piece_wp.svg")),
    WSX -> List((image, "images/piece_wp.svg")),
    WG -> List((image, "images/piece_wp.svg")),
    WK -> List((image, "images/piece_wp.svg")),
    WR -> List((image, "images/piece_wp.svg")),
    WRX -> List((image, "images/piece_wp.svg")),
    WB -> List((image, "images/piece_wp.svg")),
    WBX -> List((image, "images/piece_wp.svg")),

    BP -> List((image, "images/piece_bp.svg")),
    BPX -> List((image, "images/piece_bp.svg")),
    BL -> List((image, "images/piece_bp.svg")),
    BLX -> List((image, "images/piece_bp.svg")),
    BN -> List((image, "images/piece_bp.svg")),
    BNX -> List((image, "images/piece_bp.svg")),
    BS -> List((image, "images/piece_bp.svg")),
    BSX -> List((image, "images/piece_bp.svg")),
    BG -> List((image, "images/piece_bp.svg")),
    BK -> List((image, "images/piece_bp.svg")),
    BR -> List((image, "images/piece_bp.svg")),
    BRX -> List((image, "images/piece_bp.svg")),
    BB -> List((image, "images/piece_bp.svg")),
    BBX -> List((image, "images/piece_bp.svg")),
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
