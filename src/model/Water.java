package model;

import java.awt.Point;

public class Water {

    /**
     * xy coordinate representing the location of this water block
     */
    private Point position;

    /**
     * An integer ranging from 1 to 4 representing how many critters are currently at this water
     * source
     */
    private int numCritters;

    /**
     * Constructs a square of water
     */
    public Water(Point position, int numCritters) {
        this.position = position;
        this.numCritters = 0;
    }

    /**
     * returns this water's position
     */
    public Point getPosition() {
        return position;
    }
}
