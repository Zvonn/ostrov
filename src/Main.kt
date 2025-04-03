package main
import Animal
import Island
import Bear
import Boa
import Boar
import Buffalo
import Caterpillar
import Deer
import Duck
import Eagle
import Fox
import Goat
import Horse
import Mouse
import Rabbit
import Sheep
import Simulation
import Wolf
import java.util.concurrent.ThreadLocalRandom



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

    // Держим симуляцию 30 секунд
    Thread.sleep(30_000)

    simulation.stop()
}
