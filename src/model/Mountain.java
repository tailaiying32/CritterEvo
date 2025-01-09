package model;

import java.awt.Point;

public class Mountain {
    /**
     * xy coordinate representing the location of this water block
     */
    private Point position;

    /**
     * constructor for mountain
     */
    public Mountain(Point position) {
        this.position = position;
    }

    /**
     * gets the mountain's position
     */
    public Point getPosition() {
        return position;
    }
}
