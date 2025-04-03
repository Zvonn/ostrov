import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

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