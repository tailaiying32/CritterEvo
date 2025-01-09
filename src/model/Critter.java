package model;

import behavior.CritterAI;
import behavior.InteractionManager;
import java.awt.Point;

/** Represents a model of the critters inhabiting the world
 *
 */
public class Critter{

    public enum Sex {
        MALE(0), FEMALE(1);
        private int value;
        Sex(int value) {
            this.value = value;
        }
        public int getValue() {
            return this.value;
        }
    }

    public enum Priority {
        FOOD(0), WATER(1), LOVE(2), ATTACK(3), REST(4);
        private int value;
        Priority(int value) {
            this.value = value;
        }
        public int getValue() {
            return this.value;
        }
    }

    public enum Orientation {
        N(0), NE(1), E(2), SE(3), S(4), SW(5), W(6), NW(7);
        private int value;
        Orientation(int value) {
            this.value = value;
        }
        public int getValue() {
            return this.value;
        }
    }

    public enum critterState {
        MOVING(0), EATING(1), DRINKING(2), BREEDING(3), FIGHTING(4), RESTING(5);
        private int value;
        critterState(int value) {
            this.value = value;
        }
        public int getValue() {
            return this.value;
        }
    }

    /**
     * The world that the critter inhabits
     */
    private WorldModel world;

    /**
     * The current state of the critter
     */
    private critterState state;

    /**
     * The critter's AI, reponsible for decision-making
     */
    private CritterAI ai;

    /**
     * The critter's behavior suite
     */
    private InteractionManager interactionManager;

    /**
     * Unique positive integer id for critter, simply iterated up from 1
     */
    private int id;

    /**
     * xy coordinates representing the position of the critter on the world
     */
    private Point position;

    /**
     * The current orientation of the critter i.e. which direction it is facing
     */
    private Orientation orientation;

    /**
     * xy coordinates representing the position of the critter's target on the world
     */
    private Point target;

    /**
     * A non-negative integer representing the critter's maximum age.
     */
    private final int maxAge;

    /**
     * A non-negative integer representing the critter's current age.
     * Must be less than or equal to maxAge. If age becomes equal to maxAge,
     * the critter dies.
     */
    private int age;

    /**
     * An integer 1-100 representing max hunger
     */
    private double maxHunger;

    /**
     * An int ranging from 0 to 100 representing the critter's hunger level.
     * If hunger reaches zero, the critter loses health.
     */
    private double hunger;

    /**
     * An integer 1-100 representing max thirst level
     */
    private double maxThirst;


    /**
     * An integer ranging from 0 to 100 representing the critter's thirst level.
     * If thirst reaches zero, the critter loses health.
     */
    private double thirst;

    /**
     * An integer ranging from 0 to 100 representing the critter's max health.
     */
    private final double maxHealth;

    /**
     * An integer ranging from 0 to maxHealth representing the critter's current health.
     * If health reaches zero, the critter dies.
     */
    private double health;

    /**
     * The critter's sex, either male or female
     */
    private final Sex sex;

    /**
     * An integer ranging from 0 to 100 that represents the critter's size.
     * The critter's size effects its energy consumption when performing tasks
     * and the amount of damage it gives and the amount of damage it takes.
     */
    private final double size;

    /**
     * An integer ranging from 0 to 100 representing the critter's offensive power. Used for calculating
     * attack damage. A critter's offense must equal 100 - defense.
     */
    private double offense;

    /**
     * An integer ranging from 0 to 100 representing the critter's defensive power. Used for calculating
     * damage taken when attacked. A critter's defense must equal 100 - offense.
     */
    private double defense;

    /**
     * An integer ranging from 0 to 100 representing the critter's aggression level.
     * The aggression level is responsible for determining how likely the critter is to fight
     * or attack another critter
     */
    private final double aggression;

    /**
     * The critter's current priority, either food, water, or love. Will decide to search for food,
     * water, or another mate based on current priority.
     */
    private Priority priority;

    /**
     * The critter's additional mutation rate, added on top of the world's base mutation rate
     */
    private double mutationRate;

