// scala 2.13.6

import ammonite.ops._

import $file.runner

runner.exec[Int]("day03") { (fileName, assert) =>
  val entries = read.lines(pwd / fileName).toList.map(_.toCharArray.toList)
  val halfReports = entries.size / 2

  case class Count(char: Char, count: Int)

  def entryStats(
      entries: List[List[Char]],
      takeFirst: (Int, Int) => Boolean,
      stallbreaker: Char = '0'
  ): String =
    entries.transpose.map { bitColumn =>
      val counts = bitColumn.groupBy(identity).view.mapValues(_.size)
      counts.toSeq
        .map((Count.apply _).tupled)
        .sortWith { (count1, count2) =>
          val first = takeFirst(count1.count, count2.count)
          val last = takeFirst(count2.count, count1.count)
          if (first != last) first
          else count1.char == stallbreaker
        }
        .head
        .char
    }.mkString

  val γRateBinary = entryStats(entries, _ > _)
  val εRateBinary = entryStats(entries, _ < _)

  val γRate = assert("γ rate", 22, 189)(Integer.parseInt(γRateBinary, 2))
  val εRate = assert("ε rate", 9, 3906)(Integer.parseInt(εRateBinary, 2))

  assert("power consumption", 198, 738234)(γRate * εRate)

  def filterReport(
      entries: List[List[Char]],
      charPos: Int,
      takeFirst: (Int, Int) => Boolean,
      stallbreaker: Char
  ): String =
    entries match {
      case single :: Nil => single.mkString
      case more =>
        val criteria = entryStats(more, takeFirst, stallbreaker)
        filterReport(
          more.filter(_.apply(charPos) == criteria.apply(charPos)),
          charPos + 1,
          takeFirst,
          stallbreaker
        )
    }

  val oxygenRating = assert("oxygen rating", 23, 1071) {
    Integer.parseInt(filterReport(entries, 0, _ > _, '1'), 2)
  }
  val co2Rating = assert("CO2 rating", 10, 3706) {
    Integer.parseInt(filterReport(entries, 0, _ < _, '0'), 2)
  }

  assert("life support", 230, 3969126)(oxygenRating * co2Rating)
}
