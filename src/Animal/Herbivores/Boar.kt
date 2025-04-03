class Boar : Herbivore(weight = 400.0, maxCountPerCell = 50, speed = 2, satiety = 50.0) {
    override val symbol: String = "🐗"
    override fun createOffspring(): Herbivore = Boar()
}