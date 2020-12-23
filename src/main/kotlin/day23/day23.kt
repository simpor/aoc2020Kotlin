package day23

import AoCUtils
import solveWithTiming
import java.lang.Exception

val testInput = ""

val input = AoCUtils.readText("23.txt")

fun main() {
    solveWithTiming({ part1("389125467", moves = 10) }, "92658374", "test 1 part 1")
    solveWithTiming({ part1("389125467") }, "67384529", "test 2 part 1")
    solveWithTiming({ part1("362981754") }, "24798635", "part 1")

    solveWithTiming({ part2("389125467") }, 149245887792, "test 1 part 2")
    solveWithTiming({ part2("362981754") }, 12757828710, "part 2")
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

class Game(input: List<Int>) {
    lateinit var currentCup: Cup
    val cupMap = input.map { Pair(it, Cup(it)) }.toMap()

    init {
        var prevCup: Cup? = null
        input.forEachIndexed() { index, i ->
            val label = i.toString().toInt()
            val cup = cupMap[label]!!
            if (index == 0) currentCup = cup
            else if (index == input.size - 1) {
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

        // select destination
        var destinationCup = currentCup
        var counter = 1
        while (true) {
            var num = currentCup.label - counter
            if (num < 1) {
                num = cupMap.size + num
            }
            val cup = cupMap[num] ?: throw Exception("Wrong num: $num")
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

    val game = Game(input.map { it.toString().toInt() })

    repeat(moves) {
        game.playOneRound()
    }

    return game.createResult()
}

fun part2(input: String): Long {

    val list = input.map { it.toString().toInt() }.toMutableList()
    for (i in 10..1000000) {
        list.add(i)
    }
    val game = Game(list)

    repeat(10000000) {
        game.playOneRound()
    }
    val cup = game.cupMap[1]!!
    return cup.next!!.next!!.label.toLong() * cup.next!!.label.toLong()

}