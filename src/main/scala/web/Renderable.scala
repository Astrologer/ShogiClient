package web

import org.scalajs.dom.CanvasRenderingContext2D
import org.scalajs.dom.html.Image
import org.scalajs.dom


object Counter {
  var id: Int = 0
  def getNext: Int = { id += 1; id }
}

trait IdGenerator {
  val counter: Counter.type = Counter
  def getId: Int = counter.getNext
}

trait Renderable extends IdGenerator {
  val layer: Int
  val id: Int = getId
  var baseSize = 100
  var scale: Double = 1
  var x: Int = 0
  var y: Int = 0
  var active: Boolean = true
  var image: Awaited[Image] = Awaited[Image]

  def render(ctx2d: CanvasRenderingContext2D) {
    image.foreach(i => ctx2d.drawImage(i, x, y, baseSize * scale, baseSize * scale))
  }

  def containsPoint(x: Int, y: Int): Boolean =
    this.x <= x && this.x + baseSize * scale >= x && this.y <= y && this.y + baseSize * scale >= y

  def setImage(image: Awaited[Image]): Renderable = {
    this.image = image
    this
  }

  def setImage(image: Image): Renderable = {
    this.image.populate(image)
    this
  }

  def setImage(path: String): Renderable = {
    val image = dom.document.createElement("img").asInstanceOf[Image]
    image.src = path
    image.onload = {e => this.setImage(image)}
    this
  }

  def setX(x: Int): Renderable = {
    this.x = x
    this
  }

  def setY(y: Int): Renderable = {
    this.y = y
    this
  }

  def setScale(scale: Double): Renderable = {
    this.scale = scale
    this
  }

  def isActive = active

  def adjustPosition() {
    // TODO update X, Y and scale after window resize
    // should be overriden by child classes
  }
}



