package org.example.animals.herbivores;

import org.example.animals.Animal;
import org.example.island.Cell;
import org.example.animals.AnimalConfig;
import org.example.island.Island;
import org.example.statistics.IslandStatistics;
import org.example.island.Grass;

public class Duck extends Herbivore {
    public Duck(String name) {
        super(name);
    }

    @Override
    public synchronized void eat(Cell currentCell, Island island) {
        boolean ateSomething = false;
        double foodRequirement = AnimalConfig.getFoodRequirement(this.getClass());

        for (Grass plant : currentCell.getPlants()) {
            if (plant.getWeight() >= foodRequirement) {
                resetHunger();
                ateSomething = true;
                currentCell.removePlant(plant);
                plantsEaten++;
                break;
            }
        }

        if (!ateSomething) {
            for (Animal animal : currentCell.getAnimals()) {
                if (animal instanceof Caterpillar) {
                    resetHunger();
                    ateSomething = true;
                    currentCell.removeAnimal(animal);
                    IslandStatistics.incrementGlobalHerbivoresEaten();
                    break;
                }
            }
        }

        if (!ateSomething) {
            increaseHunger(island);
        }
    }
}
