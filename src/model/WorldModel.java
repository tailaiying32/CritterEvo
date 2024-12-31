package model;

import graph.WorldGraph;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import model.Critter.Orientation;

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
     * Represents all currently alive critters. Stored in a Map with a Point id as a key
     * and a Critter as a value
     */
    private Map<Point, Critter> critters;

    /**
     * Represents all food on the grid currently. Stored in a Map with the Point position as a key
     * and the food object as a value.
     */
    private Map<Point, Food> foods;

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
        this.critters = new HashMap<Point, Critter>();
        this.foods = new HashMap<Point, Food>();
        this.baseDamage = baseDamage;
        this.damageScalingFactor = damageScalingFactor;

        // seed the world with specified parameters
        seedWorld();

//        critters.forEach((p, c) -> {System.out.println(p.toString() + ": " + c.getSize());}); // debugging log to test if seeding worked properly
    }

    /**
     * Seeds the world based on the parameters used during construction
     * Each square as initialFoodDensity and initialCritterDensity probability of being food or a critter, respectively
     * If a square is initiated as a food square, create a new food object with quantity 5-40 and add to the world's food
     * Similarly, if a square is initiated as a critter square, create a new critter object with random attributes
     * and add to the world's critters
     */
    public void seedWorld() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                double randomValue = Math.random(); // random number used for seeding world
                if (randomValue <= initialFoodDensity) {
                    this.world[i][j] = 2; // 2 for food
                    Food food = new Food(new Point(i, j), (int) (Math.random()*35 + 5), 0);
                    addFood(food);
                } else if (randomValue <= initialFoodDensity + initialCritterDensity) {
                    this.world[i][j] = 4; // 4 for critter
                    // construct a new critter with random attributes
                    CritterFactory critterFactory = new CritterFactory();
                    addCritter(critterFactory.generateCritter(new Point(i, j)));
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
        this.width = width;
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
        this.height= height;
    }

    /**
     * Returns the tick count of the world
     */
    public int getTickCount() {
        return tickCount;
    }

    /**
     * Increments the tick count by 1
     * Return the new tick count after incrementing
     */
    public int incrementTickCount() {
        tickCount++;
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
    public Map<Point, Critter> getCritters() {
        return critters;
    }

    /**
     * Sets the critter list to "critters"
     */
    public void setCritters(Map<Point, Critter> critters) {
        this.critters = critters;
    }

    /**
     * Returns the critter on Point p
     * If there is no critter on Point p, return null
     */
    public Critter getCritter(Point p) {
        return critters.get(p);
    }

    /**
     * Adds a critter to the list of all live critters
     */
    public void addCritter(Critter critter) {
        critters.put(critter.getPosition(), critter);

    }

    /**
     * Removes the critter at point p from the list of all live critters
     */
    public void removeCritter(Point p) {
        critters.remove(p);
    }

    /**
     * Returns the list of all food on the map
     */
    public Map<Point, Food> getFoods() {
        return foods;
    }

    /**
     * Sets the foods list to "foods"
     */
    public void setFoods(Map<Point, Food> foods) {
        this.foods = foods;
    }

    /**
     * Returns the food on Point p
     * If there is no food on Point p, return null
     */
    public Food getFood(Point p) {
        return foods.get(p);
    }

    /**
     * Adds food to the list of all food
     */
    public void addFood(Food food) {
        this.foods.put(food.getPosition(), food);
    }

    /**
     * Removes food at point p from the list of all food
     */
    public void removeFood(Point p) {
        foods.remove(p);
    }

    /**
     * updates the world
     */
    public void update() {
        throw new UnsupportedOperationException();
    }

    /**
     * asserts that class invariants are not violated
     */
    private void assertInv() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
