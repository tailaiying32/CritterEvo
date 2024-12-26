package model;

import behavior.InteractionManager;
import java.awt.Point;

/** Represents a model of the critters inhabiting the world
 *
 */
public class Critter extends InteractionManager {

    public enum Sex {
        MALE(0), FEMALE(1);
        private int value;
        Sex(int value) {
            this.value = value;
        }
    }

    public enum Priority {
        FOOD(0), WATER(1), LOVE(2), ATTACK(3), REST(4);
        private int value;
        Priority(int value) {
            this.value = value;
        }
    }

    public enum Orientation {
        UP(0), DOWN(1), LEFT(2), RIGHT(3);
        private int value;
        Orientation(int value) {
            this.value = value;
        }
    }

    public enum critterState {
        MOVING(0), EATING(1), DRINKING(2), BREEDING(3), FIGHTING(4), RESTING(5);
        private int value;
        critterState(int value) {
            this.value = value;
        }
    }


    /**
     * The current state of the critter
     */
    private critterState state;

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
     * The critter's size effects its energy consumption when performing tasks
     * and the amount of damage it gives and the amount of damage it takes.
     */
    private final int size;

    /**
     * An integer ranging from 0 to 100 representing the critter's offensive power. Used for calculating
     * attack damage. A critter's offense must equal 100 - defense.
     */
    private int offense;

    /**
     * An integer ranging from 0 to 100 representing the critter's defensive power. Used for calculating
     * damage taken when attacked. A critter's defense must equal 100 - offense.
     */
    private int defense;

    /**
     * An integer ranging from 0 to 100 representing the critter's aggression level.
     * The aggression level is responsible for determining how likely the critter is to fight
     * or attack another critter
     */
    private final int aggression;

    /**
     * A non-negative integer representing the generation of this critter
     */
    private int generation;

    /**
     * The critter's current priority, either food, water, or love. Will decide to search for food,
     * water, or another mate based on current priority.
     */
    private Priority priority;

    /**
     * The critter's additional mutation rate, added on top of the world's base mutation rate
     */
    private int mutationRate;

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
            int offense,
            int defense,
            int aggression,
            int generation,
            int mutationRate
            ) {
        this.maxAge = maxAge;
        this.age = 0;
        this.maxHealth = maxHealth;
        this.hunger = 50;
        this.thirst = 50;
        this.health = 100;
        this.sex = sex;
        this.size = size;
        this.offense = offense;
        this.defense = defense;
        this.aggression = aggression;
        this.generation = generation;
        this.position = position;
        this.mutationRate = mutationRate;

        assertInv();
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
     * Return's this critter's position
     */
    public Point getPosition() {
        return position;
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
     * Returns this critter's maximum health
     */
    public int getMaxHealth() {
        return maxHealth;
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

    /**
     * Returns this critter's speed
     */
    int getOffense() {
        return offense;
    }

    /**
     * Returns this critter's power
     */
    int getDefense() {
        return defense;
    }

    /**
     * Returns this critter's aggression
     */
    int getAggression() {
        return aggression;
    }

    /**
     * Returns this critter's breeding length
     */
    int getGeneration() {
        return generation;
    }

    /**
     * Assert that class invariants are satisfied.
     */
    private void assertInv() {
        assert this.orientation == Orientation.UP || this.orientation == Orientation.DOWN;
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
