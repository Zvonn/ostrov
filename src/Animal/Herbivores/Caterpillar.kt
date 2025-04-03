class Caterpillar : Herbivore(weight = 0.01, maxCountPerCell = 1000, speed = 0, satiety = 0.0) {
    override val symbol: String = "🐛"
    override fun createOffspring(): Herbivore = Caterpillar()
    override fun move(currentLocation: Location, island: Island): Location = currentLocation
}