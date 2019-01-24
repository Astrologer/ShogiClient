package web

import scala.scalajs.js.URIUtils.decodeURIComponent
import org.scalajs.dom.document

object Args {
  val url = decodeURIComponent(document.location.search)
  val config = url.substring(1).split("&").map{x => val p = x.split("="); p(0) -> p(1)}.toMap

  val gameId: String = config.getOrElse("id", "0")
  val isBlack: Boolean = config.getOrElse("black", "true").toBoolean
}
