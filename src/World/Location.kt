
class Location(val x: Int, val y: Int) {
    val animals: MutableList<Animal> = mutableListOf()
    var plants: Int = 0
    fun addAnimal(animal: Animal) { animals.add(animal) }
    fun removeAnimal(animal: Animal) { animals.remove(animal) }
    fun countAnimal(animalClass: kotlin.reflect.KClass<out Animal>): Int = animals.count { it::class == animalClass }
}