
import Deer
import Herbivore
import Island
import Location
import Predator
import Rabbit
import java.util.concurrent.ThreadLocalRandom

class Bear : Predator(weight = 500.0, maxCountPerCell = 5, speed = 2, satiety = 80.0) {
    override val symbol: String = "🐻"
    override fun getHuntingProbability(prey: Herbivore): Int = when (prey) {
        is Deer -> 40
        is Rabbit -> 50
        else -> 30
    }
    override fun createOffspring(): Predator = Bear()
    override fun move(currentLocation: Location, island: Island): Location {
        var newLocation = currentLocation
        repeat(speed) {
            val neighbors = island.getNeighbors(newLocation.x, newLocation.y)
            if (neighbors.isNotEmpty()) newLocation = neighbors[ThreadLocalRandom.current().nextInt(neighbors.size)]
        }
        return newLocation
    }
}
