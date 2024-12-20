package nl.openweb.day10

import nl.openweb.println
import nl.openweb.readInput
import java.util.*

private data class Step(val x: Int, val y: Int, val height: Int) {
    fun getNeighbours(map: Map<Pair<Int, Int>, Step>): List<Step> = listOfNotNull(
        map.getOrDefault(x - 1 to y, null),
        map.getOrDefault(x + 1 to y, null),
        map.getOrDefault(x to y + 1, null),
        map.getOrDefault(x to y - 1, null),
    )
}

private fun canReach(start: Step, end: Step, map: Map<Pair<Int, Int>, Step>): Boolean {
    val distanceMap = mutableMapOf<Step, Int>()
    val toVisit = PriorityQueue<Step>(compareBy { distanceMap.getOrDefault(it, Int.MAX_VALUE / 2) })
    distanceMap[start] = 0
    toVisit.add(start)
    while (toVisit.isNotEmpty()) {
        val currentStep = toVisit.poll()
        if (currentStep == end) return true
        val currentDistance = distanceMap[currentStep]!!
        val neighbours = listOfNotNull(
            map.getOrDefault(currentStep.x - 1 to currentStep.y, null),
            map.getOrDefault(currentStep.x + 1 to currentStep.y, null),
            map.getOrDefault(currentStep.x to currentStep.y + 1, null),
            map.getOrDefault(currentStep.x to currentStep.y - 1, null),
        )
        neighbours.forEach { neighbour ->
            val cost = if (neighbour.height - currentStep.height == 1) 1 else Int.MAX_VALUE / 2
            val newDistance = currentDistance + cost
            if (distanceMap.getOrDefault(neighbour, Int.MAX_VALUE / 2) > newDistance) {
                distanceMap[neighbour] = newDistance
                toVisit.add(neighbour)
            }
        }
    }
    return false
}


private fun countTrails(end: Step, map: Map<Pair<Int, Int>, Step>): Long {
    var count = 0L

    val toVisit = mutableListOf(end)
    while (toVisit.isNotEmpty()) {
        val currentTrail = toVisit.removeFirst()
        if (currentTrail.height == 0) count++

        currentTrail.getNeighbours(map).forEach { neighbour ->
            if (currentTrail.height - neighbour.height == 1) {
                toVisit.add(neighbour)
            }
        }
    }

    return count
}

fun main() {
    fun part1(input: List<String>): Long {
        val steps = input.withIndex().flatMap { (i, line) ->
            line.withIndex().map { (j, point) ->
                Step(i, j, point.digitToInt())
            }
        }

        val map = steps.associateBy { it.x to it.y }
        val startingPoints = steps.filter { it.height == 0 }
        val endingPoints = steps.filter { it.height == 9 }

        return startingPoints
            .sumOf { start ->
                endingPoints.sumOf { if (canReach(start, it, map)) 1L else 0L }
            }
    }

    fun part2(input: List<String>): Long {
        val steps = input.withIndex().flatMap { (i, line) ->
            line.withIndex().map { (j, point) -> Step(i, j, point.digitToInt()) }
        }

        val map = steps.associateBy { it.x to it.y }

        return steps
            .filter { it.height == 9 }
            .sumOf { countTrails(it, map) }
    }

    // Or read a large test input from the `src/Day10_test.txt` file:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 36L)

    // Read the input from the `src/Day10.txt` file.
    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
