package web.core

import org.scalajs.dom.html.Image
import scala.collection.mutable.Map

import web.util.Awaited

trait MultiState[T <: Enumeration#Value] {
  this: Renderable =>

  protected var images: Map[T, Awaited[Image]] = Map()
  protected var currentState: Option[T] = None

  def getStates: Map[T, Awaited[Image]] = images
  def setStates(states: Map[T, Awaited[Image]]) = images = states
  def getState: Option[T] = currentState

  def setState(state: T): Unit = {
    setImage(images(state))
    currentState = Some(state)
  }
}
