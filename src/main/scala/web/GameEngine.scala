package web

import org.scalajs.dom.{CanvasRenderingContext2D, MouseEvent}
import org.scalajs.dom.html.Canvas
import scala.collection.mutable.TreeSet
import scala.scalajs.js.timers.setInterval


trait GameEngine {
  this: BussClient =>

  val board = TreeSet.empty[GameObject](GameObject)
  val pieces = TreeSet.empty[Piece](Piece)
  def shapes: Iterable[GameObject] = board.toIterable ++ pieces.toIterable

  var ctx2d: CanvasRenderingContext2D = null
  var canvas: Canvas = null
  val state: GameState = new GameState

  register[NewStateEvent](event => loadState(event.sfen))

  def init(canvas: Canvas, width: Int, height: Int, density: Double) {
    this.canvas = canvas
    ctx2d = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
    canvas.width = (width * density).toInt
    canvas.height = (height * density).toInt

    Positioner.setSize(width, height, density)
    loadObjects()
    //loadState()
    setInterval(500) { render() }
  }

  def loadState(sfen: String = "lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b") {
    def isNum(s: String) = s forall Character.isDigit
    def getPiecesWithCol(row: String): Array[(String, Int)] = {
      val items = row.reverse.split("(?!\\+)").reverse
      val ind = items.map(x => if (isNum(x)) x.toInt else 1).scanLeft(1)(_ + _)
      items
        .zip(ind)
        .filter(i => !isNum(i._1))
    }

    pieces.clear

    sfen
      .split(" ")(0)
      .split("/")
      .zipWithIndex
      .foreach{ case(x, row) =>
        getPiecesWithCol(x)
          .foreach{ case(p, col) => addPiece(Piece(PieceEnum.withName(p), row + 1, col)) }
      }
  }

  def loadObjects() {
    val board = new GameObject(0, 1)
    board
      .setImage("images/board.svg")
      .setScale(Positioner.getBoardScale)
      .setX(Positioner.getBoardX)
      .setY(Positioner.getBoardY)

    setBoard(board)
  }

  def render() {
    ctx2d.clearRect(0, 0, Positioner.width, Positioner.height)
    shapes.foreach(shape => if (shape.isActive) shape.render(ctx2d))
  }

  def setBoard(shape: GameObject) {
    board.clear
    board.add(shape)
  }

  def addPiece(piece: Piece) {
    pieces.add(piece)
  }
}
