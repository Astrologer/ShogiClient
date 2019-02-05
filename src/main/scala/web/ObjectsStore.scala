package web

import scala.collection.mutable.TreeSet

/**
 * Loads objects and shares them via `shapes` Iterable object.
 * Can be aware about objects specifics but shuldn't be avare
 * about game logic
 */
trait ObjectsStore {
  this: BussClient =>

  private val board = TreeSet.empty[GameObject](GameObject)
  private val pieces = TreeSet.empty[Piece](Piece)
  private val pieceFactory: Piece = Piece()

  def shapes: Iterable[GameObject] = board.toIterable ++ pieces.toIterable

  // TODO shouldn't be here, it's a game logic already, so it should be moved
  // to ShogiEngine as a main game logic router.
  register[NewStateEvent](event => loadState(event.sfen))
  register[InitEvent](_ => init)

  private def init() {
    loadObjects()
  }

  private def loadObjects() {
    val board = new GameObject(0)
    board
      .setImage("images/board.svg")
      .setScale(Positioner.getBoardScale)
      .setX(Positioner.getBoardX)
      .setY(Positioner.getBoardY)

    setBoard(board)
  }

  private def setBoard(shape: GameObject) {
    board.clear
    board.add(shape)
  }

  private def addPiece(piece: Piece) {
    pieces.add(piece)
  }

  // ex "lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b"
  private def loadState(sfen: String) {
    val conf = parseSfen(sfen)
    pieces.clear
    conf.foreach{ case(state, row, col) =>
      val piece = pieceFactory.copy
      piece.setState(state)
      piece.setPos(row, col)
      pieces.add(piece)
    }
  }

  private def parseSfen(sfen: String): Seq[(PieceEnum.PieceEmunType, Int, Int)] = {
    def isNum(s: String) = s forall Character.isDigit
    def getPiecesWithCol(row: String): Array[(String, Int)] = {
      val items = row.reverse.split("(?!\\+)").reverse
      val ind = items.map(x => if (isNum(x)) x.toInt else 1).scanLeft(1)(_ + _)
      items
        .zip(ind)
        .filter(i => !isNum(i._1))
    }

    pieces.clear

    sfen
      .split(" ")(0)
      .split("/")
      .zipWithIndex
      .flatMap{ case(x, row) =>
        getPiecesWithCol(x)
          .map{ case(p, col) => (PieceEnum.withName(p), row + 1, col) }
      }
  }
}
