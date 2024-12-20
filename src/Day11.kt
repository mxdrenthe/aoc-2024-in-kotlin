package nl.openweb.day11

import nl.openweb.println
import nl.openweb.readInput

private fun blink(stones: List<Long>): List<Long> = stones.flatMap { stone ->
    val stringOfStone = stone.toString()
    when {
        stone == 0L -> listOf(1)
        stringOfStone.length % 2 == 0 -> listOf(
            stringOfStone.subSequence(0, stringOfStone.length / 2)
                .toString()
                .toLong(),
            stringOfStone.subSequence((stringOfStone.length / 2), stringOfStone.length)
                .toString()
                .toLong()
        )

        else -> listOf(stone * 2024L)
    }
}

private fun getLineLengthFrom(
    stone: Long,
    memo: HashMap<Long, HashMap<Int, Long>>,
    stepsLeft: Int
): Long {
    if (stepsLeft == 0) return 0
    memo.getOrPut(stone) { HashMap() }.let { if (it.contains(stepsLeft)) return it[stepsLeft]!! }

    val stoneString = stone.toString()
    return when {
        stone == 0L -> {
            val newLineLength = getLineLengthFrom(1, memo, stepsLeft - 1)
            memo[stone]!![stepsLeft] = newLineLength
            newLineLength
        }

        stoneString.length % 2 == 0 -> {
            val left = stoneString
                .subSequence(0, stone.toString().length / 2)
                .toString().toLong()
            val right = stoneString
                .subSequence(stone.toString().length / 2, stone.toString().length)
                .toString().toLong()
            val newLineLength =
                1 + getLineLengthFrom(left, memo, stepsLeft - 1) + getLineLengthFrom(
                    right,
                    memo,
                    stepsLeft - 1
                )
            memo[stone]!![stepsLeft] = newLineLength
            newLineLength
        }

        else -> {
            val newLineLength = getLineLengthFrom(stone * 2024L, memo, stepsLeft - 1)
            memo[stone]!![stepsLeft] = newLineLength
            newLineLength
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return (0..<25)
            .fold(input[0].split(" ").map(String::toLong)) { acc, _ -> blink(acc) }
            .size
    }

    fun part2(input: List<String>): Long {
        val stones = input[0].split(" ").map(String::toLong)
        val memo = HashMap<Long, HashMap<Int, Long>>()

        return stones
            .sumOf { 1 + getLineLengthFrom(it, memo, 75) }
    }

    // Or read a large test input from the `src/Day11_test.txt` file:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 55312)

    // Read the input from the `src/Day11.txt` file.
    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
