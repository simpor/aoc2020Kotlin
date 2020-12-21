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

    val emptyList = mutableListOf<List<String>>()
    while (allergensToIngredients.isNotEmpty()) {
        val empty = menu2.filter { it.third.isEmpty() }
        emptyList.addAll(empty.map { it.second.toList() })
        menu2.forEach { t -> empty.forEach{y -> t.second.removeAll(y.second)} }


        val a = menu2.filter { it.third.size == 1 }
        if (a.isEmpty()) {
            break
        }
        a.forEach { b ->
            val allergen = b.third.first()
            if (b.second.size == 1) {
                val ingredient = b.second.first()
                println("Found: $allergen = $ingredient")
                wordBook[allergen] = ingredient
                menu2.forEach { x ->
                    x.second.remove(ingredient)
                    x.third.remove(allergen)
                }
            }
            val menuLines = menu2.filter { it.third.contains(allergen) }.filter { it != b }
            var intersecten = b.second
            menuLines.map { line ->
                val intersect = intersecten.intersect(line.second)
                if (intersect.size == 1) {
                    val ingredient = intersect.first()
                    wordBook[allergen] = ingredient
                    println("Found: $allergen = $ingredient")
                    menu2.forEach { x ->
                        x.second.remove(ingredient)
                        x.third.remove(allergen)
                    }
                }
                intersecten = intersect.toMutableSet()
                almostMatch[allergen] = intersecten.toList()
            }
        }

    }

    allIngredients.forEach { line ->
        line.removeAll(wordBook.values)
    }


    return allIngredients.map { it.size }.sum().toLong()
}

fun part2(input: String): Long {
    return 0L
}