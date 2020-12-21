package day20

import AoCUtils
import Point
import solveWithTiming
import kotlin.math.sqrt


val input = AoCUtils.readText("20.txt")

fun main() {
    solveWithTiming({ part1(testInput) }, 20899048083289, "test 1 part 1")
//    solveWithTiming({ part1(input) }, 0, "part 1")
//
//    solveWithTiming({ part2(testInput) }, 0, "test 1 part 2")
//    solveWithTiming({ part2(input) }, 0, "part 2")
}

enum class Transformation { none, rotate, flip, flipRotate, rotateFlip }
data class Tile(val id: Long, val input: Map<Point, Char>) {
    val versions: List<List<Short>>

    init {
        versions = listOf(
            generateSideNums(false, 0),
            generateSideNums(false, 1),
            generateSideNums(false, 2),
            generateSideNums(false, 3),
            generateSideNums(true, 0),
            generateSideNums(true, 1),
            generateSideNums(true, 2),
            generateSideNums(true, 3),
        )
    }

    private fun generateSideNums(flip: Boolean, rotate: Int): List<Short> {

        val num1 = (0..9).map { getPixel(it, 0, input, flip, rotate) }.joinToString(separator = "")
        val num2 = (0..9).map { getPixel(9, it, input, flip, rotate) }.joinToString(separator = "")

        val num3 = (0..9).map { getPixel(it, 9, input, flip, rotate) }.joinToString(separator = "")
        val num4 = (0..9).map { getPixel(0, it, input, flip, rotate) }.joinToString(separator = "")

        return listOf(
            num1.replace('.', '0').replace('#', '1').toShort(2),
            num2.replace('.', '0').replace('#', '1').toShort(2),
            num3.replace('.', '0').replace('#', '1').toShort(2),
            num4.replace('.', '0').replace('#', '1').toShort(2),
        )
    }

}

fun part1(input: String): Long {
    val tiles = mutableMapOf<Long, MutableMap<Point, Char>>()
    var counter = 0
    var currentTileId = 0L
    input.lines().forEachIndexed { index, line ->
        if (line.contains(":")) {
            currentTileId = line.replace("Tile ", "").replace(":", "").toLong()
            tiles[currentTileId] = mutableMapOf()
            counter = -1
        } else if (line.isEmpty()) {
            currentTileId = 0L
        } else if (currentTileId != 0L) {
            val map = tiles[currentTileId]!!
            line.forEachIndexed { i, c ->
                map[Point(i, counter)] = c
            }
        } else {
            throw Exception("Unknown line")
        }
        counter++
    }

    val imageSize = sqrt(tiles.size.toDouble())

    val tileMap = tiles.map { entry ->
        Pair(entry.key, Tile(entry.key, entry.value))
    }.toMap()


    return 0L
}

val transformations = Transformation.values().toList()
//
//fun checkTileOrder(tiles: Map<Long, MutableMap<Point, Char>>, max: Int, image: List<Long>): List<Long> {
//    val prevImageIndex = image.size
//
//    tiles.filterKeys { !image.contains(it) }
//        .filter { entry ->
//            // check all combos..
//            if (prevImageIndex < max) {
//                val prevImage = image.last()
//                // check left - right side
//                transformations.map { trans ->
//                    (0..9).map { y ->
//
//                    }.any { !it }.not()
//                }.
//
//            }
//
//
//        }
//        .map { entry -> checkTileOrder(tiles, max, image + listOf(entry.key)) }
//
//    return emptyList()
//}


fun getPixel(x: Int, y: Int, map: Map<Point, Char>, flip: Boolean, rotate: Int): Char {

    var newX = x
    var newY = y;
    if (flip) {
        newX = x
        newY = 9 - y
    }

    if (x == 0 && y >= 0) {
        when (rotate) {
            1 -> {
                newY = 9
                newX = 9 - newX
            }
            2 -> {
                newY = 9 - newY
                newX = 0
            }
            3 -> {
                newX = 9-newX
                newY = 9
            }
            else -> throw Exception("Bad rotation: $rotate")
        }

    } else if (x == 9 && y >= 0) {
        when (rotate) {
            1 -> {
                newY = 9
                newX = 9 - newX
            }
            2 -> {
                newY = 9 - newY
                newX = 0
            }
            3 -> {
                newX = 9-newX
                newY = 9
            }
            else -> throw Exception("Bad rotation: $rotate")
        }
    } else if (y == 0 && x >= 0) {
        when (rotate) {
            1 -> {
                newY = newX
                newX = 9
            }
            2 -> {
                newY = 9
                newX = 9 - newX
            }
            3 -> {
                newY = newX
                newX = 9
            }
            else -> throw Exception("Bad rotation: $rotate")
        }
    } else if (y == 9 && x >= 0) {

    }


    val point = Point(newX, newY)

    return map[point] ?: throw Exception("Point not in tile: $point, (x: $x, y: $y, flip: $flip, rotate: $rotate)")

}


fun part2(input: String): Long {
    return 0L
}

val testInput = """
Tile 2311:
..##.#..#.
##..#.....
#...##..#.
####.#...#
##.##.###.
##...#.###
.#.#.#..##
..#....#..
###...#.#.
..###..###

Tile 1951:
#.##...##.
#.####...#
.....#..##
#...######
.##.#....#
.###.#####
###.##.##.
.###....#.
..#.#..#.#
#...##.#..

Tile 1171:
####...##.
#..##.#..#
##.#..#.#.
.###.####.
..###.####
.##....##.
.#...####.
#.##.####.
####..#...
.....##...

Tile 1427:
###.##.#..
.#..#.##..
.#.##.#..#
#.#.#.##.#
....#...##
...##..##.
...#.#####
.#.####.#.
..#..###.#
..##.#..#.

Tile 1489:
##.#.#....
..##...#..
.##..##...
..#...#...
#####...#.
#..#.#.#.#
...#.#.#..
##.#...##.
..##.##.##
###.##.#..

Tile 2473:
#....####.
#..#.##...
#.##..#...
######.#.#
.#...#.#.#
.#########
.###.#..#.
########.#
##...##.#.
..###.#.#.

Tile 2971:
..#.#....#
#...###...
#.#.###...
##.##..#..
.#####..##
.#..####.#
#..#.#..#.
..####.###
..#.#.###.
...#.#.#.#

Tile 2729:
...#.#.#.#
####.#....
..#.#.....
....#..#.#
.##..##.#.
.#.####...
####.#.#..
##.####...
##..#.##..
#.##...##.

Tile 3079:
#.#.#####.
.#..######
..#.......
######....
####.#..#.
.#...#.##.
#.#####.##
..#.###...
..#.......
..#.###...
""".trimIndent()