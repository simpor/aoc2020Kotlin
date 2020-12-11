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
    part1(input) test Pair(2299, "part 1 should be 2299")

    part2(testInput) test Pair(26, "test 2 part 2 should be 26")
    part2(input) test Pair(0, "part 2 should be 0")

}

enum class Status {
    Empty, Floor, Occupied;

    companion object {
        fun of(char: String): Status = when (char) {
            "." -> Floor
            "#" -> Occupied
            "L" -> Empty
            else -> throw Exception("Wrong input: $char")
        }

    }
}

fun part1(input: String): Int {
    var map = input.lines().mapIndexed { y, line ->
        line.mapIndexed { x, s ->
            Triple(x, y, Status.of(s.toString()))
        }
    }.flatten()
        .associate { t -> Pair(Pair(t.first, t.second), t.third) }.toMutableMap()

    val seatsAroundPos = map.keys.map { Pair(it, getSurroundingSeats(map, it)) }.toMap()

    var notChanged = false
    do {
        notChanged = false
        val newMap = map.toMutableMap()
        map.forEach { (pos, status) ->
            when (status) {
                Status.Empty -> {
                    val seatsAround = seatsAroundPos[pos]!!
                    val occupied = seatsAround.count { map[it] == Status.Occupied }
                    if (occupied == 0) {
                        newMap[pos] = Status.Occupied
                        notChanged = true
                    }
                }
                Status.Floor -> {

                }
                Status.Occupied -> {
                    val seatsAround = seatsAroundPos[pos]!!
                    val occupied = seatsAround.count { map[it] == Status.Occupied }
                    if (occupied >= 4) {
                        newMap[pos] = Status.Empty
                        notChanged = true
                    }
                }
            }
        }
        map = newMap
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
    var map = input.lines().mapIndexed { y, line ->
        line.mapIndexed { x, s ->
            Triple(x, y, Status.of(s.toString()))
        }
    }.flatten()
        .associate { t -> Pair(Pair(t.first, t.second), t.third) }.toMutableMap()


    val seatsInSight = map.keys.map { Pair(it, getSeatInSight(map, it)) }.toMap()

    var notChanged = false
    do {
        notChanged = false
        val newMap = map.toMutableMap()
        map.forEach { (pos, status) ->
            when (status) {
                Status.Empty -> {
                    val seatsAround = seatsInSight[pos].orEmpty()
                    val occupied = seatsAround.count { map[it] == Status.Occupied }
                    if (occupied == 0) {
                        newMap[pos] = Status.Occupied
                        notChanged = true
                    }
                }
                Status.Floor -> {

                }
                Status.Occupied -> {
                    val seatsAround = seatsInSight[pos].orEmpty()
                    val occupied = seatsAround.count { map[it] == Status.Occupied }
                    if (occupied >= 5) {
                        newMap[pos] = Status.Empty
                        notChanged = true
                    }
                }
            }
        }
        map = newMap
    } while (notChanged)

    return map.values.count { it == Status.Occupied }
}


fun getSeatInSight(map: Map<Pair<Int, Int>, Status>, pos: Pair<Int, Int>): List<Pair<Int, Int>> {
    return listOf(
        nextVisibleSeat(map, pos, 1, 1),
        nextVisibleSeat(map, pos, 1, -1),
        nextVisibleSeat(map, pos, 1, 0),

        nextVisibleSeat(map, pos, -1, 1),
        nextVisibleSeat(map, pos, -1, -1),
        nextVisibleSeat(map, pos, -1, 0),

        nextVisibleSeat(map, pos, 0, 1),
        nextVisibleSeat(map, pos, 0, -1),

        ).filter { p -> map.containsKey(p) }.distinct()
}

fun nextVisibleSeat(map: Map<Pair<Int, Int>, Status>, pos: Pair<Int, Int>, xd: Int, yd: Int): Pair<Int, Int> {
    val nextPos = pos.copy(pos.first + xd, pos.second + yd)
    if (map.containsKey(nextPos)) {
        if (map[nextPos] != Status.Floor) return nextPos
        val nextY = if (yd > 0) yd + 1 else if (yd < 0) yd - 1 else yd
        val nextX = if (xd > 0) xd + 1 else if (xd < 0) xd - 1 else xd
        return nextVisibleSeat(map, pos, nextX, nextY)
    }
    return Pair(-1, -1)
}
