package web

import scala.scalajs.js.annotation.{JSExportTopLevel, JSExport}
import org.scalajs.dom.html.Canvas
import org.scalajs.dom.{document, window}

/**
 * TODO:
 *  - gameObjects tempaltes storage (Prefabs) and instantiation
 *  - extend pieces functionality with flipping and promotion
 *  + split gameObjects into board, pieces and suplementaries, combine them inside `def shapes`
 *  -? game object with mouse handling / coordinate resolving, board model
 *  -? resource loader with JSON like configration / removing configuration from the code
 *  - events queue to render screen ? timestamps and frame skiping
 *
 *  - webSocket command layer
 */

@JSExportTopLevel("ShogiContext")
object ShogiContext {
  var channel: Channel = null

  @JSExport("init")
  def init(canvasId: String) {
    val canvas = document.getElementById(canvasId).asInstanceOf[Canvas]
    val width = window.innerWidth
    val height = window.innerHeight
    val ration = window.devicePixelRatio

    GameEngine.init(canvas, width.toInt, height.toInt, ration)

    channel = new Channel(Args.gameId, Args.isBlack, "ws://127.0.0.1:9000/socket")
    channel.setStateHandler { sfen => GameEngine.loadState(sfen) }
    channel.init
    GameEngine.setPostMoveHandler { move => channel.sendMove(move) }

    println(document.location.search)
  }

}
