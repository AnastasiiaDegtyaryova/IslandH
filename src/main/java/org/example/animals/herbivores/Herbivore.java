package org.example.animals.herbivores;

import java.util.Optional;
import org.example.animals.Animal;
import org.example.island.Cell;
import org.example.animals.AnimalConfig;
import org.example.island.Island;
import org.example.island.Grass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Herbivore extends Animal {
    protected int plantsEaten = 0;

    protected Herbivore(String name) {
        super(name);
    }

    @Override
    public int getPlantsEaten() {
        return plantsEaten;
    }

    @Override
    public synchronized void eat(Cell currentCell, Island island) {
        if (currentCell == null || currentCell.getPlants() == null) {
            throw new IllegalArgumentException(getName()  + " cannot eat: invalid cell or plants list is null.");
        }

        double foodRequirement = AnimalConfig.getFoodRequirement(this.getClass());

        Optional<Grass> plantOptional = currentCell.getPlants().stream()
                .filter(plant -> plant != null && plant.getWeight() >= foodRequirement)
                .findFirst();

        if (plantOptional.isPresent()) {
            Grass plant = plantOptional.get();
            resetHunger();
            currentCell.removePlant(plant);
            plantsEaten++;
        } else {
            increaseHunger(island);
        }
    }
}
