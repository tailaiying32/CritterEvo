package graph;

import datastructures.Edge;
import model.WorldModel;

public class WorldEdge implements Edge {
    /**
     * The world that this edge is part of
     */
    private WorldModel world;

    /**
     * Boolean representing whether this edge is directed or not
     */
    private boolean isDirected;

    /**
     * the id of the source vertex
     */
    private int startId;

    /**
     * The id of the end vertex
     */
    private int endId;

    /**
     * The weight of this edge
     */
    private double weight;

    /**
     * constructs an edge between two squares on the world
     */
    public WorldEdge (WorldModel world, boolean isDirected, int startId, int endId, double weight) {
        this.world = world;
        this.isDirected = isDirected;
        this.startId = startId;
        this.endId = endId;
        this.weight = weight;
    }

    /**
     * Returns whether this edge is directed or not
     */
    public boolean isDirected() {
        return isDirected;
    }

    /**
     * Returns the id of the source vertex
     */
    public int startId() {
        return startId;
    }

    /**
     * Returns the id of the end vertex
     */
    public int endId() {
        return endId;
    }

    /**
     * Returns the weight of this edge
     */
    public double weight() {
        return weight;
    }
}
