import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.concurrent.ThreadLocalRandom

abstract class Animal(
    open val weight: Double,
    open val maxCountPerCell: Int,
    open val speed: Int,
    open val satiety: Double
) {
    var currentHunger: Double = 0.0
    abstract val symbol: String
    abstract fun eat(location: Location)
    abstract fun reproduce(location: Location)
    abstract fun move(currentLocation: Location, island: Island): Location
    fun chance(probability: Int): Boolean = ThreadLocalRandom.current().nextInt(100) < probability
}

abstract class Predator(
    weight: Double,
    maxCountPerCell: Int,
    speed: Int,
    satiety: Double
) : Animal(weight, maxCountPerCell, speed, satiety) {
    override fun eat(location: Location) {
        val herbivores = location.animals.filterIsInstance<Herbivore>()
        for (prey in herbivores) {
            val probability = getHuntingProbability(prey)
            if (chance(probability)) {
                location.removeAnimal(prey)
                currentHunger = 0.0
                println("${this.javaClass.simpleName} съел ${prey.javaClass.simpleName} в клетке (${location.x}, ${location.y})")
                return
            }
        }
        currentHunger += 1
    }
    abstract fun getHuntingProbability(prey: Herbivore): Int
    override fun reproduce(location: Location) {
        val mates = location.animals.filter { it::class == this::class && it !== this }
        if (mates.isNotEmpty() && location.countAnimal(this::class) < maxCountPerCell) {
            val offspring = createOffspring()
            location.addAnimal(offspring)
            println("${this.javaClass.simpleName} размножился в клетке (${location.x}, ${location.y})")
        }
    }
    abstract fun createOffspring(): Predator
}

abstract class Herbivore(
    weight: Double,
    maxCountPerCell: Int,
    speed: Int,
    satiety: Double
) : Animal(weight, maxCountPerCell, speed, satiety) {
    override fun eat(location: Location) {
        if (location.plants > 0) {
            location.plants--
            currentHunger = 0.0
            println("${this.javaClass.simpleName} съел растение в клетке (${location.x}, ${location.y})")
        } else {
            currentHunger += 1
        }
    }
    override fun reproduce(location: Location) {
        val mates = location.animals.filter { it::class == this::class && it !== this }
        if (mates.isNotEmpty() && location.countAnimal(this::class) < maxCountPerCell) {
            val offspring = createOffspring()
            location.addAnimal(offspring)
            println("${this.javaClass.simpleName} размножился в клетке (${location.x}, ${location.y})")
        }
    }
    abstract fun createOffspring(): Herbivore
    override fun move(currentLocation: Location, island: Island): Location {
        val neighbors = island.getNeighbors(currentLocation.x, currentLocation.y)
        return if (neighbors.isNotEmpty()) neighbors[ThreadLocalRandom.current().nextInt(neighbors.size)] else currentLocation
    }
}

class Location(val x: Int, val y: Int) {
    val animals: MutableList<Animal> = mutableListOf()
    var plants: Int = 0
    fun addAnimal(animal: Animal) { animals.add(animal) }
    fun removeAnimal(animal: Animal) { animals.remove(animal) }
    fun countAnimal(animalClass: kotlin.reflect.KClass<out Animal>): Int = animals.count { it::class == animalClass }
}

class Island(val width: Int, val height: Int) {
    val grid: Array<Array<Location>> = Array(height) { y -> Array(width) { x -> Location(x, y) } }
    fun getLocation(x: Int, y: Int): Location? = if (x in 0 until width && y in 0 until height) grid[y][x] else null
    fun getNeighbors(x: Int, y: Int): List<Location> {
        val directions = listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))
        return directions.mapNotNull { (dx, dy) -> getLocation(x + dx, y + dy) }
    }

}

class Wolf : Predator(weight = 50.0, maxCountPerCell = 30, speed = 3, satiety = 8.0) {
    override val symbol: String = "🐺"
    override fun getHuntingProbability(prey: Herbivore): Int = when (prey) {
        is Rabbit -> 60
        is Mouse -> 80
        else -> 50
    }
    override fun createOffspring(): Predator = Wolf()
    override fun move(currentLocation: Location, island: Island): Location {
        var newLocation = currentLocation
        repeat(speed) {
            val neighbors = island.getNeighbors(newLocation.x, newLocation.y)
            if (neighbors.isNotEmpty()) newLocation = neighbors[ThreadLocalRandom.current().nextInt(neighbors.size)]
        }
        return newLocation
    }
}

