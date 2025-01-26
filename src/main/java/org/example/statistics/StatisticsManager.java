package org.example.statistics;

import java.util.HashMap;
import java.util.Map;
import org.example.animals.Animal;
import org.example.island.Cell;
import org.example.island.Island;

public class StatisticsManager {
    private final Island island;

    public StatisticsManager(Island island) {
        this.island = island;
    }

    public IslandStatistics gatherStatistics(int currentDay) {
        int totalAnimals = 0;
        int totalPlants = 0;
        int plantsEaten = 0;
        int animalsReproduced = 0;
        int animalsMoved = 0;

        Map<String, Long> animalCounts = new HashMap<>();

        for (Cell[] row : island.getGrid()) {
            for (Cell cell : row) {
                cell.removeDeadAnimals();

                totalAnimals += cell.getAnimals().size();

                cell.getAnimals().forEach(animal ->
                        animalCounts.merge(animal.getClass().getSimpleName(), 1L, Long::sum)
                );

                totalPlants += cell.getPlants().size();

                plantsEaten += cell.getAnimals().stream()
                        .mapToInt(Animal::getPlantsEaten)
                        .sum();

                animalsReproduced += cell.getAnimals().stream()
                        .mapToInt(Animal::getReproductionsCount)
                        .sum();

                animalsMoved += cell.getAnimals().stream()
                        .mapToInt(Animal::getMovesCount)
                        .sum();
            }
        }
        return new IslandStatistics(
                totalAnimals, totalPlants, plantsEaten,
                animalsReproduced, animalsMoved, currentDay, animalCounts
        );
    }
}