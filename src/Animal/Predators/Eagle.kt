

import Herbivore
import Island
import Location
import Mouse
import Predator
import Rabbit
import java.util.concurrent.ThreadLocalRandom

class Eagle : Predator(weight = 6.0, maxCountPerCell = 20, speed = 3, satiety = 1.0) {
    override val symbol: String = "🦅"
    override fun getHuntingProbability(prey: Herbivore): Int = when (prey) {
        is Mouse -> 80
        is Rabbit -> 30
        else -> 50
    }
    override fun createOffspring(): Predator = Eagle()
    override fun move(currentLocation: Location, island: Island): Location {
        var newLocation = currentLocation
        repeat(speed) {
            val neighbors = island.getNeighbors(newLocation.x, newLocation.y)
            if (neighbors.isNotEmpty()) newLocation = neighbors[ThreadLocalRandom.current().nextInt(neighbors.size)]
        }
        return newLocation
    }
}