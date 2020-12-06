package day01

import AoCUtils.readLines

fun main() {
    part1()
    part2()
}

private fun part1() {
    val numbers = readLines("01.txt").map { i -> i.toInt() }

    val answer = numbers.mapIndexed() { ai, a ->
        numbers.filterIndexed { bi, b -> bi != ai }
            .mapIndexed { bi, b -> Triple(a, b, a + b) }
    }.flatten()
        .filter { t -> t.third == 2020 }
        .first()

    println(answer)
    println(answer.first * answer.second)
}


private fun part2() {
    val numbers = readLines("01.txt").map { i -> i.toInt() }

    val answer =
        numbers.mapIndexed() { ai, a ->
            numbers.mapIndexed { bi, b ->
                numbers.mapIndexed { ci, c ->
                    if (ai != bi && bi != ci && ai != ci)
                        Pair(Triple(a, b, c), a + b + c)
                    else
                        null
                }.filterNotNull()
            }
        }.flatten()
            .flatten()
            .filter { t -> t.second == 2020 }
            .first()
    println(answer)
    println(answer.first.first * answer.first.second * answer.first.third)
}