    /**
     * Constructs a new Critter. Takes in maxAge, maxHealth, sex, size, and aggression parameter
     * age is set to zero, health is set to maxHealth
     */
    public Critter(
            CritterAI ai,
            InteractionManager interactionManager,
            Point position,
            Orientation orientation,
            int maxAge,
            double maxHunger,
            double maxThirst,
            double maxHealth,
            Sex sex,
            double size,
            double offense,
            double defense,
            double aggression,
            double mutationRate,
            WorldModel world
            ) {
        this.ai = ai;
        this.interactionManager = interactionManager;
        this.maxAge = maxAge;
        this.age = 0;
        this.maxHealth = maxHealth;
        this.maxHunger = maxHunger;
        this.hunger = maxHunger/2;
        this.maxThirst = maxThirst;
        this.thirst = maxThirst/2;
        this.health = 100;
        this.sex = sex;
        this.size = size;
        this.offense = offense;
        this.defense = defense;
        this.aggression = aggression;
        this.position = position;
        this.orientation = orientation;
        this.mutationRate = mutationRate;
        this.world = world;
        assertInv();
    }

    /**
     * Constructs a critter without a world parameter for testing purposes
     */
    public Critter(CritterAI ai, InteractionManager interactionManager, Point position, Orientation orientation, int maxAge, int maxHunger, int maxThirst, int maxHealth, Sex sex, int size, int offense, int defense, int aggression, int generation, int mutationRate) {
        this.ai = ai;
        this.interactionManager = interactionManager;
        this.maxAge = maxAge;
        this.age = 0;
        this.maxHealth = maxHealth;
        this.maxHunger = maxHunger;
        this.hunger = maxHunger/2;
        this.maxThirst = maxThirst;
        this.thirst = maxThirst/2;
        this.health = 100;
        this.sex = sex;
        this.size = size;
        this.offense = offense;
        this.defense = defense;
        this.aggression = aggression;
        this.position = position;
        this.orientation = orientation;
        this.mutationRate = mutationRate;
        this.world = null;
        assertInv();
    }

    /**
     * Returns the world that this critter lives in
     */
    public WorldModel getWorld() {
        return world;
    }

    /**
     * Returns this critter's id
     */
    public int getId() {
        return id;
    }

    /**
     * Return this critter's state
     */
    public critterState getState() {
        return state;
    }

    /**
     * Return this critter's ai
     */
    public CritterAI getAi() {
        return ai;
    }

    /**
     * Sets this critter's ai to "ai"
     */
    public void setAi(CritterAI ai) {
        this.ai = ai;
    }

    /**
     * Returns this critter's interaction manager
     */
    public InteractionManager getInteractionManager() {
        return this.interactionManager;
    }

    /**
     * Returns this critter's priority
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Sets this critter's priority to "priority"
     */
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    /**
     *  Returns the critter's orientation
     */
    public Orientation getOrientation() { return orientation; }

    /**
     * Sets the critter's orientation to "orientation"
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    /**
     * Return's this critter's position
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Sets the critters position to "position"
     */
    public void setPosition( Point position) { this.position = position; }

    /**
     * Return's the critter's max age
     */
    public int getMaxAge() {
        return maxAge;
    }

    /**
     * Returns the critter's current age
     */
    public int getAge() {
        return age;
    }

    /**
     * sets the critter's age to "age"
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Returns the critter's max hunger level
     */
    public double getMaxHunger() {
        return this.maxHunger;
    }

    /**
     * Sets the critter's max hunger level to "max"
     */
    public void setMaxHunger(double max) {
        maxHunger = max;
    }

    /**
     * Returns the critter's current hunger level
     */
    public double getHunger() {
        return hunger;
    }

    /**
     * Sets the critter's hunger level to "hunger"
     * Returns new hunger level
     */
    public void setHunger(double hunger) {
        this.hunger = hunger;
    }

    /**
     * Returns the critter's max thirst level
     */
    public double getMaxThirst() {
        return this.maxThirst;
    }

