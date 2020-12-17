package day17

import AoCUtils.test
import solveWithTiming

val testInput = ".#.\n" +
        "..#\n" +
        "###"

val input = ".......#\n" +
        "....#...\n" +
        "...###.#\n" +
        "#...###.\n" +
        "....##..\n" +
        "##.#..#.\n" +
        "###.#.#.\n" +
        "....#..."

fun main() {
    solveWithTiming({ part1(testInput) }, 112, "test 1 part 1")
    solveWithTiming({ part1(input) }, 232, "part 1")

    solveWithTiming({ part2(testInput) }, 848, "test 1 part 2")
    solveWithTiming({ part2(input) }, 1620, "part 2")
}

enum class State { active, inActive }
data class Coordinate(val x: Int, val y: Int, val z: Int = 0, val w: Int = 0)

fun part1(input: String): Long {
    return calculateIt(input, 3)
}

fun part2(input: String): Long {
    return calculateIt(input, 4)
}

private fun calculateIt(input: String, dimensions: Int): Long {
    val cubes = input.lines().mapIndexed { y, v ->
        v.toCharArray().mapIndexed { x, c ->
            val state = when (c) {
                '#' -> State.active
                '.' -> State.inActive
                else -> throw Exception("Unknown state: $c")
            }
            Pair(Coordinate(x, y), state)
        }
    }.flatten().toMap().toMutableMap()

    for (turn in 1..6) {
        // add cubes around....
        val cubesToAdd = cubes.keys.map { cubesAround(it, dimensions) }.flatten().distinct()
        cubesToAdd.forEach { cube ->
            if (!cubes.containsKey(cube)) {
                cubes[cube] = State.inActive
            }
        }

        val cubesToChange = mutableListOf<Pair<Coordinate, State>>()
        cubes.forEach { (cube, state) ->
            val activesAround =
                cubesAround(cube, dimensions).map { cubes.getOrDefault(it, State.inActive) }
                    .count { it == State.active }

            val newState = if (state == State.inActive) {
                if (activesAround == 3) {
                    State.active
                } else {
                    State.inActive
                }
            } else {
                if (activesAround == 2 || activesAround == 3) {
                    State.active
                } else {
                    State.inActive
                }
            }
            cubesToChange.add(Pair(cube, newState))
        }
        cubesToChange.forEach { toChange ->
            cubes[toChange.first] = toChange.second
        }
    }


    return cubes.values.filter { it == State.active }.count().toLong()
}

fun cubesAround(coordinate: Coordinate, dimensions: Int): List<Coordinate> {
    val returnList = mutableListOf<Coordinate>()

    for (x in -1..1) {
        for (y in -1..1) {
            if (dimensions == 2) {
                returnList.add(Coordinate(x = coordinate.x + x, y = coordinate.y + y))
            } else {
                for (z in -1..1) {
                    if (dimensions == 3) {
                        returnList.add(Coordinate(x = coordinate.x + x, y = coordinate.y + y, z = coordinate.z + z))
                    } else if (dimensions == 4) {
                        for (w in -1..1) {
                            returnList.add(Coordinate(x = coordinate.x + x, y = coordinate.y + y, z = coordinate.z + z, w = coordinate.w + w))
                        }
                    }
                }
            }
        }
    }

    return returnList.filter { it != coordinate }.distinct()
}
