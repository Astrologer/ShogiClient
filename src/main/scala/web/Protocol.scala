package web

import scala.scalajs.js.annotation.{JSGlobal, JSExport}
import scala.scalajs.js.{JSON, isUndefined, Dynamic}
import scala.scalajs.js

object Protocol {

  @js.native
  @JSGlobal("_rawMessage")
  class RawMessage extends js.Object {
    val `type`: String = js.native
    val arg1: js.UndefOr[String] = js.native
    val arg2: js.UndefOr[String] = js.native
    val arg3: js.UndefOr[String] = js.native
  }

  object RawMessage {
    def apply(`type`: String,
              arg1: Option[String] = None,
              arg2: Option[String] = None,
              arg3: Option[String] = None): RawMessage ={
      import js.JSConverters._
      js.Dynamic.literal(
        `type` = `type`,
        arg1 = arg1.orUndefined,
        arg2 = arg2.orUndefined,
        arg3 = arg3.orUndefined
      ).asInstanceOf[RawMessage]
    }
  }

  sealed abstract class Message(
      val `type`: String,
      val arg1: Option[String] = None,
      val arg2: Option[String] = None,
      val arg3: Option[String] = None) {

    def toJSON = {
      println(s"message -> $this")
      JSON.stringify(RawMessage(`type`, arg1, arg2, arg3))
    }
  }

  object Message {
    def fromJSON(json: String): Option[Message] = {
      println(s"message <- $json")

      val raw = JSON.parse(json).asInstanceOf[RawMessage]
      val tpe = raw.`type`
      val arg1 = raw.arg1.toOption
      val arg2 = raw.arg2.toOption
      val arg3 = raw.arg3.toOption

      val msg: Option[Message] = tpe match {
        case "state" => for (gameId <- arg1; sfen <- arg2; action <- arg3) yield StateMessage(gameId, sfen, action)
        case "subs" => for (gameId <- arg1; isBlack <- arg2) yield SubscribeMessage(gameId, isBlack.toBoolean)
        case "move" => for (gameId <- arg1; move <- arg2) yield MoveMessage(gameId, move)
        case "ping" => for (id <- arg1) yield PingMessage(id.toLong)
        case "pong" => for (id <- arg1) yield PongMessage(id.toLong)

        case _ => None
      }
      if (msg.isEmpty)
        println(s"Mainformed message $this")

      msg
    }
  }

  case class PingMessage(id: Long) extends Message("ping", Some(id.toString))
  case class PongMessage(id: Long) extends Message("pong", Some(id.toString))
  case class StateMessage(gameId: String, sfen: String, action: String) extends Message("state", Some(gameId), Some(sfen), Some(action))
  case class SubscribeMessage(gameId: String, isBlack: Boolean) extends Message("subs", Some(gameId), Some(isBlack.toString))
  case class MoveMessage(gameId: String, move: String) extends Message("move", Some(gameId), Some(move))
}
