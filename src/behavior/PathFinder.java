package behavior;


import java.awt.Point;
import java.util.List;
import model.WorldModel;

/**
 * Finds the shortest path from one point to another. Used in critter pathfinding
 */
public class PathFinder {
    /**
     * The world that this pathfinder operates on
     */
    private final WorldModel world;

    /**
     * Creates a new pathfinder
     */
    public PathFinder(WorldModel world) {
        this.world = world;
    }

    /**
     * Finds the optimal path from the start node to the final node using the A* search algorithm
     */
    public List<Point> findPath(Point start, Point target) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Returns a list of the coordinates valid neighbors (traversable squares) for a point
     */
    private List<Point> getNeighbors(Point p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Returns whether a given point is a traversable square
     * In other words, returns whether it is within world bounds and is a GRASS square
     */
    private boolean isValidPosition(Point p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * calculates the heuristic from the start position to the target position
     * Takes into account the Manhattan distance and the value of the target (for food)
     */
    private double calculateHeuristic(Point start, Point target) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Reconstructs the path using back pointer data stored in each pathNode
     */
    private List<Point> reconstructPath(PathNode target) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
