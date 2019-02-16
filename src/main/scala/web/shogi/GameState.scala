package web.shogi

import web.objects.{Piece, PieceConf}

class GameState {
  var isBlackMove: Boolean = true
  var isPlayerBlack: Boolean = true
  var activePiece: Option[PieceInfo] = None
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
