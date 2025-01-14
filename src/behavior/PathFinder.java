package behavior;


import datastructures.HeapMinQueue;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.Food;
import model.WorldModel;
import model.WorldModel.CellState;

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
     * Returns an empty list if the start or target node is not a valid node
     */
    public List<PathNode> findPath(Point start, Point target) {
        // if start or target is invalid, return empty set of instructions
        if (!isValidPosition(start) || !isValidPosition(target)) {
            return new ArrayList<>();
        }

        // if not, initialize the frontier and add the start node
        HeapMinQueue<PathNode> frontier = new HeapMinQueue<>();
        PathNode startNode = new PathNode(start, null, 0, 0);
        double estimatedHeuristic = calculateHeuristic(start, target);
        frontier.addOrUpdate(startNode, estimatedHeuristic);

        // start the search
        while (!frontier.isEmpty()) {
            PathNode current = frontier.remove();

            // if current node is the target, break the loop
            if (current.getPosition().equals(target)) {
                return reconstructPath(current);
            }

            // otherwise, check neighbors
            for (Point p: getNeighbors(current.getPosition())) {
                PathNode neighbor = new PathNode(p, current, current.getFCost() + 1, calculateHeuristic(p, target));
                double dist = current.getFCost() + 1;
                if (!neighbor.discovered() || dist < neighbor.getGCost()) {
                    neighbor.setDiscovered(true);
                    double estimatedCost = calculateHeuristic(neighbor.getPosition(), target);
                    frontier.addOrUpdate(neighbor, dist + estimatedCost);
                }
            }
        }

        // if no path is found, return an empty list
        return new ArrayList<>();
    }

    /**
     * Returns a list of the coordinates valid neighbors (traversable squares) for a point
     */
    public List<Point> getNeighbors(Point p) {
        List<Point> neighbors = world.squaresAround(p);


        List<Point> validNeighbors = new ArrayList<>();
        for (Point neighbor : neighbors) {
            if (isValidPosition(neighbor)) {
                validNeighbors.add(neighbor);
            }
        }
        return validNeighbors;
    }

    /**
     * Returns whether a given point is a traversable square
     * In other words, returns whether it is within world bounds and is a GRASS or FOOD square
     */
    public boolean isValidPosition(Point p) {
        return p.x >= 0 && p.x < world.getWidth() &&
                p.y >= 0 && p.y < world.getHeight() &&
                (world.getWorldArray()[p.x][p.y] == CellState.GRASS ||
                        world.getWorldArray()[p.x][p.y] == CellState.FOOD ||
                        world.getWorldArray()[p.x][p.y] == null);
    }

    /**
     * calculates the heuristic from the start position to the target position
     * Takes into account the Manhattan distance and the value of the target (for food)
     * h(n) = distance - food value
     */
    public double calculateHeuristic(Point start, Point target) {
        CellState[][] worldArray = world.getWorldArray();
        int dx = Math.abs(target.x - start.x);
        int dy = Math.abs(target.y - start.y);

        if (worldArray[target.x][target.y] == CellState.FOOD) {
            Food food = world.getFood(new Point(target.x, target.y));
            return dx + dy - food.getQuantity();
        } else {
            return dx + dy;
        }
    }

    /**
     * Reconstructs the path using back pointer data stored in each pathNode
     */
    private List<PathNode> reconstructPath(PathNode target) {
        List<PathNode> path = new ArrayList<>();
        PathNode current = target;
        while (current.getParent() != null) {
            path.add(current);
            current = current.getParent();
        }
        Collections.reverse(path);
        return path;
    }
}
