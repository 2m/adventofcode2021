// scala 2.13.6

import ammonite.ops._

import $file.runner

runner.exec("day01") { (fileName, assert) =>
  val entries = read.lines(pwd / fileName).map(_.toInt)

  val measurementIncreased =
    entries.sliding(2).filter(el => el.head < el.last).length

  assert("measurement increased", measurementIncreased, 7, 1722)

  val slidingIncreased = entries
    .sliding(3)
    .sliding(2)
    .filter(triples => triples.head.sum < triples.last.sum)
    .length

  assert(
    "three-measurement sliding window increased",
    slidingIncreased,
    5,
    1748
  )
}
