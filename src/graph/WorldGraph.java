package graph;

import datastructures.Graph;
import graph.WorldVertex;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import model.WorldModel;

/**
 * A graph structure to represent a world model
 */
public class WorldGraph implements Graph<WorldVertex> {
    /**
     * The worldModel that this graph is representing
     */
    private WorldModel world;

    /**
     * A list of all vertices in the graph
     */
    private LinkedList<WorldVertex> vertices;

    /**
     * Constructs a new WorldGraph to provide a graph structure for "world"
     */
    public WorldGraph(WorldModel world) {
        this.world = world;
    }

    /**
     * Returns the number of vertices in this world
     */
    public int vertexCount() {
        return vertices.size();
    }

    /**
     * Returns the vertex with the given id "id"
     * If no vertex is found, throw NoSuchElementException
     */
    @Override
    public WorldVertex getVertex(int id) {
        for (WorldVertex v : vertices) {
            if (v.getId() == id) {
                return v;
            }
        }
        throw new NoSuchElementException("No such vertex: " + id);
    }
}