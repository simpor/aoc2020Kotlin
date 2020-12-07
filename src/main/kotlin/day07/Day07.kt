package day07

import AoCUtils
import AoCUtils.test

val testInput = "light red bags contain 1 bright white bag, 2 muted yellow bags.\n" +
        "dark orange bags contain 3 bright white bags, 4 muted yellow bags.\n" +
        "bright white bags contain 1 shiny gold bag.\n" +
        "muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.\n" +
        "shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.\n" +
        "dark olive bags contain 3 faded blue bags, 4 dotted black bags.\n" +
        "vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.\n" +
        "faded blue bags contain no other bags.\n" +
        "dotted black bags contain no other bags."

val testInput2 = "shiny gold bags contain 2 dark red bags.\n" +
        "dark red bags contain 2 dark orange bags.\n" +
        "dark orange bags contain 2 dark yellow bags.\n" +
        "dark yellow bags contain 2 dark green bags.\n" +
        "dark green bags contain 2 dark blue bags.\n" +
        "dark blue bags contain 2 dark violet bags.\n" +
        "dark violet bags contain no other bags."

fun main() {
    val input = AoCUtils.readText("07.txt")

    part1(testInput) test Pair(4, "test part 1 - should be 4")
    part1(input) test Pair(128, "Part 1 - should be 0")

    part2(testInput) test Pair(32, "test part 2 - should be 32")
    part2(testInput2) test Pair(126, "test part 2 - should be 126")
    part2(input) test Pair(20189, "Part 2 - should be 20189")
}

fun part1(input: String): Int {

    val rules = createRules(input)
    val yourBag = "shiny gold"

    val map = rules.map { rule ->
        val from = rule.key
        if (from == yourBag) {
            Pair(from, false)
        } else {
            var loop = true
            val newRules = mutableListOf<String>()
            newRules.addAll(rule.value.map { it.second })
            var goesToYourBag = false
            while (loop) {
                when (val to = newRules.removeAt(0)) {
                    yourBag -> {
                        loop = false
                        goesToYourBag = true
                    }
                    "no other" -> {
                    }
                    else -> {
                        newRules.addAll(rules[to].orEmpty().map { it.second })
                    }
                }

                if (newRules.isEmpty()) {
                    loop = false
                }
            }
            Pair(from, goesToYourBag)
        }
    }
    return map.count { it.second }
}

fun part2(input: String): Int {

    val rules = createRules(input)

    data class RuleCalc(val multiplier: Int, val count: Int, val bag: String)

    val yourBag = "shiny gold"
    val newRules = mutableListOf<RuleCalc>()

    val rule = rules[yourBag].orEmpty()
    var loop = true

    newRules.addAll(rule.map { RuleCalc(1, it.first, it.second) })
    val bags = mutableListOf<RuleCalc>()
    while (loop) {
        val to = newRules.removeAt(0)
        bags.add(to)
        when (to.bag) {
            "no other" -> {
            }
            else -> {
                val list = rules[to.bag].orEmpty().map { RuleCalc(to.multiplier * to.count, it.first, it.second) }
                newRules.addAll(list)
            }
        }

        if (newRules.isEmpty()) {
            loop = false
        }
    }
    return bags.map { it.multiplier * it.count }.sum()
}

private fun createRules(input: String): Map<String, List<Pair<Int, String>>> {
    return input.lines().map { rule ->
        val split = rule.split(" contain ")
        val to = split[0].replace("bags", "").replace("bag", "").trim()
        val from = split[1]
        Pair(to, from.split(",").map { s ->
            val string = s.replace(".", "").replace("bags", "").replace("bag", "").trim()
            if (string == "no other")
                Pair(0, string)
            else {
                val num = string.substringBefore(" ").trim()
                val bag = string.substringAfter(" ").trim()
                Pair(num.toInt(), bag)
            }
        })
    }.toMap()
}