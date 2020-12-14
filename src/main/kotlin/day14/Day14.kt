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
    part1(input) test Pair(6317049172545, "part 1 should be 6317049172545")

    part2("mask = 000000000000000000000000000000X1001X\n" +
            "mem[42] = 100\n" +
            "mask = 00000000000000000000000000000000X0XX\n" +
            "mem[26] = 1") test Pair(208, "test 2 part 2 should be 208")
    part2(input) test Pair(0, "part 2 should be 0")

}

data class Mask(val bits: String) {
    fun isMasked(position: Int) = bits[bits.length - position - 1].toUpperCase() != 'X'
    fun getMask(position: Int) = bits[bits.length - position - 1]
}

// input
data class Instruction(val address: Int, val value: Long)
data class Program(val mask: Mask, val instructions: MutableList<Instruction> = mutableListOf())

fun String.replaceCharAt(position: Int, value: Char): String {
    val chars: CharArray = this.toCharArray()
    chars[this.length - position - 1] = value
    return String(chars)
}

// output
data class Memory(var bits: String = "000000000000000000000000000000000000") {
    fun reset() {
        bits = "000000000000000000000000000000000000"
    }

    fun storeBit(position: Int, value: Char) {
        bits = bits.replaceCharAt(position, value)
    }


}

data class Computer(val memory: MutableMap<Long, Memory> = mutableMapOf())

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
            val address = instruction.address.toLong()
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

fun part2(input: String): Long {

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
            val address = instruction.address.toString(2).reversed()

            var maskedAdress = mask.bits
            address.forEachIndexed { index, b ->
                if (maskedAdress[maskedAdress.length - index - 1] == '0') {
                    maskedAdress = maskedAdress.replaceCharAt(index, b)
                } else if (maskedAdress[maskedAdress.length - index - 1] == 'X') {

                } else if (maskedAdress[maskedAdress.length - index - 1] == '1') {
                    maskedAdress = maskedAdress.replaceCharAt(index, '1')
                } else {
                    throw Exception("Unknown char in $maskedAdress")
                }
            }

            val listOfAddresses = createAllUnmaskedAddresses(maskedAdress)

            listOfAddresses.forEach { unmaskedAdress ->
                val key = unmaskedAdress.toLong(2)
                val memory = computer.memory.getOrDefault(key, Memory())
                computer.memory[key] = memory
                memory.reset()
                memory.bits = bits
            }

        }

    }

    return computer.memory.values.map { memory ->
        memory.bits.toLong(2)
    }.sum()
}

fun createAllUnmaskedAddresses(maskedAddress: String): List<String> {
    if (!maskedAddress.contains('X')) return listOf(maskedAddress)

    val unmask1 = maskedAddress.replaceFirst('X', '1')
    val unmask2 = maskedAddress.replaceFirst('X', '0')

    return listOf(
        createAllUnmaskedAddresses(unmask1),
        createAllUnmaskedAddresses(unmask2)
    ).flatten()
}
