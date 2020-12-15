package day15

import solveWithTiming

fun main() {
    solveWithTiming({ part1("0,3,6") }, 436L, "test 1 part 1")
    solveWithTiming({ part1("1,3,2") }, 1L, "test 2 part 1")
    solveWithTiming({ part1("2,1,3") }, 10L, "test 3 part 1")
    solveWithTiming({ part1("1,2,3") }, 27L, "test 4 part 1")
    solveWithTiming({ part1("2,3,1") }, 78L, "test 5 part 1")
    solveWithTiming({ part1("3,2,1") }, 438L, "test 6 part 1")
    solveWithTiming({ part1("3,1,2") }, 1836L, "test 7 part 1")
    solveWithTiming({ part1("0,14,6,20,1,4") }, 257, "part 1")


    solveWithTiming({ part2("0,14,6,20,1,4") }, 8546398, "part 2")

    solveWithTiming({ part2("0,3,6") }, 175594L, "part 2 - test 1")
    solveWithTiming({ part2("1,3,2") }, 2578L, "part 2 - test 2")
    solveWithTiming({ part2("2,1,3") }, 3544142L, "part 2 - test 3")
    solveWithTiming({ part2("1,2,3") }, 261214L, "part 2 - test 4")
    solveWithTiming({ part2("2,3,1") }, 6895259L, "part 2 - test 5")
    solveWithTiming({ part2("3,2,1") }, 18L, "part 2 - test 6")
    solveWithTiming({ part2("3,1,2") }, 362L, "part 2 - test 7")

}

fun part1(input: String): Long {
    return numberGame(input, 2020L)
}

fun part2(input: String): Long {
    return numberGame(input, 30000000L)
}

private fun numberGame(input: String, counter: Long): Long {
    val nums = input.split(",").map { it.toLong() }.toMutableList()
    val numIndex = nums.mapIndexed { index, i -> Pair(i, Pair(-1L, index + 1L)) }.toMap().toMutableMap()
    var last = nums.last()
    for (i in (nums.size + 1)..counter) {
        if (numIndex.containsKey(last) && numIndex[last]!!.first != -1L) {
            val lastPair = numIndex[last]!!
            val newNum = lastPair.second - lastPair.first
            val pair = numIndex.getOrDefault(newNum, Pair(-1, -1))
            numIndex[newNum] = Pair(pair.second, i)
            last = newNum
        } else {
            val pair = numIndex.getOrDefault(0, Pair(-1, -1))
            numIndex[0] = Pair(pair.second, i)
            last = 0
        }
    }
    return last
}