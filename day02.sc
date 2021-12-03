// scala 2.13.6

import ammonite.ops._

import $file.runner

case class Position(hoz: Int = 0, depth: Int = 0)
case class WithAim(aim: Int = 0, pos: Position = Position())

runner.exec("day02") { (fileName, assert) =>
  val entries = read.lines(pwd / fileName)
  val finalPos = entries.foldLeft(Position()) { (pos, command) =>
    command match {
      case s"forward $step" => pos.copy(hoz = pos.hoz + step.toInt)
      case s"down $step"    => pos.copy(depth = pos.depth + step.toInt)
      case s"up $step"      => pos.copy(depth = pos.depth - step.toInt)
    }
  }

  assert("final position", 150, 1383564)(finalPos.hoz * finalPos.depth)

  val withAim = entries.foldLeft(WithAim()) { (pos, command) =>
    command match {
      case s"forward $step" =>
        val newPosition = pos.pos.copy(
          hoz = pos.pos.hoz + step.toInt,
          depth = pos.pos.depth + pos.aim * step.toInt
        )
        pos.copy(pos = newPosition)
      case s"down $step" => pos.copy(aim = pos.aim + step.toInt)
      case s"up $step"   => pos.copy(aim = pos.aim - step.toInt)
    }
  }

  val hoz = assert("hoz with aim", 15, 1911)(withAim.pos.hoz)
  val depth = assert("depth with aim", 60, 778813)(withAim.pos.depth)
  assert("position with aim", 900, 1488311643)(hoz * depth)
}
