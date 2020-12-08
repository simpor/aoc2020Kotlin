package day09

import AoCUtils
import AoCUtils.test

val testInput = "nop +0\n" +
        "acc +1\n" +
        "jmp +4\n" +
        "acc +3\n" +
        "jmp -3\n" +
        "acc -99\n" +
        "acc +1\n" +
        "jmp -4\n" +
        "acc +6"

val input = AoCUtils.readText("08.txt")

fun main() {
    part1(testInput) test Pair(5, "test 1 part 1 should be 5")
    part1(input) test Pair(1928, "part 1 should be 1928")

    part2(testInput) test Pair(8, "test 2 part 2 should be 8")
    part2(input) test Pair(1319, "part 2 should be 1319")

}

enum class InstructionType { acc, jmp, nop }
data class Instruction(val type: InstructionType, val value: Int, var executed: Boolean = false)

fun String.toType(): InstructionType = when {
    this == "acc" -> InstructionType.acc
    this == "jmp" -> InstructionType.jmp
    this == "nop" -> InstructionType.nop
    else -> throw Exception("Can't parse instruction: $this")
}

fun part1(input: String): Int {

    val instructions = input.lines().map {
        val split = it.split(" ")
        Instruction(split[0].toType(), split[1].toInt())
    }
    var accumulator = 0
    var index = 0
    while (true) {
        val instruction =
            instructions[index] ?: throw Exception("Can't execute at index: $index for instructions: $instructions")
        if (instruction.executed) {
            break
        }
        when (instruction.type) {
            InstructionType.acc -> {
                accumulator += instruction.value
                index++
            }
            InstructionType.jmp -> {
                index += instruction.value
            }
            InstructionType.nop -> {
                index++
            }
        }
        instruction.executed = true
    }

    return accumulator
}

fun part2(input: String): Int {

    val instructions = input.lines().map {
        val split = it.split(" ")
        Instruction(split[0].toType(), split[1].toInt())
    }

    val instructionsToTest = instructions.mapIndexed { index, instruction ->
        when (instruction.type) {
            InstructionType.nop -> {
                val list = instructions.toMutableList()
                list[index] = instruction.copy(type = InstructionType.jmp)
                list.toList()
            }
            InstructionType.jmp -> {
                val list = instructions.toMutableList()
                list[index] = instruction.copy(type = InstructionType.nop)
                list.toList()
            }
            else -> {
                null
            }
        }
    }.filterNotNull()

    var result = Pair(0, false)

    for (i in instructionsToTest) {
        i.forEach { it.executed = false }
        result = runComputer(i)
        if (result.second) {
            break
        }
    }


    return result.first
}

private fun runComputer(instructions: List<Instruction>): Pair<Int, Boolean> {
    var accumulator = 0
    var index = 0
    var foundLoop = false
    while (index < instructions.size) {
        val instruction =
            instructions[index] ?: throw Exception("Can't execute at index: $index for instructions: $instructions")
        if (instruction.executed) {
            foundLoop = true
            break
        }
        when (instruction.type) {
            InstructionType.acc -> {
                accumulator += instruction.value
                index++
            }
            InstructionType.jmp -> {
                index += instruction.value
            }
            InstructionType.nop -> {
                index++
            }
        }
        instruction.executed = true
    }
    return Pair(accumulator, !foundLoop)
}

