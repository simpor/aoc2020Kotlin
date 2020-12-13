package day13

import AoCUtils
import AoCUtils.test

val testInput = "939\n" +
        "7,13,x,x,59,x,31,19"

val input = AoCUtils.readText("13.txt")

fun main() {
    part1(testInput) test Pair(295, "test 1 part 1 should be 295")
    part1(input) test Pair(161, "part 1 should be 161")

    part2(testInput) test Pair(1068781, "test 2 part 2 should be 1068781")
    part2(
        "xxx\n" +
                "17,x,13,19"
    ) test Pair(3417, "test 2 part 2 should be 3417")
    part2(
        "xxx\n" +
                "67,7,59,61"
    ) test Pair(754018, "test 2 part 2 should be 754018")
    part2(
        "xxx\n" +
                "67,x,7,59,61"
    ) test Pair(779210, "test 2 part 2 should be 779210")
    part2(
        "xxx\n" +
                "67,7,x,59,61"
    ) test Pair(1261476, "test 2 part 2 should be 1261476")
    part2(
        "xxx\n" +
                "1789,37,47,1889"
    ) test Pair(1202161486, "test 2 part 2 should be 1202161486")
    part2(input) test Pair(0, "part 2 should be 0")

}

fun part1(input: String): Int {
    val time = input.lines().let { it[0].toInt() }
    val buses = input.lines().let { it[1].split(",").filter { it != "x" }.map { it.toInt() } }
    val map = buses.map { Pair(it, it - time % it) }
    val first = map.sortedBy { it.second }.first()
    return first.first * first.second
}

data class Bus(val id: Long, val timeStamp: Long)

fun part2(input: String): Long {
    val buses =
        input.lines().let {
            it[1].split(",")
                .mapIndexed { index, t -> Pair(t, index) }
        }
            .filter { it.first != "x" }
            .map { Bus(it.first.toLong(), it.second.toLong()) }


    var num = 1L
    var num2 = 1L
    var num3 = 1L
    for(i in buses.indices){
        val bus = buses[i]
        num = lcm(num, (bus.id + bus.timeStamp))
        num2 = lcm(num2, (bus.id))
        num3 = lcm(num3, (bus.id - bus.timeStamp))
        println()
        println(num)
        println(num2)
        println(num3)
    }



    if (checkCondition(buses, num)) {
        return num
    }

    return 0
}

fun checkCondition(buses: List<Bus>, timeToCheck: Long): Boolean {
    return buses.map { bus ->
        if (bus.timeStamp == 0L) {
            true
        } else {
            val t = bus.id - timeToCheck % bus.id
            t == bus.timeStamp
        }
    }.count { it } == buses.size
}


fun lcm(a: Long, b: Long): Long {
    if (a > b)
        return (a / gcd(a, b)) * b;
    else
        return (b / gcd(a, b)) * a;
}

fun gcd(a: Long, b: Long): Long {
    if (b == 0L)
        return a
    return gcd(b, a % b);
}
