package behavior;


import datastructures.HeapMinQueue;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import model.Food;
import model.WorldModel;
import model.WorldModel.CellState;

/**
 * Finds the shortest path from one point to another. Used in critter pathfinding
 */
public class Pathfinder {
    /**
     * direct pathing threshold
     */
    private final static int DIRECT_PATHING_THRESHOLD = 5;

    /**
     * The world that this pathfinder operates on
     */
    private final WorldModel world;

    /**
     * A cache of paths to improve performance
     */
    private final Map<PathKey, List<Point>> pathCache = new ConcurrentHashMap<>();
    private final static int CACHE_SIZE = 1000;

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
    public List<Point> findPath(Point start, Point target) {
        // check cache first
        PathKey key = new PathKey(start, target);
        List<Point> cachedPath = pathCache.get(key);
        if (cachedPath != null) {
            return new ArrayList<>(cachedPath);
        }

        // initialize frontier, visited, and start node
        HeapMinQueue<PathNode> frontier = new HeapMinQueue<>();
        Set<Point> visited = new HashSet<>();
        PathNode startNode = new PathNode(start, null, 0, 0);
        double estimatedHeuristic = calculateHeuristic(start, target);
        frontier.addOrUpdate(startNode, estimatedHeuristic);

        // the search loop
        while (!frontier.isEmpty()) {
            PathNode current = frontier.remove();
            visited.add(current.getPosition());

            // if we are adjacent to our target, return the path
            if (isAdjacent(current.getPosition(), target)) {
                List<Point> path = reconstructPath(current);
                if (pathCache.size() < CACHE_SIZE) {
                    pathCache.put(key, path);
                } else {
                    System.out.println("cache full!");
                }
                return path;
            }

            // if not, iterate through neighbors and continue loop
            for (Point p: getNeighbors(current.getPosition())) {
                if (visited.contains(p)) continue;
                visited.add(p);

                PathNode neighbor = new PathNode(p, current, current.getFCost() + 1, calculateHeuristic(p, target));

                double dist = current.getFCost() + 1;
                if (!neighbor.discovered() || dist < neighbor.getGCost()) {
                    neighbor.setDiscovered(true);
                    double estimatedCost = calculateHeuristic(neighbor.getPosition(), target);
                    frontier.addOrUpdate(neighbor, dist + estimatedCost);
                }
            }
        }

        // return empty path if no path can be found
        return new ArrayList<>();
    }   

    /**
     * Returns whether a point is adjacent to another target point
     */
    private boolean isAdjacent(Point p1, Point p2) {
        return Math.abs(p1.x - p2.x) <= 1 &&
                Math.abs(p1.y - p2.y) <= 1 &&
                !(p1.x == p2.x && p1.y == p2.y);
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
     * Takes into account the Euclidean distance and the value of the target (for food)
     * h(n) = distance - food value
     */
    public double calculateHeuristic(Point start, Point target) {
        CellState[][] worldArray = world.getWorldArray();
        double euclidean = calculateDistance(start, target);

        if (worldArray[target.x][target.y] == CellState.FOOD) {
            Food food = world.getFood(new Point(target.x, target.y));
            return (euclidean) - food.getQuantity() * 0.08;
        } else {
            return (euclidean);
        }
    }

    /**
     * helper method for calculating Euclidean distance between two points
     */
    private double calculateDistance(Point p1, Point p2) {
        int dx = Math.abs(p1.x - p2.x);
        int dy = Math.abs(p1.y - p2.y);
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }


    /**
     * Reconstructs the path using back pointer data stored in each pathNode
     */
    private List<Point> reconstructPath(PathNode target) {
        List<Point> path = new ArrayList<>();
        PathNode current = target;

        while (current != null) {
            path.add(current.getPosition());
            current = current.getParent();
        }
        Collections.reverse(path);
        return path;
    }

    private record PathKey(Point start, Point target) {

        @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof PathKey(Point start1, Point target1))) {
                    return false;
                }
                return start.equals(start1) && target.equals(target1);
            }

    }
}


