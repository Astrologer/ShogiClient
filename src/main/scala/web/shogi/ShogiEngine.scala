package web.shogi

import web.core.{BussClient, GameEngine, MouseListener, GameObject, NewStateEvent, PieceClicked, BoardClicked, PlayerMoveEvent}
import web.util.Networking
import web.objects.{Board, ConnectIcon, BlackHand, WhiteHand}

import scala.collection.mutable.TreeSet


object ShogiEngine extends BussClient with GameEngine with MouseListener with Networking {
  private val background = TreeSet.empty[GameObject[_]](GameObject)

  def shapes: Iterable[GameObject[_]] = background.toIterable
  val state: GameState = new GameState

  register[PieceClicked](pieceClicked(_))
  register[BoardClicked](boardClicked(_))

  def loadShapes() {
    background.clear
    background.add(new ConnectIcon)
    background.add(new Board)
    background.add(new BlackHand)
    background.add(new WhiteHand)
  }

  def pieceClicked(event: PieceClicked) {
    state.activePiece = Some(event.piece)
    println(state.activePiece)
  }

  def boardClicked(event: BoardClicked) {
    state.activePiece.foreach { p =>
      val move = p.getMove(event.row, event.col)
      println(move)
      state.activePiece = None
      notify(PlayerMoveEvent(move))
    }
  }
}
