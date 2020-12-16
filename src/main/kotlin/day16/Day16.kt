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
    solveWithTiming({ part1(input) }, 18142, "part 1")

    solveWithTiming({
        part2(
            "class: 0-1 or 4-19\n" +
                    "row: 0-5 or 8-19\n" +
                    "seat: 0-13 or 16-19\n" +
                    "\n" +
                    "your ticket:\n" +
                    "11,12,13\n" +
                    "\n" +
                    "nearby tickets:\n" +
                    "3,9,18\n" +
                    "15,1,5\n" +
                    "5,14,9"
        )
    }, 1, "test 1 part 2")
    solveWithTiming({ part2(input) }, 0, "part 2")

}

fun part1(input: String): Int {

    val split = input.replace("\n\n", "\n").split("your ticket:", "nearby tickets:")
    val ranges = split[0].lines().filter { it.isNotBlank() }.map { line ->
        line.split(": ", " or ", "-").let {
            listOf(
                Pair(it[0] + "-1", Pair(it[1].toInt(), it[2].toInt())),
                Pair(it[0] + "-2", Pair(it[3].toInt(), it[4].toInt()))
            )
        }
    }.flatten().toMap()

    val yourTicket = split[1].replace("\n", "").split(",").map { it.toInt() }
    val otherTickets =
        split[2].replaceFirst("\n", "").lines().filter { it.isNotBlank() }.map { it.split(",").map { m -> m.toInt() } }


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


fun part2(input: String): Long {

    val split = input.replace("\n\n", "\n").split("your ticket:", "nearby tickets:")
    val ranges = split[0].lines().filter { it.isNotBlank() }.map { line ->
        line.split(": ", " or ", "-").let {
            Triple(
                it[0],
                Pair(it[1].toInt(), it[2].toInt()),
                Pair(it[3].toInt(), it[4].toInt())
            )
        }
    }

    val yourTicket = split[1].replace("\n", "").split(",").map { it.toInt() }
    val otherTickets =
        split[2].replaceFirst("\n", "").lines().filter { it.isNotBlank() }.map { it.split(",").map { m -> m.toInt() } }


    val validTickets = otherTickets.map { ticket ->
        val wrongNums = mutableSetOf<Int>()
        ticket.forEach { num ->
            var wrong = true
            ranges.forEach { range ->
                if (num >= range.second.first && num <= range.second.second) {
                    wrong = false
                } else if (num >= range.third.first && num <= range.third.second) {
                    wrong = false
                }
            }
            if (wrong) {
                wrongNums.add(num)
            }
        }
        if (wrongNums.size > 0) null else ticket
    }.filterNotNull()

    // identify positions...

    val positions = validTickets.map { ticket ->
        ticket.map { num ->
            ranges.map { range ->
                if (num >= range.second.first && num <= range.second.second) {
                    range.first
                } else if (num >= range.third.first && num <= range.third.second) {
                    range.first
                } else {
                    ""
                }
            }
        }
    }

    val positionMap = mutableMapOf<Int, MutableList<List<String>>>()

    val map = positions.map { m ->
        m.mapIndexed { index, i ->
            val list = positionMap.getOrDefault(index, mutableListOf())
            list.add(i)
            positionMap[index] = list
        }
    }


    val nameCounter = positionMap.map { (k, v) ->
        val names = v.flatten().distinct().filter { it.isNotBlank() }.map { Pair(it, 0) }.toMap().toMutableMap()
        v.map { a ->
            a.filter { it.isNotBlank() }.map { b ->
                names[b] = names[b]!! + 1
            }
        }
        names
    }.toMutableList()

    // decide position...
    val positionsForSeats = mutableMapOf<Int, String>()

    val toRemove = mutableSetOf<String>()
    while (nameCounter.size != toRemove.size) {
        nameCounter.forEachIndexed { index, m ->
            val candidates = m
                .filter { (k, v) -> !toRemove.contains(k) }
                .filter { (k, v) -> v == validTickets.size }
            if (candidates.size == 1) {
                positionsForSeats[index] = candidates.keys.first()
                toRemove.add(candidates.keys.first())
            }
        }
    }

    val depatrureNums = positionsForSeats.filter { (k, v) -> v.contains("departure") }
        .map { (k, v) ->
            yourTicket[k].toLong()
        }

    // 337527599 to low
    return depatrureNums.fold(1L, {acc, num -> acc * num})
}