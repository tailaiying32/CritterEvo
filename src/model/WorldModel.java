package model;

import controller.WorldGenerator;
import graph.WorldGraph;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import model.Critter.Orientation;
import model.Critter.Priority;

/**
 * Represents the world, and contains all fields necessary to represent the current world state.
 */
public class WorldModel {

    // energy cost constants
    private double MOVE_COST_FACTOR = 0.001;
    public double getMOVE_COST() {
        return MOVE_COST_FACTOR;
    }
    public void setMOVE_COST(double MOVE_COST_FACTOR) {
        this.MOVE_COST_FACTOR = MOVE_COST_FACTOR;
    }

    private double ROTATE_COST_FACTOR = 0.0004;
    public double getROTATE_COST() {
        return ROTATE_COST_FACTOR;
    }
    public void setROTATE_COST_FACTOR(double ROTATE_COST_FACTOR) {
        this.ROTATE_COST_FACTOR = ROTATE_COST_FACTOR;
    }

    private double BASE_MOVE_COST = 0.5;
    public double getBASE_MOVE_COST() {
        return BASE_MOVE_COST;
    }
    public void setBASE_MOVE_COST(double BASE_MOVE_COST) {
        this.BASE_MOVE_COST = BASE_MOVE_COST;
    }

    private double BASE_ROTATE_COST = 0.5;
    public double getBASE_ROTATE_COST() {
        return BASE_ROTATE_COST;
    }
    public void setBASE_ROTATE_COST(double BASE_ROTATE_COST) {
        this.BASE_ROTATE_COST = BASE_ROTATE_COST;
    }

    private double SIZE_COST = 1.05;
    public double getSIZE_COST() {
        return SIZE_COST;
    }
    public void setSIZE_COST(double SIZE_COST) {
        this.SIZE_COST = SIZE_COST;
    }

    //variables representing baseDamage and damageScalingFactor
    private double BASE_DAMAGE = 40;
    public double getBASE_DAMAGE() {
        return BASE_DAMAGE;
    }
    public void setBASE_DAMAGE(double BASE_DAMAGE) {
        this.BASE_DAMAGE = BASE_DAMAGE;
    }

    private double DAMAGE_SCALING_FACTOR = 1.5;
    public double getDAMAGE_SCALING_FACTOR() {
        return DAMAGE_SCALING_FACTOR;
    }
    public void setDAMAGE_SCALING_FACTOR(double DAMAGE_SCALING_FACTOR) {
        this.DAMAGE_SCALING_FACTOR = DAMAGE_SCALING_FACTOR;
    }

    // variable representing the base reproduction energy usage
    private double BASE_REPRODUCTION_COST = 0.5;
    public double getBASE_REPRODUCTION_COST() {
        return BASE_REPRODUCTION_COST;
    }
    public void setBASE_REPRODUCTION_COST(double BASE_REPRODUCTION_COST) {
        this.BASE_REPRODUCTION_COST = BASE_REPRODUCTION_COST;
    }

    // base hunger expenditure
    private double BASE_HUNGER_EXPENDITURE = 0.4;
    public double getBASE_HUNGER_EXPENDITURE() {
        return BASE_HUNGER_EXPENDITURE;
    }
    public void setBaseHungerExpenditure(double BASE_HUNGER_EXPENDITURE) {
        this.BASE_HUNGER_EXPENDITURE = BASE_HUNGER_EXPENDITURE;
    }

    /**
     * Factor to scale food generation by (higher means food is less common)
     */
    private double FOOD_GENERATION_FACTOR = 1.0;
    public double getFOOD_GENERATION_FACTOR() {
        return FOOD_GENERATION_FACTOR;
    }
    public void setFOOD_GENERATION_FACTOR(double FOOD_GENERATION_FACTOR) {
        this.FOOD_GENERATION_FACTOR = FOOD_GENERATION_FACTOR;
    }

    /**
     * the world generator
     */
    private WorldGenerator worldGenerator;

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
     * maximum amount a mutation is allowed to change a trait by
     */
    private double maxMutationChange;

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
    public WorldModel(
            int width,
            int height,
            double initialFoodDensity,
            double initialCritterDensity,
            double moveCost,
            double rotateCost,
            double baseMoveCost,
            double baseRotateCost,
            double sizeCost,
            double mutationRate,
            double baseDamage,
            double damageScalingFactor,
            double baseHungerExpenditure,
            double foodGenRate,
            double scale,
            long seed
    ) {
        this.width = width;
        this.height = height;
        this.tickCount = 0;
        this.initialFoodDensity = initialFoodDensity;
        this.initialCritterDensity = initialCritterDensity;
        this.MOVE_COST_FACTOR = moveCost;
        this.ROTATE_COST_FACTOR = rotateCost;
        this.BASE_MOVE_COST = baseMoveCost;
        this.BASE_ROTATE_COST = baseRotateCost;
        this.SIZE_COST = sizeCost;
        this.worldArray = new CellState[width][height];
        this.mutationRate = mutationRate;
        this.critters = new HashMap<Point, Critter>();
        this.foods = new HashMap<Point, Food>();
        this.waters = new HashMap<Point, Water>();
        this.baseDamage = baseDamage;
        this.damageScalingFactor = damageScalingFactor;
        this.BASE_HUNGER_EXPENDITURE = baseHungerExpenditure;
        this.FOOD_GENERATION_FACTOR = foodGenRate;

        // generate the terrain
        this.worldGenerator = new WorldGenerator(scale, seed);
        worldGenerator.generateTerrain(this);
        // seed the world with specified parameters
        seedWorld();
    }

    /**
     * constructor for worldFactory and testing purposes
     */
    public WorldModel(int width, int height, double initialFoodDensity, double initialCritterDensity, double mutationRate, double baseDamage, double damageScalingFactor) {}

    /**
     * Seeds the world based on the parameters used during construction
     * Each square as initialFoodDensity and initialCritterDensity probability of being food or a critter, respectively
     * If a square is initiated as a food square, create a new food object with quantity 5-40 and add to the world's food
     * Similarly, if a square is initiated as a critter square, create a new critter object with random attributes
     * and add to the world's critters
     */
    public void seedWorld() {
        // loop through the world array
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                // only place critters and food on grass squares
                if (this.getWorldArray()[i][j] == CellState.GRASS) {
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
                if (critter.getPriority() == Priority.ATTACK) {
                    worldArray[pos.x][pos.y] = CellState.ANGRY_CRITTER;
                }
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
    public List<Point> squaresAround(Point p) {
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
