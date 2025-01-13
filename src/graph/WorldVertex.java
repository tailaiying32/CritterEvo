package graph;

import datastructures.Vertex;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import model.WorldModel;
import model.WorldModel.CellState;

public class WorldVertex implements Vertex<WorldEdge> {
    /**
     * The world model that this vertex is on
     */
    private WorldModel world;

    /**
     * The id of this vertex. Must be a positive integer
     */
    private int id;

    /**
     * the x-coordinate corresponding to this vertex
     */
    private int x;

    /**
     * the y-coordinate corresponding to this vertex
     */
    private int y;

    /**
     * The list of all outgoing edges from this vertex
     */
//    private ArrayList<WorldEdge> outgoingEdges;

    /**
     * Constructs a vertex corresponding a square on the world
     */
    public WorldVertex(WorldModel world, int x, int y) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.id = calculateId(x, y);
    }

    /**
     * Returns the world that this vertex is on
     */
    public WorldModel getWorld() {
        return world;
    }

    /**
     * Returns the vertex's x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the vertex's y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Calculates this vertex's id based on its x and y coordinates
     * Formula: x + y * world.getWidth()
     */
    private int calculateId(int x, int y) {
        assert x >= 0 && y >= 0;
        assert x < world.getWidth() && y < world.getHeight();
        return x + y * world.getWidth();
    }

    /**
     * Returns an iterable representing the world's edges
     */
    @Override
    public Iterable<WorldEdge> outgoingEdges() {
        return new Iterable<WorldEdge>() {
            @Override
            public Iterator<WorldEdge> iterator() {
                return new EdgeIterator();
            }
        };
    }

    /**
     * an Iterator for enumerating the valid outgoing edges for this WorldVertex
     */
    private class EdgeIterator implements Iterator<WorldEdge> {
        /**
         * the next edge to iterate through, or 8 if all have been yielded
         */
        private int nextDir;

        @Override
        public boolean hasNext() {
            return nextDir < 8;
        }

        @Override
        public WorldEdge next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            WorldEdge nextEdge = new WorldEdge(world, false, getId(), neighborId(nextDir), 1);
            nextDir += 1;
            findNextValidDir();
            return nextEdge;
        }

        /**
         * Advances "nextDir" until it falls on the next valid edge direction.
         * Advances to 8 when there are no more valid edge directions
         */
        private void findNextValidDir() {
            while (nextDir < 8 && !validDir(nextDir)) {
                nextDir += 1;
            }

        }
    }

    /**
     * Returns the id of the neighbor in "dir" direction.
     * Requires that the neighbor is within world bounds.
     * "dir" is in [0...7], where 0 represents N, and 2 represents E.
     */
    public int neighborId(int dir) {
        return switch (dir) {
            case 0 -> calculateId(x, y - 1);
            case 1 -> calculateId(x + 1, y - 1);
            case 2 -> calculateId(x + 1, y);
            case 3 -> calculateId(x + 1, y + 1);
            case 4 -> calculateId(x, y + 1);
            case 5 -> calculateId(x - 1, y + 1);
            case 6 -> calculateId(x - 1, y);
            case 7 -> calculateId(x - 1, y - 1);
            default -> throw new IllegalArgumentException("Invalid dir: " + dir);
        };
    }

    /**
     * Asserts that a potential neighbor in "dir" direction is within world bounds
     * "dir" is in [0...7], where 0 represents N, and 2 represents E.
     */
    public boolean validDir(int dir) {
        return switch (dir) {
            case 0 -> y > 0 && isValidNeighbor(x, y - 1);
            case 1 -> x < world.getWidth() && y > 0 && isValidNeighbor(x + 1, y - 1);
            case 2 -> x < world.getWidth() && isValidNeighbor(x + 1, y);
            case 3 -> x < world.getWidth() && y < world.getHeight() && isValidNeighbor(x + 1, y + 1);
            case 4 -> y < world.getHeight() && isValidNeighbor(x - 1, y + 1);
            case 5 -> x > 0 && y < world.getHeight() && isValidNeighbor(x - 1, y + 1);
            case 6 -> x > 0 && isValidNeighbor(x - 1, y);
            case 7 -> x > 0 && y > 0 && isValidNeighbor(x - 1, y - 1);
            default -> throw new IllegalArgumentException("Invalid dir: " + dir);
        };
    }

    /**
     * helper method for calculating whether the neighbor is a valid node or not
     * (Must be a GRASS square)
     */
    private boolean isValidNeighbor(int nx, int ny) {
        return world.getWorldArray()[nx][ny] == CellState.GRASS;
    }

    /**
     * Returns the vertex's x-y coordinates based off its id
     */
    public Point idToPoint(int id) {
        assert id >= 0 && id < world.getWidth() * world.getHeight();
        int y = id / world.getWidth();
        int x = id % world.getWidth();
        return new Point(x, y);
    }

    /**
     * Returns this vertex's id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets this vertex's id to "id"
     */
    public void setId(int id) {
        this.id = id;
    }

//    /**
//     * Returns the list of outgoing edges from this vertex
//     */
//    public ArrayList<WorldEdge> outgoingEdges() {
//        return outgoingEdges;
//    }

}