class Boa : Predator(weight = 15.0, maxCountPerCell = 30, speed = 1, satiety = 3.0) {
    override val symbol: String = "🐍"
    override fun getHuntingProbability(prey: Herbivore): Int = when (prey) {
        is Rabbit -> 40
        is Mouse -> 70
        else -> 50
    }
    override fun createOffspring(): Predator = Boa()
    override fun move(currentLocation: Location, island: Island): Location {
        val neighbors = island.getNeighbors(currentLocation.x, currentLocation.y)
        return if (neighbors.isNotEmpty()) neighbors[ThreadLocalRandom.current().nextInt(neighbors.size)] else currentLocation
    }
}

class Fox : Predator(weight = 8.0, maxCountPerCell = 30, speed = 2, satiety = 2.0) {
    override val symbol: String = "🦊"
    override fun getHuntingProbability(prey: Herbivore): Int = when (prey) {
        is Rabbit -> 70
        is Mouse -> 90
        else -> 50
    }
    override fun createOffspring(): Predator = Fox()
    override fun move(currentLocation: Location, island: Island): Location {
        val neighbors = island.getNeighbors(currentLocation.x, currentLocation.y)
        return if (neighbors.isNotEmpty()) neighbors[ThreadLocalRandom.current().nextInt(neighbors.size)] else currentLocation
    }
}

class Bear : Predator(weight = 500.0, maxCountPerCell = 5, speed = 2, satiety = 80.0) {
    override val symbol: String = "🐻"
    override fun getHuntingProbability(prey: Herbivore): Int = when (prey) {
        is Deer -> 40
        is Rabbit -> 50
        else -> 30
    }
    override fun createOffspring(): Predator = Bear()
    override fun move(currentLocation: Location, island: Island): Location {
        var newLocation = currentLocation
        repeat(speed) {
            val neighbors = island.getNeighbors(newLocation.x, newLocation.y)
            if (neighbors.isNotEmpty()) newLocation = neighbors[ThreadLocalRandom.current().nextInt(neighbors.size)]
        }
        return newLocation
    }
}

class Eagle : Predator(weight = 6.0, maxCountPerCell = 20, speed = 3, satiety = 1.0) {
    override val symbol: String = "🦅"
    override fun getHuntingProbability(prey: Herbivore): Int = when (prey) {
        is Mouse -> 80
        is Rabbit -> 30
        else -> 50
    }
    override fun createOffspring(): Predator = Eagle()
    override fun move(currentLocation: Location, island: Island): Location {
        var newLocation = currentLocation
        repeat(speed) {
            val neighbors = island.getNeighbors(newLocation.x, newLocation.y)
            if (neighbors.isNotEmpty()) newLocation = neighbors[ThreadLocalRandom.current().nextInt(neighbors.size)]
        }
        return newLocation
    }
}

class Rabbit : Herbivore(weight = 2.0, maxCountPerCell = 150, speed = 2, satiety = 0.45) {
    override val symbol: String = "🐇"
    override fun createOffspring(): Herbivore = Rabbit()
}

class Mouse : Herbivore(weight = 0.05, maxCountPerCell = 500, speed = 1, satiety = 0.01) {
    override val symbol: String = "🐁"
    override fun createOffspring(): Herbivore = Mouse()
}

class Horse : Herbivore(weight = 400.0, maxCountPerCell = 20, speed = 4, satiety = 60.0) {
    override val symbol: String = "🐎"
    override fun createOffspring(): Herbivore = Horse()
}

class Deer : Herbivore(weight = 300.0, maxCountPerCell = 20, speed = 4, satiety = 50.0) {
    override val symbol: String = "🦌"
    override fun createOffspring(): Herbivore = Deer()
}

class Goat : Herbivore(weight = 60.0, maxCountPerCell = 140, speed = 3, satiety = 10.0) {
    override val symbol: String = "🐐"
    override fun createOffspring(): Herbivore = Goat()
}

class Sheep : Herbivore(weight = 70.0, maxCountPerCell = 140, speed = 3, satiety = 15.0) {
    override val symbol: String = "🐑"
    override fun createOffspring(): Herbivore = Sheep()
}

class Boar : Herbivore(weight = 400.0, maxCountPerCell = 50, speed = 2, satiety = 50.0) {
    override val symbol: String = "🐗"
    override fun createOffspring(): Herbivore = Boar()
}

