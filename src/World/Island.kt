
class Island(val width: Int, val height: Int) {
    val grid: Array<Array<Location>> = Array(height) { y -> Array(width) { x -> Location(x, y) } }
    fun getLocation(x: Int, y: Int): Location? = if (x in 0 until width && y in 0 until height) grid[y][x] else null
    fun getNeighbors(x: Int, y: Int): List<Location> {
        val directions = listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))
        return directions.mapNotNull { (dx, dy) -> getLocation(x + dx, y + dy) }
    }

}
