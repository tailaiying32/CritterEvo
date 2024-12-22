package model;

import behavior.InteractionManager;
import java.awt.Point;
import java.awt.geom.Point2D;

/** Represents a model of the critters inhabiting the world
 *
 */
public class Critter extends InteractionManager {

    public enum Sex {
        MALE, FEMALE
    }

    public enum Priority {
        FOOD, WATER, LOVE
    }

    public enum Orientation {
        UP, DOWN, LEFT, RIGHT
    }

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
     * An integer ranging from 0 to 100 representing the critter's hunger level.
     * If hunger reaches zero, the critter loses health.
     */
    private int hunger;

    /**
     * An integer ranging from 0 to 100 representing the critter's thirst level.
     * If thirst reaches zero, the critter loses health.
     */
    private int thirst;

    /**
     * An integer ranging from 0 to 100 representing the critter's max health.
     */
    private final int maxHealth;

    /**
     * An integer ranging from 0 to maxHealth representing the critter's current health.
     * If health reaches zero, the critter dies.
     */
    private int health;

    /**
     * The critter's sex, either male or female
     */
    private final Sex sex;

    /**
     * An integer ranging from 0 to 100 that represents the critter's size.
     * The critter's size has effects on the critter's movement speed and power.
     */
    private final int size;

    /**
     * An integer ranging from 0 to 100 that represents the critter's movement speed.
     * ~ speed scales inversely proportionally to size squared
     */
    private int speed;

    /**
     * An integer ranging from 0 to 100 that represents the critter's power
     * ~ strength scales proportionally to size squared
     */
    private int power;

    /**
     * An integer ranging from 0 to 100 representing the critter's aggression level.
     * The aggression level is responsible for determining how likely the critter is to fight
     * or attack another critter
     */
    private final int aggression;

    /**
     * An integer ranging from 0 to 100 representing the critter's breeding length
     */
    private final int breedLength;


    /**
     * The critter's current priority, either food, water, or love. Will decide to search for food,
     * water, or another mate based on current priority.
     */
    private Priority priority;

    /**
     * Constructs a new Critter. Takes in maxAge, maxHealth, sex, size, and aggression parameter
     * age is set to zero, health is set to maxHealth
     */
    public Critter(
            Point position,
            Orientation orientation,
            int maxAge,
            int maxHealth,
            Sex sex,
            int size,
            int aggression,
            int breedLength,
            Priority priority
            ) {
        this.maxAge = maxAge;
        this.age = 0;
        this.maxHealth = maxHealth;
        this.hunger = 50;
        this.thirst = 50;
        this.health = 100;
        this.sex = sex;
        this.size = size;
        this.aggression = aggression;
        this.speed =  (int) (Math.pow(1 - size, 2) / 100); // speed = 1 / size, scaled by 100 to keep the 1-100 scale
        this.power = (int) (Math.pow(size, 2)  / 100); // power = size^2, scaled by 100 to keep the 1-100 scale
        this.breedLength = breedLength;
        this.position = position;
    }

    /**
     * Returns this critter's id
     */
    public int getId() {
        return id;
    }

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
     * Returns the critter's current hunger level
     */
    public int getHunger() {
        return hunger;
    }

    /**
     * Returns the critter's current thirst level
     */
    public int getThirst() {
        return thirst;
    }

    /**
     * Returns the critter's current health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Returns the critter's sex
     */
    Sex getSex() {
        return sex;
    }

    /**
     * Returns the critter's size
     */
    int getSize() {
        return size;
    }
}
