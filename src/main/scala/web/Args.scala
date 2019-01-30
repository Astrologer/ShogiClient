package web

import scala.scalajs.js.URIUtils.decodeURIComponent
import org.scalajs.dom.document

object Args {
  val url = decodeURIComponent(document.location.search).replaceAll("^\\?", "")
  val config: Map[String, String] = if (url.isEmpty) Map() else url.split("&").map{x => val p = x.split("="); p(0) -> p(1)}.toMap

  val gameId: String = config.getOrElse("id", "")
  val isBlack: Boolean = config.getOrElse("side", "black") == "black"
}
