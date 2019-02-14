package web.core

import org.scalajs.dom.MouseEvent

import web.objects.Piece


trait MouseListener {
  this: GameEngine with BussClient =>

  register[InitEvent](_ => init)

  private def init() {
    canvas.onclick = (e: MouseEvent) => clickHandler(Positioner.scale(e.clientX), Positioner.scale(e.clientY))
  }

  private def clickHandler(x: Int, y: Int) {
    shapes
      .filter(_.containsPoint(x,  y))
      .foreach(_.onClick(x, y))
  }
}
