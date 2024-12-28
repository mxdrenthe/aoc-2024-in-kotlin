package nl.openweb.day23

import nl.openweb.println
import nl.openweb.readInput

private fun getAllConnected(
    from: String,
    map: Map<String, List<String>>,
    groupCount: Int
): List<String> {
    var groups = listOf(listOf(from))

    while (groups.first().count() < groupCount) {
        groups = groups.flatMap { group ->
            map[group.last()]!!
                .filter { !group.contains(it) }
                .map { group + it }
        }
    }

    return groups
        .filter { group ->
            group.all { computer ->
                group.all { peer ->
                    map[computer]!!.contains(peer) || computer == peer
                }
            }
        }.map { it.sorted().joinToString(",") }
}

private fun getAllCliques(
    graph: Map<String, List<String>>,
    currentClique: Set<String>,
    remainingNodes: MutableSet<String>,
    visitedNodes: MutableSet<String>
): List<Set<String>> {
    if (remainingNodes.isEmpty() && visitedNodes.isEmpty()) return listOf(currentClique)
    val results = mutableListOf<Set<String>>()

    remainingNodes.toList().forEach { v ->
        val neighbours = graph[v]?.toSet() ?: emptySet()
        results.addAll(
            getAllCliques(
                graph,
                currentClique + v,
                remainingNodes.intersect(neighbours).toMutableSet(),
                visitedNodes.intersect(neighbours).toMutableSet()
            )
        )
        remainingNodes.remove(v)
        visitedNodes.add(v)
    }
    return results
}

fun main() {
    fun part1(input: List<String>): Int {
        val map = input
            .flatMap {
                val (from, towards) = it.split("-")
                listOf(from to towards, towards to from)
            }.groupBy({ it.first }, { it.second })

        return map.keys.filter { it[0] == 't' }
            .flatMap { getAllConnected(it, map, 3) }
            .toSet()
            .count()
    }

    fun part2(input: List<String>): String {
        val map = input
            .flatMap {
                val (from, towards) = it.split("-")
                listOf(from to towards, towards to from)
            }.groupBy({ it.first }, { it.second })

        return getAllCliques(map, emptySet(), map.keys as MutableSet<String>, mutableSetOf())
            .maxBy { it.count() }
            .sorted()
            .joinToString(",")
    }

    val testInput = readInput("Day23_test")
    check(part1(testInput) == 7)

    val input = readInput("Day23")
    part1(input).println()
    part2(input).println()
}
