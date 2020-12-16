package day16

import AoCUtils
import solveWithTiming

val testInput = "class: 1-3 or 5-7\n" +
        "row: 6-11 or 33-44\n" +
        "seat: 13-40 or 45-50\n" +
        "\n" +
        "your ticket:\n" +
        "7,1,14\n" +
        "\n" +
        "nearby tickets:\n" +
        "7,3,47\n" +
        "40,4,50\n" +
        "55,2,20\n" +
        "38,6,12"

val input = AoCUtils.readText("16.txt")

fun main() {
    solveWithTiming({ part1(testInput) }, 71, "test 1 part 1")
    solveWithTiming({ part1(input) }, 0, "part 1")

    solveWithTiming({ part2(testInput) }, 0, "test 1 part 2")
    solveWithTiming({ part2(input) }, 0, "part 2")

}

fun part1(input: String): Int {

    val split = input.replace("\n\n", "\n").split("your ticket:", "nearby tickets:")
    val ranges = split[0].lines().filter { it.isNotBlank() }.map { line ->
        line.split(": ", " or ", "-").let {
            listOf(
                Pair(it[0]+"-1", Pair(it[1].toInt(), it[2].toInt())),
                Pair(it[0]+"-2", Pair(it[3].toInt(), it[4].toInt()))
            )
        }
    }.flatten().toMap()

    val yourTicket = split[1].replace("\n", "").split(",").map { it.toInt() }
    val otherTickets = split[2].replaceFirst("\n", "").lines().filter { it.isNotBlank() }.map { it.split(",").map { m -> m.toInt() } }


    val map = otherTickets.map { ticket ->
        val wrongNums = mutableSetOf<Int>()
        ticket.forEach { num ->
            var wrong = true
            ranges.values.forEach { range ->
                if (num >= range.first && num <= range.second) {
                    wrong = false
                }
            }
            if (wrong) {
                wrongNums.add(num)
            }
        }
        wrongNums
    }
    return map.flatten().sum()
}

fun part2(input: String): Int {
    return 0
}