package web

import org.scalajs.dom.CanvasRenderingContext2D
import org.scalajs.dom.html.Image
import org.scalajs.dom


class Piece(id: Int) extends GameObject(1, id) {
}


object Piece extends Enumeration {
  val image, scale, x, y = Value
  val config: Map[String, List[(Value, String)]] = Map(
    "p" -> List((scale, "1.5"), (image, "images/piece_p.svg")),
    "xp" -> List((scale, "1.5")),
    "P" ->  List((scale, "1.5"))
  )
  var id: Int = 0

  def apply(kind: String) = {
    id += 1
    var piece = new Piece(id)

    config(kind).foreach { case (kind, value) => kind match {
      case Piece.scale => piece.setScale(value.toDouble)
      case Piece.image => piece.setImage(value)
      case Piece.x => piece.setX(value.toInt)
      case Piece.y => piece.setY(value.toInt)
    }}
    piece
  }
}
