package web.objects

import web.core.{GameObject, ObjectConf, Positioner}

object PieceConf extends ObjectConf {
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

  val states: Map[Value, String] = Map(
    WP -> "images/piece_wp.svg",
    WPX -> "images/piece_wpx.svg",
    WL -> "images/piece_wl.svg",
    WLX -> "images/piece_wlx.svg",
    WN -> "images/piece_wn.svg",
    WNX -> "images/piece_wnx.svg",
    WS -> "images/piece_ws.svg",
    WSX -> "images/piece_wsx.svg",
    WG -> "images/piece_wg.svg",
    WK -> "images/piece_wk.svg",
    WR -> "images/piece_wr.svg",
    WRX -> "images/piece_wrx.svg",
    WB -> "images/piece_wb.svg",
    WBX -> "images/piece_wbx.svg",

    BP -> "images/piece_bp.svg",
    BPX -> "images/piece_bpx.svg",
    BL -> "images/piece_bl.svg",
    BLX -> "images/piece_blx.svg",
    BN -> "images/piece_bn.svg",
    BNX -> "images/piece_bnx.svg",
    BS -> "images/piece_bs.svg",
    BSX -> "images/piece_bsx.svg",
    BG -> "images/piece_bg.svg",
    BK -> "images/piece_bk.svg",
    BR -> "images/piece_br.svg",
    BRX -> "images/piece_brx.svg",
    BB -> "images/piece_bb.svg",
    BBX -> "images/piece_bbx.svg",
  )

  lazy val scale = Positioner.getPieceScale
  val x = 0
  val y = 0
}

class Piece() extends GameObject(PieceConf) {
  var row: Int = 0
  var col: Int = 0

  def this(state: PieceConf.Value, row: Int, col: Int) {
    this
    setState(state)
    setPos(row, col)
  }

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

  def getMove(row: Int, col: Int): String = {
    getState.map{ kind =>
      s"${kind}${10 - this.col}${(this.row + 96).toChar}-${10 - col}${(row + 96).toChar}"
    }.getOrElse("")
  }
}

object Piece extends Ordering[Piece] {
  def compare(l: Piece, r: Piece): Int = {
    if (l.id == r.id) 0
    else if (l.id < r.id) -1
    else 1
  }
}
