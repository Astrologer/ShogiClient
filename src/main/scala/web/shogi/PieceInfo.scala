package web.shogi

import web.objects.PieceConf


object PieceTypes extends Enumeration {
  val PAWN, ROCK, BISHOP, LINE, KNIGHT, SIVER, GOLD, KING = Value
}

case class PieceInfo (
  kind: PieceTypes.Value,
  black: Boolean,
  promoted: Boolean,
  row: Int,
  col: Int,
  hand: Boolean = false
) {
  def getMove(row: Int, col: Int): String =
    s"${state}${10 - this.col}${(this.row + 96).toChar}-${10 - col}${(row + 96).toChar}"

  def state: PieceConf.Value = PieceInfo.piece2state(kind, black, promoted)
}

object PieceInfo {
  import PieceTypes._
  import PieceConf._

  private val state2piece: Map[PieceConf.Value, (PieceTypes.Value, Boolean, Boolean)] = Map(
    WP -> (PAWN, false, false),
    BP -> (PAWN, true, false),
    WPX -> (PAWN, false, true),
    BPX -> (PAWN, true, true),
    /*... TODO*/
  )

  private val piece2state: Map[(PieceTypes.Value, Boolean, Boolean), PieceConf.Value] = state2piece.map(_.swap)

  def apply(tpe: PieceConf.Value, row: Int, col: Int): PieceInfo = {
    val (kind, black, promoted) = state2piece(tpe)
    PieceInfo(kind, black, promoted, row, col)
  }

  def apply(tpe: PieceConf.Value): PieceInfo = {
    val (kind, black, promoted) = state2piece(tpe)
    PieceInfo(kind, black, false, 0, 0, true)
  }
}
