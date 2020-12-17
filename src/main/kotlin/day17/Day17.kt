package day17

import AoCUtils.test

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
    part1(testInput) test Pair(112, "test 1 part 1 should be 0")
    part1(input) test Pair(232, "part 1 should be 0")

    part2(testInput) test Pair(848, "test 2 part 2 should be 0")
    part2(input) test Pair(1620, "part 2 should be 0")

}

enum class State { active, inActive }
data class Cube(val x: Int, val y: Int, val z: Int = 0)
data class Cube4(val x: Int, val y: Int, val z: Int = 0, val w: Int = 0)

fun part1(input: String): Long {
    val cubes = input.lines().mapIndexed { y, v ->
        v.toCharArray().mapIndexed { x, c ->
            val state = when (c) {
                '#' -> State.active
                '.' -> State.inActive
                else -> throw Exception("Unknown state: $c")
            }
            Pair(Cube(x, y), state)
        }
    }.flatten().toMap().toMutableMap()

    for (turn in 1..6) {
        // add cubes around....
        val cubesToAdd = cubes.keys.map { cubesAround(it) }.flatten().distinct()
        cubesToAdd.forEach { cube ->
            if (!cubes.containsKey(cube)) {
                cubes[cube] = State.inActive
            }
        }

        val cubesToChange = mutableListOf<Pair<Cube, State>>()
        cubes.forEach { (cube, state) ->
            val activesAround =
                cubesAround(cube).map { cubes.getOrDefault(it, State.inActive) }.count { it == State.active }

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

fun cubesAround(cube: Cube): List<Cube> {
    val returnList = mutableListOf<Cube>()

    for (x in -1..1) {
        for (y in -1..1) {
            for (z in -1..1) {
                returnList.add(cube.copy(x = cube.x + x, y = cube.y + y, z = cube.z + z))
            }
        }
    }

    return returnList.filter { it != cube }.distinct()
}

fun cubesAround4(cube: Cube4): List<Cube4> {
    val returnList = mutableListOf<Cube4>()
    for (x in -1..1) {
        for (y in -1..1) {
            for (z in -1..1) {
                for (w in -1..1) {
                    returnList.add(cube.copy(x = cube.x + x, y = cube.y + y, z = cube.z + z, w = cube.w + w))
                }
            }
        }
    }
    return returnList.filter { it != cube }.distinct()
}

fun part2(input: String): Long {
    val cubes = input.lines().mapIndexed { y, v ->
        v.toCharArray().mapIndexed { x, c ->
            val state = when (c) {
                '#' -> State.active
                '.' -> State.inActive
                else -> throw Exception("Unknown state: $c")
            }
            Pair(Cube4(x, y), state)
        }
    }.flatten().toMap().toMutableMap()

    for (turn in 1..6) {
        // add cubes around....
        val cubesToAdd = cubes.keys.map { cubesAround4(it) }.flatten().distinct()
        cubesToAdd.forEach { cube ->
            if (!cubes.containsKey(cube)) {
                cubes[cube] = State.inActive
            }
        }

        val cubesToChange = mutableListOf<Pair<Cube4, State>>()
        cubes.forEach { (cube, state) ->
            val activesAround =
                cubesAround4(cube).map { cubes.getOrDefault(it, State.inActive) }.count { it == State.active }

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