

import Herbivore
import Island
import Location
import Mouse
import Predator
import Rabbit
import java.util.concurrent.ThreadLocalRandom

class Fox : Predator(weight = 8.0, maxCountPerCell = 30, speed = 2, satiety = 2.0) {
    override val symbol: String = "🦊"
    override fun getHuntingProbability(prey: Herbivore): Int = when (prey) {
        is Rabbit -> 70
        is Mouse -> 90
        else -> 50
    }
    override fun createOffspring(): Predator = Fox()
    override fun move(currentLocation: Location, island: Island): Location {
        val neighbors = island.getNeighbors(currentLocation.x, currentLocation.y)
        return if (neighbors.isNotEmpty()) neighbors[ThreadLocalRandom.current().nextInt(neighbors.size)] else currentLocation
    }
}