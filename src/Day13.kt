package nl.openweb.day13

import nl.openweb.println
import nl.openweb.readAllInput

private data class Equation(
    val a1: Long,
    val b1: Long,
    val c1: Long,
    val a2: Long,
    val b2: Long,
    val c2: Long
)

private fun parseEquation(text: String): Equation {
    fun parseRegexToTuple(regex: Regex, text: String): Pair<Long, Long> = regex
        .find(text)
        ?.groupValues
        ?.let { it[1].toLong() to it[2].toLong() }
        ?: (0L to 0L)

    val (a1, a2) = parseRegexToTuple(Regex("""A: X\+(\d+), Y\+(\d+)"""), text)
    val (b1, b2) = parseRegexToTuple(Regex("""B: X\+(\d+), Y\+(\d+)"""), text)
    val (c1, c2) = parseRegexToTuple(Regex("""Prize: X=(\d+), Y=(\d+)"""), text)

    return Equation(a1, b1, c1, a2, b2, c2)
}

private fun solveEquation(equation: Equation): Pair<Long, Long>? {
    fun areMultiples(equation: Equation): Boolean = when {
        equation.a1 * equation.b2 != equation.a2 * equation.b1 -> false
        equation.a1 * equation.c2 != equation.a2 * equation.c1 -> false
        equation.b1 * equation.c2 != equation.b2 * equation.c1 -> false
        else -> true
    }

    if (areMultiples(equation)) {
        if (equation.a1 != 0L) {
            val a = equation.c1 / equation.a1
            if (equation.c1 % equation.a1 == 0L && a >= 0) {
                return a to 0L
            }
        }
        if (equation.b1 != 0L) {
            val b = equation.c1 / equation.b1
            if (equation.c1 % equation.b1 == 0L && b >= 0) {
                return 0L to b
            }
        }
        return null
    }

    val delta = equation.a1 * equation.b2 - equation.a2 * equation.b1
    if (delta != 0L) {
        val x = (equation.c1 * equation.b2 - equation.c2 * equation.b1) / delta
        if ((equation.c1 * equation.b2 - equation.c2 * equation.b1) % delta != 0L) {
            return null
        }
        val y = (equation.a1 * equation.c2 - equation.a2 * equation.c1) / delta
        if ((equation.a1 * equation.c2 - equation.a2 * equation.c1) % delta != 0L) {
            return null
        }
        if (x >= 0 && y >= 0) {
            return x to y
        }
    }

    return null
}

private fun upgradeClawMachine(equation: Equation): Equation = Equation(
    equation.a1,
    equation.b1,
    equation.c1 + 10000000000000,
    equation.a2,
    equation.b2,
    equation.c2 + 10000000000000
)

fun main() {
    fun part1(input: String): Long {
        return input.split("\r\n\r\n")
            .map(::parseEquation)
            .mapNotNull(::solveEquation)
            .sumOf { (it.first * 3 + it.second) }
    }

    fun part2(input: String): Long {
        return input.split("\r\n\r\n")
            .map(::parseEquation)
            .map(::upgradeClawMachine)
            .mapNotNull(::solveEquation)
            .sumOf { (it.first * 3 + it.second) }
    }

    val testInput = readAllInput("Day13_test")
    check(part1(testInput) == 480L)

    val input = readAllInput("Day13")
    part1(input).println()
    part2(input).println()
}
