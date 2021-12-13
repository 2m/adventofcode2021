// scala 2.13.6

import ammonite.ops._

import $file.runner

runner.exec[Int]("day13") { (fileName, assert) =>
  val entries = read.lines(pwd / fileName).toList

  val dots = entries.collect { case s"$x,$y" =>
    x.toInt -> y.toInt
  }.toSet

  case class Fold(axis: Char, pos: Int)
  val instructions = entries.collect { case s"fold along $axis=$pos" =>
    Fold(axis.head, pos.toInt)
  }

  def fold(dots: Set[(Int, Int)], f: Fold) = {
    dots.map {
      case dot if f.axis == 'x' && dot._1 > f.pos => dot.copy(_1 = f.pos * 2 - dot._1)
      case dot if f.axis == 'y' && dot._2 > f.pos => dot.copy(_2 = f.pos * 2 - dot._2)
      case dot                                    => dot
    }
  }

  assert("dots after first fold", 17, 706)(fold(dots, instructions.head).size)

  val result = instructions.foldLeft(dots)(fold)
  for {
    y <- 0 to 5
    x <- 0 to 38
  } {
    if (x == 0) println()
    if (result.contains(x -> y)) print("#") else print(".")
  }
  println()
}
