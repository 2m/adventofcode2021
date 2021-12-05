// scala 2.13.6

import ammonite.ops._

import $file.runner
import os.truncate

case class Point(x: Int, y: Int)

runner.exec("day05") { (fileName, assert) =>
  val entries = read.lines(pwd / fileName).toList

  val lines = entries.map { case s"$x1,$y1 -> $x2,$y2" =>
    (Point(x1.toInt, y1.toInt), Point(x2.toInt, y2.toInt))
  }

  val (straight, diagonal) = lines
    .partition {
      case (p1, p2) if p1.x == p2.x => true
      case (p1, p2) if p1.y == p2.y => true
      case _                        => false
    }

  def range(p1: Int, p2: Int) = (p1 to p2 by (if (p1 > p2) -1 else 1))

  val points =
    straight.flatMap { case (p1, p2) =>
      for {
        x <- range(p1.x, p2.x)
        y <- range(p1.y, p2.y)
      } yield Point(x, y)
    }

  def overlaps(points: List[Point]) =
    points.groupBy(identity).collect { case (point, coll) if coll.size > 1 => coll.size }

  assert("straight overlaps", 5, 6666)(overlaps(points).size)

  val pointsDiagonal =
    diagonal.flatMap { case (p1, p2) =>
      for {
        (x, y) <- range(p1.x, p2.x).zip(range(p1.y, p2.y))
      } yield Point(x, y)
    }

  assert("overlaps diagonal", 12, 19081)(overlaps(points ++ pointsDiagonal).size)
}
