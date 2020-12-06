package day05

import readLines
import test

fun main() {

    findRow("FBFBBFFRLR") test Pair(44, "test1 part 1 row 44")
    findSeat("FBFBBFFRLR") test Pair(5, "test1 part 1 col 5")
    findSeatId("FBFBBFFRLR") test Pair(357, "test1 part1 seatId 357")

    findRow("BFFFBBFRRR") test Pair(70, "test1 part 1 row 70")
    findSeat("BFFFBBFRRR") test Pair(7, "test1 part 1 col 7")
    findSeatId("BFFFBBFRRR") test Pair(567, "test1 part1 seatId 567")

    findRow("FFFBBBFRRR") test Pair(14, "test1 part 1 row 14")
    findSeat("FFFBBBFRRR") test Pair(7, "test1 part 1 col 7")
    findSeatId("FFFBBBFRRR") test Pair(119, "test1 part1 seatId 119")

    findRow("BBFFBBFRLL") test Pair(102, "test1 part 1 row 102")
    findSeat("BBFFBBFRLL") test Pair(4, "test1 part 1 col 4")
    findSeatId("BBFFBBFRLL") test Pair(820, "test1 part1 seatId 820")

    val input = readLines("05.txt")
    input.map { findSeatId(it) }.sorted().reversed().first() test Pair(878, "part1 seatId 878")

    val seated = input.map { input ->
        val row = findRow(input)
        val seat = findSeat(input)
        Triple(row, seat, row * 8 + seat)
    }
    val sortedBy = seated.sortedBy { it.third }

    var part2 = 0
    for (i in 1 until sortedBy.size) {
        val prev = sortedBy[i - 1]
        val current = sortedBy[i]
        if (prev.third + 1 != current.third) {
            part2 = prev.third + 1
        }
    }
    part2 test Pair(504, "Part2 my seat is 504")


    // found in kotlin slack
    val numbers = input.map { line ->
        val row = line.take(7).map { if (it == 'F') 0 else 1 }.joinToString("").toInt(2)
        val col = line.takeLast(3).map { if (it == 'L') 0 else 1 }.joinToString("").toInt(2)
        row * 8 + col to row
    }
    numbers.map { it.first }.maxOrNull()!!.let { println("Part 1: $it") }
    val filtered = numbers.filter { it.second != 0 && it.second != 127 }.map { it.first }
    filtered.forEach { one ->
        filtered.forEach { two ->
            if (one - two == 2 && (one - 1) !in filtered) println("Part 2: ${one - 1}")
        }
    }
}

fun findSeatId(testInput: String): Int {
    return findRow(testInput) * 8 + findSeat(testInput)
}

fun findSeat(testInput: String): Int {
    var upper = 7
    var lower = 0

    for (i in 7 until testInput.length) {
        if (testInput[i] == 'L') {
            upper = (upper - lower) / 2 + lower
        } else {
            lower = (upper - lower) / 2 + lower
        }
    }
    return upper
}

fun findRow(testInput: String): Int {

    var upper = 127
    var lower = 0

    for (i in 0..6) {
        if (testInput[i] == 'F') {
            upper = (upper - lower) / 2 + lower
        } else {
            lower = (upper - lower) / 2 + lower
        }
    }
    return upper
}
