package web

import org.scalajs.dom.CanvasRenderingContext2D
import org.scalajs.dom.html.Canvas
import scala.scalajs.js.timers.setInterval


trait GameEngine {
  this: ObjectsStore =>

  var ctx2d: CanvasRenderingContext2D = null
  var canvas: Canvas = null
  val state: GameState = new GameState

  def init(canvas: Canvas, width: Int, height: Int, density: Double) {
    this.canvas = canvas
    ctx2d = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
    canvas.width = (width * density).toInt
    canvas.height = (height * density).toInt

    Positioner.setSize(width, height, density)
    loadObjects
    setInterval(500) { render() }
  }

  def render() {
    ctx2d.clearRect(0, 0, Positioner.width, Positioner.height)
    shapes.foreach(shape => if (shape.isActive) shape.render(ctx2d))
  }
}
