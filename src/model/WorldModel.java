package model;

import graph.WorldGraph;
import java.util.HashMap;

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

    /**
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
    private HashMap<Integer, Critter> critters;

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

    }

    /**
     * Returns the width of the world
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the world
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the tick count of the world
     */
    public int getTickCount() {
        return tickCount;
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
    public HashMap<Integer, Critter> getCritters() {
        return critters;
    }
}
