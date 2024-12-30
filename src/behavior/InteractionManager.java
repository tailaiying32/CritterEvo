package behavior;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import model.Critter;
import model.Critter.Orientation;
import model.Critter.Priority;
import model.Food;
import model.Water;

/**
 * class defining the behavior of the critter, and how it interacts with its environment
 * and other critters
 */
public class InteractionManager {

    // energy cost constants
    private static final double MOVE_COST_FACTOR = 0.002;
    private static final double ROTATE_COST_FACTOR = 0.0004;

    /**
     * constructor for Interaction Manager
     */
    public InteractionManager() {}

    /**
     * Eat the food directly in front of the critter.
     * Replenish hunger equal to the food's quantity attribute.
     * Returns hunger level after eating.
     */
    public int eat(Critter critter, Food food) {
        int newHunger = Math.min(
                (critter.getHunger() + food.getQuantity()),
                critter.getMaxHunger()
        );
        critter.setHunger(newHunger);
        return critter.getHunger();
    }

    /**
     * Drink the water directly in front of the critter.
     * Fully replenishes thirst.
     * Returns thirst level after drinking.
     */
    public int drink(Critter critter, Water water) {
        int newThirst = critter.getMaxThirst();
        critter.setThirst(newThirst);
        return critter.getThirst();
    }

    /**
     * Rotates the critter to a new orientation.
     * Returns new orientation of critter after rotating.
     * Uses up a small amount of hunger proportional to its size.
     */
    public Orientation rotate(Critter critter, Orientation orientation) {
        // calculate how many unit rotations are needed for the critter to arrive at the new orientation
        int before = critter.getOrientation().getValue();
        int after = orientation.getValue();
        int difference = (after - before) % 4;
        int factor = 0;

        if (difference == 0 && before != after) {
            factor = 4;
        } else {
            factor = difference;
        }

        critter.setOrientation(orientation);
        int hungerUsed = factor * (int) (ROTATE_COST_FACTOR * Math.pow(critter.getSize(), 2));
        critter.setHunger(Math.max(
                critter.getHunger() - hungerUsed,
                0
        ));
        return critter.getOrientation();
    }

    /**
     * Moves the critter forward in the direction it is facing
     * Uses up hunger proportional to its size
     * Takes in distance parameter: how many units the critter moves
     * Returns the new coordinates after moving (remember that x and y start at 0 and at the top left)
     */
    public Point move(Critter critter, int distance) {
        int x = (int) critter.getPosition().getX();
        int y = (int) critter.getPosition().getY();
        if (critter.getOrientation().equals(Orientation.N)) {
            critter.setPosition(new Point(x, y - distance));
        } else if (critter.getOrientation().equals(Orientation.NE)){
            critter.setPosition(new Point(x + distance, y - distance));
        } else if (critter.getOrientation().equals(Orientation.E)) {
            critter.setPosition(new Point(x + distance, y));
        } else if (critter.getOrientation().equals(Orientation.SE)) {
            critter.setPosition(new Point(x + distance, y + distance));
        } else if (critter.getOrientation().equals(Orientation.S)) {
            critter.setPosition(new Point(x, y + distance));
        } else if (critter.getOrientation().equals(Orientation.SW)) {
            critter.setPosition(new Point(x - distance, y + distance));
        } else if (critter.getOrientation().equals(Orientation.W)) {
            critter.setPosition(new Point(x - distance, y));
        } else if (critter.getOrientation().equals(Orientation.NW)) {
            critter.setPosition(new Point(x - distance, y - distance));
        }

        int hungerUsed = distance * (int) (MOVE_COST_FACTOR * Math.pow(critter.getSize(), 2));
        critter.setHunger(Math.max(
                critter.getHunger() - hungerUsed,
                0
        ));
        return critter.getPosition();
    }

    /**
     * Creates and returns a new critter based on its parents' attributes.
     * Uses up a large amount of hunger
     * maxHealth, maxAge, size, breedLength, and aggression, offense, and defense are taken as an average of its parents values
     * 50/50 chance for M/F
     * Moves either parent one to the left or right and spawns in between parents with random orientation
     */
    public Critter reproduce(Critter father, Critter mother) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Uses the critter's critterAI calculatePriority() to decide priority and update critter's priority
     * Returns the new priority
     */
    public Priority updatePriority(Critter critter) {
        Priority newPriority = critter.getAi().calculatePriority();
        critter.setPriority(newPriority);
        return critter.getPriority();
    }

    /**
     * Attacks the critter directly in front of itself.
     * Takes away health from other critter following this equation: D(S1O1/S2D2)^b,
     * where D and b are baseDamage and damageScalingFactor of the world the critter inhabits
     */
    public void attack(Critter critter1, Critter critter2) {
        throw new UnsupportedOperationException("Not supported yet.");
        //TODO
    }

    /**
     * Determines the outcome in the case that two critters fight over resources.
     */
    public void fight(CritterAI ai, Critter critter1, Critter critter2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * The critter dies
     * Turns the current square critter is on into food with quantity proportional to its size / 2
     * Removes itself from list of live critters
     */
    public void die(Critter critter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
