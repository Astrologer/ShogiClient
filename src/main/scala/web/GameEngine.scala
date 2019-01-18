package web

import scala.scalajs.js.annotation.{JSExportTopLevel, JSExport}
import org.scalajs.dom.CanvasRenderingContext2D
import org.scalajs.dom.html.Canvas
import org.scalajs.dom.WebSocket
import org.scalajs.dom
import scala.collection.mutable.TreeSet
import scala.scalajs.js.timers.setInterval

/**
 * TODO:
 *  - gameObjects tempaltes storage (Prefabs) and instantiation
 *  - extend pieces functionality with flipping and promotion
 *  - split gameObjects into board, pieces and suplementaries, combine them inside `def shapes`
 *  -? game object with mouse handling / coordinate resolving, board model
 *  -? resource loader with JSON like configration / removing configuration from the code
 *  - events queue to render screen ? timestamps and frame skiping
 *
 *  - webSocket command layer
 */

@JSExportTopLevel("GameEngine")
object GameEngine {
  val shapes = TreeSet.empty[GameObject](GameObject)
  var ctx2d: CanvasRenderingContext2D = null
  val state: GameState = new GameState
  var sock: WebSocket = null

  @JSExport("init")
  def init(canvas: Canvas, width: Int, height: Int, density: Double) {
    ctx2d = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
    canvas.width = (width * density).toInt
    canvas.height = (height * density).toInt

    Positioner.setSize((width * density).toInt, (height * density).toInt)
    loadObjects()
    loadState()
    setInterval(500) { render() }

    sock = new WebSocket("ws://127.0.0.1:9000/socket")
    sock.onopen = e => {
      sock.send("""{"type": "subs", "body": "111", "isBlack": true}""")
      setInterval(30000) { sock.send("""{"type": "ping", "body": "x"}""") }
      canvas.onclick = (e: dom.MouseEvent) => clickHandler((e.clientX * density).toInt, (e.clientY * density).toInt)
    }

    // sock.onclose = e => ...
    // sock.onmessage = e: dom.MessageEvent => ... update board

    shapes.foreach { p => println(s"${p.isInstanceOf[Piece]}") }
  }

  def getPieceClicked(x: Int, y: Int): Option[Piece] =
    shapes
      .filter(p => p.isInstanceOf[Piece] && p.containsPoint(x,  y))
      .headOption
      .asInstanceOf[Option[Piece]]

  def clickHandler(x: Int, y: Int) {
    val piece = getPieceClicked(x, y)
    println(s"${x} ${y} ${piece.isEmpty} ${state.activePiece.isEmpty}")
    (piece.nonEmpty, state.activePiece.nonEmpty) match {
      case (false, true) =>
        state.activePiece.foreach(_.setPos(Positioner.getPieceRow(y), Positioner.getPieceCol(x)))
        state.activePiece = None
      case (true, false) => state.activePiece = piece
      case _ => println("miss")
    }
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
