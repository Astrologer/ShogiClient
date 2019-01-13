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

  @JSExport("init")
  def init(canvas: Canvas, width: Int, height: Int) {
    ctx2d = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
    canvas.width = width;
    canvas.height = height;

    Positioner.setSize(width, height)
    loadObjects()
    loadState()
    setInterval(500) { render() }
  }

  def loadState(sfen: String = "9/9/9/9/4PP3/4PP3/4PP3/9/9 b G") {
    def isNum(s: String) = s forall Character.isDigit
    def getPiecesWithCol(row: String): Array[(String, Int)] = {
      val items = row.reverse.split("(?!\\+)").reverse
      val ind = items.map(x => if (isNum(x)) x.toInt else 1).scanLeft(1)(_ + _)
      items
        .zip(ind)
        .filter(i => !isNum(i._1))
    }

    sfen
      .split(" ")(0)
      .split("/")
      .zipWithIndex
      .foreach{ case(x, row) =>
        getPiecesWithCol(x)
          .foreach{ case(p, col) => addShape(Piece(PieceEnum.withName(p), row + 1, col)) }
      }
  }

  def loadObjects() {
    val board = new GameObject(0, 1)
    board
      .setImage("images/board.svg")
      .setScale(Positioner.getBoardScale)
      .setX(Positioner.getBoardX)
      .setY(Positioner.getBoardY)

    addShape(board)
    println(shapes)
  }

  def render() {
    ctx2d.clearRect(0, 0, Positioner.width, Positioner.height)
    shapes.foreach(shape => if (shape.isActive) shape.render(ctx2d))
  }

  def addShape(shape: GameObject) {
    shapes.add(shape)
  }
}
