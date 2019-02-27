package web.shogi

import web.core.{BussClient, GameEngine, MouseListener, GameObject, NewStateEvent, PieceClicked, BoardClicked, PlayerMoveEvent, InitEvent, Positioner}
import web.util.Networking
import web.objects.{Board, ConnectIcon, BlackHand, WhiteHand, MoveIcon}

import scala.collection.mutable.TreeSet


object ShogiEngine extends BussClient with GameEngine with MouseListener with Networking {
  private val background = TreeSet.empty[GameObject[_]](GameObject)

  def shapes: Iterable[GameObject[_]] = background.toIterable
  val state: GameState = new GameState

  register[PieceClicked](pieceClicked(_))
  register[BoardClicked](boardClicked(_))
  register[InitEvent](init(_))

  private def init(event: InitEvent) {
    state.isPlayerBlack = event.isBlack
    Positioner.setSide(event.isBlack)
  }

  def loadShapes() {
    background.clear
    background.add(new ConnectIcon)
    background.add(new Board)
    background.add(new BlackHand)
    background.add(new WhiteHand)
    background.add(new MoveIcon)
  }

  private def pieceClicked(event: PieceClicked) {
    if (event.piece.black) {
      state.activePiece = Some(event.piece)
    } else state.activePiece.foreach { p =>
      state.activePiece = None
      notify(PlayerMoveEvent(p.getMove(event.piece)))
    }
  }

  private def boardClicked(event: BoardClicked) {
    state.activePiece.foreach { p =>
      state.activePiece = None
      notify(PlayerMoveEvent(p.getMove(event.row, event.col)))
    }
  }
}
