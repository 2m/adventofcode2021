// scala 2.13.6

import ammonite.ops._

import $file.runner

runner.exec[Long]("day10") { (fileName, assert) =>
  val entries = read.lines(pwd / fileName).toList

  val syntax = Map('<' -> '>', '(' -> ')', '[' -> ']', '{' -> '}')

  object Open {
    def unapply(c: Char): Option[Char] = syntax.keySet.find(_ == c)
  }

  trait Result
  case class Remaining(r: List[Char]) extends Result
  case class Unexpected(c: Char) extends Throwable with Result

  def parseLine(line: String) =
    line.foldLeft(List.empty[Char]) {
      case (sofar, Open(next)) => next :: sofar
      case (last :: rest, next) =>
        if (syntax(last) == next) rest
        else throw Unexpected(next)
    }

  def lineResult(line: String) =
    try {
      Remaining(parseLine(line))
    } catch {
      case u: Unexpected => u
    }

  val parsed = entries.map(lineResult)

  val errorScore = parsed.collect {
    case Unexpected(')') => 3
    case Unexpected(']') => 57
    case Unexpected('}') => 1197
    case Unexpected('>') => 25137
    case _               => 0
  }.sum

  assert("total error score", 26397, 168417)(errorScore)

  val autocompleteScores = parsed.collect { case Remaining(r) =>
    r.foldLeft(0L) { (score, char) =>
      score * 5 + (syntax(char) match {
        case ')' => 1
        case ']' => 2
        case '}' => 3
        case '>' => 4
      })
    }
  }.sorted

  assert("autocomplete score", 288957, 2802519786L) {
    autocompleteScores.drop(autocompleteScores.size / 2).head
  }
}
