
import Herbivore
import Island
import Location
import Mouse
import Predator
import Rabbit
import java.util.concurrent.ThreadLocalRandom

class Boa : Predator(weight = 15.0, maxCountPerCell = 30, speed = 1, satiety = 3.0) {
    override val symbol: String = "🐍"
    override fun getHuntingProbability(prey: Herbivore): Int = when (prey) {
        is Rabbit -> 40
        is Mouse -> 70
        else -> 50
    }
    override fun createOffspring(): Predator = Boa()
    override fun move(currentLocation: Location, island: Island): Location {
        val neighbors = island.getNeighbors(currentLocation.x, currentLocation.y)
        return if (neighbors.isNotEmpty()) neighbors[ThreadLocalRandom.current().nextInt(neighbors.size)] else currentLocation
    }
}