package day15

import AoCUtils.test

fun main() {
    part1("0,3,6") test Pair(436, "test 1 part 1 should be 436")
    part1("1,3,2") test Pair(1, "test 2 part 1 should be 1")
    part1("2,1,3") test Pair(10, "test 3 part 1 should be  10")
    part1("1,2,3") test Pair(27, "test 4 part 1 should be  27")
    part1("2,3,1") test Pair(78, "test 5 part 1 should be  78")
    part1("3,2,1") test Pair(438, "test 6 part 1 should be  438")
    part1("3,1,2") test Pair(1836, " test 7 part 1 should be 1836")

    part1("0,14,6,20,1,4") test Pair(257, "part 1 should be 0")


    part2("0,14,6,20,1,4") test Pair(8546398, "part 2 should be 8546398")

    part2("0,3,6") test Pair(175594, "test 2 part 2 should be 175594")
    part2("1,3,2") test Pair(2578, "test 2 part 2 should be 2578")
    part2("2,1,3") test Pair(3544142, "test 2 part 2 should be 3544142")
    part2("1,2,3") test Pair(261214, "test 2 part 2 should be 261214")
    part2("2,3,1") test Pair(6895259, "test 2 part 2 should be 6895259")
    part2("3,2,1") test Pair(18, "test 2 part 2 should be 18")
    part2("3,1,2") test Pair(362, "test 2 part 2 should be 362")

}

fun part1(input: String): Int {
    val nums = input.split(",").map { it.toInt() }.toMutableList()
    val numIndex = nums.mapIndexed{index, i -> Pair(i, mutableListOf(index+1)) }.toMap().toMutableMap()
    for (i in (nums.size+1)..2020) {
        val last = nums.last()
        if (numIndex.containsKey(last) && numIndex[last]!!.size > 1) {
            val list = numIndex[last]!!
            val newNum = list.last() - list[list.size-2]
            val numList = numIndex.getOrDefault(newNum, mutableListOf())
            numList.add(i)
            numIndex[newNum] = numList
            nums.add(newNum)
        } else {
            val numList = numIndex.getOrDefault(0, mutableListOf())
            numList.add(i)
            numIndex[0] = numList
            nums.add(0)
        }
    }
    return nums.last()
}

fun part2(input: String): Int {
    val nums = input.split(",").map { it.toInt() }.toMutableList()
    val numIndex = nums.mapIndexed{index, i -> Pair(i, mutableListOf(index+1)) }.toMap().toMutableMap()
    for (i in (nums.size+1)..30000000) {
        val last = nums.last()
        if (numIndex.containsKey(last) && numIndex[last]!!.size > 1) {
            val list = numIndex[last]!!
            val newNum = list.last() - list[list.size-2]
            val numList = numIndex.getOrDefault(newNum, mutableListOf())
            numList.add(i)
            numIndex[newNum] = numList
            nums.add(newNum)
        } else {
            val numList = numIndex.getOrDefault(0, mutableListOf())
            numList.add(i)
            numIndex[0] = numList
            nums.add(0)
        }
    }
    return nums.last()
}