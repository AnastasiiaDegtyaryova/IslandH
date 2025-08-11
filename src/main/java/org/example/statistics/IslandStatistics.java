package org.example.statistics;

import java.util.Map;

public class IslandStatistics {
    private final int totalAnimals;
    private final int totalPlants;
    private static int globalHerbivoresEaten = 0;
    private final int plantsEaten;
    private final int animalsReproduced;
    private final int animalsMoved;
    private final int day;
    private final Map<String, Long> animalCounts;

    public IslandStatistics(int totalAnimals, int totalPlants, int plantsEaten, int animalsReproduced, int animalsMoved, int day, Map<String, Long> animalCounts) {
        this.totalAnimals = totalAnimals;
        this.totalPlants = totalPlants;
        this.plantsEaten = plantsEaten;
        this.animalsReproduced = animalsReproduced;
        this.animalsMoved = animalsMoved;
        this.day = day;
        this.animalCounts = animalCounts;
    }

    public static synchronized void incrementGlobalHerbivoresEaten() {
        globalHerbivoresEaten++;
    }

    public static synchronized int getGlobalHerbivoresEaten() {
        return globalHerbivoresEaten;
    }

    @Override
    public String toString() {
        StringBuilder stats = new StringBuilder();
        stats.append("Island Statistics (Day ").append(day).append("):\n")
                .append("- Total Animals: ").append(totalAnimals).append("\n")
                .append("- Total Plants: ").append(totalPlants).append("\n")
                .append("- Plants Eaten: ").append(plantsEaten).append("\n")
                .append("- Animals Reproduced: ").append(animalsReproduced).append("\n")
                .append("- Animals Moved: ").append(animalsMoved).append("\n")
                .append("Animals by type:\n");

        animalCounts.forEach((animalType, count) ->
                stats.append("  ").append(animalType).append(": ").append(count).append("\n")
        );
        return stats.toString();
    }
}

