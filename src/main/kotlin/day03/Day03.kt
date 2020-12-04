package day03

import readLines
import test

fun main() {
    val input = readLines("03.txt")

    val testInput = ("..##.......\n" +
            "#...#...#..\n" +
            ".#....#..#.\n" +
            "..#.#...#.#\n" +
            ".#...##..#.\n" +
            "..#.##.....\n" +
            ".#.#.#....#\n" +
            ".#........#\n" +
            "#.##...#...\n" +
            "#...##....#\n" +
            ".#..#...#.#").lines()

    walkPath(input, 3, 1) test Pair(207, "Part 1 visited 207 trees")

    // part 2
    listOf(
        walkPath(testInput, 1, 1),
        walkPath(testInput, 3, 1),
        walkPath(testInput, 5, 1),
        walkPath(testInput, 7, 1),
        walkPath(testInput, 1, 2)
    ).fold(1L, { acc, n -> acc * n }) test Pair(336, "Part 2 - test should be 336")

    listOf(
        walkPath(input, 1, 1),
        walkPath(input, 3, 1),
        walkPath(input, 5, 1),
        walkPath(input, 7, 1),
        walkPath(input, 1, 2)
    ).fold(1L, { acc, n -> acc * n.toLong() }) test Pair(2655892800, "Part 2 should be 2655892800")

}


private fun countTrees(lines: List<String>, x: Int, y: Int): Long = lines
    .filterIndexed { index, line -> index % y == 0 && line[index / y * x % line.length] == '#' }
    .count().toLong()

private fun walkPath(input: List<String>, xStep: Int, yStep: Int): Int {
    val visited = mutableListOf<Triple<Int, Int, String>>()

    var x = 0
    var y = 0
    val maxX = input[0].length
    val maxY = input.size

    while (y + yStep < maxY) {
        y += yStep
        x += xStep
        if (x >= maxX) {
            x -= maxX
        }
        visited.add(Triple(x, y, input[y][x].toString()))
    }
    return visited.filter { it.third == "#" }.count()
}
