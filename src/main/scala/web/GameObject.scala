package web

import org.scalajs.dom.CanvasRenderingContext2D
import org.scalajs.dom.html.Image
import org.scalajs.dom


class GameObject(val layer: Int, val id: Int) {
  var baseSize = 100
  var scale: Double = 1
  var x: Int = 0
  var y: Int = 0
  var active: Boolean = true
  var image: Option[Image] = None

  def render(ctx2d: CanvasRenderingContext2D) {
    image.foreach(i => ctx2d.drawImage(i, x, y, baseSize * scale, baseSize * scale))
  }

  def containsPoint(x: Int, y: Int): Boolean =
    this.x <= x && this.x + baseSize * scale >= x && this.y <= y && this.y + baseSize * scale >= y

  def setImage(image: Image): GameObject = {
    this.image = Some(image)
    this
  }

  def setImage(path: String): GameObject = {
    val image = dom.document.createElement("img").asInstanceOf[Image]
    image.src = path
    image.onload = {e => this.setImage(image)}
    this
  }

  def setX(x: Int): GameObject = {
    this.x = x
    this
  }

  def setY(y: Int): GameObject = {
    this.y = y
    this
  }

  def setScale(scale: Double): GameObject = {
    this.scale = scale
    this
  }

  def isActive = active

  def adjustPosition() {
    // TODO update X, Y and scale after window resize
    // should be overriden by child classes
  }
}


object GameObject extends Ordering[GameObject] {
  def compare(l: GameObject, r: GameObject): Int = {
    if (l.id == r.id) 0
    else if (l.id < r.id) -1
    else 1
  }
}
