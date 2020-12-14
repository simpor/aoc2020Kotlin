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

const val yourBag = "shiny gold"
const val noOther = "no other"

data class BagRule(val count: Int, val bag: String)

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

    val map = rules.map { rule ->
        val from = rule.key
        val newRules = mutableListOf<String>()
        newRules.addAll(rule.value.map { it.bag })
        var goesToYourBag = false
        while (newRules.isNotEmpty()) {
            when (val to = newRules.removeAt(0)) {
                yourBag -> {
                    newRules.clear()
                    goesToYourBag = true
                    Pair(from, goesToYourBag)
                }
                noOther -> {
                }
                else -> {
                    newRules.addAll(rules[to].orEmpty().map { it.bag })
                }
            }
        }
        Pair(from, goesToYourBag)
    }

    return map.count { it.second }
}

fun part2(input: String): Int {
    val rules = createRules(input)

    data class RuleCalc(val multiplier: Int, val count: Int, val bag: String)

    val newRules = mutableListOf<RuleCalc>()
    newRules.addAll(rules[yourBag].orEmpty().map { rule -> RuleCalc(1, rule.count, rule.bag) })
    val bags = mutableListOf<RuleCalc>()
    while (newRules.isNotEmpty()) {
        val to = newRules.removeAt(0)
        bags.add(to)
        if (to.bag != noOther) {
            newRules.addAll(rules[to.bag].orEmpty().map { rule ->
                RuleCalc(to.multiplier * to.count, rule.count, rule.bag)
            })
        }
    }
    return bags.map { it.multiplier * it.count }.sum()
}


private fun createRules(input: String): Map<String, List<BagRule>> {
    return input.replace(".", "").replace("bags", "").replace("bag", "").lines().map { rule ->
        val split = rule.split(" contain ")
        val to = split[0].trim()
        val from = split[1]
        Pair(to, from.split(",").map { s ->
            val string = s.trim()
            if (string == noOther)
                BagRule(0, string)
            else {
                val num = string.substringBefore(" ").trim()
                val bag = string.substringAfter(" ").trim()
                BagRule(num.toInt(), bag)
            }
        })
    }.toMap()
}