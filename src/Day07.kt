package nl.openweb.day07

import nl.openweb.println
import nl.openweb.readInput

fun main() {
    fun part1(input: List<String>): Long {
        val equations = input.filter { it.isNotEmpty() }.map { line ->
            val (lhs, rhs) = line.split(": ", limit = 2)
            lhs.toLong() to rhs.split(' ').map { it.toLong() }
        }.toList()

        fun solve(op: suspend SequenceScope<Long>.(Long, Long) -> Unit) = equations.sumOf {
            val stack = mutableListOf(it)
            while (stack.isNotEmpty()) {
                val (x, values) = stack.removeLast()
                val y = values.last()
                if (values.size == 1) {
                    if (x == y) return@sumOf it.first else continue
                }
                val rest = values.subList(0, values.lastIndex)
                sequence { op(x, y) }.mapTo(stack) { it to rest }
            }
            0
        }
        return solve { x, y ->
            if (x >= y) yield(x - y)
            if (x % y == 0L) yield(x / y)
        }
    }

    fun part2(input: List<String>): Long {
        val equations = input.filter { it.isNotEmpty() }.map { line ->
            val (lhs, rhs) = line.split(": ", limit = 2)
            lhs.toLong() to rhs.split(' ').map { it.toLong() }
        }.toList()

        fun solve(op: suspend SequenceScope<Long>.(Long, Long) -> Unit) = equations.sumOf {
            val stack = mutableListOf(it)
            while (stack.isNotEmpty()) {
                val (x, values) = stack.removeLast()
                val y = values.last()
                if (values.size == 1) {
                    if (x == y) return@sumOf it.first else continue
                }
                val rest = values.subList(0, values.lastIndex)
                sequence { op(x, y) }.mapTo(stack) { it to rest }
            }
            0
        }
        return solve { x, y ->
            if (x >= y) yield(x - y)
            if (x % y == 0L) yield(x / y)
            if (x > y) {
                var d = 10L
                while (d <= y) d *= 10
                if (x % d == y) yield(x / d)
            }
        }
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 3749L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
