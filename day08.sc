// scala 2.13.6

import ammonite.ops._

import $file.runner

runner.exec[Int]("day08") { (fileName, assert) =>
  val entries = read.lines(pwd / fileName).toList

  val input = entries.map { e =>
    val patterns :: output :: Nil = e.split('|').toList
    (patterns.split(" ").toList, output.drop(1).split(" ").toList)
  }

  val easy = input.map(_._2.map(_.size).count(s => List(2, 3, 4, 7).contains(s))).sum
  assert("easy number appearance", 26, 381)(easy)

  val output =
    input.map { case (patterns, output) =>
      def findByLength(l: Int): Set[Char] =
        patterns.find(_.size == l).map(_.toSet).get

      val `1` = findByLength(2)
      val `4` = findByLength(4)

      val key = patterns.map {
        case s if s.length == 5 =>
          s.toSet match {
            case chars if chars.intersect(`1`).size == 2 => chars -> 3
            case chars if chars.intersect(`4`).size == 2 => chars -> 2
            case chars                                   => chars -> 5
          }
        case s if s.length == 6 =>
          s.toSet match {
            case chars if chars.intersect(`1`).size == 1 => chars -> 6
            case chars if chars.intersect(`4`).size == 4 => chars -> 9
            case chars                                   => chars -> 0
          }
        case s =>
          s.size match {
            case 2 => s.toSet -> 1
            case 3 => s.toSet -> 7
            case 4 => s.toSet -> 4
            case 7 => s.toSet -> 8
          }
      }.toMap

      output.map(o => key(o.toSet)).mkString.toInt
    }

  assert("output sum", 61229, 1023686)(output.sum)
}
