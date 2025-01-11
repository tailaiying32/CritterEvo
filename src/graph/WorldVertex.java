package graph;

import datastructures.Vertex;
import java.awt.Point;
import java.util.LinkedList;
import model.WorldModel;

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
    private LinkedList<WorldEdge> outgoingEdges;

    /**
     * Constructs a vertex corresponding a square on the world
     */
    public WorldVertex(WorldModel world, int x, int y) {
        this.world = world;
        this.x = x;
        this.y = y;
        setId((calculateId(x, y)));
    }

    /**
     * Calculates this vertex's id based on its x and y coordinates
     */
    private int calculateId(int x, int y) {
        assert x >= 0 && y >= 0;
        assert x < world.getWidth() && y < world.getHeight();
        return x + y * world.getWidth();
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

    /**
     * Returns the list of outgoing edges from this vertex
     */
    public LinkedList<WorldEdge> outgoingEdges() {
        return outgoingEdges;
    }

}
