import com.github.mm.coloredconsole.colored

public fun <T> solveWithTiming(solver: () -> T, correctAnswer: T, text: String) {
    val measure = measureIt {
        solver()
    }
    if (measure.first == correctAnswer)
        colored {
            println("PASS - ${measure.second}ms - ${measure.first} is correct - $text".cyan.bold)
        }
    else
        colored {
            println("FAIL - ${measure.second}ms - ${measure.first} does not match $correctAnswer - $text".red.bold)
        }
}

public fun <T> measureIt(method: () -> T): Pair<T, Long> {
    val start = System.currentTimeMillis()
    val t = method()
    val time = System.currentTimeMillis() - start
    return Pair(t, time)
}
