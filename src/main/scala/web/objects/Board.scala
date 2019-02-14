package web.objects

import org.scalajs.dom.CanvasRenderingContext2D
import web.core.{BussClient, GameObject, ObjectConf, Positioner, InitEvent, NewStateEvent, PieceClicked, BoardClicked}
import web.shogi.PieceInfo


object BoardConf extends ObjectConf {
  val BLACK, WHITE = Value
  lazy val scale = Positioner.getBoardScale
  lazy val x = Positioner.getBoardX
  lazy val y = Positioner.getBoardY

  val states: Map[Value, String] = Map(
    BLACK -> "images/board.svg",
    WHITE -> "images/board.svg"
  )
}

class Board extends GameObject(BoardConf) with BussClient {
  var pieces: Iterable[Piece] = List()

  register[InitEvent](init(_))
  register[NewStateEvent](event => loadState(event.sfen))

  private def init(event: InitEvent) {
    if (event.isBlack) setState(BoardConf.BLACK)
    else setState(BoardConf.WHITE)
  }

  override def render(ctx2d: CanvasRenderingContext2D) {
    super.render(ctx2d)
    pieces.foreach { _.render(ctx2d) }
  }

  override def onClick(x: Int, y: Int) {
    val piece = pieces
      .filter(_.containsPoint(x,  y))
      .headOption

    if (piece.nonEmpty) {
      piece.foreach { piece =>
        piece.getState.foreach {
          state => notify(PieceClicked(PieceInfo(state, piece.row, piece.col)))
        }
      }
    } else {
      notify(BoardClicked(Positioner.getPieceRow(y), Positioner.getPieceCol(x)))
    }
  }

  // TODO preload all objects and update state duing loadState operation
  private def loadState(sfen: String) {
    pieces = sfen
      .split(" ")(0)
      .split("/")
      .zipWithIndex
      .flatMap{ case(x, row) =>
        getPiecesWithCol(x)
          .map{ case(p, col) => new Piece(PieceConf.withName(p), row + 1, col) }
      }
  }

  private def isNum(s: String) = s forall Character.isDigit

  private def getPiecesWithCol(row: String): Array[(String, Int)] = {
    val items = row.reverse.split("(?!\\+)").reverse
    val ind = items.map(x => if (isNum(x)) x.toInt else 1).scanLeft(1)(_ + _)
    items
      .zip(ind)
      .filter(i => !isNum(i._1))
  }
}
