package day11

import AoCUtils
import AoCUtils.test

val testInput = "L.LL.LL.LL\n" +
        "LLLLLLL.LL\n" +
        "L.L.L..L..\n" +
        "LLLL.LL.LL\n" +
        "L.LL.LL.LL\n" +
        "L.LLLLL.LL\n" +
        "..L.L.....\n" +
        "LLLLLLLLLL\n" +
        "L.LLLLLL.L\n" +
        "L.LLLLL.LL"

val input = AoCUtils.readText("11.txt")

fun main() {
    part1(testInput) test Pair(37, "test 1 part 1 should be 37")
    part1(input) test Pair(0, "part 1 should be 0")

    part2(testInput) test Pair(0, "test 2 part 2 should be 0")
    part2(input) test Pair(0, "part 2 should be 0")

}

enum class Status {
    Free, Floor, Occupied;

    companion object {
        fun of(char: String): Status = when (char) {
            "." -> Floor
            "#" -> Occupied
            "L" -> Free
            else -> throw Exception("Wrong input: $char")
        }

    }
}

fun part1(input: String): Int {
    val map = input.lines().mapIndexed { y, line ->
        line.mapIndexed { x, s ->
            Triple(x, y, Status.of(s.toString()))
        }
    }.flatten()
        .associate { t -> Pair(Pair(t.first, t.second), t.third) }.toMutableMap()


    var notChanged = false
    do {
        notChanged = false
        map.forEach { (pos, status) ->
            when (status) {
                Status.Free -> {
                    val seatsAround = getSurroundingSeats(map, pos)
                    val occupied = seatsAround.count { map[it] != Status.Occupied }
                    if (occupied == 0) {
                        map[pos] = Status.Occupied
                        notChanged = true
                    }
                }
                Status.Floor -> {

                }
                Status.Occupied -> {
                    val seatsAround = getSurroundingSeats(map, pos)
                    val occupied = seatsAround.count { map[it] != Status.Occupied }
                    if (occupied >= 4) {
                        map[pos] = Status.Free
                        notChanged = true
                    }
                }
            }
        }
    } while (notChanged)

    return map.values.count { it == Status.Occupied }
}

fun getSurroundingSeats(map: Map<Pair<Int, Int>, Status>, pos: Pair<Int, Int>): List<Pair<Int, Int>> {
    return listOf(
        pos.copy(first = pos.first + 1, second = pos.second + 0),
        pos.copy(first = pos.first + 1, second = pos.second + 1),
        pos.copy(first = pos.first + 1, second = pos.second + -1),

        pos.copy(first = pos.first + -1, second = pos.second + 0),
        pos.copy(first = pos.first + -1, second = pos.second + 1),
        pos.copy(first = pos.first + -1, second = pos.second + -1),

        pos.copy(first = pos.first + 0, second = pos.second + 1),
        pos.copy(first = pos.first + 0, second = pos.second + -1),
    ).filter { p -> map.containsKey(p) }
}

fun part2(input: String): Int {
    return 0
}