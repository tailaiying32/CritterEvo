package model;

import java.util.HashMap;

public class WorldModel {

    /**
     * Positive integer representing the width of the world.
     */
    private int width;

    /**
     * Positive integer representing the height of the world.
     */
    private int height;

    /**
     * Non-negative integer representing the number of ticks, or turns, that have taken place
     */
    private int tickCount;

    /**
     * Initial food density on the map i.e. the percentage of squares that initially have food.
     */
    private double initialFoodDensity;

    /**
     * Initial critter density on the map i.e. the percentage of squares that initially have a critter.
     */
    private double initialCritterDensity;

    /**
     * Base mutation rate for the world i.e. the base probability for each trait that a mutation occurs.
     */
    private double mutationRate;

    /**
     * Constructor the world. Takes in width and height parameters
     */
    public WorldModel(int width, int height, double initialFoodDensity, double initialCritterDensity, double mutationRate) {
        this.width = width;
        this.height = height;
        this.tickCount = 0;
        this.initialFoodDensity = initialFoodDensity;
        this.initialCritterDensity = initialCritterDensity;
        this.mutationRate = mutationRate;
    }

    /**
     * 2-D array representing the current world state. Entries in the array are an integer,
     * either 0, 1, 2, 3, 4, for grass, mountain, food, water, or critter, respectively
     */
    private int[][] world;

    /**
     * Represents all currently alive critters. Stored in a HashMap with an integer id as a key
     * and a Critter as a value
     */
    private HashMap<Integer, Critter> critters;
}
