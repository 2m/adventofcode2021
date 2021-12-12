// scala 2.13.6

import ammonite.ops._

import $file.runner

runner.exec[Int]("day12") { (fileName, assert) =>
  val entries = read.lines(pwd / fileName).toList
  val conns = entries.map { case s"$from-$to" =>
    from :: to :: Nil
  }

  def findPaths(maxStops: Int, current: String, sofar: List[String]): List[List[String]] =
    current match {
      case "end" =>
        List(current :: sofar)
      case _ =>
        def seenCount(l: List[String]) = l
          .filter(_.forall(_.isLower))
          .groupBy(identity)
          .values
          .map(_.size)

        conns
          .filter(_.contains(current))
          .map(_.filterNot(_ == current).head)
          .filter(_ != "start")
          .filter(dest =>
            !dest.forall(_.isLower) ||
              !sofar.contains(dest) ||
              !seenCount(current :: sofar).exists(_ >= maxStops)
          )
          .flatMap(findPaths(maxStops, _, current :: sofar))
    }

  assert("total paths", 226, 5076)(
    findPaths(maxStops = 1, "start", List.empty).size
  )
  assert("total paths with one double stop", 3509, 145643)(
    findPaths(maxStops = 2, "start", List.empty).size
  )
}
