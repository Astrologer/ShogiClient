package web.core

import org.scalajs.dom.html.Image
import org.scalajs.dom
import scala.collection.mutable.{Map => MutableMap}

import web.util.Awaited

trait ObjectConf extends Enumeration {
  def states: Map[Value, String]
  def scale: Double
  def x: Int
  def y: Int
  def width: Int = (100 * scale).toInt
  def height: Int = (100 * scale).toInt

  private var loadedStates: MutableMap[Value, Awaited[Image]] = null

  def getStates(): MutableMap[Value, Awaited[Image]] = {
    if (loadedStates == null) {
      loadedStates = MutableMap()
      states.foreach { case (state, imagePath) =>
        val image = dom.document.createElement("img").asInstanceOf[Image]
        image.src = imagePath
        loadedStates(state) = Awaited[Image]
        image.onload = {e => loadedStates(state).populate(image)}
      }
    }
    loadedStates
  }
}

abstract class GameObject[T <: ObjectConf](config: T, val layer: Int = 1) extends Renderable with MultiState[T#Value] {
  setScale(config.scale)
  setStates(config.getStates.asInstanceOf[MutableMap[T#Value, Awaited[Image]]])
  setX(config.x)
  setY(config.y)
  setWidth(config.width)
  setHeight(config.height)

  def onClick(x: Int, y: Int) {}
}

object GameObject extends Ordering[GameObject[_]] {
  def compare(l: GameObject[_], r: GameObject[_]): Int = {
    if (l.id == r.id) 0
    else if (l.id < r.id) -1
    else 1
  }
}
