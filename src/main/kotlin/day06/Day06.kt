package day06

import readText
import test

fun main() {
    val testInput = "abc\n" +
            "\n" +
            "a\n" +
            "b\n" +
            "c\n" +
            "\n" +
            "ab\n" +
            "ac\n" +
            "\n" +
            "a\n" +
            "a\n" +
            "a\n" +
            "a\n" +
            "\n" +
            "b"
    val input = readText("06.txt")

    part1(testInput) test Pair(11, "test part 1 - Should be 11")
    part1(input) test Pair(6443, "Part1 - Should be 6443")

    part1Smaller(testInput) test Pair(11, "test part 1 - Should be 11")
    part1Smaller(input) test Pair(6443, "Part1 - Should be 6443")

    part2(testInput) test Pair(6, "test part 2 - Should be 6")
    part2(input) test Pair(3232, "Part 2 - Should be 3232")

    part2Smaller(testInput) test Pair(6, "test part 2 - Should be 6")
    part2Smaller(input) test Pair(3232, "Part 2 - Should be 3232")
}

fun part1(input: String): Int {

    val split = input.split("\n\n".toRegex())
    val map = split.map { a ->
        var personCount = 0
        val questions = mutableSetOf<Char>()
        for (line in a.lines()) {
            personCount++
            for (char in line) {
                questions.add(char)
            }
        }
        Pair(personCount, questions)
    }
    return map.map { it.second.size }.sum()

}

fun part1Smaller(input: String): Int = input.split("\n\n".toRegex()).map { a ->
    a.lines()
        .map { it.toCharArray().toList() }
        .flatten()
        .distinct()
        .count()
}.sum()


fun part2(input: String): Int {
    val split = input.split("\n\n".toRegex())
    val map = split.map { a ->
        var personCount = 0
        val questions = mutableMapOf<Char, Int>()
        for (line in a.lines()) {
            personCount++
            for (char in line) {
                questions[char] = questions.getOrDefault(char, 0) + 1
            }
        }
        questions.count { it.value == personCount }
    }
    return map.sum()

}

fun part2Smaller(input: String): Int {
    val split = input.split("\n\n".toRegex())
    val map = split.map { a ->
        val questions = mutableMapOf<Char, Int>()
        for (line in a.lines()) {
            for (char in line) {
                questions[char] = questions.getOrDefault(char, 0) + 1
            }
        }
        questions.count { it.value == a.lines().count() }
    }
    return map.sum()
}
