package day20

import AoCUtils
import Point
import solveWithTiming


val input = AoCUtils.readText("20.txt")

fun main() {
    solveWithTiming({ part1(testInput) }, 20899048083289, "test 1 part 1")
    solveWithTiming({ part1(input) }, 16192267830719, "part 1")

    solveWithTiming({ part2(testInput) }, 273, "test 1 part 2")
//    solveWithTiming({ part2(input) }, 0, "part 2")
}

data class Tile(val id: Long, val input: Map<Point, Char>) {
    val tiles: List<Map<Point, Char>>
    private val maxX = input.keys.map { it.x }.max()!!
    private val maxY = input.keys.map { it.y }.max()!!

    val seaMonsterPattern = "                  # \n" +
            "#    ##    ##    ###\n" +
            " #  #  #  #  #  #   \n".lines()
                .mapIndexed { y, line ->
                    line.mapIndexed { x, c ->
                        if (c == '#') Point(x, y)
                        null
                    }.filterNotNull()
                }.flatten()

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
        repeat(maxX + 1) { x ->
            repeat(maxY + 1) { y ->
                try {
                    val c = tile[Point(x, y)]!!
                    val newX = maxY - y
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
        repeat(maxX + 1) { x ->
            repeat(maxY + 1) { y ->
                val c = this[Point(x, y)]!!
                val newX = maxX - x
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

    fun getMatch(tile: Tile): Pair<Map<Point, Char>, Map<Point, Char>>? {
        val list = tiles.map { pic ->
            val m1 = tile.tiles.filter { pic.right() == it.left() }
            val m2 = tile.tiles.filter { pic.left() == it.right() }
            val m3 = tile.tiles.filter { pic.up() == it.down() }
            val m4 = tile.tiles.filter { pic.down() == it.up() }
            if (m1.size == 1) Pair(pic, m1.first())
            else if (m2.size == 1) Pair(pic, m2.first())
            else if (m3.size == 1) Pair(pic, m3.first())
            else if (m4.size == 1) Pair(pic, m4.first())
            else null
        }.filterNotNull()
        if (list.size == 8) return list.first()
        else if (list.size > 8) {
            println("Hmm: $list")
        }
        return null
    }


}

fun Map<Point, Char>.left(): String {
    var string = ""
    repeat(10) { counter ->
        string += this[Point(0, counter)]!!
    }
    return string
}

fun Map<Point, Char>.right(): String {
    var string = ""
    repeat(10) { counter ->
        string += this[Point(9, counter)]!!
    }
    return string
}

fun Map<Point, Char>.up(): String {
    var string = ""
    repeat(10) { counter ->
        string += this[Point(counter, 0)]!!
    }
    return string
}

fun Map<Point, Char>.down(): String {
    var string = ""
    repeat(10) { counter ->
        string += this[Point(counter, 9)]!!
    }
    return string
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
    val cornerList = tileMap.map { (id, tile) ->
        val matches = tileMap.filterKeys { it != id }
            .map { entry ->
                val match = entry.value.getMatch(tile)
                if (match != null)
                    Pair(id, entry.key)
                else null
            }
            .filterNotNull()
        if (matches.size == 2) {
            Pair(tile, matches)
        } else null
    }.filterNotNull()

    val sideList = tileMap.map { (id, tile) ->
        val matches = tileMap.filterKeys { it != id }
            .map { entry ->
                val match = entry.value.getMatch(tile)
                if (match != null)
                    Pair(id, entry.key)
                else null
            }
            .filterNotNull()
        if (matches.size == 3) {
            Pair(tile, matches)
        } else null
    }.filterNotNull()

    val middleList = tileMap.map { (id, tile) ->
        val matches = tileMap.filterKeys { it != id }
            .map { entry ->
                val match = entry.value.getMatch(tile)
                if (match != null)
                    Pair(id, entry.key)
                else null
            }
            .filterNotNull()
        if (matches.size == 4) {
            Pair(tile, matches)
        } else null
    }.filterNotNull()

    // locate Top left
    val topLeftCandidates = cornerList.map { pair ->
        val corner = pair.first
        val temp = corner.tiles.map { tile ->
            val firstTile = tileMap[pair.second.first().second]!!
            val secondTile = tileMap[pair.second.last().second]!!

            val leftFirstHit = firstTile.tiles.filter { tile.right() == it.left() }
            val leftSecondHit = secondTile.tiles.filter { tile.right() == it.left() }

            val downFirstHit = firstTile.tiles.filter { tile.down() == it.up() }
            val downSecondHit = secondTile.tiles.filter { tile.down() == it.up() }

            listOf(Triple(tile, leftFirstHit, downSecondHit), Triple(tile, leftSecondHit, downFirstHit))
        }.flatten()
            .filter { it.second.isNotEmpty() && it.third.isNotEmpty() }

        Pair(corner.id, temp)
    }

    val topLeftId = topLeftCandidates.first().first
    val topRight = topLeftCandidates.first().second.first().second
    val topDown = topLeftCandidates.first().second.first().third

    return 0L
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