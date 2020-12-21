package day21

import AoCUtils
import solveWithTiming

val testInput = "mxmxvkd kfcds sqjhc nhms (contains dairy, fish)\n" +
        "trh fvjkl sbzzf mxmxvkd (contains dairy)\n" +
        "sqjhc fvjkl (contains soy)\n" +
        "sqjhc mxmxvkd sbzzf (contains fish)"

val input = AoCUtils.readText("21.txt")

fun main() {
    //solveWithTiming({ part1(testInput) }, 5, "test 1 part 1")
    solveWithTiming({ part1(input) }, 0, "part 1")

    solveWithTiming({ part2(testInput) }, 0, "test 1 part 2")
    solveWithTiming({ part2(input) }, 0, "part 2")
}

fun part1(input: String): Long {

    val menu = input.lines().mapIndexed { index, line ->
        val split = line.replace("(", "").replace(")", "").split("contains ")
        val ingredients =
            split[0].split(" ").let { it.filter { t -> t.isNotBlank() }.map { z -> z.trim() } }.toMutableSet()
        val allergen =
            split[1].split(", ").let { it.filter { t -> t.isNotBlank() }.map { z -> z.trim() } }.toMutableSet()
        Triple(index, ingredients, allergen)
    }

    val allergensToIngredients = menu.map { p ->
        p.third.map { Triple(p.first, it, p.second) }
    }.flatten().toMutableList()

    val allIngredients = menu.map { it.second.toMutableList() }

    val menu2 = menu.toMutableList()

    val wordBook = mutableMapOf<String, String>()
    val almostMatch = mutableMapOf<String, List<String>>()
    val allergents = menu.map { it.third }.flatten().distinct()

    val allergentsToMenu =
        allergents.map { allergent -> Pair(allergent, menu.filter { it.third.contains(allergent) }.map { it.second }) }

    val intersected = allergentsToMenu.map { a ->
        Pair(a.first, a.second.fold(setOf<String>()) { acc, set ->
            if (acc.isEmpty()) set
            else acc.intersect(set)
        })
    }

    while (wordBook.size < intersected.size) {
        intersected
            .map { i -> Pair(i.first, i.second.filter { !wordBook.values.contains(it) }) }
            .filter { it.second.size == 1 }
            .forEach { a -> wordBook[a.first] = a.second.first() }
    }


    allIngredients.forEach { line ->
        line.removeAll(wordBook.values)
    }


    return allIngredients.map { it.size }.sum().toLong()
}

fun part2(input: String): Long {
    return 0L
}