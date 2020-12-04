package day04

import readText
import test

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
    part1(input) test Pair(192, "Part1 - 192 Valid passports")




    part2(
        "eyr:1972 cid:100\n" +
                "hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926\n" +
                "\n" +
                "iyr:2019\n" +
                "hcl:#602927 eyr:1967 hgt:170cm\n" +
                "ecl:grn pid:012533040 byr:1946\n" +
                "\n" +
                "hcl:dab227 iyr:2012\n" +
                "ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277\n" +
                "\n" +
                "hgt:59cm ecl:zzz\n" +
                "eyr:2038 hcl:74454a iyr:2023\n" +
                "pid:3556412378 byr:2007"
    ) test Pair(0, "Test 2 - 0 Valid passports")

    part2(
        "pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980\n" +
                "hcl:#623a2f\n" +
                "\n" +
                "eyr:2029 ecl:blu cid:129 byr:1989\n" +
                "iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm\n" +
                "\n" +
                "hcl:#888785\n" +
                "hgt:164cm byr:2001 iyr:2015 cid:88\n" +
                "pid:545766238 ecl:hzl\n" +
                "eyr:2022\n" +
                "\n" +
                "iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719"
    ) test Pair(4, "Test 2 - 4 Valid passports")

    part2(input) test Pair(101, "Part 2 - 101 Valid passports")


}

private fun part1(testInput: String): Int {

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

    val counts = passfieldCheck.map { passport ->
        if (passport.values.count { it } == passFields.size) {
            true
        } else passport.filterKeys { it != "cid" }.values.count { it } == passFields.filterKeys { it != "cid" }.count()
    }


    return counts.count { it }
}

private fun part2(testInput: String): Int {

    val split1 = testInput.split("\n\n".toRegex())
    val data = split1
        .map { a ->
            a.split(" ", "\n").map {
                val split = it.split(":")
                Pair(split[0], split[1])
            }.toMap()
        }

    // check all fields
    val lastCheck = data.filter { passports ->
        val test = listOf(
            byrCheck(passports["byr"].orEmpty()),
            iyrCheck(passports["iyr"].orEmpty()),
            eyrCheck(passports["eyr"].orEmpty()),
            hgtCheck(passports["hgt"].orEmpty()),
            hclCheck(passports["hcl"].orEmpty()),
            eclCheck(passports["ecl"].orEmpty()),
            pidCheck(passports["pid"].orEmpty()),
            cidCheck(passports["cid"].orEmpty()),
        )
        test.count { it } == 8
    }



    return lastCheck.size
}

fun byrCheck(input: String): Boolean {
    val num = input.toIntOrNull() ?: return false
    return num in 1920..2002
}

fun iyrCheck(input: String): Boolean {
    val num = input.toIntOrNull() ?: return false
    return num in 2010..2020
}

fun eyrCheck(input: String): Boolean {
    val num = input.toIntOrNull() ?: return false
    return num in 2020..2030
}

fun hgtCheck(input: String): Boolean {
    when {
        input.contains("cm") -> {
            val num = input.replaceFirst("cm", "").toIntOrNull() ?: return false
            return num in 150..193
        }
        input.contains("in") -> {
            val num = input.replaceFirst("in", "").toIntOrNull() ?: return false
            return num in 59..76
        }
        else -> return false
    }
}

fun hclCheck(input: String): Boolean {
    if (input.length != 7 || input[0] != '#') return false
    return input.toCharArray().drop(1)
        .count { it in '0'..'9' || it in 'a'..'f' } == 6
}

fun eclCheck(input: String): Boolean {
    val eyeColors = listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
    return eyeColors.count { it == input } == 1
}

fun pidCheck(input: String): Boolean {
    if (input.length != 9) return false
    return input.toCharArray().count { it in '0'..'9' } == 9
}

fun cidCheck(input: String): Boolean {
    return true
}