package template

import AoCUtils
import solveWithTiming

val testInput = ""

val input = AoCUtils.readText("XX.txt")

fun main() {
    solveWithTiming({ part1(testInput) }, 0, "test 1 part 1")
    solveWithTiming({ part1(input) }, 0, "part 1")

    solveWithTiming({ part2(testInput) }, 0, "test 1 part 2")
    solveWithTiming({ part2(input) }, 0, "part 2")

}

fun part1(input: String): Int {
    return 0
}

fun part2(input: String): Int {
    return 0
}