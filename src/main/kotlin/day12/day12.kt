package day12

import AoCUtils
import AoCUtils.test
import kotlin.math.abs

val testInput = "F10\n" +
        "N3\n" +
        "F7\n" +
        "R90\n" +
        "F11"

val input = AoCUtils.readText("12.txt")

fun main() {
    part1(testInput) test Pair(25, "test 1 part 1 should be 25")
    part1(input) test Pair(0, "part 1 should be 0")

    part2(testInput) test Pair(0, "test 2 part 2 should be 0")
    part2(input) test Pair(0, "part 2 should be 0")

}

data class Position(var x: Int, var y: Int)

fun part1(input: String): Int {

    val list = input.lines().map { Pair(it.first().toString(), it.substring(1).toInt()) }


    var direction = 90 // 0=N, 1=E, 2=S, 3=W
    var currentPoint = Position(0, 0)
    val movementList = mutableListOf(currentPoint.copy())

    list.forEach { p ->
        when (p.first) {
            "F" -> {
                when (direction) {
                    0 -> currentPoint.y += p.second
                    90 -> currentPoint.x += p.second
                    180 -> currentPoint.y -= p.second
                    270 -> currentPoint.x -= p.second
                    else -> throw Exception("Unknown direction: $direction")
                }
            }
            "R" -> {
                direction += p.second
                while (direction >= 360) direction -= 360
            }
            "L" -> {
                direction -= p.second
                while (direction < 0) direction += 360
            }
            "N" -> {
                currentPoint.y += p.second
            }
            "E" -> {
                currentPoint.x += p.second
            }
            "S" -> {
                currentPoint.y -= p.second
            }
            "W" -> {
                currentPoint.y += p.second
            }
            else -> throw Exception("Unknown movement: $p")
        }
        movementList.add(currentPoint.copy())
    }


    return abs(currentPoint.x) + abs(currentPoint.y)
}

fun part2(input: String): Int {
    return 0
}