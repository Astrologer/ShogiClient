package web

import org.scalajs.dom.html.Image
import org.scalajs.dom
import scala.collection.mutable.{Map => MutableMap}


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

  def pong(event: PongEvent) {
    if (event.foe) setState(ConnectConf.DOUBLE)
    else setState(ConnectConf.SINGLE)
  }
}