    /**
     * Sets the critter's max thirst to "max"
     */
    public void setMaxThirst(double max) {
        maxThirst = max;
    }

    /**
     * Returns the critter's current thirst level
     */
    public double getThirst() {
        return thirst;
    }

    /**
     * Sets the critter's thirst level to "thirst"
     * Returns the new thirst level
     */
    public void setThirst(double thirst) {
        this.thirst = thirst;
    }

    /**
     * Returns this critter's maximum health
     */
    public double getMaxHealth() {
        return maxHealth;
    }

    /**
     * Returns the critter's current health
     */
    public double getHealth() {
        return health;
    }

    /**
     * Sets the critter's health to "health"
     */
    public void setHealth(double health) {
        this.health = health;
    }

    /**
     * Returns the critter's sex
     */
    public Sex getSex() {
        return sex;
    }

    /**
     * Returns the critter's size
     */
    public double getSize() {
        return size;
    }

    /**
     * Returns this critter's speed
     */
    public double getOffense() {
        return offense;
    }

    /**
     * Returns this critter's power
     */
    public double getDefense() {
        return defense;
    }

    /**
     * Returns this critter's aggression
     */
    public double getAggression() {
        return aggression;
    }

    /**
     * Returns this critter's mutation rate
     */
    public double getMutationRate() {
        return this.mutationRate;
    }

    /**
     * Eats the food directly in front of the critter
     */
    public void eat(Critter this, Food food) {
        interactionManager.eat(this, food);
        getWorld().removeFood(food.getPosition());
    }

    /**
     * Drinks the water directly in front of the critter
     */
    public void drink(Critter this, Water water) {
        interactionManager.drink(this, water);
    }

    /**
     * Rotates the critter to a new orientation
     */
    public void rotate(Critter this, Orientation orientation) {
        interactionManager.rotate(this, orientation);
    }

    /**
     * Moves the critter forward in the direction it is facing
     */
    public void move(Critter this, int distance) {
        interactionManager.move(this, distance);
    }

    /**
     * updates priority
     */
    public void updatePriority(Critter this) {
        ai.updatePriority(this);
    }

    /**
     * Critter asexually reproduces
     */
    public void reproduce(Critter this) {
        interactionManager.reproduce(this);
    }

    /**
     * makes move
     */
    public void makeMove(Critter this) {
        ai.makeMove(this);
    }

    /**
     * attack another critter
     */
    public void attack(Critter this, Critter critter) {
        interactionManager.attack(this, critter);
    }

    /**
     * locates the critter's nearest target
     */
    public Point locateTarget(Critter this) {
        return ai.locateTarget(this, this.getPriority());
    }

    /**
     * determines what the critter's orientation should be
     */
    public Orientation determineOrientation() {
        return ai.determineOrientation(this);
    }

    /**
     * decrements health when critter runs out of hunger
     */
    public void starve(Critter this) {
        this.health -= 25;
    }

    /**
     * the critter dies
     */
    public void die(Critter this) {
        this.interactionManager.die(this);
    }

    /**
     * Assert that class invariants are satisfied.
     */
    private void assertInv() {
        assert this.orientation == Orientation.N || this.orientation == Orientation.NE || this.orientation == Orientation.E || this.orientation == Orientation.S || this.orientation == Orientation.SE || this.orientation == Orientation.SW || this.orientation == Orientation.W || this.orientation == Orientation.NW;
        assert this.sex == Sex.MALE || this.sex == Sex.FEMALE;
        assert this.maxAge >= 0 && this.maxAge <= 100;
        assert this.maxHealth >= 0 && this.maxHealth <= 100;
        assert this.hunger >= 0 && this.hunger <= 100;
        assert this.thirst >= 0 && this.thirst <= 100;
        assert this.age >= 0 && this.age <= 100;
        assert this.size >= 0 && this.size <= 100;
        assert this.offense >= 0 && this.offense <= 100;
        assert this.defense >= 0 && this.defense <= 100;
        assert this.aggression >= 0 && this.aggression <= 100;

    }
}