class Buffalo : Herbivore(weight = 700.0, maxCountPerCell = 10, speed = 3, satiety = 100.0) {
    override val symbol: String = "🐃"
    override fun createOffspring(): Herbivore = Buffalo()
}

class Duck : Herbivore(weight = 1.0, maxCountPerCell = 200, speed = 4, satiety = 0.15) {
    override val symbol: String = "🦆"
    override fun createOffspring(): Herbivore = Duck()
    override fun eat(location: Location) {
        val caterpillars = location.animals.filterIsInstance<Caterpillar>()
        if (caterpillars.isNotEmpty() && chance(70)) {
            location.removeAnimal(caterpillars.first())
            currentHunger = 0.0
            println("Duck съела Caterpillar в клетке (${location.x}, ${location.y})")
        } else {
            super.eat(location)
        }
    }
}

class Caterpillar : Herbivore(weight = 0.01, maxCountPerCell = 1000, speed = 0, satiety = 0.0) {
    override val symbol: String = "🐛"
    override fun createOffspring(): Herbivore = Caterpillar()
    override fun move(currentLocation: Location, island: Island): Location = currentLocation
}

class Plant(val nutrition: Double = 1.0) {
    val symbol: String = "🌱"
}

class Simulation(val island: Island, val simulationTickMillis: Long = 1000L) {
    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
    private val animalPool = Executors.newFixedThreadPool(10)
    var running: Boolean = true
    fun start() {
        scheduler.scheduleAtFixedRate({ tick() }, 0, simulationTickMillis, TimeUnit.MILLISECONDS)
    }
    private fun clearConsole() {
        print("\u001b[H\u001b[2J")
        System.out.flush()
    }
    private fun printField() {
        clearConsole()
        for (row in island.grid) {
            val line = row.joinToString("") { cell ->
                when {
                    cell.animals.isNotEmpty() -> cell.animals.first().symbol
                    cell.plants > 0 -> "🌱"
                    else -> "∙"
                }
            }
            println(line)
        }
    }
    fun tick() {
        val tasks = mutableListOf<Callable<Unit>>()
        for (row in island.grid) {
            for (location in row) {
                val animalsCopy = location.animals.toList()
                for (animal in animalsCopy) {
                    tasks.add(Callable {
                        animal.eat(location)
                        animal.reproduce(location)
                        val newLocation = animal.move(location, island)
                        if (newLocation != location) {
                            synchronized(location) { location.removeAnimal(animal) }
                            synchronized(newLocation) {
                                if (newLocation.animals.size < animal.maxCountPerCell) {
                                    newLocation.addAnimal(animal)
                                }
                            }
                        }
                        if (animal.currentHunger > 5) {
                            synchronized(location) {
                                location.removeAnimal(animal)
                                println("${animal.javaClass.simpleName} умер от голода в клетке (${location.x}, ${location.y})")
                            }
                        }
                    })
                }
            }
        }
        animalPool.invokeAll(tasks)
        for (row in island.grid) {
            for (location in row) {
                location.plants = (location.plants + 1).coerceAtMost(200)
            }
        }
        printField()

        if (island.grid.flatten().all { it.animals.isEmpty() }) {
            println("Симуляция завершена: все животные погибли.")
            stop()
        }
    }
    fun stop() {
        running = false
        scheduler.shutdown()
        animalPool.shutdown()
    }
}

fun main() {
    val island = Island(50, 20)
    for (row in island.grid) {
        for (loc in row) {
            loc.plants = ThreadLocalRandom.current().nextInt(0, 50)
            if (ThreadLocalRandom.current().nextDouble() < 0.1) {
                val animal: Animal = when (ThreadLocalRandom.current().nextInt(0, 15)) {
                    0 -> Wolf()
                    1 -> Boa()
                    2 -> Fox()
                    3 -> Bear()
                    4 -> Eagle()
                    5 -> Horse()
                    6 -> Deer()
                    7 -> Rabbit()
                    8 -> Mouse()
                    9 -> Goat()
                    10 -> Sheep()
                    11 -> Boar()
                    12 -> Buffalo()
                    13 -> Duck()
                    else -> Caterpillar()
                }
                loc.addAnimal(animal)
            }
        }
    }
    val simulation = Simulation(island, simulationTickMillis = 1000L)
    simulation.start()
    Thread.sleep(30000)
    simulation.stop()
}
