package web

import org.scalajs.dom.html.Image
import org.scalajs.dom
import scala.collection.mutable.Map


trait MultiState[T <: Enumeration#Value] {
  this: Renderable =>

  private var images: Map[T, Option[Image]] = Map()
  private var currentState: Option[T] = None

  def getStates: Map[T, Option[Image]] = images
  def setStates(states: Map[T, Option[Image]]) = images = states
  def getState: Option[T] = currentState

  def setState(state: T): Unit = {
    setImage(images(state))
    currentState = Some(state)
  }

  def setImage(state: T, image: Option[Image]): Unit = images(state) = image
  def setImage(state: T, path: String): Unit = {
    val image = dom.document.createElement("img").asInstanceOf[Image]
    image.src = path
    setImage(state, None)
    image.onload = {e => this.setImage(state, Some(image))}
  }
}
