package day23

import AoCUtils
import solveWithTiming

val testInput = ""

val input = AoCUtils.readText("23.txt")

fun main() {
    solveWithTiming({ part1("389125467", moves = 10) }, "92658374", "test 1 part 1")
    solveWithTiming({ part1("389125467") }, "67384529", "test 2 part 1")
    solveWithTiming({ part1("362981754") }, "0", "part 1")

    solveWithTiming({ part2(testInput) }, 0, "test 1 part 2")
    solveWithTiming({ part2(input) }, 0, "part 2")
}

data class Cup(val label: Int, var inGame: Boolean = true, var next: Cup? = null, var prev: Cup? = null)

class Game(input: String) {
    lateinit var currentCup: Cup
    val cupMap = input.map { it.toString().toInt() }
        .map { Pair(it, Cup(it)) }.toMap()

    init {
        var prevCup: Cup? = null
        input.forEachIndexed() { index, i ->
            val label = i.toString().toInt()
            val cup = cupMap[label]!!
            if (index == 0) currentCup = cup
            else if (index == input.length - 1) {
                prevCup!!.next = cup
                cup.next = currentCup
                cup.prev = prevCup
                currentCup.prev = cup
            } else {
                cup.prev = prevCup
                prevCup!!.next = cup
            }
            prevCup = cup
        }
    }

    fun playOneRound() {
        // remove 3 from list
        var cup = currentCup
        val list = mutableListOf<Cup>()
        repeat(3) {
            cup = cup.next!!
            cup.inGame = false
            list.add(cup)
        }
        val start = list[0].prev!!
        val end = list[2].next!!
        list[0].prev = null
        list[2].next = null
        start.next = end
        end.prev = start

        // select destination
        val destinationCup = (1..9).dropWhile { i ->
            val num = if (currentCup.label - i < 1) 9 - currentCup.label - i else currentCup.label - i
            !cupMap[num]!!.inGame
        }.take(1)
            .map { cupMap[it]!! }.first()

        // inserts items in circle again
        val next = destinationCup.next!!
        val first = list[0]
        val last = list[2]
        first.prev = destinationCup
        destinationCup.next = first
        last.next = next
        next.prev = last

        currentCup = currentCup.next!!

    }

    fun createResult(): String {
        var cup = cupMap[1]!!

        var result = ""
        while (cup.next!!.label != 1) {
            result += cup.label
        }
        return result
    }
}

fun part1(input: String, moves: Int = 100): String {

    val game = Game(input)

    repeat(moves) {
        game.playOneRound()
    }

    return game.createResult()
}

fun part2(input: String): Long {
    return 0L
}