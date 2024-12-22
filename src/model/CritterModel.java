package model;

/** Represents a model of the critters inhabiting the world
 *
 */
public class CritterModel {

    public enum Sex {
        MALE, FEMALE
    }

    enum Priority {
        FOOD, WATER, LOVE
    }

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
     * The critter's current priority, either food, water, or love. Will decide to search for food,
     * water, or another mate based on current priority.
     */
    private Priority priority;

    /**
     * Constructs a new Critter. Takes in maxAge, maxHealth, sex, size, and aggression parameter
     * age is set to zero, health is set to maxHealth
     */
    public CritterModel(int maxAge, int maxHealth, Sex sex, int size, int aggression) {
        this.maxAge = maxAge;
        this.age = 0;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.sex = sex;
        this.size = size;
        this.aggression = aggression;
        this.speed = (1 / size) * 100; // speed = 1 / size, scaled by 100 to keep the 1-100 scale
        this.power = (int) (Math.pow(size, 2)  / 100); // power = size^2, scaled by 100 to keep the 1-100 scale
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
