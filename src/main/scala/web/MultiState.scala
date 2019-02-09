package web

import org.scalajs.dom.html.Image
import scala.collection.mutable.Map


trait MultiState[T <: Enumeration#Value] {
  this: Renderable =>

  private var images: Map[T, Awaited[Image]] = Map()
  private var currentState: Option[T] = None

  def getStates: Map[T, Awaited[Image]] = images
  def setStates(states: Map[T, Awaited[Image]]) = images = states
  def getState: Option[T] = currentState

  def setState(state: T): Unit = {
    setImage(images(state))
    currentState = Some(state)
  }
}
