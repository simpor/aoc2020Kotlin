package day14

import AoCUtils
import AoCUtils.test

val testInput = "mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X\n" +
        "mem[8] = 11\n" +
        "mem[7] = 101\n" +
        "mem[8] = 0"

val input = AoCUtils.readText("14.txt")

fun main() {
    part1(testInput) test Pair(165, "test 1 part 1 should be 165")
    part1(input) test Pair(0, "part 1 should be 0")

    part2(testInput) test Pair(0, "test 2 part 2 should be 0")
    part2(input) test Pair(0, "part 2 should be 0")

}

data class Mask(val bits: String) {
    fun isMasked(position: Int) = bits[bits.length - position - 1].toUpperCase() != 'X'
    fun getMask(position: Int) = bits[bits.length - position - 1]
}

// input
data class Instruction(val address: Int, val value: Long)
data class Program(val mask: Mask, val instructions: MutableList<Instruction> = mutableListOf())

// output
data class Memory(var bits: String = "000000000000000000000000000000000000") {
    fun reset() {
        bits = "000000000000000000000000000000000000"
    }

    fun storeBit(position: Int, value: Char) {
        val chars: CharArray = bits.toCharArray()
        chars[bits.length - position - 1] = value
        bits = String(chars)
    }
}

data class Computer(val memory: MutableMap<Int, Memory> = mutableMapOf())

fun part1(input: String): Long {

    val programs = mutableListOf<Program>()
    input.lines().forEach { row ->
        if (row.contains("mask")) {
            row.split(" = ").let { programs.add(Program(Mask(it[1]))) }
        } else {
            row.replace("mem[", "")
                .replace("]", "")
                .split(" = ").let {
                    programs.last().instructions.add(Instruction(it[0].toInt(), it[1].toLong()))
                }
        }
    }

    val computer = Computer()
    programs.forEach { program ->
        program.instructions.forEach { instruction ->
            val bits = instruction.value.toString(2)
            val mask = program.mask
            val address = instruction.address
            val memory = computer.memory.getOrDefault(address, Memory())
            computer.memory.put(address, memory)
            memory.reset()
            mask.bits.reversed().forEachIndexed { index, m ->
                if (m != 'X') {
                    memory.storeBit(index, m)
                }
            }

            bits.reversed().forEachIndexed { index, bit ->
                val bitToStore = if (mask.isMasked(index)) mask.getMask(index) else bit
                memory.storeBit(index, bitToStore)
            }

        }

    }

    return computer.memory.values.map { memory ->
        memory.bits.toLong(2)
    }.sum()
}

fun part2(input: String): Int {
    return 0
}