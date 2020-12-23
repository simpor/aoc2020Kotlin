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

data class Cup(val label: Int, var inGame: Boolean = true, var next: Cup? = null, var prev: Cup? = null) {
    fun addNext(cup: Cup) {
        next = cup
        cup.prev = this
    }

    fun cutPrev(): Cup {
        val prevCup = prev
        prevCup!!.next = null
        prev = null
        return prevCup
    }

    fun cutNext(): Cup {
        val nextCup = next!!
        next = null
        nextCup.prev = null
        return nextCup
    }

    fun addPrev(cup: Cup) {
        prev = cup
        cup.next = this
    }

    override fun toString(): String {
        return "Cup(label=$label, inGame=$inGame, next=${next?.label}, prev=${prev?.label})"
    }


}

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
                cup.addPrev(prevCup!!)
                cup.addNext(currentCup)
            } else {
                cup.addPrev(prevCup!!)
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
        val beforeCut = list[0].cutPrev()
        val afterCut = list[2].cutNext()
        beforeCut.addNext(afterCut)
//        println(printNextFrom(currentCup))
//        println(printPrevFrom(currentCup))

        // select destination
        var destinationCup = currentCup
        var counter = 1
        while (true) {
            var num = currentCup.label - counter
            if (num < 1) {
                num = 9 + num
            }
            val cup = cupMap[num]!!
            if (cup.inGame) {
                destinationCup = cup
                break
            }
            counter++
        }

        // inserts items in circle again
        val next = destinationCup.next!!
        destinationCup.addNext(list[0])
        next.addPrev(list[2])
        list.forEach { it.inGame = true }
        currentCup = currentCup.next!!

    }

    fun createResult(): String {
        var cup = cupMap[1]!!

        var result = ""
        cup = cup.next!!
        while (cup.next!!.label != 1) {
            result += cup.label
            cup = cup.next!!
        }
        return result + cup.label
    }

    fun printNextFrom(startWith: Cup): String {
        var cup = startWith

        var result = ""
        while (cup.next!!.label != startWith.label) {
            result += cup.label
            cup = cup.next!!
        }
        return result + cup.label
    }

    fun printPrevFrom(startWith: Cup): String {
        var cup = startWith

        var result = ""
        while (cup.prev!!.label != startWith.label) {
            result += cup.label
            cup = cup.prev!!
        }
        return result + cup.label
    }
}

fun part1(input: String, moves: Int = 100): String {

    val game = Game(input)

    repeat(moves) {
//        println("Result: " + game.createResult())
//        println("Next: " + game.printNextFrom(game.currentCup))
//        println("Prev: " + game.printPrevFrom(game.currentCup))
//        println()
        game.playOneRound()
    }

    return game.createResult()
}

fun part2(input: String): Long {
    return 0L
}