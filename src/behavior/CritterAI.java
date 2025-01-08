package behavior;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import model.Critter;
import model.Critter.Orientation;
import model.Critter.Priority;
import model.Food;
import model.WorldModel;

/**
 * Defines what the critter's next priority is i.e. food, water, or love
 * Defines behavior when critter goes into "fight" state based off game theory principles
 * Defines what the critter does next
 */
public class CritterAI {

    /**
     * Constructor for ai
     */
    public CritterAI() {}

    /**
     * Calculates priority based off of critter's current state and attributes
     * Returns the calculated priority
     */
    public Priority updatePriority(Critter critter) {
//        if (critter.getHunger() >= 80 && critter.getThirst() >= 80 && critter.getHealth() >= 80) {
//            critter.setPriority(Priority.LOVE);
//        } else {
//            if (critter.getHunger() >= critter.getThirst()) {
//                critter.setPriority(Priority.FOOD);
//            } else {
//                critter.setPriority(Priority.WATER);
//            }
//        }
//
//        return critter.getPriority();
        critter.setPriority(Priority.FOOD);
        return critter.getPriority();
    }

    /**
     * Has the critter take turn based on its priority and state
     */
    public void makeMove(Critter critter) {
        Point target = critter.locateTarget();
        Priority priority = critter.getPriority();
        WorldModel world = critter.getWorld();
        Orientation properOrientation = critter.determineOrientation();

        critter.rotate(properOrientation);
        // what to do if the critter is already at its target
        if (squareInFront(critter) == target) {
            if (priority == Priority.FOOD) {
                critter.eat(world.getFood(squareInFront(critter)));
            } else if (priority == Priority.WATER) {
                critter.drink(world.getWater(squareInFront(critter)));
            }
        } else  { // what to do if the critter is not already at its target
            critter.move(1);
        }
    }

    /**
     * locates the nearest instance of the critter's target
     * !!! Eventually change to A* algorithm, but I will figure that out later
     */
    public Point locateTarget(Critter critter, Priority priority) {
        WorldModel world = critter.getWorld();
        double distance = Double.MAX_VALUE; // the farthest traversable distance in the world
        Point nearestTarget = critter.getPosition();
        System.out.println("critter's position: " + critter.getPosition());
        System.out.println("nearestTarget: " + nearestTarget);

        // Get the appropriate target set based on priority
        Map<Point, ?> targets = switch (priority) {
            case FOOD -> world.getFoods();
            case WATER -> world.getWaters();
            case LOVE -> world.getCritters();
            default -> new HashMap<>(); // Empty set for unsupported priorities
        };


        for (Point target : targets.keySet()) {
            double targetDistance = calculateDistance(target, critter.getPosition());
            if (targetDistance < distance) {
                distance = targetDistance;
                nearestTarget = target;
            }
        }

        if (nearestTarget == null) {
            System.out.println("No valid target found for priority: " + priority);
            return critter.getPosition(); // Fallback to current position or other logic
        }

        return nearestTarget;
    }

    /**
     * calculates the direction (orientation) in which the critter needs to move
     */
    public Orientation determineOrientation(Critter critter) {
        Point nearestTarget = critter.locateTarget();

        int dx = nearestTarget.x - critter.getPosition().x;
        int dy = nearestTarget.y - critter.getPosition().y;

        if (dx > 0 && dy > 0) {
            return Orientation.SE;
        } else if (dx > 0 && dy < 0) {
            return Orientation.NE;
        } else if (dx < 0 && dy > 0) {
            return Orientation.SW;
        } else if (dx < 0 && dy < 0) {
            return Orientation.NW;
        } else if (dx == 0 && dy < 0) {
            return Orientation.N;
        } else if (dx == 0 && dy > 0) {
            return Orientation.S;
        } else if (dx > 0 && dy == 0) {
            return Orientation.E;
        } else if (dx < 0 && dy == 0 ) {
            return Orientation.W;
        } else {
            return critter.getOrientation();
        }
    }

    /**
     * private helper method for calculating the Manhattan distance between two points
     */
    private int calculateDistance(Point p1, Point p2) {
        return Math.abs((p1.x - p2.x)) + Math.abs((p1.y - p2.y));
    }

    /**
     * private helper method for determining the coordinates of the square in front of the critter
     */
    private Point squareInFront(Critter critter) {
        Orientation orientation = critter.getOrientation();
        Point position = critter.getPosition();
        Point squareInFront = null;

        switch (orientation) {
            case N: squareInFront = new Point(position.x, position.y - 1); break;
            case NE: squareInFront = new Point(position.x + 1, position.y - 1); break;
            case E: squareInFront = new Point(position.x + 1, position.y); break;
            case SE: squareInFront = new Point(position.x + 1, position.y + 1); break;
            case S: squareInFront = new Point(position.x, position.y + 1); break;
            case SW: squareInFront = new Point(position.x - 1, position.y + 1); break;
            case W: squareInFront = new Point(position.x - 1, position.y); break;
            case NW: squareInFront = new Point(position.x - 1, position.y - 1); break;
        }

        return squareInFront;
    }
}
