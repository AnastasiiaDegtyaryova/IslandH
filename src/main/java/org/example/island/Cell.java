package org.example.island;

import org.example.animals.Animal;
import org.example.animals.AnimalConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Cell {
    private final List<Animal> animals;
    private final List<Grass> plants;
    private final ReadWriteLock animalLock = new ReentrantReadWriteLock();
    private final ReadWriteLock plantLock = new ReentrantReadWriteLock();


    public Cell() {
        animals = new ArrayList<>();
        plants = new ArrayList<>();
    }

    public boolean addAnimal(Animal animal) {
        animalLock.writeLock().lock();
        try {
            if (canAddAnimal(animal.getClass())) {
                animals.add(animal);
                return true;
            }
        } finally {
            animalLock.writeLock().unlock();
        }
        return false;
    }

    public void removeAnimal(Animal animal) {
        animalLock.writeLock().lock();
        try {
            animals.remove(animal);
        } finally {
            animalLock.writeLock().unlock();
        }
    }

    public void removeDeadAnimals() {
        animalLock.writeLock().lock();
        try {
            animals.removeIf(animal -> !animal.isAlive());
        } finally {
            animalLock.writeLock().unlock();
        }
    }

    public List<Animal> getAnimals() {
        animalLock.readLock().lock();
        try {
            return new ArrayList<>(animals);
        } finally {
            animalLock.readLock().unlock();
        }
    }

    public void addPlant(Grass grass) {
        plantLock.writeLock().lock();
        try {
            plants.add(grass);
        } finally {
            plantLock.writeLock().unlock();
        }
    }

    public void removePlant(Grass grass) {
        plantLock.writeLock().lock();
        try {
            plants.remove(grass);
        } finally {
            plantLock.writeLock().unlock();
        }
    }

    public List<Grass> getPlants() {
        plantLock.readLock().lock();
        try {
            return new ArrayList<>(plants);
        } finally {
            plantLock.readLock().unlock();
        }
    }

    public void growPlants() {
        plantLock.writeLock().lock();
        try {
            plants.forEach(Grass::grow);
        } finally {
            plantLock.writeLock().unlock();
        }
    }

    public boolean canAddAnimal(Class<? extends Animal> animalClass) {
        animalLock.readLock().lock();
        try {
            int maxPopulation = AnimalConfig.getMaxPopulation(animalClass);
            long count = animals.stream().filter(a -> a.getClass().equals(animalClass)).count();
            return count < maxPopulation;
        } finally {
            animalLock.readLock().unlock();
        }
    }
}

