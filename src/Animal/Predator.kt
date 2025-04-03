
abstract class Predator(
    weight: Double,
    maxCountPerCell: Int,
    speed: Int,
    satiety: Double
) : Animal(weight, maxCountPerCell, speed, satiety) {
    override fun eat(location: Location) {
        val herbivores = location.animals.filterIsInstance<Herbivore>()
        for (prey in herbivores) {
            val probability = getHuntingProbability(prey)
            if (chance(probability)) {
                location.removeAnimal(prey)
                currentHunger = 0.0
                println("${this.javaClass.simpleName} съел ${prey.javaClass.simpleName} в клетке (${location.x}, ${location.y})")
                return
            }
        }
        currentHunger += 1
    }
    abstract fun getHuntingProbability(prey: Herbivore): Int
    override fun reproduce(location: Location) {
        val mates = location.animals.filter { it::class == this::class && it !== this }
        if (mates.isNotEmpty() && location.countAnimal(this::class) < maxCountPerCell) {
            val offspring = createOffspring()
            location.addAnimal(offspring)
            println("${this.javaClass.simpleName} размножился в клетке (${location.x}, ${location.y})")
        }
    }
    abstract fun createOffspring(): Predator
}