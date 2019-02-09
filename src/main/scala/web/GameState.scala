package web

class GameState {
  var isBlackMove: Boolean = true
  var activePiece: Option[Piece] = None
}

class BoardState {
  private val board = Array.fill[Option[PieceConf.Value]](9, 9)(None)
  private var isBlackMove = true

  override def toString: String = {
    val boardStr = (9 to 1 by -1).map(i => s"  $i ").mkString("|") + "|\n" +
      board.map(_
          .map(_.getOrElse(" "))
          .map(a => f"${a}%3s")
          .mkString(" |")
      ).zipWithIndex
        .map { case(row, i) => s"${row} | ${(i + 97).toChar}"}
        .mkString("\n")
    val move = "Move: " + (if (isBlackMove) "black" else "white")

    s"${boardStr}\n${move}"
  }


}

/*
object BoardState {
  def fromSFEN(sfen: String): BoardState = {
    def isNum(s: String) = s forall Character.isDigit
    def getPiecesWithCol(row: String): Array[(String, Int)] = {
      val items = row.reverse.split("(?!\\+)").reverse
      val ind = items.map(x => if (isNum(x)) x.toInt else 1).scanLeft(1)(_ + _)
      items
        .zip(ind)
        .filter(i => !isNum(i._1))
    }

    sfen
      .split(" ")(0)
      .split("/")
      .zipWithIndex
      .flatMap{ case(x, row) =>
        getPiecesWithCol(x)
          .map{ case(p, col) => (PieceConf.withName(p), row + 1, col) }
      }
  }
}
*/
