import $ivy.`net.je2sh:asciitable:1.1.0`
import net.je2sh.asciitable.{JTable, AnsiContentParser}
import net.je2sh.asciitable.style.JPadding

import $ivy.`com.lihaoyi::fansi:0.2.14`

import scala.collection.immutable.SeqMap

case class Assertion[T](
    valueEx: Option[T] = None,
    valueReal: Option[T] = None,
    expectedEx: Option[T] = None,
    expectedReal: Option[T] = None
)

class Test[T]() {
  var results = SeqMap.empty[String, Assertion[T]]

  def assert(ex: Boolean)(descr: String, expectedEx: T, expectedReal: T)(actual: T): T = {
    val assertion = results.getOrElse(descr, Assertion[T]())
    if (ex)
      results += descr -> assertion.copy(
        valueEx = Some(actual),
        expectedEx = Some(expectedEx)
      )
    else
      results += descr -> assertion.copy(
        valueReal = Some(actual),
        expectedReal = Some(expectedReal)
      )
    actual
  }
}

type Assert[T] = (String, T, T) => T => T

def exec[T](prefix: String)(code: (String, Assert[T]) => Unit) = {
  val test = new Test[T]()
  code(s"$prefix-ex.txt", test.assert(true))
  code(s"$prefix.txt", test.assert(false))

  val table = JTable.of().width(90).contentParser(new AnsiContentParser())
  val row = table.row().padding(JPadding.DEFAULT_PADDING)
  row.col().width(3).content(" ").done()
  row.col().content("Assertion").done()
  row.col().content("Example value (expected)").done()
  row.col().content("Real value (expected)").done()
  row.done()

  test.results.foreach { case (descr, assertion) =>
    val exampleMatches = assertion.valueEx == assertion.expectedEx
    val realMatches = assertion.valueReal == assertion.expectedReal
    val row = table.row().padding(JPadding.DEFAULT_PADDING)
    row
      .col()
      .width(3)
      .content(if (exampleMatches && realMatches) fansi.Color.Green("✓") else fansi.Color.Red("✗"))
      .done()
    row.col().content(descr).done()
    row
      .col()
      .content(
        s"${assertion.valueEx.getOrElse("None")} (${assertion.expectedEx.getOrElse("None")})"
      )
      .done()
    row
      .col()
      .content(
        s"${assertion.valueReal.getOrElse("None")} (${assertion.expectedReal.getOrElse("None")})"
      )
      .done()
    row.done()
  }
  table.render().forEach(println)
}
