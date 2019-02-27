package web.core


object Positioner {
  var width: Int = 0
  var height: Int = 0
  var density: Double = 0
  var isBlack: Boolean = true

  def rotate(v: Int): Int = if (isBlack) v else 10 - v

  def setSize(width: Int, height: Int, density: Double) {
    this.density = density
    this.width = scale(width)
    this.height = scale(height)
  }

  def setSide(isBlack: Boolean): Unit = this.isBlack = isBlack

  def boardSize: Double = height * 0.9
  def boardPadding: Double = boardSize * 0.05
  def cellSize: Double = boardSize * 0.1

  def scale(x: Int): Int = (x * density).toInt
  def scale(x: Double): Int = (x * density).toInt

  def getBoardScale: Double = boardSize / 100
  def getBoardY: Int = (height * 0.05).toInt
  def getBoardX: Int = ((width - boardSize) / 2).toInt

  def getPieceScale: Double = cellSize / 100
  def getPieceRow(y: Int): Int = rotate(((y - getBoardY - boardPadding) / cellSize).toInt + 1)
  def getPieceCol(x: Int): Int = rotate(((x - getBoardX - boardPadding) / cellSize).toInt + 1)

  def getPieceX(col: Int): Int = (getBoardX + boardPadding + (rotate(col) - 1) * cellSize).toInt
  def getPieceY(row: Int): Int = (getBoardY + boardPadding + (rotate(row) - 1) * cellSize).toInt
}
