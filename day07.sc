// scala 2.13.6

import ammonite.ops._

import $file.runner

runner.exec("day07") { (fileName, assert) =>
  val entries = read.lines(pwd / fileName).head.split(",").map(_.toInt).toList
  val maxPos = entries.max

  val constantCosts = for {
    pos <- 0 to maxPos
  } yield entries.map(p => math.abs(p - pos)).sum

  assert("minimal constant cost", 37, 343441)(constantCosts.min)

  val incrCosts = for {
    pos <- 0 to maxPos
  } yield entries.map { p =>
    val n = math.abs(p - pos)
    (n * (n + 1)) / 2
  }.sum

  assert("minimal increasing cost", 168, 98925151)(incrCosts.min)
}
