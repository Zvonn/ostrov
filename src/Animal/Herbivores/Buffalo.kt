class Buffalo : Herbivore(weight = 700.0, maxCountPerCell = 10, speed = 3, satiety = 100.0) {
    override val symbol: String = "🐃"
    override fun createOffspring(): Herbivore = Buffalo()
}