package behavior;

import java.awt.Point;
import model.Critter;
import model.Critter.Orientation;
import model.Critter.Priority;

/**
 * Abstract class defining the behavior of the critter, and how it interacts with its environment
 * and other critters
 */
public abstract class InteractionManager {

    /**
     * Eat the food directly in front of the critter.
     * Replenish hunger equal to the quantity of food eaten.
     * Returns hunger level after eating.
     */
    int eat() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Drink the water directly in front of the critter.
     * Fully replenishes thirst.
     * Returns thirst level after drinking.
     */
    int drink() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Rotates the critter either left, right, or full 180 degrees.
     * Returns new orientation of critter after rotating.
     */
    Orientation rotate(int degrees) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Moves the critter in one of four directions: up, down, left, or right
     * Returns the new coordinates after moving
     */
    Point move() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Creates and returns a new critter based on its parents' attributes.
     * maxHealth, maxAge, size, breedLength, and aggression are taken as an average of its parents values
     * 50/50 chance for M/F
     * Moves either parent one to the left or right and spawns in between parents with random orientation
     */
    Critter reproduce(Critter father, Critter mother) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Uses CritterAi to decide priority and update critter's priority
     */
    Priority updatePriority() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Attacks the critter directly in front of itself.
     * Deals damage to other critter's health equivalent to its power.
     */
    void attack() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * The critter dies
     * Turns the current square critter is on into food with quantity proportional to its size / 2
     * Removes itself from list of live critters
     */
    void die() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
