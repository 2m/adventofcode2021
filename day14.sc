// scala 2.13.6

import ammonite.ops.{pwd, read}

import scala.util.chaining._

import $file.runner

runner.exec[Long]("day14") { (fileName, assert) =>
  val entries = read.lines(pwd / fileName).toList

  val template = entries.head
  val rules = entries.collect { case s"$pair -> $result" =>
    pair -> result.head.toString
  }.toMap

  val polymer = template.sliding(2).toList.groupBy(identity).view.mapValues(_.size.toLong).toMap

  def step(pairs: Map[String, Long]) =
    pairs.foldLeft(Map.empty[String, Long]) { case (counts, (pair, count)) =>
      val insertion = rules(pair)
      counts
        .updatedWith(pair.head + insertion)(c => Some(c.getOrElse(0L) + count))
        .updatedWith(insertion + pair.last)(c => Some(c.getOrElse(0L) + count))
    }

  def counts(m: Map[String, Long]) = m.toList
    .map { case (pair, count) =>
      pair.head -> count
    }
    .groupBy(_._1)
    .view
    .mapValues {
      _.map(_._2).sum
    }
    .values

  assert("after 10 steps", 1588, 2899) {
    val result = (0 until 10)
      .foldLeft(polymer) { case (t, _) =>
        step(t)
      }
      .pipe(counts)
    result.max - result.min + 1
  }

  assert("after 40 steps", 2188189693529L, 3528317079545L) {
    val result = (0 until 40)
      .foldLeft(polymer) { case (t, _) =>
        step(t)
      }
      .pipe(counts)
    result.max - result.min + 1
  }
}
