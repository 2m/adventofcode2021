// scala 2.13.6

import ammonite.ops._

import $file.runner

case class Grid(data: List[List[Int]]) {
  def adjacentPos(x: Int, y: Int): List[(Int, Int)] = {
    for {
      (xp, yp) <- List(x - 1, x + 1).map((_, y)) ::: List(y - 1, y + 1).map((x, _))
      if (xp >= 0 && xp < data.head.size)
      if (yp >= 0 && yp < data.size)
    } yield (xp, yp)
  }

  def adjacent(x: Int, y: Int): List[Int] = adjacentPos(x, y).map((get _).tupled)

  def get(x: Int, y: Int): Int = data(y)(x)

  def basin(x: Int, y: Int, sofar: Set[(Int, Int)]): Set[(Int, Int)] = {
    val newPos = adjacentPos(x, y).toSet.filterNot(sofar.contains).filterNot { case (x, y) =>
      get(x, y) == 9
    }
    newPos.foldLeft(sofar ++ newPos) { case (visited, (x, y)) =>
      basin(x, y, visited)
    }
  }
}

runner.exec[Int]("day09") { (fileName, assert) =>
  val entries = read.lines(pwd / fileName).toList.map(_.split("").toList.map(_.toInt))
  val grid = Grid(entries)
  val lowPoints = for {
    x <- 0 until entries.head.size
    y <- 0 until entries.size
    h = grid.get(x, y)
    if grid.adjacent(x, y).forall(_ > h)
  } yield h

  assert("risk level sum", 15, 526)(lowPoints.map(_ + 1).sum)

  val basins = for {
    x <- 0 until entries.head.size
    y <- 0 until entries.size
    if grid.get(x, y) != 9
  } yield grid.basin(x, y, Set.empty)

  assert("3 largest basin product", 1134, 1123524) {
    basins.distinct.map(_.size).sortBy(s => -s).take(3).reduce(_ * _)
  }
}
