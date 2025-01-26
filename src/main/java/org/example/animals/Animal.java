package org.example.animals;

import org.example.island.Cell;
import org.example.island.Island;
import java.util.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Animal implements Cloneable {
    protected String name;
    protected boolean isAlive = true;
    protected double hunger = 0;
    protected int x, y;
    protected int reproductionTimer = 0;
    protected final int reproductionPeriod = 5;
    protected int reproductionsCount = 0;
    protected int movesCount = 0;

    protected Animal(String name) {
        this.name = name;
    }

    public double getWeight() {
        return AnimalConfig.getWeight(this.getClass());
    }

    public int getMaxSpeed() {
        return AnimalConfig.getMaxSpeed(this.getClass());
    }

    public double getFoodRequirement() {
        return AnimalConfig.getFoodRequirement(this.getClass());
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void increaseHunger(Island island) {
        hunger += 1;
        if (hunger >= 10) {
            die(island);
        }
    }

    public void resetHunger() {
        hunger = 0;
    }

    public void die(Island island) {
        if (!isAlive) {
            return;
        }
        isAlive = false;
        Cell currentCell = island.getCellAt(x, y);
        synchronized (currentCell) {
            currentCell.removeAnimal(this);
        }
    }

    public int getPlantsEaten() {
        return 0;
    }

    public abstract void eat(Cell currentCell, Island island);

    public void move(Cell currentCell, Island island) {
        Random rand = new Random();
        int maxAttempts = 10;
        int attempts = 0;
        boolean moved = false;

        while (!moved && attempts < maxAttempts) {
            int xDirection = rand.nextInt(-getMaxSpeed(), getMaxSpeed());
            int yDirection = rand.nextInt(-getMaxSpeed(), getMaxSpeed());

            int newX = getX() + xDirection;
            int newY = getY() + yDirection;

            if (newX >= 0 && newX < island.getWidth() && newY >= 0 && newY < island.getHeight()) {
                Cell newCell = island.getCellAt(newX, newY);

                synchronized (currentCell.getAnimals()) {
                    synchronized (newCell.getAnimals()) {
                        if (newCell.addAnimal(this)) {
                            currentCell.removeAnimal(this);
                            setX(newX);
                            setY(newY);
                            movesCount++;
                            moved = true;
                        }
                    }
                }
            }
            attempts++;
        }
    }

    @Override
    protected Animal clone() throws CloneNotSupportedException {
        return (Animal) super.clone();
    }

    public void reproduce(Cell currentCell) {
        if (!isAlive()) return;

        reproductionTimer++;

        synchronized (this) {
            if (reproductionTimer >= reproductionPeriod) {
                try {
                    synchronized (currentCell.getAnimals()) {
                        if (currentCell.canAddAnimal(this.getClass())) {
                            Animal offspring = this.clone();
                            currentCell.addAnimal(offspring);
                            reproductionsCount++;
                        }
                    }
                } catch (CloneNotSupportedException e) {
                    System.err.println("Failed to clone " + getName());
                }
                reproductionTimer = 0;
            }
        }
    }
}

