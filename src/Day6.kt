enum class Tile {
    EMPTY, OBJECT, START
}

enum class Direction {
    UP {
        override fun turnRight() = RIGHT
    },
    RIGHT {
        override fun turnRight() = DOWN
    },
    DOWN {
        override fun turnRight() = LEFT
    },
    LEFT {
        override fun turnRight() = UP
    };

    abstract fun turnRight(): Direction
    fun getAllDirectionsFrom(): List<Direction> {
        return listOf(this, turnRight(), turnRight().turnRight(), turnRight().turnRight().turnRight())
    }
}

data class Position(val y: Int, val x: Int) {
    fun getNextValidPosition(
        direction: Direction,
        grid: List<List<Tile>>
    ): Pair<Position, Direction> {
        val (forward, right, backward, left) = direction.getAllDirectionsFrom()
        val nextForward = getNextPosition(forward)
        val nextRight = getNextPosition(right)
        val nextBackward = getNextPosition(backward)
        val nextLeft = getNextPosition(left)
        return when {
            nextForward.isValidPosition(grid) -> nextForward to forward
            nextRight.isValidPosition(grid) -> nextRight to right
            nextBackward.isValidPosition(grid) -> nextBackward to backward
            nextLeft.isValidPosition(grid) -> nextLeft to left
            else -> error("No valid position found")
        }
    }

    fun getNextValidPosition(
        direction: Direction,
        obstacle: Position,
        grid: List<List<Tile>>
    ): Pair<Position, Direction> {
        val (forward, right, backward, left) = direction.getAllDirectionsFrom()
        val nextForward = getNextPosition(forward)
        val nextRight = getNextPosition(right)
        val nextBackward = getNextPosition(backward)
        val nextLeft = getNextPosition(left)
        return when {
            nextForward.isValidPosition(obstacle, grid) -> nextForward to forward
            nextRight.isValidPosition(obstacle, grid) -> nextRight to right
            nextBackward.isValidPosition(obstacle, grid) -> nextBackward to backward
            nextLeft.isValidPosition(obstacle, grid) -> nextLeft to left
            else -> error("No valid position found")
        }
    }

    fun isOutsideGrid(grid: List<List<Tile>>): Boolean {
        return x < 0 || y < 0 || x >= grid[0].size || y >= grid.size
    }

    private fun isValidPosition(grid: List<List<Tile>>): Boolean {
        return isOutsideGrid(grid) || listOf(Tile.EMPTY, Tile.START).contains(grid[y][x])
    }

    private fun isValidPosition(obstacle: Position, grid: List<List<Tile>>): Boolean =
        when {
            isOutsideGrid(grid) -> true
            obstacle == this -> false
            grid[y][x] == Tile.OBJECT -> false
            else -> true
        }

    private fun getNextPosition(direction: Direction): Position {
        return when (direction) {
            Direction.UP -> Position(y - 1, x)
            Direction.RIGHT -> Position(y, x + 1)
            Direction.DOWN -> Position(y + 1, x)
            Direction.LEFT -> Position(y, x - 1)
        }
    }
}

private fun doesItLoop(
    start: Pair<Position, Direction>,
    extraObstacle: Position,
    grid: List<List<Tile>>
): Boolean {
    var (currentPosition, currentDirection) = start
    val visited = HashSet<Pair<Position, Direction>>()

    while (true) {
        if ((currentPosition to currentDirection) in visited) return true
        visited.add(currentPosition to currentDirection)

        val (nextPosition, nextDir) = currentPosition.getNextValidPosition(currentDirection, extraObstacle, grid)
        if (nextPosition.isOutsideGrid(grid)) return false

        currentPosition = nextPosition
        currentDirection = nextDir
    }
}

private tailrec fun getWholePath(
    currentPath: List<Position>,
    currentDir: Direction,
    grid: List<List<Tile>>,
): List<Position> {
    val currentPosition = currentPath.last()
    val (nextPosition, nextDir) = currentPosition.getNextValidPosition(currentDir, grid)
    return when {
        nextPosition.isOutsideGrid(grid) -> currentPath
        else -> getWholePath(currentPath + nextPosition, nextDir, grid)
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val grid = input.map { line ->
            line.map { char ->
                when (char) {
                    '.' -> Tile.EMPTY
                    '^' -> Tile.START
                    '#' -> Tile.OBJECT
                    else -> error("Unknown tile: $char")
                }
            }
        }

        val startingPosition = grid.mapIndexed { y, row ->
            val x = row.indexOfFirst { it == Tile.START }
            if (x != -1) Position(y, x) else null
        }.first { it != null } ?: error("No starting position found")

        val wholePath = getWholePath(listOf(startingPosition), Direction.UP, grid)

        return wholePath.toSet().count()
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { line ->
            line.map { char ->
                when (char) {
                    '.' -> Tile.EMPTY
                    '^' -> Tile.START
                    '#' -> Tile.OBJECT
                    else -> error("Unknown tile: $char")
                }
            }
        }

        val startingPosition = grid.mapIndexed { y, row ->
            val x = row.indexOfFirst { it == Tile.START }
            if (x != -1) Position(y, x) else null
        }.first { it != null } ?: error("No starting position found")

        val possibleObstaclePositions = grid.flatMapIndexed { y, row ->
            row.mapIndexed { x, tile ->
                if (tile == Tile.EMPTY) Position(y, x) else null
            }.filterNotNull()
        }

        val starting = (startingPosition to Direction.UP)

        return possibleObstaclePositions
            .count { doesItLoop(starting, it, grid) }
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
