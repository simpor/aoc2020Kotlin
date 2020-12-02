package day01

import readLines
import test

data class Password(val num1: Int, val num2: Int, val c: Char, val pass: String)

fun main() {
    val testInput = "1-3 a: abcde\n" +
            "1-3 b: cdefg\n" +
            "2-9 c: ccccccccc"
    val input = readLines("02.txt")

    part1(testInput.lines()) test Pair(2, "Should find 2 correct passwords")
    part1(input) test Pair(660, "Part 1 - Should find 660 correct passwords")

    part2(testInput.lines()) test Pair(1, "Should find 1 correct passwords")
    part2(input) test Pair(530, "Part 2 - Should find 530 correct passwords")
}

private fun part1(input: List<String>): Int {
    return mapPasswords(input).map { passwords ->
        val count = passwords.pass.count { it == passwords.c }
        count >= passwords.num1 && count <= passwords.num2
    }.filter { it }
        .count()
}

private fun part2(input: List<String>): Int {
    return mapPasswords(input).map { password ->
        val first = password.pass[password.num1 - 1]
        val second = password.pass[password.num2 - 1]

        listOf(first == password.c, second == password.c).filter { it }.size == 1
    }.filter { it }
        .count()
}

private fun mapPasswords(input: List<String>) = input.map { line ->
    val split = line.split(":")
    val req = split[0].split(" ")
    val nums = req[0].split("-")
    val num1 = nums[0].toInt()
    val num2 = nums[1].toInt()
    val character = req[1].trim()
    val password = split[1].trim()
    Password(num1, num2, character[0], password)
}
