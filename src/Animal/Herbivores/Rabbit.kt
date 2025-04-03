
class Rabbit : Herbivore(weight = 2.0, maxCountPerCell = 150, speed = 2, satiety = 0.45) {
    override val symbol: String = "🐇"
    override fun createOffspring(): Herbivore = Rabbit()
}