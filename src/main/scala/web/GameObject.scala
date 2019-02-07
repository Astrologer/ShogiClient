package web

import org.scalajs.dom.html.Image
import org.scalajs.dom
import scala.collection.mutable.{Map => MutableMap}


trait ObjectConf extends Enumeration {
  val states: Map[Value, String]
  val scale: Double
  val x, y: Int

  private var loadedStates: MutableMap[Value, Option[Image]] = null

  def getStates(): MutableMap[Value, Option[Image]] = {
    if (loadedStates == null) {
      loadedStates = MutableMap()
      states.foreach { case (state, imagePath) =>
        val image = dom.document.createElement("img").asInstanceOf[Image]
        image.src = imagePath
        loadedStates(state) = None
        image.onload = {e => loadedStates(state) = Some(image)}
      }
    }
    loadedStates
  }
}

class GameObject[T <: ObjectConf](config: T, val layer: Int = 1) extends Renderable with MultiState[T#Value] {
  setScale(config.scale)
  setStates(config.getStates.asInstanceOf[MutableMap[T#Value, Option[Image]]])
  setX(config.x)
  setY(config.y)
}

object GameObject extends Ordering[GameObject[_]] {
  def compare(l: GameObject[_], r: GameObject[_]): Int = {
    if (l.id == r.id) 0
    else if (l.id < r.id) -1
    else 1
  }
}
