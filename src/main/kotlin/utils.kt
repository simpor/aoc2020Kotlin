import java.io.File

data class Point(val x: Int, val y: Int)
data class Point3(var x: Int, var y: Int, var z: Int)

operator fun Point3.plus(other: Point3): Point3 {
    return Point3(
        this.x + other.x,
        this.y + other.y,
        this.z + other.z
    )
}

object AoCUtils {

    fun readText(source: String): String =
        File(ClassLoader.getSystemResource(source).file).readText().replace("\r\n", "\n")

    fun readLines(source: String): List<String> = readText(source).lines()


    infix fun <T> T.test(pair: Pair<T, String>) =
        if (this == pair.first) println("PASS - $this is correct - " + pair.second) else println("FAIL - $this does not match ${pair.first} - " + pair.second)


    fun <T> uniqueNumber(input: List<T>): List<List<T>> {
        val returnList = mutableListOf<List<T>>()
        val allCombos = allCombos(input, 5)
        allCombos.forEach {
            val str = it
            var add = true
            str.forEachIndexed { i1, c1 ->
                str.forEachIndexed { i2, c2 ->
                    if (i1 != i2 && c1 == c2) {
                        add = false
                    }
                }
            }
            if (add) returnList.add(str)
        }
        return returnList
    }

    fun <T> allCombos(set: List<T>, k: Int): List<List<T>> {
        val n = set.size
        val list = mutableListOf<MutableList<T>>()
        allCombosRec(set, mutableListOf(), n, k, list);
        return list
    }

    fun <T> allCombosRec(
        set: List<T>,
        prefix: MutableList<T>,
        n: Int, k: Int, list: MutableList<MutableList<T>>,
    ) {
        if (k == 0) {
            list.add(prefix)
            return
        }
        for (i in 0 until n) {
            val newPrefix = prefix + set[i]
            allCombosRec(set, newPrefix.toMutableList(), n, k - 1, list)
        }
    }
}

// Driver Code
fun main() {
    val set1 = listOf("a", "b")
    val k = 3
    println("First test " + AoCUtils.allCombos(set1, k))

    val set2 = listOf("a", "b", "c", "d")
    val k2 = 1
    println("Second test " + AoCUtils.allCombos(set2, k2))

    val set3 = listOf("0", "1", "2", "3", "4")
    val k3 = 5
    println("Third test: " + AoCUtils.allCombos(set3, k3))

    println("Unique test: " + AoCUtils.uniqueNumber(set3))


    listOf("", "").mapIndexed { index, s -> Pair(index, s) }
        .toMap()


}