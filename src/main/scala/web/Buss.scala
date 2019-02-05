package web

import scala.reflect.ClassTag
import scala.reflect.classTag
import scala.language.existentials


sealed trait Event
case class InitEvent(gameId: String, isBlack: Boolean, url: String) extends Event
case class NewStateEvent(sfen: String) extends Event
case class PlayerMoveEvent(move: String) extends Event

trait Buss {
  type EventHandler[T] = T => Unit

  protected var callbacks: List[(Class[_], EventHandler[T] forSome {type T <: Event})] = Nil

  def register[T <: Event](handler: EventHandler[T])(implicit ct: ClassTag[T]) = callbacks = (ct.runtimeClass -> handler) :: callbacks

  def notify[T <: Event](event: T)(implicit ct: ClassTag[T]) = {
    callbacks
      .filter(_._1 == ct.runtimeClass)
      .foreach{ case(_, h) => h.asInstanceOf[EventHandler[T]](event) }
  }
}

object MessageBuss extends Buss

trait BussClient {
  val buss: Buss = MessageBuss

  def register[T <: Event](handler: Buss#EventHandler[T])(implicit ct: ClassTag[T]) = buss.register[T](handler)
  def notify[T <: Event](event: T)(implicit ct: ClassTag[T]) = buss.notify(event)
}
