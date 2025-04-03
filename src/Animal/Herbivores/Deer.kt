
class Deer : Herbivore(weight = 300.0, maxCountPerCell = 20, speed = 4, satiety = 50.0) {
    override val symbol: String = "🦌"
    override fun createOffspring(): Herbivore = Deer()
}