package day12

import AoCUtils
import AoCUtils.readText
import AoCUtils.test
import kotlin.math.abs
import kotlin.math.cos

val testInput = "F10\n" +
        "N3\n" +
        "F7\n" +
        "R90\n" +
        "F11"

val testInput2 =
    "F10\n" +
            "N3\n" +
            "F7\n" +
            "R90\n" +
            "R90\n" +
            "L270\n" +
            "R180\n" +
            "R270\n" +
            "L270\n" +
            "R90\n" +
            "R90\n" +
            "L180\n" +
            "F11"
val input = AoCUtils.readText("12.txt")

fun main() {
    part1(testInput) test Pair(25, "test 1 part 1 should be 25")
    part1(input) test Pair(759, "part 1 should be 759")

    part2(testInput2) test Pair(286, "test 2 part 2 should be 286")
    part2(testInput) test Pair(286, "test 2 part 2 should be 286")
    part2(input) test Pair(45763, "part 2 should be 45763")


    // peter f
    part1(readText("12-2.txt")) test Pair(562, "Peter part 1 should be 562")
    part2(readText("12-2.txt")) test Pair(101860, "Peter part 2 should be 101860")

    // ray
    part1(readText("12-3.txt")) test Pair(508, "Peter part 1 should be 508")
    part2(readText("12-3.txt")) test Pair(30761, "Peter part 2 should be 30761")

}

data class Position(var x: Long, var y: Long)

fun part1(input: String): Long {

    val list = input.lines().map { Pair(it.first().toString(), it.substring(1).toInt()) }


    var direction = 90 // 0=N, 1=E, 2=S, 3=W
    val currentPoint = Position(0, 0)
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
            "N" -> currentPoint.y += p.second
            "E" -> currentPoint.x += p.second
            "S" -> currentPoint.y -= p.second
//            "W" -> currentPoint.y += p.second
            "W" -> currentPoint.x -= p.second

            else -> throw Exception("Unknown movement: $p")
        }
        movementList.add(currentPoint.copy())
    }


    return abs(currentPoint.x) + abs(currentPoint.y)
}

fun part2(input: String): Long {

    val list = input.lines().map { Pair(it.first().toString(), it.substring(1).toLong()) }

    val shipPoint = Position(0, 0)
    val wayPoint = Position(10, 1)
    val movementList = mutableListOf(Pair(shipPoint.copy(), wayPoint.copy()))

    list.forEach { p ->
        when (p.first) {
            "F" -> {
                shipPoint.x += wayPoint.x * p.second
                shipPoint.y += wayPoint.y * p.second
            }
            "R" -> {
                var degrees = p.second
                while (degrees != 0L) {
                    val y = wayPoint.y
                    val x = wayPoint.x
                    wayPoint.x = y
                    wayPoint.y = -x
                    degrees -= 90
                }
            }
            "L" -> {
                var degrees = p.second
                while (degrees != 0L) {
                    val y = wayPoint.y
                    val x = wayPoint.x
                    wayPoint.x = -y
                    wayPoint.y = x
                    degrees -= 90
                }
            }
            "N" -> wayPoint.y += p.second
            "E" -> wayPoint.x += p.second
            "S" -> wayPoint.y -= p.second
            "W" -> wayPoint.x -= p.second
            else -> throw Exception("Unknown movement: $p")
        }
        movementList.add(Pair(shipPoint.copy(), wayPoint.copy()))
    }


    return abs(shipPoint.x) + abs(shipPoint.y)
}