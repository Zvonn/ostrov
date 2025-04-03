import java.util.concurrent.ThreadLocalRandom

abstract class Animal(
    open val weight: Double,
    open val maxCountPerCell: Int,
    open val speed: Int,
    open val satiety: Double
) {
    var currentHunger: Double = 0.0
    abstract val symbol: String
    abstract fun eat(location: Location)
    abstract fun reproduce(location: Location)
    abstract fun move(currentLocation: Location, island: Island): Location
    fun chance(probability: Int): Boolean = ThreadLocalRandom.current().nextInt(100) < probability
}