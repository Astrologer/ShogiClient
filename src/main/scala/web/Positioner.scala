package web


object Positioner {
  var width: Int = 0
  var height: Int = 0

  def setSize(width: Int, height: Int) {
    this.width = width
    this.height = height
  }

  protected def boardSize: Double = height * 0.9
  protected def boardPadding: Double = boardSize * 0.05
  protected def cellSize: Double = boardSize * 0.1

  def getBoardScale: Double = boardSize / 100
  def getBoardY: Int = (height * 0.05).toInt
  def getBoardX: Int = ((width - boardSize) / 2).toInt

  def getPieceScale: Double = cellSize / 100
  def getPieceRow(y: Int): Int = ((y - getBoardY - boardPadding) / cellSize).toInt + 1
  def getPieceCol(x: Int): Int = ((x - getBoardX - boardPadding) / cellSize).toInt + 1

  def getPieceX(col: Int): Int = (getBoardX + boardPadding + (col - 1) * cellSize).toInt
  def getPieceY(row: Int): Int = (getBoardY + boardPadding + (row - 1) * cellSize).toInt
}
