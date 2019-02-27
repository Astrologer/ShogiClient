package web.shogi

import web.objects.PieceConf
import org.scalajs.dom.window

object PieceTypes extends Enumeration {
  val PAWN, ROCK, BISHOP, LINE, KNIGHT, SILVER, GOLD, KING = Value
}

case class PieceInfo (
  kind: PieceTypes.Value,
  black: Boolean,
  promoted: Boolean,
  row: Int,
  col: Int,
  isBlack: Boolean,
  hand: Boolean = false
) {
  private def getCell(col: Int, row: Int): String = s"${10 - col}${(row + 96).toChar}"
  private def checkPromotion(fromRow: Int, toRow: Int): Boolean = {
    if (!promoted && kind != PieceTypes.KING && kind != PieceTypes.GOLD &&
        ((isBlack && (fromRow < 4 || toRow < 4)) || (!isBlack && (fromRow > 6 || toRow > 6)))) {
      window.confirm("Do you want to promote?")
    } else false
  }

  def getMove(row: Int, col: Int): String = {
    if (hand) s"${state}*${getCell(col, row)}"
    else {
      val p = if (checkPromotion(row, this.row)) "+" else ""
      s"${state}${getCell(this.col, this.row)}-${getCell(col, row)}${p}"
    }
  }

  def getMove(piece: PieceInfo): String = {
    val p = if (checkPromotion(piece.row, this.row)) "+" else ""
    s"${state}${getCell(this.col, this.row)}x${getCell(piece.col, piece.row)}${p}"
  }

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

    WR -> (ROCK, false, false),
    BR -> (ROCK, true, false),
    WRX -> (ROCK, false, true),
    BRX -> (ROCK, true, true),

    WB -> (BISHOP, false, false),
    BB -> (BISHOP, true, false),
    WBX -> (BISHOP, false, true),
    BBX -> (BISHOP, true, true),

    WL -> (LINE, false, false),
    BL -> (LINE, true, false),
    WLX -> (LINE, false, true),
    BLX -> (LINE, true, true),

    WN -> (KNIGHT, false, false),
    BN -> (KNIGHT, true, false),
    WNX -> (KNIGHT, false, true),
    BNX -> (KNIGHT, true, true),

    WS -> (SILVER, false, false),
    BS -> (SILVER, true, false),
    WSX -> (SILVER, false, true),
    BSX -> (SILVER, true, true),

    WG -> (GOLD, false, false),
    BG -> (GOLD, true, false),

    WK -> (KING, false, false),
    BK -> (KING, true, false),
  )

  private val piece2state: Map[(PieceTypes.Value, Boolean, Boolean), PieceConf.Value] = state2piece.map(_.swap)

  def apply(tpe: PieceConf.Value, isBlack: Boolean, row: Int, col: Int): PieceInfo = {
    val (kind, black, promoted) = state2piece(tpe)
    PieceInfo(kind, black, promoted, row, col, isBlack)
  }

  def apply(tpe: PieceConf.Value, isBlack: Boolean): PieceInfo = {
    val (kind, black, promoted) = state2piece(tpe)
    PieceInfo(kind, black, false, 0, 0, isBlack, true)
  }
}
