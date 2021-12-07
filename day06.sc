// scala 2.13.6

import ammonite.ops._

import $file.runner

runner.exec[Long]("day06") { (fileName, assert) =>
  val entries = read.lines(pwd / fileName).head.split(",").toList.map(_.toInt)

  val popMap = entries.groupBy(identity).view.mapValues(_.size.toLong).toMap

  def after(days: Int, popMap: Map[Int, Long]): Map[Int, Long] = days match {
    case 0 => popMap
    case _ =>
      val newPopulation = popMap.flatMap {
        case (0, c) => None
        case (t, c) => Some((t - 1, c))
      }
      val withKids = popMap.get(0) match {
        case None => newPopulation
        case Some(kids) =>
          def incr(by: Long) = (c: Option[Long]) => Some(c.getOrElse(0L) + by)
          newPopulation.updatedWith(6)(incr(kids)).updatedWith(8)(incr(kids))
      }
      after(days - 1, withKids)
  }

  assert("population after 80", 5934, 393019)(after(80, popMap).view.values.sum)
  assert("population after 256", 26984457539L, 1757714216975L)(after(256, popMap).view.values.sum)
}
