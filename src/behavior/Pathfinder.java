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
public class Pathfinder {
    /**
     * The world that this pathfinder operates on
     */
    private final WorldModel world;

    /**
     * Creates a new pathfinder
     */
    public Pathfinder(WorldModel world) {
        this.world = world;
    }

    /**
     * Finds the optimal path from the start node to the final node using the A* search algorithm
     * Returns an empty list if the start or target node is not a valid node
     */
    public List<PathNode> findPath(Point start, Point target) {
        System.out.println("Starting pathfinding from " + start + " to " + target);

        if (!isValidPosition(start) || !isValidPosition(target)) {
            System.out.println("Invalid start or target position");
            return new ArrayList<>();
        }

        HeapMinQueue<PathNode> frontier = new HeapMinQueue<>();
        PathNode startNode = new PathNode(start, null, 0, 0);
        double estimatedHeuristic = calculateHeuristic(start, target);
        frontier.addOrUpdate(startNode, estimatedHeuristic);

        int iterations = 0;
        while (!frontier.isEmpty() && iterations < world.getWidth() * world.getHeight()) { // Add iteration limit
            iterations++;
            PathNode current = frontier.remove();

            if (iterations % 100 == 0) {
                System.out.println("Iteration " + iterations + ", exploring: " + current.getPosition());
            }

            if (current.getPosition().equals(target)) {
                System.out.println("Path found in " + iterations + " iterations");
                return reconstructPath(current);
            }

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

        System.out.println("No path found after " + iterations + " iterations");
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

    public boolean isValidPosition(Point p) {
        // Check if this is a valid destination point
        return p.x >= 0 && p.x < world.getWidth() &&
                p.y >= 0 && p.y < world.getHeight() &&
                world.getWorldArray()[p.x][p.y] != CellState.MOUNTAIN &&
                world.getWorldArray()[p.x][p.y] != CellState.WATER &&
                world.getWorldArray()[p.x][p.y] != CellState.PEACEFUL_CRITTER &&
                world.getWorldArray()[p.x][p.y] != CellState.ANGRY_CRITTER;
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

        while (current != null) {
            path.add(current);
            current = current.getParent();
        }
        Collections.reverse(path);
        return path;
    }
}
