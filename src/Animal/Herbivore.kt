
import java.util.concurrent.ThreadLocalRandom

abstract class Herbivore(
    weight: Double,
    maxCountPerCell: Int,
    speed: Int,
    satiety: Double
) : Animal(weight, maxCountPerCell, speed, satiety) {
    override fun eat(location: Location) {
        if (location.plants > 0) {
            location.plants--
            currentHunger = 0.0
            println("${this.javaClass.simpleName} съел растение в клетке (${location.x}, ${location.y})")
        } else {
            currentHunger += 1
        }
    }
    override fun reproduce(location: Location) {
        val mates = location.animals.filter { it::class == this::class && it !== this }
        if (mates.isNotEmpty() && location.countAnimal(this::class) < maxCountPerCell) {
            val offspring = createOffspring()
            location.addAnimal(offspring)
            println("${this.javaClass.simpleName} размножился в клетке (${location.x}, ${location.y})")
        }
    }
    abstract fun createOffspring(): Herbivore
    override fun move(currentLocation: Location, island: Island): Location {
        val neighbors = island.getNeighbors(currentLocation.x, currentLocation.y)
        return if (neighbors.isNotEmpty()) neighbors[ThreadLocalRandom.current().nextInt(neighbors.size)] else currentLocation
    }
}