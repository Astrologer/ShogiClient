package web.objects

import web.core.{BussClient, GameObject, ObjectConf, InitEvent, PongEvent, Positioner}

object ConnectConf extends ObjectConf {
  val SINGLE, DOUBLE = Value
  val scale = Positioner.getPieceScale / 2
  def x = (Positioner.width  - 100 * scale - 10).toInt
  def y = 10

  val states: Map[Value, String] = Map(
    SINGLE -> "images/connection_part.svg",
    DOUBLE -> "images/connection_full.svg"
  )
}

class ConnectIcon extends GameObject(ConnectConf) with BussClient {
  register[PongEvent](pong(_))
  register[InitEvent](_ => init)

  def init = setState(ConnectConf.SINGLE)
  def pong(event: PongEvent) {
    if (event.foe) setState(ConnectConf.DOUBLE)
    else setState(ConnectConf.SINGLE)
  }
}
