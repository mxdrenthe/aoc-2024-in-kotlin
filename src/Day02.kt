package nl.openweb.day02

import nl.openweb.println
import nl.openweb.readInput

fun main() {
    fun part1(data: List<String>): Int {
        return data.count { line ->
            val numbers = line.split(" ").map(String::toInt)

            val add = numbers[1] - numbers[0] > 0
            return@count numbers.zipWithNext().all { (a, b) ->
                if (add) {
                    (b - a) > 0 && (b - a) < 4
                } else {
                    (a - b) > 0 && (a - b) < 4
                }
            }
        }
    }

    fun part2(data: List<String>): Int {
        return data.count { line ->
            val numbers = line.split(" ").map(String::toInt)

            repeat(numbers.size) { n ->
                val new = numbers.subList(0, n) + numbers.subList(n + 1, numbers.size)

                val add = new[1] - new[0] > 0
                val nowOk = new.zipWithNext().all { (a, b) ->
                    if (add) {
                        (b - a) > 0 && (b - a) < 4
                    } else {
                        (a - b) > 0 && (a - b) < 4
                    }
                }

                if (nowOk) return@count true
            }

            return@count false
        }
    }

    // Test if implementation meets criteria from the description, like:
    check(part1(listOf("7 6 4 2 1")) == 1)
    check(part1(listOf("1 3 6 7 9")) == 1)
    check(part2(listOf("8 6 4 4 1")) == 1)

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
