package day21

import AoCUtils
import solveWithTiming

val testInput = "mxmxvkd kfcds sqjhc nhms (contains dairy, fish)\n" +
        "trh fvjkl sbzzf mxmxvkd (contains dairy)\n" +
        "sqjhc fvjkl (contains soy)\n" +
        "sqjhc mxmxvkd sbzzf (contains fish)"

val input = AoCUtils.readText("21.txt")

fun main() {
    solveWithTiming({ part1(testInput) }, 5, "test 1 part 1")
    solveWithTiming({ part1(input) }, 2324, "part 1")

    solveWithTiming({ part2(testInput) }, "mxmxvkd,sqjhc,fvjkl", "test 1 part 2")
    solveWithTiming({ part2(input) }, "bxjvzk,hqgqj,sp,spl,hsksz,qzzzf,fmpgn,tpnnkc", "part 2")
}

fun part1(input: String): Long {
    val menu = parseInput(input)
    val wordBook = getWordBook(menu)

    val allIngredients = menu.map { it.second.toMutableList() }

    allIngredients.forEach { line ->
        line.removeAll(wordBook.values)
    }


    return allIngredients.map { it.size }.sum().toLong()
}

fun part2(input: String): String {
    val menu = parseInput(input)
    val wordBook = getWordBook(menu)

    return wordBook.keys.sorted().map { wordBook[it] }.joinToString(separator = ",")

}

private fun parseInput(input: String): List<Triple<Int, MutableSet<String>, MutableSet<String>>> {
    return input.lines().mapIndexed { index, line ->
        val split = line.replace("(", "").replace(")", "").split("contains ")
        val ingredients =
            split[0].split(" ").let { it.filter { t -> t.isNotBlank() }.map { z -> z.trim() } }.toMutableSet()
        val allergen =
            split[1].split(", ").let { it.filter { t -> t.isNotBlank() }.map { z -> z.trim() } }.toMutableSet()
        Triple(index, ingredients, allergen)
    }
}

private fun getWordBook(menu: List<Triple<Int, MutableSet<String>, MutableSet<String>>>): MutableMap<String, String> {
    val wordBook = mutableMapOf<String, String>()
    val intersected = menu.asSequence().map { it.third }.flatten().distinct()
        .map { a -> Pair(a, menu.filter { it.third.contains(a) }.map { it.second }) }
        .map { a ->
            Pair(a.first, a.second.fold(setOf<String>()) { acc, set ->
                if (acc.isEmpty()) set
                else acc.intersect(set)
            })
        }.toList()

    while (wordBook.size < intersected.size) {
        intersected
            .map { i -> Pair(i.first, i.second.filter { !wordBook.values.contains(it) }) }
            .filter { it.second.size == 1 }
            .forEach { a -> wordBook[a.first] = a.second.first() }
    }
    return wordBook
}
