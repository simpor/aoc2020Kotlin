package day19

import AoCUtils
import solveWithTiming

val testInput = "0: 4 1 5\n" +
        "1: 2 3 | 3 2\n" +
        "2: 4 4 | 5 5\n" +
        "3: 4 5 | 5 4\n" +
        "4: \"a\"\n" +
        "5: \"b\"\n" +
        "\n" +
        "ababbb\n" +
        "bababa\n" +
        "abbbab\n" +
        "aaabbb\n" +
        "aaaabbb"

val input = AoCUtils.readText("19.txt")

fun main() {
    solveWithTiming({ part1(testInput) }, 2, "test 1 part 1")
    solveWithTiming({ part1(input) }, 0, "part 1")

    solveWithTiming({ part2(testInput) }, 0, "test 1 part 2")
    solveWithTiming({ part2(input) }, 0, "part 2")
}

data class Rule(
    val id: Int,
    val letter: String = "",
    val match1: List<Int> = emptyList(),
    val match2: List<Int> = emptyList()
)

fun part1(input: String): Long {
    val rules = input.lines()
        .filter { it.isNotBlank() }
        .mapNotNull { line ->
            if (line.contains(":")) {
                val split = line.split(":")
                val id = split[0].trim()
                when {
                    split[1].trim().contains("|") -> {
                        val or = split[1].trim().split("|")
                        Rule(
                            id.toInt(),
                            match1 = or[0].trim().split(" ").map { it.toInt() },
                            match2 = or[1].trim().split(" ").map { it.toInt() })
                    }
                    split[1].contains("\"") -> {
                        Rule(id.toInt(), letter = split[1].trim()
                            .replace('"', ' ')
                            .trim()
                        )
                    }
                    else -> {
                        Rule(id.toInt(), match1 = split[1].trim().split(" ").map { it.toInt() })
                    }
                }
            } else null
        }.associateBy { it.id }

    val messages = input.lines()
        .filter { it.isNotBlank() }.mapNotNull { line ->
            if (!line.contains(":")) {
                line
            } else {
                null
            }
        }



    return 0L
}

fun createRuleChain(rules: List<Rule>): List<List<Boolean>> {

}

fun part2(input: String): Long {
    return 0L
}