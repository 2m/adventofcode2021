import $ivy.`net.je2sh:asciitable:1.1.0`
import net.je2sh.asciitable.{JTable, AnsiContentParser}
import net.je2sh.asciitable.style.JPadding

import $ivy.`com.lihaoyi::fansi:0.2.14`

case class Assertion(
    valueEx: Option[Int] = None,
    valueReal: Option[Int] = None,
    expectedEx: Option[Int] = None,
    expectedReal: Option[Int] = None
)

class Test() {
  var results = Map.empty[String, Assertion]

  def assert(ex: Boolean)(
      descr: String,
      actual: Int,
      expectedEx: Int,
      expectedReal: Int
  ) = {
    val assertion = results.getOrElse(descr, Assertion())
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
  }
}

type Assert = (String, Int, Int, Int) => Unit

def exec(prefix: String)(code: (String, Assert) => Unit) = {
  val test = new Test()
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
