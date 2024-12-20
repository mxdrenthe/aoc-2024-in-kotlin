package nl.openweb.day09

import nl.openweb.println
import nl.openweb.readInput

private sealed class Block {
    data class File(val id: Long) : Block()
    class Space : Block()
}

private data class Chunk(val content: Block, val length: Int)

fun main() {
    fun part1(input: List<String>): Long {
        val memory = input[0]
            .filter { it.isDigit() }
            .withIndex()
            .fold((0L to listOf<Block>())) { (currId, currList), value ->
                when (value.index % 2) {
                    0 -> (currId + 1 to (currList + List(value.value.digitToInt()) {
                        Block.File(
                            currId
                        )
                    }))

                    else -> (currId to (currList + List(value.value.digitToInt()) { Block.Space() }))
                }
            }.second

        val fragmented = memory
            .fold((0 to memory.count() - 1) to listOf<Block.File>()) { (indices, currList), value ->
                var (indexLeft, indexRight) = indices
                while (memory[indexRight] is Block.Space) indexRight--
                if (indexLeft > indexRight) {
                    return@fold (indices to currList)
                }

                when (value) {
                    is Block.File -> ((indexLeft + 1 to indexRight) to (currList + memory[indexLeft] as Block.File))
                    is Block.Space -> ((indexLeft + 1 to indexRight - 1) to (currList + memory[indexRight] as Block.File))
                }
            }.second

        return fragmented
            .withIndex()
            .sumOf { it.value.id * it.index }
    }

    fun part2(input: List<String>): Long {
        val memory = input[0]
            .filter { it.isDigit() }
            .withIndex()
            .fold((0L to listOf<Chunk>())) { (currId, currList), value ->
                when (value.index % 2) {
                    0 -> (currId + 1 to (currList + Chunk(
                        Block.File(currId),
                        value.value.digitToInt()
                    )))

                    else -> (currId to (currList + Chunk(Block.Space(), value.value.digitToInt())))
                }
            }.second.toMutableList()

        val chunksIdsToMove = memory
            .mapNotNull {
                when (it.content) {
                    is Block.File -> it.content.id
                    is Block.Space -> null
                }
            }.reversed()

        chunksIdsToMove.forEach { chunkId ->
            val chunk = memory.last { it.content is Block.File && (it.content).id == chunkId }
            val chunkIndex = memory.indexOf(chunk)

            val emptyChunk =
                memory.firstOrNull() { it.content is Block.Space && it.length >= chunk.length }
            if (emptyChunk == null) return@forEach
            val emptyChunkIndex = memory.indexOf(emptyChunk)

            if (emptyChunkIndex >= chunkIndex) return@forEach
            when {
                chunk.length == emptyChunk.length -> {
                    memory[emptyChunkIndex] = chunk
                    memory[chunkIndex] = emptyChunk
                }

                chunk.length < emptyChunk.length -> {
                    memory[emptyChunkIndex] = Chunk(Block.File(chunkId), chunk.length)
                    memory[chunkIndex] = Chunk(Block.Space(), chunk.length)
                    memory.add(
                        emptyChunkIndex + 1,
                        Chunk(Block.Space(), emptyChunk.length - chunk.length)
                    )
                }
            }
        }

        return memory
            .flatMap { List(it.length) { _ -> it.content } }
            .withIndex()
            .sumOf {
                when (it.value) {
                    is Block.File -> (it.value as Block.File).id * it.index
                    is Block.Space -> 0
                }
            }
    }

    // Or read a large test input from the `src/Day09_test.txt` file:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    // Read the input from the `src/Day09.txt` file.
    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
