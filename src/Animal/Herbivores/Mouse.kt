


class Mouse : Herbivore(weight = 0.05, maxCountPerCell = 500, speed = 1, satiety = 0.01) {
    override val symbol: String = "🐁"
    override fun createOffspring(): Herbivore = Mouse()
}