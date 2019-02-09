package web

import scala.collection.mutable.TreeSet

/**
 * Loads objects and shares them via `shapes` Iterable object.
 * Can be aware about objects specifics but shuldn't be avare
 * about game logic
 */
trait ObjectsStore {
  this: BussClient =>

  private val background = TreeSet.empty[GameObject[_]](GameObject)
  private val middleground = TreeSet.empty[GameObject[_]](GameObject)
  private val foreground = TreeSet.empty[Piece](Piece)

  def shapes: Iterable[GameObject[_]] = background.toIterable ++ middleground.toIterable ++ foreground.toIterable

  // TODO shouldn't be here, it's a game logic already, so it should be moved
  // to ShogiEngine as a main game logic router.
  register[NewStateEvent](event => loadState(event.sfen))

  def loadObjects() {
    background.clear
    middleground.clear
    foreground.clear

    middleground.add(new ConnectIcon)
    background.add(new Board)
  }

  // ex "lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b"
  private def loadState(sfen: String) {
    foreground.clear
    parseSfen(sfen).foreach{ case(state, row, col) => foreground.add(new Piece(state, row, col)) }
  }

  private def parseSfen(sfen: String): Seq[(PieceConf.Value, Int, Int)] = {
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
