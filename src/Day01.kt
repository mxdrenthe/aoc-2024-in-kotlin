package nl.openweb.day01

import nl.openweb.println
import nl.openweb.readInput

fun main() {
    fun part1(input: List<String>): Int {
        val leftList = mutableListOf<Int>()
        val rightList = mutableListOf<Int>()
        input.forEach {
            val (left, right) = it.split("   ")
            leftList.add(left.toInt())
            rightList.add(right.toInt())
        }
        leftList.sort()
        rightList.sort()

        var sum = 0
        leftList.forEachIndexed { index, item ->
            sum += if (item <= rightList[index]) {
                rightList[index] - item
            } else {
                item - rightList[index]
            }
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val leftList = mutableListOf<Int>()
        val rightList = mutableListOf<Int>()
        input.forEach {
            val (left, right) = it.split("   ")
            leftList.add(left.toInt())
            rightList.add(right.toInt())
        }
        var sum = 0
        leftList.forEach {
            sum += it * rightList.count { item ->
                item == it
            }
        }
        return sum
    }

    check(part1(listOf("1   3")) == 2)
    check(part2(listOf("1   3")) == 0)
    check(part2(listOf("3   3")) == 0)

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
