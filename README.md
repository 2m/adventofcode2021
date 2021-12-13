This repository contains solutions to various puzzles of the [Advent of Code 2021][aoc2021].
Puzzles are solved with various idioms and libraries from the Scala ecosystem.

[Ammonite][amm] is used to run the code.
It is available from many package managers (e.g. `brew install ammonite-repl`).
When running a puzzle solution (e.g. `amm day02.sc`) dependencies will be downloaded automatically.

Custom [runner](runner.sc) is used to run the solution code with [example](day02-ex.txt) and [real](day02.txt) inputs.
Runner registers all of the assertions in the solution and prints out results table at the end:

```
┌───┬─────────────────────────────┬─────────────────────────────┬─────────────────────────────┐
│   │ Assertion                   │ Example value (expected)    │ Real value (expected)       │
├───┼─────────────────────────────┼─────────────────────────────┼─────────────────────────────┤
│ ✓ │ final position              │ 150 (150)                   │ 1383564 (1383564)           │
├───┼─────────────────────────────┼─────────────────────────────┼─────────────────────────────┤
│ ✓ │ hoz with aim                │ 15 (15)                     │ 1911 (1911)                 │
├───┼─────────────────────────────┼─────────────────────────────┼─────────────────────────────┤
│ ✓ │ depth with aim              │ 60 (60)                     │ 778813 (778813)             │
├───┼─────────────────────────────┼─────────────────────────────┼─────────────────────────────┤
│ ✓ │ position with aim           │ 900 (900)                   │ 1488311643 (1488311643)     │
└───┴─────────────────────────────┴─────────────────────────────┴─────────────────────────────┘
```

List of solutions and Scala idioms/libraries used:

| Puzzle              | Idioms and libraries                                                   |
| ------------------- | ---------------------------------------------------------------------- |
| [Day 1](day01.sc)   | [Ammonite Ops][amm-ops], [Collections Sliding Window][subsets]         |
| [Day 2](day02.sc)   | [Match on interpolator][match-interp]                                  |
| [Day 3](day03.sc)   | [Parsing text to integers][parse-int]                                  |
| [Day 4](day04.sc)   | [Mutable vars][vars]                                                   |
| [Day 5](day05.sc)   | [zip][]                                                                |
| [Day 6](day06.sc)   | [Map.updatedWith][updated-with]                                        |
| [Day 7](day07.sc)   | [Sum of consecutive numbers][sum-cons]                                 |
| [Day 8](day08.sc)   | [Set intersection][sum-cons]                                           |
| [Day 9](day09.sc)   | [foldLeft][fold-left]                                                  |
| [Day 10](day10.sc)  | [Extractor objects][extractor]                                         |
| [Day 11](day11.sc)  | [Unfold][unfold]                                                       |
| [Day 12](day12.sc)  |                                                                        |
| [Day 13](day13.sc)  |                                                                        |

[aoc2021]:      https://adventofcode.com/2021
[amm]:          https://ammonite.io/
[amm-ops]:      https://ammonite.io/#Operations
[subsets]:      https://alvinalexander.com/scala/how-to-split-sequences-subsets-groupby-partition-scala-cookbook/
[match-interp]: https://cucumbersome.net/2020/11/28/four-new-features-of-scala-2-13-releases-that-you-probably-missed/#2130-s-interpolator-on-pattern-matching
[parse-int]:    https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Integer.html#parseInt(java.lang.String,int)
[vars]:         https://docs.scala-lang.org/overviews/scala-book/two-types-variables.html
[zip]:          https://alvinalexander.com/scala/how-to-merge-sequential-collection-pairs-zip-unzip-scala-cookbook/
[updated-with]: https://www.scala-lang.org/api/2.13.x/scala/collection/immutable/Map.html#updatedWith[V1%3E:V](key:K)(remappingFunction:Option[V]=%3EOption[V1]):CC[K,V1]
[sum-cons]:     https://math.stackexchange.com/questions/1100897/sum-of-consecutive-numbers
[sets]:         https://alvinalexander.com/scala/union-intersection-difference-scala-sets/
[fold-left]:    http://allaboutscala.com/tutorials/chapter-8-beginner-tutorial-using-scala-collection-functions/scala-foldleft-example/
[extractor]:    https://docs.scala-lang.org/tour/extractor-objects.html
[unfold]:       https://blog.genuine.com/2020/07/scala-unfold/
