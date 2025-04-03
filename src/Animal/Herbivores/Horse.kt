

class Horse : Herbivore(weight = 400.0, maxCountPerCell = 20, speed = 4, satiety = 60.0) {
    override val symbol: String = "🐎"
    override fun createOffspring(): Herbivore = Horse()
}