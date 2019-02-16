package web.objects

import org.scalajs.dom.CanvasRenderingContext2D
import web.core.{BussClient, GameObject, ObjectConf, Positioner, InitEvent, NewStateEvent, PieceClicked}
import web.shogi.PieceInfo


object HandConf extends ObjectConf {
  val WP = Value("p")
  val WL = Value("l")
  val WN = Value("n")
  val WS = Value("s")
  val WG = Value("g")
  val WR = Value("r")
  val WB = Value("b")

  val BP = Value("P")
  val BL = Value("L")
  val BN = Value("N")
  val BS = Value("S")
  val BG = Value("G")
  val BR = Value("R")
  val BB = Value("B")

  lazy val scale = Positioner.getBoardScale
  lazy val x: Int = Positioner.getBoardX + (Positioner.boardSize * 1.03).toInt
  lazy val y: Int = Positioner.getBoardY
  lazy override val width: Int = Positioner.cellSize.toInt
  lazy override val height: Int = Positioner.boardSize.toInt

  val states: Map[Value, String] = Map(
    WP -> "images/piece_wp.svg",
    WL -> "images/piece_wl.svg",
    WN -> "images/piece_wn.svg",
    WS -> "images/piece_ws.svg",
    WG -> "images/piece_wg.svg",
    WR -> "images/piece_wr.svg",
    WB -> "images/piece_wb.svg",

    BP -> "images/piece_bp.svg",
    BL -> "images/piece_bl.svg",
    BN -> "images/piece_bn.svg",
    BS -> "images/piece_bs.svg",
    BG -> "images/piece_bg.svg",
    BR -> "images/piece_br.svg",
    BB -> "images/piece_bb.svg",
  )
}

abstract class Hand extends GameObject(HandConf) with BussClient {
  private var state: Map[HandConf.Value, Int] = Map()

  protected def pos2y(i: Int): Int
  protected def y2pos(y: Int): Int
  protected def pieceSize: Int = (baseSize * Positioner.getPieceScale).toInt
  protected val isBlack: Boolean

  register[NewStateEvent](updateState(_))

  private def updateState(event: NewStateEvent) {
    state = event.sfen.split(" ").lift(2).toList.flatMap(_.split(""))
      .filter(_.forall(_.isUpper) == isBlack)
      .map(HandConf.withName(_))
      .foldLeft(Map.empty[HandConf.Value, Int]) {
        (count, piece) => count + (piece -> (count.getOrElse(piece, 0) + 1))
      }
  }

  override def onClick(x: Int, y: Int) {
    val pos = y2pos(y)
    if (state.size > pos) {
      val piece = state.toList.sortWith(_._1 > _._1)(pos)._1
      notify(PieceClicked(PieceInfo(PieceConf.withName(piece.toString))))
    }
  }

  override def render(ctx2d: CanvasRenderingContext2D) {
    ctx2d.font = ctx2d.font.replaceAll("^\\d+px", s"${pieceSize / 3}px")

    state.toList.sortWith(_._1 > _._1).zipWithIndex.foreach { case((piece, count), pos) =>
      images(piece).foreach { i =>
        val y = pos2y(pos)
        ctx2d.drawImage(i, x, y, pieceSize, pieceSize)
        ctx2d.fillText(count.toString, x + pieceSize*0.9, y + pieceSize)
      }
    }
  }
}

class BlackHand extends Hand {
  setX(Positioner.getBoardX + (Positioner.boardSize * 1.03).toInt)
  protected def pos2y(i: Int): Int = (Positioner.boardSize + Positioner.getBoardY - (i + 1) * pieceSize).toInt
  protected def y2pos(y: Int): Int = ((Positioner.boardSize + Positioner.getBoardY - y) / pieceSize).toInt
  protected val isBlack: Boolean = true
}

class WhiteHand extends Hand {
  setX(Positioner.getBoardX - (Positioner.boardSize * 0.03).toInt - pieceSize)
  protected def pos2y(i: Int): Int = (Positioner.getBoardY + i * pieceSize).toInt
  protected def y2pos(y: Int): Int = ((y - Positioner.getBoardY) / pieceSize).toInt
  protected val isBlack: Boolean = false
}
