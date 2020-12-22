package day22

import AoCUtils
import solveWithTiming

val testInput = "Player 1:\n" +
        "9\n" +
        "2\n" +
        "6\n" +
        "3\n" +
        "1\n" +
        "\n" +
        "Player 2:\n" +
        "5\n" +
        "8\n" +
        "4\n" +
        "7\n" +
        "10"

val input = AoCUtils.readText("22.txt")

fun main() {
    solveWithTiming({ part1(testInput) }, 306, "test 1 part 1")
    solveWithTiming({ part1(input) }, 33694, "part 1")

    solveWithTiming({
        part2(
            "Player 1:\n" +
                    "43\n" +
                    "19\n" +
                    "\n" +
                    "Player 2:\n" +
                    "2\n" +
                    "29\n" +
                    "14"
        )
    }, 291, "test 1 part 2")
    solveWithTiming({ part2(testInput) }, 291, "test 1 part 2")
    solveWithTiming({ part2(input) }, 0, "part 2") // to low: 8598, 10018
}

val debug = false
fun part1(input: String): Long {
    val player1 = input.lines()
        .takeWhile { it.isNotBlank() }
        .filter { it != "Player 1:" }
        .map { it.toLong() }
        .toMutableList()
    val player2 = input.lines()
        .dropWhile { it != "Player 2:" }
        .drop(1)
        .map { it.toLong() }
        .toMutableList()

    while (player1.isNotEmpty() && player2.isNotEmpty()) {
        val p1 = player1.removeFirst()
        val p2 = player2.removeFirst()

        if (p1 > p2) {
            player1.add(p1)
            player1.add(p2)
        } else {
            player2.add(p2)
            player2.add(p1)
        }
    }

    if (player1.isNotEmpty())
        return player1.reversed().foldIndexed(0L, { index, acc, next ->
            acc + (index + 1) * next
        })
    if (player2.isNotEmpty())
        return player2.reversed().foldIndexed(0L, { index, acc, next ->
            acc + (index + 1) * next
        })

    return 0L
}

fun part2(input: String): Long {
    val player1 = input.lines()
        .takeWhile { it.isNotBlank() }
        .filter { it != "Player 1:" }
        .map { it.toLong() }
        .toMutableList()
    val player2 = input.lines()
        .dropWhile { it != "Player 2:" }
        .drop(1)
        .map { it.toLong() }
        .toMutableList()
    historyWin = false
    memory.clear()
    historyRec.clear()
    recGame(player1, player2, 0, true)

    if (player1.isNotEmpty() || historyWin)
        return player1.reversed().foldIndexed(0L, { index, acc, next ->
            acc + (index + 1) * next
        })
    if (player2.isNotEmpty())
        return player2.reversed().foldIndexed(0L, { index, acc, next ->
            acc + (index + 1) * next
        })

    return 0L
}

val memory = mutableMapOf<Pair<String, String>, Boolean>()
var historyWin = false
val historyRec = mutableSetOf<Pair<String, String>>()

private fun recGame(
    player1: MutableList<Long>,
    player2: MutableList<Long>,
    init: Int,
    start: Boolean
) {
//    val p1History = mutableSetOf<String>()
//    val p2History = mutableSetOf<String>()
    var counter = init.toLong()
    val historyInternal = mutableSetOf<Pair<String, String>>()
    while (player1.isNotEmpty() && player2.isNotEmpty()) {
        counter++
        val p1DeckString = player1.joinToString(separator = "-")
        val p2DeckString = player2.joinToString(separator = "-")
        val key = Pair(p1DeckString, p2DeckString)
        if (counter % 1000 == 0L) {
            println("[${if(start) "main" else "rec"}, $counter] Counter: ${counter}")
        }

//        if (p1History.contains(p1DeckString) && p2History.contains(p2DeckString)) {
        if (counter % 1000 == 0L || historyInternal.contains(key) || historyRec.contains(key)) {
            historyRec.add(key)
            player2.clear()
            println("[${if(start) "main" else "rec"}, $counter] p1 win!, history: " + player1.reversed().foldIndexed(0L, { index, acc, next ->
                acc + (index + 1) * next
            }))
            historyWin = false
            break
        }
        historyInternal.add(key)
//        p1History.add(p1DeckString)
//        p2History.add(p2DeckString)

        val p1 = player1.removeFirst()
        val p2 = player2.removeFirst()
//        //println("[$counter] p1: $p1, p2: $p2")

        if (p1 <= (player1.size.toLong() + 0) && p2 <= (player2.size.toLong() + 0) && player1.size > 0 && player2.size > 0) {
            // sub game
            val p1Copy = player1.toList().toMutableList()
            val p2Copy = player2.toList().toMutableList()

            val p1CopyDeckString = p1Copy.joinToString(separator = "-")
            val p2CopyDeckString = p2Copy.joinToString(separator = "-")
            val key2 = Pair(p1CopyDeckString, p2CopyDeckString)
            if (memory.containsKey(key2)
            ) {
                println("[${if(start) "main" else "rec"}, Memoryhit: p1: $p2DeckString, p2: $p2DeckString")
                if (memory[key2]!!) {
                    player1.add(p1)
                    player1.add(p2)
                } else {
                    player2.add(p2)
                    player2.add(p1)
                }
            } else {
                recGame(p1Copy, p2Copy, 0, false)
                if (historyWin) {
                    p2Copy.clear()
                    if (!start) break
                }
                if (p1Copy.isNotEmpty()) {
//                println("[$counter] p1 win!-1, p1: $p1, p2: $p2")
//                    if (player1.size + player2.size > 40)
                        memory[key2] = true
                    player1.add(p1)
                    player1.add(p2)
                } else {
//                println("[$counter] p2 win!-1, p1: $p1, p2: $p2")
//                    if (player1.size + player2.size > 40)
                        memory[key2] = false
                    player2.add(p2)
                    player2.add(p1)
                }
            }
        } else if (p1 > p2) {
//            println("[$counter] p1 win!-2, p1: $p1, p2: $p2")
//            if (player1.size + player2.size > 40)
               // memory[key] = true
            player1.add(p1)
            player1.add(p2)
        } else {
//            println("[$counter] p2 win!-2, p1: $p1, p2: $p2")
//            if (player1.size + player2.size > 40)
               // memory[key] = false
            player2.add(p2)
            player2.add(p1)
        }
        if (historyWin) {
            break
        }
    }
}