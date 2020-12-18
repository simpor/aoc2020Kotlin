package day18

import AoCUtils
import AoCUtils.readText
import solveWithTiming

val testInput = ""

val input = readText("18.txt")

fun main() {

    solveWithTiming({ part1("( ( 1 + 2 ) * 3 ) + ( 4 * ( 5 + 6 ) )") }, 53, "test 2 part 1")
    solveWithTiming({ part1("1 + 2 * 3 + 4 * 5 + 6") }, 71, "test 1 part 1")
    solveWithTiming({ part1("2 * 3 + (4 * 5)") }, 26, "test 1 part 1")
    solveWithTiming({ part1("(((2 * 3)) + ((4 * 5)))") }, 26, "test 1 part 1")
    solveWithTiming({ part1("5 + (8 * 3 + 9 + 3 * 4 * 3)") }, 437, "test 1 part 1")
    solveWithTiming({ part1("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))") }, 12240, "test 1 part 1")
    solveWithTiming({ part1("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2") }, 13632, "test 1 part 1")
    solveWithTiming({ part1(input) }, 4491283311856, "part 1")

    solveWithTiming({ part2("1 + 2 * 3 + 4 * 5 + 6") }, 231, "test 2 part 2")
    solveWithTiming({ part2("1 + (2 * 3) + (4 * (5 + 6))") }, 51, "test 2 part 2")
    solveWithTiming({ part2("2 * 3 + (4 * 5)") }, 46, "test 2 part 2")
    solveWithTiming({ part2("(1 + 2 * 3) + (4 * 5 + 6)") }, 53, "test 2 part 2")
    solveWithTiming({ part2("5 + (8 * 3 + 9 + 3 * 4 * 3)") }, 1445, "test 2 part 2")
    solveWithTiming({ part2("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))") }, 669060, "test 2 part 2")
    solveWithTiming({ part2("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2") }, 23340, "test 2 part 2")

    solveWithTiming({ part2("1 + 2 + 3 * 4 + 5 + 6 * 2") }, 180, "test 2 part 2")
    solveWithTiming({ part2("2 * 1 + 2 + 3 * 4 + 5 + 6") }, 180, "test 2 part 2")
    solveWithTiming({ part2("2 * 1 + (2 + 3 * 4 + 5 + 6)") }, 152, "test 2 part 2")
    solveWithTiming({ part2("2 * (1 + ((2 + 3) * (4 + 5 + 6)))") }, 152, "test 2 part 2")
    solveWithTiming({ part2("1 + 2 + 3 * 4 + 5 + 6") }, 90, "test 2 part 2")
    solveWithTiming({ part2("(1 + 2 + 3) * (4 + 5 + 6)") }, 90, "test 2 part 2")
    solveWithTiming({ part2("((1 + 2 + 3) * (4 + 5 + 6))") }, 90, "test 2 part 2")
    solveWithTiming({ part2("(((1 + 2 + 3)) * ((4 + 5 + 6)))") }, 90, "test 2 part 2")
    solveWithTiming({ part2("(((1 + (2 + 3))) * (((4 + 5) + 6)))") }, 90, "test 2 part 2")
    solveWithTiming({ part2("(((1 + (2 + 3))) * ((4 + (5 + 6))))") }, 90, "test 2 part 2")
    solveWithTiming({ part2("1 + (2 * (3 + (4 * (5 + 6)))))") }, 95, "test 2 part 2")


    solveWithTiming({ part1("5 * (( 8 + 6 ) + ( 7 * 7 ))") }, 315, "test 2 part 2")
    solveWithTiming({ part2("5 * 8 + 6 + (7 * 7)") }, 315, "test 2 part 2")

    solveWithTiming({ part2(input) }, 68852578641904, "part 2")
}

val debug = false

enum class Type {
    num, plus, times, start, end
}

data class Expression(val type: Type, val number: Long = -1L) {
    fun isNum() = type == Type.num
    fun isStartParentheses() = type == Type.start
    fun isEndParentheses() = type == Type.end
    fun isOperand() = when (type) {
        Type.num -> false
        Type.plus, Type.times -> true
        Type.start -> false
        Type.end -> false
    }

    override fun toString(): String = when (type) {
        Type.num -> number.toString()
        Type.plus -> "+"
        Type.times -> "*"
        Type.start -> "("
        Type.end -> ")"
    }

}

fun part1(input: String): Long {
    val expressions = parseInput(input)

    val map = expressions.map { expression ->
        traverseExpression(expression)
    }

    return map.sum()
}

