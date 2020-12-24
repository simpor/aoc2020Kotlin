package day24

import AoCUtils
import Point
import day24.Tile.black
import day24.Tile.white
import solveWithTiming

val testInput = "sesenwnenenewseeswwswswwnenewsewsw\n" +
        "neeenesenwnwwswnenewnwwsewnenwseswesw\n" +
        "seswneswswsenwwnwse\n" +
        "nwnwneseeswswnenewneswwnewseswneseene\n" +
        "swweswneswnenwsewnwneneseenw\n" +
        "eesenwseswswnenwswnwnwsewwnwsene\n" +
        "sewnenenenesenwsewnenwwwse\n" +
        "wenwwweseeeweswwwnwwe\n" +
        "wsweesenenewnwwnwsenewsenwwsesesenwne\n" +
        "neeswseenwwswnwswswnw\n" +
        "nenwswwsewswnenenewsenwsenwnesesenew\n" +
        "enewnwewneswsewnwswenweswnenwsenwsw\n" +
        "sweneswneswneneenwnewenewwneswswnese\n" +
        "swwesenesewenwneswnwwneseswwne\n" +
        "enesenwswwswneneswsenwnewswseenwsese\n" +
        "wnwnesenesenenwwnenwsewesewsesesew\n" +
        "nenewswnwewswnenesenwnesewesw\n" +
        "eneswnwswnwsenenwnwnwwseeswneewsenese\n" +
        "neswnwewnwnwseenwseesewsenwsweewe\n" +
        "wseweeenwnesenwwwswnew"

val input = AoCUtils.readText("24.txt")

fun main() {
    solveWithTiming({ part1(testInput) }, 10, "test 1 part 1")
    solveWithTiming({ part1(input) }, 332, "part 1")

    solveWithTiming({ part2(testInput) }, 2208, "test 1 part 2")
    solveWithTiming({ part2(input) }, 3900, "part 2")
}

enum class Direction { e, se, sw, w, nw, ne }
enum class Tile { white, black }

val dirMovement = mapOf(
    Pair(Direction.e, Point(1, 0)),
    Pair(Direction.se, Point(0, 1)),
    Pair(Direction.sw, Point(-1, 1)),
    Pair(Direction.w, Point(-1, 0)),
    Pair(Direction.nw, Point(0, -1)),
    Pair(Direction.ne, Point(1, -1)),
)

fun part1(input: String): Long {
    val hexMap = createHexMap(input)
    return (hexMap.values.count { it == black }).toLong()
}

fun part2(input: String): Long {
    var hexMap = createHexMap(input).toMap()
    repeat(100) {
        // expand map
        var newMap = hexMap
            .map { tile ->
                if (tile.value == black)
                    dirMovement
                        .map { it.value + tile.key }
                        .map { Pair(it, hexMap.getOrDefault(it, white)) }
                else listOf(Pair(tile.key, tile.value))
            }.flatten()
            .distinct()
            .toMap()

        // check flip rules
        newMap = newMap.map { tile ->
            val around = dirMovement.map { tile.key + it.value }
                .map { hexMap.getOrDefault(it, white) }
                .count { it == black }
            when (tile.value) {
                white -> Pair(tile.key, if (around == 2) black else white)
                black -> Pair(tile.key, if (around == 0 || around > 2) white else black)
            }
        }.toMap()
        hexMap = newMap
    }

    return (hexMap.values.count { it == black }).toLong()
}


private fun createHexMap(input: String): Map<Point, Tile> {
    val list = input.lines().map {
        var str = it
        val dirs = mutableListOf<Direction>()
        while (str.isNotBlank()) {
            val dir = when {
                str.startsWith("e") -> {
                    str = str.substring(1)
                    Direction.e
                }
                str.startsWith("w") -> {
                    str = str.substring(1)
                    Direction.w
                }
                str.startsWith("se") -> {
                    str = str.substring(2)
                    Direction.se
                }
                str.startsWith("sw") -> {
                    str = str.substring(2)
                    Direction.sw
                }
                str.startsWith("nw") -> {
                    str = str.substring(2)
                    Direction.nw
                }
                str.startsWith("ne") -> {
                    str = str.substring(2)
                    Direction.ne
                }
                else -> throw Exception("Unknown string: $str")
            }
            dirs.add(dir)
        }
        dirs.toList()
    }

    val hexMap = mutableMapOf<Point, Tile>()

    list.forEach { round ->
        val newPoint = round
            .map { dirMovement[it]!! }
            .fold(Point(0, 0)) { acc, dir -> acc + dir }
        val tile = hexMap.getOrDefault(newPoint, white)
        hexMap[newPoint] = if (tile == white) black else white
    }
    return hexMap.toMap()
}
