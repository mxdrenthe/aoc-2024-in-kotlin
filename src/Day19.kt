package nl.openweb.day19

import nl.openweb.println
import nl.openweb.readInput

private fun getValidDesigns(designs: List<String>, towels: List<String>): List<String> = designs
    .filter {
        Regex("""^(${towels.joinToString("|")})+$""")
            .matches(it)
    }

private fun isValidDesign(design: String, towels: List<String>): Boolean =
    Regex("""^(${towels.joinToString("|")})+$""")
        .matches(design)

private fun waysToMake(
    design: String,
    towels: List<String>,
    memo: HashMap<String, Long>
): Long {
    if (memo.contains(design)) return memo[design]!!
    if (!isValidDesign(design, towels)) return 0

    val result = towels.sumOf {
        when {
            design == it -> 1
            design.startsWith(it) -> waysToMake(design.removePrefix(it), towels, memo)
            else -> 0
        }
    }

    memo[design] = result
    return result
}

fun main() {
    fun part1(input: List<String>): Int {
        val towels = input[0].split(", ")
        val designs = input.subList(2, input.size)
        return getValidDesigns(designs, towels).count()
    }

    fun part2(input: List<String>): Long {
        val towels = input[0].split(", ")
        val designs = input.subList(2, input.size)
        val memo = HashMap<String, Long>()

        return designs
            .sumOf { waysToMake(it, towels, memo) }
    }

    val testInput = readInput("Day19_test")
    check(part1(testInput) == 6)

    val input = readInput("Day19")
    part1(input).println()
    part2(input).println()
}
