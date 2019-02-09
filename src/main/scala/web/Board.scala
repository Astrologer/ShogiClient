package web


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
  register[InitEvent](init(_))

  private def init(event: InitEvent) {
    if (event.isBlack) setState(BoardConf.BLACK)
    else setState(BoardConf.WHITE)
  }
}
