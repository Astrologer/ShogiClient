package web

import scala.scalajs.js.annotation.{JSExportTopLevel, JSExport}
import org.scalajs.dom.CanvasRenderingContext2D
import org.scalajs.dom.html.Canvas
import scala.collection.mutable.TreeSet
import scala.scalajs.js.timers.setInterval

/**
 * TODO:
 *  - game object with mouse handling / coordinate resolving, board model
 *  - resource loader with JSON like configration / removing configuration from the code
 *  - events queue to render screen ? timestamps and frame skiping
 *
 *  - webSocket command layer
 */

@JSExportTopLevel("GameEngine")
object GameEngine {
  val shapes = TreeSet.empty[GameObject](GameObject)
  var ctx2d: CanvasRenderingContext2D = null
  var width: Int = 0
  var height: Int = 0

  @JSExport("init")
  def init(canvas: Canvas, width: Int, height: Int) {
    ctx2d = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
    canvas.width = width;
    canvas.height = height;
    this.width = width
    this.height = height

    loadObjects()
    setInterval(500) { render() }
  }

  def loadState(sfen: String = "9/9/9/9/4P4/9/9/9/9 b G") {
  }

  def loadObjects() {
    val board = new GameObject(0, 1)
    board
      .setImage("images/board.svg")
      .setScale(15)
      .setX(800)
      .setY(120)

    val piece = new GameObject(0, 2)
    piece
      .setImage("images/piece_p.svg")
      .setScale(1.5)
      .setX(876)
      .setY(195)

    addShape(board)
    addShape(piece)
  }

  def render() {
    ctx2d.clearRect(0, 0, width, height)
    shapes.foreach(shape => if (shape.isActive) shape.render(ctx2d))
  }

  def addShape(shape: GameObject) {
    shapes.add(shape)
  }
}
