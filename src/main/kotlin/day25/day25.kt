package day25

import solveWithTiming

val testInput = "5764801\n" +
        "17807724"

val input = "8421034\n" +
        "15993936"

fun main() {
    solveWithTiming({ part1(testInput) }, 14897079, "test 1 part 1")
    solveWithTiming({ part1(input) }, 0, "part 1")

    solveWithTiming({ part2(testInput) }, 0, "test 1 part 2")
    solveWithTiming({ part2(input) }, 0, "part 2")
}

fun part1(input: String): Long {
    val divider = 20201227
    val subjectNum = 7

    val inputList = input.lines()

    val doorPublicKey = inputList.first().toLong()
    val keyPublicKey = inputList.last().toLong()


    // door
    var doorCounter = 0
    var num = 1L
    while (true) {
        num *= subjectNum
        num %= divider
        doorCounter++
        if (num == doorPublicKey) break
    }

    // key
    var keyCounter = 0
    num = 1L
    while (true) {
        num *= subjectNum
        num %= divider
        keyCounter++
        if (num == keyPublicKey) break
    }

    var doorEncrypt = 1L
    repeat(doorCounter) {
        doorEncrypt *= keyPublicKey
        doorEncrypt %= divider
    }

    var keyEncrypt = 1L
    repeat(keyCounter) {
        keyEncrypt *= doorPublicKey
        keyEncrypt %= divider
    }


    return keyEncrypt
}

fun part2(input: String): Long {
    return 0L
}