package day04

import readText
import test

fun main() {
    val input = readText("04.txt")
    val testInput = "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd\n" +
            "byr:1937 iyr:2017 cid:147 hgt:183cm\n" +
            "\n" +
            "iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884\n" +
            "hcl:#cfa07d byr:1929\n" +
            "\n" +
            "hcl:#ae17e1 iyr:2013\n" +
            "eyr:2024\n" +
            "ecl:brn pid:760753108 byr:1931\n" +
            "hgt:179cm\n" +
            "\n" +
            "hcl:#cfa07d eyr:2025 pid:166559648\n" +
            "iyr:2011 ecl:brn hgt:59in"


    part1(testInput) test Pair(2, "Test - 2 Valid passports")
    part1(input) test Pair(2, "Test - 2 Valid passports")


}

private fun part1(testInput: String): Int {
    val passFields = mapOf(
        "byr" to "(Birth Year)",
        "iyr" to "(Issue Year)",
        "eyr" to "(Expiration Year)",
        "hgt" to "(Height)",
        "hcl" to "(Hair Color)",
        "ecl" to "(Eye Color)",
        "pid" to "(Passport ID)",
        "cid" to "(Country ID)"
    )

    val split1 = testInput.split("\n\n".toRegex())
    val data = split1
        .map { a ->
            a.split(" ", "\n").map {
                val split = it.split(":")
                Pair(split[0], split[1])
            }.toMap()
        }


    val passfieldCheck = data.map { passport ->
        passFields.map { Pair(it.key, passport.containsKey(it.key)) }.toMap()
    }

    passfieldCheck.map { passport ->
        if (passport.values.count() { it } == passFields.size) true
        else

    }


    return 0
}