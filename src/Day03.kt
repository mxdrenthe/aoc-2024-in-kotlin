package nl.openweb.day03

import nl.openweb.println
import nl.openweb.readInput

fun main() {
    fun part1(input: List<String>): Int {
        return Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)").findAll(input.joinToString("\n"))
            .map {
                it.groupValues[1].toInt() * it.groupValues[2].toInt()
            }
            .sum()
    }

    fun part2(input: List<String>): Int {
        var skip = false
        var result = 0
        Regex("do\\(\\)|don't\\(\\)|mul\\((\\d{1,3}),(\\d{1,3})\\)").findAll(input.joinToString("\n"))
            .forEach {
                when (it.groupValues[0]) {
                    in "do()" -> skip = false
                    in "don't()" -> skip = true
                    else -> {
                        if (!skip) {
                            result += it.groupValues[1].toInt() * it.groupValues[2].toInt()
                        }
                    }
                }
            }
        return result
    }

    // Test if implementation meets criteria from the description, like:
    check(part1(listOf("mul(44,46)")) == 2024)

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 161)
    check(part2(testInput) == 48)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
