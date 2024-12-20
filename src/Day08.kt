package nl.openweb.day08

import nl.openweb.println
import nl.openweb.readInput
import kotlin.math.abs
import kotlin.math.min

fun main() {
    fun <T, R> List<T>.mapPairs(transform: (T, T) -> R): List<R> {
        return this.flatMapIndexed { i, a ->
            this.drop(i + 1).map { b ->
                transform(a, b)
            }
        }
    }

    data class Position(val x: Int, val y: Int) {
        operator fun plus(other: Position): Position = Position(x + other.x, y + other.y)
        operator fun minus(other: Position): Position = Position(x - other.x, y - other.y)
        operator fun times(other: Int): Position = Position(x * other, y * other)
    }

    fun getAntiNodes(firstAntenna: Position, secondAntenna: Position): List<Position> {
        val offset = firstAntenna - secondAntenna
        return listOf(firstAntenna + offset, secondAntenna - offset)
    }

    fun getAntiNodes(
        firstAntenna: Position,
        secondAntenna: Position,
        width: Int,
        height: Int
    ): List<Position> {
        val offset = firstAntenna - secondAntenna
        val maxMovementVert = abs(height / offset.x)
        val maxMovementHoriz = abs(width / offset.y)
        val maxMovement = min(maxMovementVert, maxMovementHoriz)
        return (0..maxMovement)
            .flatMap { listOf(firstAntenna + offset * it, secondAntenna - offset * it) }
            .filter { it.x in 0 until height && it.y in 0 until width }
    }

    fun part1(input: List<String>): Int {
        val mapWidth = input.first().count()
        val mapHeight = input.count()

        val antennae = input
            .withIndex()
            .flatMap { (i, line) ->
                line
                    .toCharArray()
                    .withIndex()
                    .map { (j, character) ->
                        if (character != '.') character to Position(
                            i,
                            j
                        ) else null
                    }
            }
            .filterNotNull()
            .groupBy({ it.first }, { it.second })

        return antennae
            .values
            .flatMap { it.mapPairs { a, b -> getAntiNodes(a, b) }.flatten() }
            .toSet()
            .count { it.x in 0 until mapHeight && it.y in 0 until mapWidth }
    }

    fun part2(input: List<String>): Int {
        val mapWidth = input.first().count()
        val mapHeight = input.count()

        val antennae = input
            .withIndex()
            .flatMap { (i, line) ->
                line
                    .toCharArray()
                    .withIndex()
                    .map { (j, character) ->
                        if (character != '.') character to Position(
                            i,
                            j
                        ) else null
                    }
            }
            .filterNotNull()
            .groupBy({ it.first }, { it.second })

        return antennae
            .values
            .flatMap { it.mapPairs { a, b -> getAntiNodes(a, b, mapWidth, mapHeight) }.flatten() }
            .toSet()
            .count()
    }

    // Or read a large test input from the `src/Day08_test.txt` file:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 14)

    // Read the input from the `src/Day08.txt` file.
    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
