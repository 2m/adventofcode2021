// scala 2.13.6

import ammonite.ops._

import $file.runner

runner.exec[Int]("day11") { (fileName, assert) =>
  val entries = read.lines(pwd / fileName).toList.map(_.split("").map(_.toInt).toList)

  val xMax = entries.head.size - 1
  val yMax = entries.size - 1

  val levels = (for {
    y <- 0 to yMax
    x <- 0 to xMax
  } yield List.fill(entries(y)(x))((x, y))).flatten.toList

  def adjacent(x: Int, y: Int) = for {
    x <- x - 1 to x + 1
    y <- y - 1 to y + 1
    if x >= 0 && x <= xMax
    if y >= 0 && y <= yMax
  } yield (x, y)

  def printGrid(l: List[(Int, Int)]) = {
    val grid = l.groupBy(identity).view.mapValues(_.size).toMap
    for {
      y <- 0 to yMax
      x <- 0 to xMax
    } {
      if (x == 0) println()
      print(grid.getOrElse((x, y), 0))
    }
  }

  def step(l: List[(Int, Int)]) = {
    val increased = l ++ (for {
      y <- 0 to yMax
      x <- 0 to xMax
    } yield (x, y))

    def flash(l: List[(Int, Int)], sofar: Set[(Int, Int)]) =
      l.groupBy(identity).view.mapValues(_.size).collectFirst {
        case (f @ (x, y), count) if count > 9 && !sofar.contains(f) => (x, y)
      }

    def processFlashes(l: List[(Int, Int)], sofar: Set[(Int, Int)]): (List[(Int, Int)], Int) =
      flash(l, sofar) match {
        case Some(f @ (x, y)) =>
          processFlashes(l ++ adjacent(x, y), sofar + f)
        case _ => (l.filterNot(sofar.contains), sofar.size)
      }

    processFlashes(increased, Set.empty)
  }

  val steps = () =>
    Iterator.unfold((levels, 0)) { case (levelsSofar, flashesSofar) =>
      val (newLevels, newFlashes) = step(levelsSofar)
      val result = (newLevels, flashesSofar + newFlashes)
      Some((result, result))
    }

  assert("total flashes", 1656, 1594) {
    steps().drop(99).take(1).toList.map(_._2).head
  }

  assert("mega flash", 195, 0) {
    steps().zipWithIndex.collectFirst {
      case ((levels, _), step) if levels.size == 0 =>
        step
    }.get + 1
  }
}
