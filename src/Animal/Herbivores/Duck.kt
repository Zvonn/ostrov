



class Duck : Herbivore(weight = 1.0, maxCountPerCell = 200, speed = 4, satiety = 0.15) {
    override val symbol: String = "🦆"
    override fun createOffspring(): Herbivore = Duck()
    override fun eat(location: Location) {
        val caterpillars = location.animals.filterIsInstance<Caterpillar>()
        if (caterpillars.isNotEmpty() && chance(70)) {
            location.removeAnimal(caterpillars.first())
            currentHunger = 0.0
            println("Duck съела Caterpillar в клетке (${location.x}, ${location.y})")
        } else {
            super.eat(location)
        }
    }
}