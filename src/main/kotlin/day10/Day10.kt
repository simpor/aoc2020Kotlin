package day10


import AoCUtils
import AoCUtils.test

val testInput1 = "16\n" +
        "10\n" +
        "15\n" +
        "5\n" +
        "1\n" +
        "11\n" +
        "7\n" +
        "19\n" +
        "6\n" +
        "12\n" +
        "4"
val testInput2 = "28\n" +
        "33\n" +
        "18\n" +
        "42\n" +
        "31\n" +
        "14\n" +
        "46\n" +
        "20\n" +
        "48\n" +
        "47\n" +
        "24\n" +
        "23\n" +
        "49\n" +
        "45\n" +
        "19\n" +
        "38\n" +
        "39\n" +
        "11\n" +
        "1\n" +
        "32\n" +
        "25\n" +
        "35\n" +
        "8\n" +
        "17\n" +
        "7\n" +
        "9\n" +
        "4\n" +
        "2\n" +
        "34\n" +
        "10\n" +
        "3"

val input = AoCUtils.readText("10.txt")

fun main() {
    part1(testInput1) test Pair(35, "test 1 part 1 should be 35")
    part1(testInput2) test Pair(220, "test 1 part 1 should be 220")
    part1(input) test Pair(2738, "part 1 should be 2738")

    part2(testInput1) test Pair(8, "test 1 part 2 should be 8")
    part2(testInput2) test Pair(19208, "test 1 part 2 should be 19208")
    part2(input) test Pair(74_049_191_673_856, "part 2 should be 74 049 191 673 856")

}

data class Adapter(val diff1: Int = 0, val diff2: Int = 0, val diff3: Int = 0, val last: Int = 0)

fun part1(input: String): Int {
    return input.lines().map { it.toInt() }.sorted().reversed().foldRight(Adapter(), { num, adapter ->
        when {
            num - adapter.last == 1 -> adapter.copy(diff1 = adapter.diff1 + 1, last = num)
            num - adapter.last == 2 -> adapter.copy(diff2 = adapter.diff2 + 1, last = num)
            else -> adapter.copy(diff3 = adapter.diff3 + 1, last = num)
        }
    }).let { it.diff1 * (it.diff3 + 1) }
}

fun part2(input: String): Long {
    val adapters = input.lines().map { it.toLong() }.sorted()
    val result = mutableMapOf(0L to 1L)
    for (i in adapters) {
        val a = result.getOrDefault(i - 1, 0)
        val b = result.getOrDefault(i - 2, 0)
        val c = result.getOrDefault(i - 3, 0)
        result[i] = a + b + c
    }
    return result.getValue(adapters.last())
}

