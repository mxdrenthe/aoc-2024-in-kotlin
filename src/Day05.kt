fun main() {
    fun isValidUpdate(update: List<String>, orderingRules: Map<String, List<String>>): Boolean =
        update
            .withIndex()
            .fold(listOf<Boolean>()) { a, (index, page) ->
                val previousPages = update.take(index)
                val shouldBeLaterPages = orderingRules[page] ?: listOf()
                if (previousPages.any { shouldBeLaterPages.contains(it) }) (a + true) else (a + false)
            }.all(Boolean::not)

    fun part1(input: List<String>): Int {
        val combinedInput = input.joinToString("\n")

        val orderingRules = Regex("""(\d+)\|(\d+)""")
            .findAll(combinedInput)
            .groupBy({ it.groupValues[1] }, { it.groupValues[2] })

        val pagesToCheck = Regex("""\d+(,\d+)+""")
            .findAll(combinedInput)
            .map { it.groupValues[0].split(",") }
            .toList()

        return pagesToCheck
            .filter { isValidUpdate(it, orderingRules) }
            .sumOf { it[it.count() / 2].toInt() }
    }

    fun part2(input: List<String>): Int {
        val combinedInput = input.joinToString("\n")

        fun correctUpdate(
            update: List<String>,
            orderingRules: Map<String, List<String>>
        ): List<String> {
            var correctedUpdate = update.toMutableList()

            while (!isValidUpdate(correctedUpdate, orderingRules)) {
                run breaking@{
                    correctedUpdate.withIndex().forEach { (index, page) ->
                        val previousPages = correctedUpdate.take(index)
                        val shouldBeLaterPages = orderingRules[page] ?: listOf()
                        val firstPageToSwapIndex =
                            previousPages.indexOfFirst { shouldBeLaterPages.contains(it) }
                        if (firstPageToSwapIndex != -1) {
                            val firstPageToSwap = previousPages[firstPageToSwapIndex]
                            correctedUpdate[index] = firstPageToSwap
                            correctedUpdate[firstPageToSwapIndex] = page
                            return@breaking
                        }
                    }
                }
            }

            return correctedUpdate
        }

        val orderingRules = Regex("""(\d+)\|(\d+)""")
            .findAll(combinedInput)
            .groupBy({ it.groupValues[1] }, { it.groupValues[2] })

        val pagesToCheck = Regex("""\d+(,\d+)+""")
            .findAll(combinedInput)
            .map { it.groupValues[0].split(",") }
            .toList()

        return pagesToCheck
            .filter { !isValidUpdate(it, orderingRules) }
            .sumOf {
                correctUpdate(it, orderingRules)
                    .let { it[it.count() / 2].toInt() }
            }
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
