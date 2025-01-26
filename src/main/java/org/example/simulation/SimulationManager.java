package org.example.simulation;

import org.example.animals.Animal;
import org.example.animals.AnimalConfig;
import org.example.animals.AnimalData;
import org.example.config.SimulationConfig;
import org.example.config.YAMLLoader;
import org.example.island.Cell;
import org.example.island.Island;
import org.example.island.IslandInitializer;
import org.example.statistics.IslandStatistics;
import org.example.statistics.StatisticsManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class SimulationManager {
    private final Island island;
    private final StatisticsManager statisticsManager;
    private final ScheduledExecutorService plantGrowthScheduler;
    private final ScheduledExecutorService animalLifeCycleScheduler;
    private final ExecutorService animalExecutor;
    private final int plantGrowthInterval;
    private final int animalLifeCycleInterval;
    private int currentDay = 1;

    public SimulationManager() {
        SimulationConfig config = new SimulationConfig();
        List<AnimalData> animalDataList = new YAMLLoader().loadAnimalData(config.getAnimalDataFile());
        AnimalConfig.initialize(animalDataList);
        this.island = IslandInitializer.initialize(config);
        this.statisticsManager = new StatisticsManager(island);
        this.plantGrowthScheduler = Executors.newScheduledThreadPool(1);
        this.animalLifeCycleScheduler = Executors.newScheduledThreadPool(1);
        this.animalExecutor = Executors.newFixedThreadPool(50);
        this.plantGrowthInterval = config.getPlantGrowthInterval();
        this.animalLifeCycleInterval = config.getAnimalLifeCycleInterval();
    }

    public void startSimulation() {
        System.out.println("Simulation Started");
        plantGrowthScheduler.scheduleAtFixedRate(this::growPlants, 0, plantGrowthInterval, TimeUnit.SECONDS);
        animalLifeCycleScheduler.scheduleAtFixedRate(this::processAnimalLifeCycle, 0, animalLifeCycleInterval, TimeUnit.SECONDS);
    }

    public void stopSimulation() {
        try {
            System.out.println("Game Over");
            plantGrowthScheduler.shutdown();
            animalLifeCycleScheduler.shutdown();
            animalExecutor.shutdown();

            if (!plantGrowthScheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                plantGrowthScheduler.shutdownNow();
            }
            if (!animalLifeCycleScheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                animalLifeCycleScheduler.shutdownNow();
            }
            if (!animalExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                animalExecutor.shutdownNow();
            }
            System.out.println("Simulation stopped successfully.");
        } catch (InterruptedException e) {
            System.err.println("Error during simulation shutdown: " + e.getMessage());
        }
    }

    private void growPlants() {
        Arrays.stream(island.getGrid())
                .flatMap(Arrays::stream)
                .forEach(Cell::growPlants);
    }

    private void processAnimalLifeCycle() {
        Arrays.stream(island.getGrid())
                .flatMap(Arrays::stream)
                .parallel()
                .forEach(cell -> {
                    List<Animal> cellAnimals = new ArrayList<>(cell.getAnimals());
                    for (Animal animal : cellAnimals) {
                        if (!animal.isAlive()) {
                            cell.removeAnimal(animal);
                            continue;
                        }

                        animalExecutor.submit(() -> {
                            synchronized (animal) {
                                try {
                                    if (animal.isAlive()) {
                                        animal.eat(cell, island);
                                        if (!animal.isAlive()) {
                                            synchronized (cell.getAnimals()) {
                                                cell.removeAnimal(animal);
                                            }
                                            return;
                                        }
                                    }

                                    if (animal.isAlive()) {
                                        animal.move(cell, island);
                                        if (!animal.isAlive()) {
                                            synchronized (cell.getAnimals()) {
                                                cell.removeAnimal(animal);
                                            }
                                            return;
                                        }
                                    }

                                    if (animal.isAlive()) {
                                        animal.reproduce(cell);
                                    }
                                } catch (Exception e) {
                                    System.err.println("Error in task for " + animal.getName() + ": " + e.getMessage());
                                }
                            }
                        });
                    }
                });
        IslandStatistics stats = statisticsManager.gatherStatistics(currentDay);
        System.out.print(stats);
        currentDay++;
        logThreadPoolStatus();
        checkForEndOfSimulation();
    }

    private void checkForEndOfSimulation() {
        boolean allAnimalsDead = Arrays.stream(island.getGrid())
                .flatMap(Arrays::stream)
                .noneMatch(cell -> cell.getAnimals().stream().anyMatch(Animal::isAlive));

        if (allAnimalsDead) {
            System.out.println("All animals are dead. Simulation stopping.");
            stopSimulation();
        }
    }

    private void logThreadPoolStatus() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) animalExecutor;
        System.out.println("Active threads: " + executor.getActiveCount());
        System.out.println("Tasks in queue: " + executor.getQueue().size());
        System.out.println("Completed tasks: " + executor.getCompletedTaskCount());
    }
}

