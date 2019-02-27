package web.objects

import web.core.{BussClient, GameObject, ObjectConf, InitEvent, NewStateEvent, Positioner}

object MoveConf extends ObjectConf {
  val WHITE, BLACK = Value
  val scale = Positioner.getPieceScale / 2
  def x = Positioner.getBoardX - (Positioner.cellSize / 1.5).toInt
  def y = Positioner.getBoardY + (Positioner.boardSize - Positioner.cellSize / 2).toInt

  val states: Map[Value, String] = Map(
    BLACK -> "images/move_icon_black.svg",
    WHITE -> "images/move_icon_white.svg"
  )
}

class MoveIcon extends GameObject(MoveConf) with BussClient {
  register[InitEvent](init(_))
  register[NewStateEvent](move(_))

  private def init(event: InitEvent) {
    println(s"move init $event")
    if (event.isBlack) setState(MoveConf.BLACK)
    else setState(MoveConf.WHITE)
  }

  private def move(event: NewStateEvent) {
    println(s"move update $event")
    getState.foreach { state =>
      println(s"${(state == MoveConf.BLACK) == isBlackMove(event.sfen)}")
      setActive((state == MoveConf.BLACK) == isBlackMove(event.sfen))
    }
  }

  private def isBlackMove(sfen: String): Boolean = sfen.split(" ").lift(1).getOrElse("b").map(_.toLower) == "b"
}
