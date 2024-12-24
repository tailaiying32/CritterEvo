package model;

import java.awt.Point;

public class Water {

    /**
     * xy coordinate representing the location of this water block
     */
    private Point location;

    /**
     * An integer ranging from 1 to 4 representing how many critters are currently at this water
     * source
     */
    private int numCritters;
}
