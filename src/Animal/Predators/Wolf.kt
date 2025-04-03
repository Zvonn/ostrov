
import Herbivore
import Island
import Location
import Mouse
import Predator
import Rabbit
import java.util.concurrent.ThreadLocalRandom

class Wolf : Predator(weight = 50.0, maxCountPerCell = 30, speed = 3, satiety = 8.0) {
    override val symbol: String = "🐺"
    override fun getHuntingProbability(prey: Herbivore): Int = when (prey) {
        is Rabbit -> 60
        is Mouse -> 80
        else -> 50
    }
    override fun createOffspring(): Predator = Wolf()
    override fun move(currentLocation: Location, island: Island): Location {
        var newLocation = currentLocation
        repeat(speed) {
            val neighbors = island.getNeighbors(newLocation.x, newLocation.y)
            if (neighbors.isNotEmpty()) newLocation = neighbors[ThreadLocalRandom.current().nextInt(neighbors.size)]
        }
        return newLocation
    }
}