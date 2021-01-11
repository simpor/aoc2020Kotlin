package day20

import AoCUtils
import Point
import solveWithTiming
import kotlin.math.sqrt


val input = AoCUtils.readText("20.txt")

fun main() {
//    solveWithTiming({ part1(testInput) }, 20899048083289, "test 1 part 1")
//    solveWithTiming({ part1(input) }, 16192267830719, "part 1")

    solveWithTiming({ part2(testInput) }, 273, "test 1 part 2")
//    solveWithTiming({ part2(input) }, 0, "part 2")
}

data class Tile(val id: Long, val input: Map<Point, Char>) {
    val tiles: List<Map<Point, Char>>

    init {
        val flipped = input.flip()
        tiles = listOf(
            input,
            rotate(input, 1),
            rotate(input, 2),
            rotate(input, 3),
            flipped,
            rotate(flipped, 1),
            rotate(flipped, 2),
            rotate(flipped, 3),
        )
    }

    private fun rotate(tile: Map<Point, Char>, rotations: Int): Map<Point, Char> {
        val map = mutableMapOf<Point, Char>()
        repeat(10) { x ->
            repeat(10) { y ->
                try {
                    val c = tile[Point(x, y)]!!
                    val newX = 9 - y
                    val newY = x
                    map[Point(newX, newY)] = c
                } catch (e: Exception) {
                    println("[$x, $y] not in map: $tile")
                }
            }
        }
        return if (rotations == 1) map else rotate(map, rotations - 1)
    }

    private fun Map<Point, Char>.flip(): Map<Point, Char> {
        val map = mutableMapOf<Point, Char>()
        repeat(10) { x ->
            repeat(10) { y ->
                val c = this[Point(x, y)]!!
                val newX = 9 - x
                val newY = y
                map[Point(newX, newY)] = c
            }
        }
        return map
    }

    fun match(tile: Tile): Boolean {
        return tiles.filter { pic ->
            if (tile.tiles.count { pic.right() == it.left() } == 1) {
                return true
            }
            if (tile.tiles.count { pic.left() == it.right() } == 1) {
                return true
            }
            if (tile.tiles.count { pic.up() == it.down() } == 1) {
                return true
            }
            if (tile.tiles.count { pic.down() == it.up() } == 1) {
                return true
            }
            return false
        }.any()
    }

    private fun Map<Point, Char>.left(): String {
        var string = ""
        repeat(10) { counter ->
            string += this[Point(0, counter)]!!
        }
        return string
    }

    private fun Map<Point, Char>.right(): String {
        var string = ""
        repeat(10) { counter ->
            string += this[Point(9, counter)]!!
        }
        return string
    }

    private fun Map<Point, Char>.up(): String {
        var string = ""
        repeat(10) { counter ->
            string += this[Point(counter, 0)]!!
        }
        return string
    }

    private fun Map<Point, Char>.down(): String {
        var string = ""
        repeat(10) { counter ->
            string += this[Point(counter, 9)]!!
        }
        return string
    }

}

fun part1(input: String): Long {
    val tileMap = parseInput(input)


    // locate the corner tiles
    val cornerList = mutableListOf<Tile>()

    tileMap.forEach { id, tile ->
        val matches = tileMap.filterKeys { it != id }
            .filterValues { tile.match(it) }
            .count()
        if (matches == 2) {
            cornerList.add(tile)
        }
    }

    return cornerList.fold(1, { acc, tile -> acc * tile.id })
}

fun part2(input: String): Long {
    val tileMap = parseInput(input)


    // locate the corner tiles
    val cornerList = mutableListOf<Tile>()

    tileMap.forEach { id, tile ->
        val matches = tileMap.filterKeys { it != id }
            .filterValues { tile.match(it) }
            .count()
        if (matches == 2) {
            cornerList.add(tile)
        }
    }

    return cornerList.fold(1, { acc, tile -> acc * tile.id })
}

private fun parseInput(input: String): Map<Long, Tile> {
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

    val tileMap = tiles.map { entry ->
        Pair(entry.key, Tile(entry.key, entry.value))
    }.toMap()
    return tileMap
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