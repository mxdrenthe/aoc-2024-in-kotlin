fun main() {
    data class Coordinate(val x: Int, val y: Int)

    fun List<List<Char>>.nestedGetOrNull(position: Coordinate) =
        this.getOrNull(position.x)?.getOrNull(position.y)

    fun part1(input: List<String>): Int {


        fun getPossibleLocations(from: Coordinate): List<List<Coordinate>> {
            val range = 0..<"XMAS".length
            return listOf(
                range.map { Coordinate(from.x, from.y + it) },
                range.map { Coordinate(from.x + it, from.y + it) },
                range.map { Coordinate(from.x + it, from.y) },
                range.map { Coordinate(from.x + it, from.y - it) },
                range.map { Coordinate(from.x, from.y - it) },
                range.map { Coordinate(from.x - it, from.y - it) },
                range.map { Coordinate(from.x - it, from.y) },
                range.map { Coordinate(from.x - it, from.y + it) }
            )
        }

        fun wordsStartingAt(grid: List<List<Char>>, position: Coordinate) =
            getPossibleLocations(position)
                .map {
                    val lettersFound = it.map(grid::nestedGetOrNull)
                    if (lettersFound.any { it == null }) 0 else (if (lettersFound.joinToString("") == "XMAS") 1 else 0)
                }.sum()

        val grid = input.map { it.toList() }

        return grid
            .flatMapIndexed { j, row ->
                List(row.size) { i ->
                    wordsStartingAt(grid, Coordinate(i, j))
                }
            }
            .sum()
    }

    fun part2(input: List<String>): Int {
        fun getPossibleLocations(from: Coordinate): List<List<Coordinate>> {
            val range = 0..<"MAS".length
            return listOf(
                range.map { Coordinate(from.x + it, from.y + it) },
                range.map { Coordinate(from.x + it, from.y - it) },
                range.map { Coordinate(from.x - it, from.y - it) },
                range.map { Coordinate(from.x - it, from.y + it) }
            )
        }

        fun findACoordsInMas(grid: List<List<Char>>, position: Coordinate) = getPossibleLocations(position)
            .mapNotNull {
                val lettersFound = it.map(grid::nestedGetOrNull)
                if (lettersFound.any { it == null }) null else (if (lettersFound.joinToString("") == "MAS") it[1] else null)
            }
        val grid = input.map { it.toList() }

        return grid
            .asSequence()
            .flatMapIndexed { j, row ->
                List(row.size) { i ->
                    findACoordsInMas(grid, Coordinate(i, j))
                }
            }
            .flatten()
            .groupBy { it }
            .filter { it.value.size > 1 }
            .flatMap { it.value }
            .count()
            .div(2)
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
