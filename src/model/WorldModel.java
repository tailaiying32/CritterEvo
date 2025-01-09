package model;

import graph.WorldGraph;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private CellState[][] worldArray;

    /**
     * enum for cell state
     */
    public enum CellState {
        GRASS(0), MOUNTAIN(1), FOOD(2), WATER(3), PEACEFUL_CRITTER(4), ANGRY_CRITTER(5);
        private int value;
        CellState(int value) {
            this.value = value;
        }
        public int getValue() {
            return this.value;
        }
    }

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
     * Represents all water on grid currently. Stored in a Map with the Point position as a key
     * and the water object as a value
     */
    private Map<Point, Water> waters;

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
        this.worldArray = new CellState[width][height];
        this.mutationRate = mutationRate;
        this.critters = new HashMap<Point, Critter>();
        this.foods = new HashMap<Point, Food>();
        this.waters = new HashMap<Point, Water>();
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
                    this.worldArray[i][j] = CellState.FOOD; // 2 for food
                    Food food = new Food(new Point(i, j), (int) (Math.random()*35 + 5), 0);
                    addFood(food);
                } else if (randomValue <= initialFoodDensity + initialCritterDensity) {
                    this.worldArray[i][j] = CellState.PEACEFUL_CRITTER; // 4 for critter
                    // construct a new critter with random attributes
                    CritterFactory critterFactory = new CritterFactory();
                    Critter critter = critterFactory.generateCritter(new Point(i, j), this);
                    addCritter(critter);
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
    public CellState[][] getWorldArray() {
        return worldArray;
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
        updateWorldArray();
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
        updateWorldArray();
    }

    /**
     * Removes the critter at point p from the list of all live critters
     */
    public void removeCritter(Point p) {
        critters.remove(p);
        updateWorldArray();
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
        updateWorldArray();
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
        updateWorldArray();
    }

    /**
     * Removes food at point p from the list of all food
     */
    public void removeFood(Point p) {
        foods.remove(p);
        updateWorldArray();
    }

    /**
     * Returns the list of all food on the map
     */
    public Map<Point, Water> getWaters() {
        return waters;
    }

    /**
     * Sets the foods list to "foods"
     */
    public void setWaters(Map<Point, Water> waters) {
        this.waters = waters;
        updateWorldArray();
    }

    /**
     * Returns the food on Point p
     * If there is no food on Point p, return null
     */
    public Water getWater(Point p) {
        return waters.get(p);
    }

    /**
     * Adds food to the list of all food
     */
    public void addWater(Water water) {
        this.waters.put(water.getPosition(), water);
        updateWorldArray();
    }

    /**
     * Removes food at point p from the list of all food
     */
    public void removeWater(Point p) {
        waters.remove(p);
        updateWorldArray();
    }

    /**
     * Return's the world's mutation rate
     */
    public double getMutationRate() {
        return this.mutationRate;
    }


    /**
     * updates the world array per tick
     */
    public void updateWorldArray() {
        // Clear the current world array (except for mountains/terrain)
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (worldArray[i][j] != CellState.MOUNTAIN) { // Don't clear mountains
                    worldArray[i][j] = CellState.GRASS; // Set to grass
                }
            }
        }

        // Update food positions
        for (Food food : foods.values()) {
            Point pos = food.getPosition();
            if (pos.x >= 0 && pos.x < width && pos.y >= 0 && pos.y < height) {
                worldArray[pos.x][pos.y] = CellState.FOOD; // Food
            }
        }

        // Update water positions
        for (Water water : waters.values()) {
            Point pos = water.getPosition();
            if (pos.x >= 0 && pos.x < width && pos.y >= 0 && pos.y < height) {
                worldArray[pos.x][pos.y] = CellState.WATER; // Water
            }
        }

        // Update critter positions
        for (Critter critter : critters.values()) {
            Point pos = critter.getPosition();
            if (pos.x >= 0 && pos.x < width && pos.y >= 0 && pos.y < height) {
                worldArray[pos.x][pos.y] = CellState.PEACEFUL_CRITTER; // Critter
            }
        }
    }

    /**
     * Retrieves all info about critters
     */
    public List<Map<String, Object>> getCritterTraits() {
        List<Map<String, Object>> traits = new ArrayList<>();
        for (Critter critter : critters.values()) {
            Map<String, Object> critterData = new HashMap<>();
            critterData.put("Position", critter.getPosition());
            critterData.put("Orientation", critter.getOrientation());
            critterData.put("Health", critter.getHealth());
            critterData.put("Hunger", critter.getHunger());
            critterData.put("Thirst", critter.getThirst());
            critterData.put("Age", critter.getAge());
            critterData.put("Size", critter.getSize());
            critterData.put("Offense", critter.getOffense());
            critterData.put("Defense", critter.getDefense());
            traits.add(critterData);
        }
        return traits;
    }

    /**
     * Returns the list of 8 points around a single point
     */
    public List<?> squaresAround(Point p) {
        List<Point> squaresAround = new ArrayList<>();
        int currentX = p.x;
        int currentY = p.y;

        squaresAround.add(new Point(currentX, currentY - 1));
        squaresAround.add(new Point(currentX, currentY + 1));
        squaresAround.add(new Point(currentX + 1, currentY));
        squaresAround.add(new Point(currentX - 1, currentY));
        squaresAround.add(new Point(currentX + 1, currentY + 1));
        squaresAround.add(new Point(currentX + 1, currentY - 1));
        squaresAround.add(new Point(currentX - 1, currentY + 1));
        squaresAround.add(new Point(currentX - 1, currentY - 1));

        return squaresAround;
    }


    /**
     * asserts that class invariants are not violated
     */
    private void assertInv() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