fun part2(input: String): Long {
    val expressions = parseInput(input)


    val modifiedExpressions = expressions.map { expression ->
        if (debug) {
            println("Begin mod")
            println(expression.map {
                when (it.type) {
                    Type.num -> it.number.toString()
                    Type.plus -> "+"
                    Type.times -> "*"
                    Type.start -> "("
                    Type.end -> ")"
                }
            }.joinToString(separator = " "))
        }
        val modifiedExpression = plusIsCoolest(expression)
        if (debug) {
            println("Mod complete")
            println(modifiedExpression.map {
                when (it.type) {
                    Type.num -> it.number.toString()
                    Type.plus -> "+"
                    Type.times -> "*"
                    Type.start -> "("
                    Type.end -> ")"
                }
            }.joinToString(separator = " "))
        }
        modifiedExpression
    }

    val map = modifiedExpressions.map { expression ->
        traverseExpression(expression)
    }

    return map.sum()
}

private fun parseInput(input: String): List<List<Expression>> {
    val expressions = input.lines().map { line ->
        line.toCharArray()
            .filter { it != ' ' }
            .map { s ->
                when {
                    s.toString().matches(AoCUtils.Regexp.isNumber) -> Expression(Type.num, s.toString().toLong())
                    s == '+' -> Expression(Type.plus)
                    s == '*' -> Expression(Type.times)
                    s == '(' -> Expression(Type.start)
                    s == ')' -> Expression(Type.end)
                    else -> throw Exception("Can't parse: $s")
                }
            }
    }
    return expressions
}

private fun traverseExpression(expression: List<Expression>): Long {
    val mem = mutableListOf<Long>()
    var counter = 0
    while (counter < expression.size - 1) {
        val a = expression[counter + 0]
        val b = expression[counter + 1]

        if (a.isOperand() && b.isNum()) {
            val oldNum = mem.removeLast()
            val newNum = when (a.type) {
                Type.plus -> oldNum + b.number
                Type.times -> oldNum * b.number
                else -> throw  Exception("Should not happen")
            }
            mem.add(newNum)
            counter += 2
        } else if (a.isNum() && b.isOperand()) {
            mem.add(a.number)
            counter++
        } else if (a.isStartParentheses()) {
            var countP = 0
            val subExpression = mutableListOf<Expression>()
            for (i in counter until expression.size) {
                if (expression[i].isStartParentheses()) countP++
                else if (expression[i].isEndParentheses()) countP--
                if (countP == 0) {
                    subExpression.addAll(expression.subList(counter + 1, i))
                    mem.add(traverseExpression(subExpression))
                    counter = i + 1
                    break
                }
            }
        } else if (b.isStartParentheses()) {
            var countP = 0
            val subExpression = mutableListOf<Expression>()
            for (i in (counter + 1) until expression.size) {
                if (expression[i].isStartParentheses()) countP++
                else if (expression[i].isEndParentheses()) countP--
                if (countP == 0) {
                    subExpression.addAll(expression.subList(counter + 2, i))
                    val newNum = traverseExpression(subExpression)

                    val oldNum = mem.removeLast()
                    val newNum2 = when (a.type) {
                        Type.plus -> oldNum + newNum
                        Type.times -> oldNum * newNum
                        else -> throw  Exception("Should not happen")
                    }
                    mem.add(newNum2)
                    counter = i + 1
                    break
                }
            }
        } else {
            println("Should not get here?, a: $a, b: $b")
        }

    }
    if (debug) {
        println(expression.map {
            when (it.type) {
                Type.num -> it.number.toString()
                Type.plus -> "+"
                Type.times -> "*"
                Type.start -> "("
                Type.end -> ")"
            }
        }.joinToString(separator = " ") + " = " + mem.first())
    }
    return mem.first()
}

