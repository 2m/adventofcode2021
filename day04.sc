// scala 2.13.6

import ammonite.ops._

import $file.runner

case class Entry(number: Int, var seen: Boolean)
object Entry {
  def apply(number: Int): Entry = Entry(number, false)
}

case class BingoTable(values: List[List[Entry]]) {
  def wins: Boolean = {
    def rowSeen(values: List[List[Entry]]) =
      values.map(_.map(_.seen).reduce(_ && _)).exists(identity)
    rowSeen(values) || rowSeen(values.transpose)
  }
  def drawn(number: Int) = for {
    row <- values
    entry <- row
    if entry.number == number
  } {
    entry.seen = true
  }
  def unmarked = for {
    row <- values
    entry <- row
    if !entry.seen
  } yield entry.number
}
object BingoTable {
  def fromInts(values: List[List[Int]]): BingoTable =
    BingoTable(values.map(_.map(Entry.apply)))
}

runner.exec("day04") { (fileName, assert) =>
  val entries = read.lines(pwd / fileName).toList
  val (drawn, tables) = entries.splitAt(1)
  val drawnNumbers = drawn.head.split(',').toList.map(_.toInt)
  val bingoTables = tables
    .grouped(6)
    .map(_.tail.map(_.trim.split("\\s+").map(_.toInt).toList))
    .map(BingoTable.fromInts)
    .toList

  val drawnSoFar = drawnNumbers.dropWhile { number =>
    bingoTables.map(_.drawn(number))
    !bingoTables.map(_.wins).exists(identity)
  }

  val winningNumber = assert("winning number", 24, 57)(drawnSoFar.head)
  val winningTable = bingoTables.filter(_.wins).head
  val winningSum = assert("winning unmarked sum", 188, 439)(winningTable.unmarked.sum)

  assert("winning score", 4512, 25023)(winningNumber * winningSum)

  var remainingTables = bingoTables
  val drawnUntilAllWin = drawnSoFar.dropWhile { number =>
    remainingTables = remainingTables.filterNot(_.wins)
    remainingTables.map(_.drawn(number))
    !remainingTables.map(_.wins).forall(identity)
  }

  val allWinningNumber = assert("all winning number", 13, 6)(drawnUntilAllWin.head)
  val allWinningTable = remainingTables.head
  val allWinningSum = assert("all winning unmarked sum", 148, 439)(allWinningTable.unmarked.sum)

  assert("all winning score", 1924, 2634)(allWinningNumber * allWinningSum)
}
