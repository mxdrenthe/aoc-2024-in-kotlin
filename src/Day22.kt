package nl.openweb.day22

import nl.openweb.println
import nl.openweb.readInput

private const val MODULUS = 16777216L

private fun calculate(initial: Long, times: Int = 2000): Long {
    var value = initial
    repeat(times) {
        value = value.xor(value * 64L) % MODULUS
        value = value.xor(value / 32L) % MODULUS
        value = value.xor(value * 2048L) % MODULUS
    }
    return value
}

private fun getSequencePricesAfter(
    initial: Long,
    iterations: Int = 2000
): LinkedHashMap<List<Long>, Long> {
    var value = initial
    val prices = mutableListOf(value % 10)
    repeat(iterations) {
        value = (value * 64).xor(value) % MODULUS
        value = (value / 32).xor(value) % MODULUS
        value = (value * 2048).xor(value) % MODULUS
        prices.add(value % 10)
    }
    val sequences: List<Pair<List<Long>, Long>> = prices.mapIndexedNotNull { index, price ->
        if (index < 4) return@mapIndexedNotNull null
        listOf(
            prices[index - 3] - prices[index - 4],
            prices[index - 2] - prices[index - 3],
            prices[index - 1] - prices[index - 2],
            prices[index] - prices[index - 1],
        ) to price
    }

    return sequences.mapIndexedNotNull { index, (sequence, value) ->
        if (sequences.subList(0, index).any { it.first == sequence }) null
        else sequence to value
    }.toMap(LinkedHashMap())
}

fun main() {
    fun part1(input: List<String>): Long {
        return input.sumOf { value -> calculate(value.toLong()) }
    }

    fun part2(input: List<String>): Long? {
        val sequencePrices = input
            .map(String::toLong)
            .map { getSequencePricesAfter(it, 2000) }

        return sequencePrices
            .flatMap { it.keys }
            .toSet()
            .maxOfOrNull { sequence -> sequencePrices.sumOf { it[sequence] ?: 0L } }
    }

    val testInput = readInput("Day22_test")
    check(calculate(123L, 1) == 15887950L)
    check(calculate(123L, 2) == 16495136L)
    check(calculate(123L, 3) == 527345L)
    check(calculate(123L, 4) == 704524L)
    check(calculate(123L, 10) == 5908254L)
    check(part1(testInput) == 37327623L)

    val input = readInput("Day22")
    part1(input).println()
    part2(input).println()
}
