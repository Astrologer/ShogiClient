package web

import scala.scalajs.js.annotation.{JSExportTopLevel, JSExport}
import org.scalajs.dom.html.Canvas
import org.scalajs.dom.{document, window, XMLHttpRequest, Event => DEvent, Node, Element}

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
  val BASE_CLIENT_URL = document.location.origin + document.location.pathname
  // val API_SERVER_URL = "http://127.0.0.1:9000"
  val API_SERVER_URL = "https://aqueous-sierra-80782.herokuapp.com"
  // val SOCK_SERVER_URL = "ws://127.0.0.1:9000/socket"
  val SOCK_SERVER_URL = "wss://aqueous-sierra-80782.herokuapp.com/socket"

  @JSExport("init")
  def init(canvasId: String) {
    if (Args.gameId.nonEmpty)
      startGame(canvasId)
    else
      createNew
  }

  def createNew() {
    val xhr = new XMLHttpRequest()

    xhr.open("GET", API_SERVER_URL, true)
    xhr.withCredentials = true
    xhr.onload = { (e: DEvent) =>
      if (xhr.status == 200) {
        val gameId = xhr.responseText
        val dialog = document.getElementById("dlg")
        val black = document.getElementById("black")
        val white = document.getElementById("white")
        val blackUrl = s"${BASE_CLIENT_URL}?id=${gameId}&side=black"
        val whiteUrl = s"${BASE_CLIENT_URL}?id=${gameId}&side=white"

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
    val ratio = window.devicePixelRatio

    ShogiEngine.init(canvas, width.toInt, height.toInt, ratio)
    ShogiEngine.notify(InitEvent(Args.gameId, Args.isBlack, SOCK_SERVER_URL))
  }

}
