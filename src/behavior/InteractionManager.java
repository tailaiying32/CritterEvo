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
public class InteractionManager extends Critter{

    /**
     * Constructs a new Critter. Takes in maxAge, maxHealth, sex, size, and aggression parameter age is
     * set to zero, health is set to maxHealth
     */
    public InteractionManager(CritterAI ai, Point position, Orientation orientation, int maxAge,
            int maxHealth, Sex sex, int size, int offense, int defense, int aggression,
            int generation,
            int mutationRate) {
        super(ai, position, orientation, maxAge, maxHealth, sex, size, offense, defense, aggression,
                generation, mutationRate);
    }

    /**
     * Eat the food directly in front of the critter.
     * Replenish hunger equal to the food's quantity attribute.
     * Returns hunger level after eating.
     */
    int eat(Food food) {
        throw new UnsupportedOperationException("Not supported yet.");
        //TODO
    }

    /**
     * Drink the water directly in front of the critter.
     * Fully replenishes thirst.
     * Returns thirst level after drinking.
     */
    int drink(Water water) {
        throw new UnsupportedOperationException("Not supported yet.");
        //TODO
    }

    /**
     * Rotates the critter to a new orientation.
     * Returns new orientation of critter after rotating.
     * Uses up a small amount of hunger proportional to its size.
     */
    Orientation rotate(Orientation orientation) {
        throw new UnsupportedOperationException("Not supported yet.");
        //TODO
    }

    /**
     * Moves the critter forward in the direction it is facing
     * Uses up hunger proportional to its size
     * Takes in distance parameter: how many units the critter moves
     * Returns the new coordinates after moving (remember that x and y start at 0 and at the top left)
     */
    Point move(int distance) {
        throw new UnsupportedOperationException("Not supported yet.");
        //TODO
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
     * Uses the critter's critterAI calculatePriority() to decide priority and update critter's priority
     */
    Priority updatePriority(CritterAI ai) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Attacks the critter directly in front of itself.
     * Takes away health from other critter following this equation: D(S1O1/S2D2)^b,
     * where D and b are baseDamage and damageScalingFactor of the world the critter inhabits
     */
    void attack(Critter critter) {
        throw new UnsupportedOperationException("Not supported yet.");
        //TODO
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
