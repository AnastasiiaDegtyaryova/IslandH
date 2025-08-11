package org.example.island;

import lombok.Getter;
import org.example.animals.Animal;

@Getter
public class Island {
    private final int width;
    private final int height;
    private final Cell[][] grid;

    public Island(int width, int height) {
        this.width = width;
        this.height = height;
        grid = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    public void addAnimalToCell(int x, int y, Animal animal) {
        if (isValidCell(x, y)) {
            synchronized (grid[x][y]) {
                if (!grid[x][y].addAnimal(animal)) {
                    System.out.println("Could not add " + animal.getName() + " to cell (" + x + ", " + y + ").");
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid cell coordinates: (" + x + ", " + y + ")");
        }
    }

    public void addPlantToCell(int x, int y, Grass grass) {
        if (isValidCell(x, y)) {
            synchronized (grid[x][y]) {
                grid[x][y].addPlant(grass);
            }
        } else {
            throw new IllegalArgumentException("Invalid cell coordinates: (" + x + ", " + y + ")");
        }
    }

    private boolean isValidCell(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public Cell getCellAt(int x, int y) {
        if (isValidCell(x, y)) {
            return grid[x][y];
        }
        throw new IllegalArgumentException("Coordinates out of bounds: (" + x + ", " + y + ")");
    }
}
