package nl.openweb.day25

import nl.openweb.println
import nl.openweb.readAllInput

private enum class ObjectType { KEY, LOCK }
private data class Object(val type: ObjectType, val value: List<Int>) {
    fun overlaps(other: Object): Boolean =
        value.zip(other.value).all { (a, b) -> a + b <= other.value.count() }
}

private fun getObjects(input: String): List<Object> = input.split("\r\n\r\n")
    .map { schematic ->
        val type = if (schematic.lines().first().first() == '#') ObjectType.LOCK else ObjectType.KEY
        val reducedSchematic = schematic.lines().drop(1).dropLast(1)
        val value = (0 until reducedSchematic.count()).map { j ->
            (0 until reducedSchematic[j].count()).sumOf { i ->
                (if (reducedSchematic[i][j] == '#') 1 else 0).toInt()
            }
        }
        Object(type, value)
    }

fun main() {
    fun part1(input: String): Int {
        val objects = getObjects(input)
        val keys = objects.filter { it.type == ObjectType.KEY }
        val locks = objects.filter { it.type == ObjectType.LOCK }

        return keys
            .sumOf { key -> locks.count { lock -> key.overlaps(lock) } }
    }

    val testInput = readAllInput("Day25_test")
    check(part1(testInput) == 3)

    val input = readAllInput("Day25")
    part1(input).println()
}
