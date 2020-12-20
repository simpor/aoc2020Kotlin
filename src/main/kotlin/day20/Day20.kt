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
    val transformations = Transformation.values().toList()


    while (true) {

    }




    return 0L
}

fun getPixel(x: Int, y: Int, map: Map<Point, Char>, transformation: Transformation): Char {
    val char = when (transformation) {
        Transformation.none -> map[Point(x, y)]
        Transformation.rotate -> map[Point(y, x)]
        Transformation.flip -> map[Point(x, 9 - y)]
        Transformation.flipRotate -> map[Point(9 - y, x)]
        Transformation.rotateFlip -> map[Point(y, 9 - x)]
    } ?: throw Exception("Can't handle point($x, $y) in map: $map")

    return char
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