class Sheep : Herbivore(weight = 70.0, maxCountPerCell = 140, speed = 3, satiety = 15.0) {
    override val symbol: String = "🐑"
    override fun createOffspring(): Herbivore = Sheep()
}