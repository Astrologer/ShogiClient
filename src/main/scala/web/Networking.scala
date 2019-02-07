package web

import org.scalajs.dom.WebSocket
import org.scalajs.dom
import scala.scalajs.js.timers.{setInterval, clearInterval, setTimeout}
import scala.scalajs.js.timers.SetIntervalHandle
import web.Protocol._

trait Networking {
  this: BussClient =>

  private var gameId: String = null
  private var isBlack: Boolean = true
  private var url: String = null

  private var sock: Option[WebSocket] = None

  private val PING_INTERVAL = 10000
  private val RECONNECT_TIMEOUT = 5000

  private var pingId: Long = 0
  private var pingTs: Long = 0
  private var pingLatency: Long = 0
  private var pingIntervalId: SetIntervalHandle = null

  register[PlayerMoveEvent](m => sendMove(m.move))
  register[InitEvent](init(_))

  private def init(event: InitEvent) {
    gameId = event.gameId
    isBlack = event.isBlack
    url = event.url

    init
  }

  private def sendMessage(msg: Message) {
    sock.foreach { s =>
      s.send(msg.toJSON)
    }
  }

  private def ping() {
    pingId += 1
    sendMessage(PingMessage(pingId))
    pingTs = System.currentTimeMillis
  }

  private def register(gameId: String, isBlack: Boolean) {
    sendMessage(SubscribeMessage(gameId, isBlack))
  }

  private def reconnect() {
    clearInterval(pingIntervalId)
    setTimeout(RECONNECT_TIMEOUT) { init }
  }

  private def messageHandler(data: String) {
    Message.fromJSON(data).map(_ match {
      case PongMessage(id) => {
        pingLatency = System.currentTimeMillis - pingTs
        println(s"ping[$id]: $pingLatency")
        notify(PongEvent(true))
      }

      case StateMessage(gameId, sfen, action) => notify(NewStateEvent(sfen))

      case _ => println("miss")
    })
  }

  private def init() {
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
}
