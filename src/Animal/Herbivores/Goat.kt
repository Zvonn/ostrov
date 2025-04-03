class Goat : Herbivore(weight = 60.0, maxCountPerCell = 140, speed = 3, satiety = 10.0) {
    override val symbol: String = "🐐"
    override fun createOffspring(): Herbivore = Goat()
}