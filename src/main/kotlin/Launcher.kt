import kotlin.system.measureTimeMillis

fun <T> part1(solver: () -> T) {
    val timeInMillis = measureTimeMillis {
        val s =  solver.apply {  }

    }

}