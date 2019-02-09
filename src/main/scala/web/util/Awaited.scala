package web.util

class Awaited[T] {
  private var value: Option[T] = None

  def foreach(f: T => Unit) = value.foreach(f)
  def populate(v: T) = value = Some(v)
}

object Awaited {
  def apply[T] = new Awaited[T]
}
