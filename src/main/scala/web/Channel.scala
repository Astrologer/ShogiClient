package web

import org.scalajs.dom.WebSocket
import org.scalajs.dom
import scala.scalajs.js.timers.{setInterval, clearInterval, setTimeout}
import scala.scalajs.js.timers.SetIntervalHandle
import web.Protocol._

class Channel(gameId: String, isBlack: Boolean, url: String) {
  protected var sock: Option[WebSocket] = None

  protected val PING_INTERVAL = 10000
  protected val RECONNECT_TIMEOUT = 5000

  protected var pingId: Long = 0
  protected var pingTs: Long = 0
  protected var pingLatency: Long = 0
  protected var pingIntervalId: SetIntervalHandle = null

  protected var stateHandler: String => Unit = identity

  protected def sendMessage(msg: Message) {
    sock.foreach { s =>
      s.send(msg.toJSON)
    }
  }

  protected def ping() {
    pingId += 1
    sendMessage(PingMessage(pingId))
    pingTs = System.currentTimeMillis
  }

  protected def register(gameId: String, isBlack: Boolean) {
    sendMessage(SubscribeMessage(gameId, isBlack))
  }

  protected def reconnect() {
    clearInterval(pingIntervalId)
    setTimeout(RECONNECT_TIMEOUT) { init }
  }

  protected def messageHandler(data: String) {
    Message.fromJSON(data).map(_ match {
      case PongMessage(id) => {
        pingLatency = System.currentTimeMillis - pingTs
        println(s"ping[$id]: $pingLatency")
      }

      case StateMessage(gameId, sfen, action) => stateHandler(sfen)

      case _ => println("miss")
    })
  }

  def init() {
    sock = Some(new WebSocket(url))

    sock.foreach { s =>
      s.onopen = e => {
        register(gameId, isBlack)
        pingIntervalId = setInterval(PING_INTERVAL) { ping }
      }
      s.onclose = e => reconnect
      s.onmessage = e => messageHandler(e.data.toString)
    }
  }

  def sendMove(move: String) {
    sendMessage(MoveMessage(gameId, move))
  }

  def setStateHandler(handler: String => Unit) {
    stateHandler = handler
  }
}
