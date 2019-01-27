package web

import scala.scalajs.js.annotation.{JSExportTopLevel, JSExport}
import org.scalajs.dom.html.Canvas
import org.scalajs.dom.{document, window, XMLHttpRequest, Event, Node, Element}

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
    if (Args.gameId.nonEmpty)
      startGame(canvasId)
    else
      createNew
  }

  def createNew() {
    val xhr = new XMLHttpRequest()

    xhr.open("GET", "http://127.0.0.1:9000", true)
    xhr.withCredentials = true
    xhr.onload = { (e: Event) =>
      if (xhr.status == 200) {
        val gameId = xhr.responseText
        val dialog = document.getElementById("dlg")
        val black = document.getElementById("black").asInstanceOf[Element]
        val white = document.getElementById("white")
        val blackUrl = s"https://astrologer.github.io/ShogiClient?id=${gameId}&side=black"
        val whiteUrl = s"https://astrologer.github.io/ShogiClient?id=${gameId}&side=white"

        black.textContent = blackUrl
        black.setAttribute("href", blackUrl)
        white.textContent = whiteUrl
        white.setAttribute("href", whiteUrl)
        dialog.removeAttribute("hidden")
      }
    }
    xhr.send()

  }

  def startGame(canvasId: String) {
    val canvas = document.getElementById(canvasId).asInstanceOf[Canvas]
    val width = window.innerWidth
    val height = window.innerHeight
    val ration = window.devicePixelRatio

    GameEngine.init(canvas, width.toInt, height.toInt, ration)

    channel = new Channel(Args.gameId, Args.isBlack, "ws://127.0.0.1:9000/socket")
    channel.setStateHandler { sfen => GameEngine.loadState(sfen) }
    channel.init
    GameEngine.setPostMoveHandler { move => channel.sendMove(move) }
  }

}