private fun plusIsCoolest(expression: List<Expression>): MutableList<Expression> {
    val modifiedExpression = expression.toMutableList()

    var counter = 0
    while (counter < modifiedExpression.size) {
        val modExpString = modifiedExpression.joinToString(separator = " ") {
            when (it.type) {
                Type.num -> it.number.toString()
                Type.plus -> "+"
                Type.times -> "*"
                Type.start -> "("
                Type.end -> ")"
            }
        }

        val currentExp = modifiedExpression[counter]
        if (currentExp.type == Type.plus) {
            if (debug) {
                println(modExpString)
            }

            val backExp = modifiedExpression[counter - 1]
            val numBackward = when {
                backExp.isNum() -> true
                backExp.type == Type.end -> false
                else -> throw Exception("Impossible backward: $backExp")
            }

            val forwardExp = modifiedExpression[counter + 1]
            val numForward = when {
                forwardExp.isNum() -> true
                forwardExp.type == Type.start -> false
                else -> throw Exception("Impossible forward: $forwardExp")
            }

            if (numBackward && numForward) {
                val listBefore = modifiedExpression.take(counter - 1)
                val listAfter = modifiedExpression.drop(counter + 2)
                val expList = modifiedExpression.drop(counter - 1).take(counter + 2 - (counter - 1))

                var newExpressionList =
                    listBefore + listOf(Expression(Type.start)) + expList + listOf(Expression(Type.end)) + listAfter


                modifiedExpression.clear()
                modifiedExpression.addAll(newExpressionList)
                counter += 3
            } else if (!numBackward && !numForward) {
                var pCounter = 0
                var backLoop = counter - 1
                while (backLoop >= 0) {
                    val prev = modifiedExpression[backLoop]
                    if (prev.isStartParentheses()) pCounter++
                    else if (prev.isEndParentheses()) pCounter--
                    if (pCounter == 0) break
                    backLoop--
                }

                pCounter = 0
                var forwardLoop = counter + 1
                while (forwardLoop < modifiedExpression.size) {
                    val next = modifiedExpression[forwardLoop]
                    if (next.isStartParentheses()) pCounter++
                    else if (next.isEndParentheses()) pCounter--
                    if (pCounter == 0) break
                    forwardLoop++
                }

                val listBefore = modifiedExpression.take(backLoop)
                val listAfter = modifiedExpression.drop(forwardLoop)
                val expList = modifiedExpression.drop(backLoop).take(forwardLoop - backLoop)

                var newExpressionList =
                    listBefore + listOf(Expression(Type.start)) + expList + listOf(Expression(Type.end)) + listAfter


                modifiedExpression.clear()
                modifiedExpression.addAll(newExpressionList)
                counter += 2

            } else if (!numBackward && numForward) {
                var pCounter = 0
                var loop = counter - 1
                while (loop >= 0) {
                    val prev = modifiedExpression[loop]
                    if (prev.isStartParentheses()) pCounter++
                    else if (prev.isEndParentheses()) pCounter--
                    if (pCounter == 0) break
                    loop--
                }
                val listBefore = modifiedExpression.take(loop)
                val listAfter = modifiedExpression.drop(counter + 2)
                val expList = modifiedExpression.drop(loop).take(counter + 2 - loop)

                var newExpressionList =
                    listBefore + listOf(Expression(Type.start)) + expList + listOf(Expression(Type.end)) + listAfter


                modifiedExpression.clear()
                modifiedExpression.addAll(newExpressionList)
                counter += 2

            } else if (numBackward && !numForward) {
                var pCounter = 0
                var loop = counter + 1
                while (loop < modifiedExpression.size) {
                    val next = modifiedExpression[loop]
                    if (next.isStartParentheses()) pCounter++
                    else if (next.isEndParentheses()) pCounter--
                    if (pCounter == 0) break
                    loop++
                }
                val listBefore = modifiedExpression.take(counter - 1)
                val listAfter = modifiedExpression.drop(loop)
                val expList = modifiedExpression.drop(counter - 1).take(loop - counter + 1)

                var newExpressionList =
                    listBefore + listOf(Expression(Type.start)) + expList + listOf(Expression(Type.end)) + listAfter


                modifiedExpression.clear()
                modifiedExpression.addAll(newExpressionList)
                counter += 2

            } else {
                counter++
            }

        } else {
            counter++
        }
    }
    return modifiedExpression
}

// Much better solution
// From: https://todd.ginsberg.com/post/advent-of-code/2020/day18/


fun Char.asLong(): Long =
    this.toString().toLong()
fun <T> Iterable<T>.sumToLongBy(fn: (T) -> Long): Long =
    this.fold(0L) { carry, next -> carry + fn(next) }
fun Iterable<Long>.product(): Long =
    this.reduce { a, b -> a * b }

class Day18(input: List<String>) {

    private val equations = input.map { it.replace(" ", "") }

    fun solvePart1(): Long =
        equations.sumToLongBy { solvePart1(it.iterator()) }

    fun solvePart2(): Long =
        equations.sumToLongBy { solvePart2(it.iterator()) }

    private fun solvePart1(equation: CharIterator): Long {
        val numbers = mutableListOf<Long>()
        var op = '+'
        while (equation.hasNext()) {
            when (val next = equation.nextChar()) {
                '(' -> numbers += solvePart1(equation)
                ')' -> break
                in setOf('+', '*') -> op = next
                else -> numbers += next.asLong()
            }
            if (numbers.size == 2) {
                val a = numbers.removeLast()
                val b = numbers.removeLast()
                numbers += if (op == '+') a + b else a * b
            }
        }
        return numbers.first()
    }

    private fun solvePart2(equation: CharIterator): Long {
        val multiplyThese = mutableListOf<Long>()
        var added = 0L
        while (equation.hasNext()) {
            val next = equation.nextChar()
            when {
                next == '(' -> added += solvePart2(equation)
                next == ')' -> break
                next == '*' -> {
                    multiplyThese += added
                    added = 0L
                }
                next.isDigit() -> added += next.asLong()
            }
        }
        return (multiplyThese + added).product()
    }

}