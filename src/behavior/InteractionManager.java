package behavior;

import java.awt.Point;
import model.Critter;
import model.Critter.Orientation;
import model.Critter.Priority;
import model.Food;
import model.Water;

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
    int eat(Food food) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Drink the water directly in front of the critter.
     * Fully replenishes thirst.
     * Returns thirst level after drinking.
     */
    int drink(Water water) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Rotates the critter either left, right, or full 180 degrees.
     * Returns new orientation of critter after rotating.
     * Uses up a small amount of hunger proportional to its size.
     */
    Orientation rotate(int degrees) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Moves the critter forward in the direction it is facing
     * Uses up hunger proportional to its size
     * Takes in distance parameter: how many units the critter moves
     * Returns the new coordinates after moving
     */
    Point move(int distance) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Creates and returns a new critter based on its parents' attributes.
     * Uses up a large amount of hunger
     * maxHealth, maxAge, size, breedLength, and aggression, offense, and defense are taken as an average of its parents values
     * 50/50 chance for M/F
     * Moves either parent one to the left or right and spawns in between parents with random orientation
     */
    Critter reproduce(Critter father, Critter mother) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Uses CritterAI to decide priority and update critter's priority
     */
    Priority updatePriority(CritterAI ai) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Attacks the critter directly in front of itself.
     */
    void attack(Critter critter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Determines the outcome in the case that two critters fight over resources.
     */
    void fight(CritterAI ai, Critter critter1, Critter critter2) {
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
