package day09

import AoCUtils
import AoCUtils.test

val testInput = "35\n" +
        "20\n" +
        "15\n" +
        "25\n" +
        "47\n" +
        "40\n" +
        "62\n" +
        "55\n" +
        "65\n" +
        "95\n" +
        "102\n" +
        "117\n" +
        "150\n" +
        "182\n" +
        "127\n" +
        "219\n" +
        "299\n" +
        "277\n" +
        "309\n" +
        "576"

val input = AoCUtils.readText("09.txt")

fun main() {
    part1(testInput, 5) test Pair(127, "test 1 part 1 should be 127")
    part1(input, 25) test Pair(1639024365, "part 1 should be 1639024365")

    part2(testInput, 5) test Pair(62, "test 2 part 2 should be 62")
    part2(input, 25) test Pair(219202240, "part 2 should be 219202240")

}

fun part1(input: String, startIndex: Int): Long {
    val allNums = input.lines().map { it.toLong() }

    for (a in startIndex until allNums.size) {
        val numbersToCheck = allNums.subList(a - startIndex, a)

        val sums =
            numbersToCheck.mapIndexed { firstIndex, first ->
                numbersToCheck.mapIndexed { secondIndex, second ->
                    if (firstIndex != secondIndex) first + second else 0
                }
            }
                .flatten()
                .distinct()

        val element = allNums[a]
        if (!sums.contains(element))
            return element
    }

    return -1
}

fun part2(input: String, startNum: Int): Long {
    val allNums = input.lines().map { it.toLong() }

    val missingNum = part1(input, startNum)
    for (a in allNums.indices) {
        val startA = allNums[a]
        var sums = allNums[a]
        val list = mutableListOf(startA)
        for (b in a + 1 until allNums.size) {
            val currentB = allNums[b]
            list.add(currentB)
            sums += currentB
            if (sums == missingNum) {
                val newList = list.sorted()
                val smallest = newList.first()
                val biggest = newList.last()
                return (smallest + biggest)
            } else if (sums > missingNum) {
                break
            }
        }
    }

    return -1
}

