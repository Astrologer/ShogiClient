package web.objects

import web.core.{BussClient, GameObject, ObjectConf, InitEvent, PongEvent}

object ConnectConf extends ObjectConf {
  val SINGLE, DOUBLE = Value
  val scale = 1
  val x = 10
  val y = 10

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
