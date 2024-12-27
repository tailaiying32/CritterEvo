package model;

import java.awt.Point;

public class Food {

    /**
     * xy coordinates representing the location of this food
     */
    private Point position;

    /**
     * An integer value ranging from 5 to 40 representing how much food is on this square
     */
    private int quantity;

    /**
     * An integer value ranging from 0 to 4 representing how many critters are currently
     * at this food source
     */
    private int numCritters;

    /**
     * Constructor for a square of food
     */
    public Food(Point position, int quantity, int numCritters) {
        this.position = position;
        this.quantity = quantity;
        this.numCritters = 0; // the food should not spawn within 1 square of an existing critter
    }

    /**
     * Returns the location of this food
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Returns the quantity of this food
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Removes "consumed" amount of food from this square's quantity.
     * "consumed" must be a non-negative integer less than or equal to
     * Returns the new quantity of food leftover
     */
    public int consume(int consumed) {
        quantity -= consumed;
        return quantity;
    }
}
