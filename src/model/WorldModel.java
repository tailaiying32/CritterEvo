package model;

import graph.WorldGraph;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Represents the world, and contains all fields necessary to represent the current world state.
 */
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


    /**6
     * Initial critter density on the map i.e. the percentage of squares that initially have a critter.
     */
    private double initialCritterDensity;

    /**
     * Base mutation rate for the world i.e. the base probability for each trait that a mutation occurs.
     */
    private double mutationRate;

    /**
     * 2-D array representing the current world state. Entries in the array are an integer,
     * either 0, 1, 2, 3, 4, for grass, mountain, food, water, or critter, respectively
     */
    private int[][] world;

    /**
     * Represents all currently alive critters. Stored in a HashMap with an integer id as a key
     * and a Critter as a value
     */
    private Map<Integer, Critter> critters;

    /**
     * The base damage done by critters in the game (default 25)
     */
    private double baseDamage;

    /**
     * The scaling exponent for damage done in fights (1.2-1.5 for realism)
     */
    private double damageScalingFactor;

    /**
     * Constructor for the world. Takes in width and height parameters
     */
    public WorldModel(int width, int height, double initialFoodDensity, double initialCritterDensity, double mutationRate, double baseDamage, double damageScalingFactor) {
        this.width = width;
        this.height = height;
        this.tickCount = 0;
        this.initialFoodDensity = initialFoodDensity;
        this.initialCritterDensity = initialCritterDensity;
        this.world = new int[width][height];
        this.mutationRate = mutationRate;
        this.critters = new HashMap<Integer, Critter>();
        this.baseDamage = baseDamage;
        this.damageScalingFactor = damageScalingFactor;

        // TODO: populate the world such that this.world is seeded with food and critters according to the given initial food and critter density
        // for each square in this matrix, it will have a probability of initialFoodDensity of being a food square (value = 2),
        // and a probability of initialCritterDensity of being a critter (value = 4).
        // (hint: use the java Math.random method -> returns a random double between 0.0 and 1.0)
        // (you can assume all parameters do not violate class invariants)
        // The default value will be grass (value = 0).
        // If the square becomes a food square, construct a new food object with position (j, i),
        // quantity a random number between 5 and 40, and numCritters = 0.
        // If the square becomes a critter square, construct a new critter with position (j, i),
        // and randomized attributes (reference the Critter class!)

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                double random1 = Math.random();
                double random2 = Math.random();
                if (random1 <= initialFoodDensity) {
                    world[i][j] = 2;
                }
                if (random2 <= initialCritterDensity) {
                        world[i][j] = 4;
                }
            }
        }

    }

    /**
     * Returns the width of the world
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets width to "width"
     */
    public void setWidth(int width) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Returns the height of the world
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets height to "height"
     */
    public void setHeight(int height) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Returns the tick count of the world
     */
    public int getTickCount() {
        return tickCount;
    }

    /**
     * Increments the tick count by 1
     */
    public int incrementTickCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Returns the 2D array representing this world
     */
    public int[][] getWorld() {
        return world;
    }

    /**
     * Returns the graph representation of this world
     */
    public WorldGraph getWorldGraph() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Returns the list of all live critters
     */
    public Map<Integer, Critter> getCritters() {
        return critters;
    }

    /**
     * Returns the critter associated with "id"
     */
    public Critter getCritter(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Adds a critter to the list of all live critters
     */
    public void addCritter(Critter critter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Removes a critter from the list of all live critters
     */
    public void removeCritter(Critter critter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
