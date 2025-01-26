package org.example.animals.predators;

import java.util.Map;
import java.util.Random;
import org.example.animals.Animal;
import org.example.island.Cell;
import org.example.animals.herbivores.Herbivore;
import org.example.island.Island;
import org.example.statistics.IslandStatistics;

public abstract class Predator extends Animal {
    private static final Random random = new Random();
    protected Map<Class<? extends Animal>, Integer> eatingChances;

    protected Predator(String name) {
        super(name);
    }

    public int getEatingChance(Animal food) {
        return eatingChances.getOrDefault(food.getClass(), 0);
    }

    @Override
    public synchronized void eat(Cell currentCell, Island island) {
        if (currentCell == null || currentCell.getAnimals() == null) {
            throw new IllegalArgumentException(getName() + " cannot eat: invalid cell or animals list is null.");
        }

        currentCell.getAnimals().stream()
                .filter(Herbivore.class::isInstance)
                .findFirst()
                .ifPresentOrElse(
                        prey -> {
                            Herbivore herbivore = (Herbivore) prey;
                            if (random.nextInt(100) < getEatingChance(herbivore)) {
                                currentCell.removeAnimal(herbivore);
                                IslandStatistics.incrementGlobalHerbivoresEaten();
                                resetHunger();
                            } else {
                                increaseHunger(island);
                            }
                        },
                        () -> increaseHunger(island)
                );
    }
}
