package graph;

import datastructures.Graph;
import graph.WorldVertex;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import model.WorldModel;
import model.WorldModel.CellState;

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
    private ArrayList<WorldVertex> vertices;

    /**
     * Constructs a new WorldGraph to provide a graph structure for "world"
     */
    public WorldGraph(WorldModel world) {
        this.world = world;
        this.vertices = new ArrayList<>();
        setVertices(world);
    }

    /**
     * Returns the number of vertices in this world
     */
    public int vertexCount() {
        return vertices.size();
    }

    /**
     * Returns the vertex with the given id "id"
     * If no vertex is found, return null
     */
    @Override
    public WorldVertex getVertex(int id) {
        for (WorldVertex v : vertices) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }

    /**
     * calculates the id for a vertex at (x, y)
     * x and y must be within world bounds
     */
    public int calculateId(int x, int y) {
        assert x >= 0 && x < world.getWidth() && y >= 0 && y < world.getHeight();
        return x + y * world.getWidth();
    }

    /**
     * Returns a representation of the vertex at coordinates (x, y)
     * Not the actual vertex, just a copy!!!
     */
    public WorldVertex vertexAt(int x, int y) {
        assert x >= 0 && x < world.getWidth() && y >= 0 && y < world.getHeight();
        return new WorldVertex(world, x, y);
    }

    /**
     * asserts that xy coordinates are within world bounds
     */
    private boolean validCoordinates(int x, int y) {
        return x >= 0 && x < world.getWidth() && y >= 0 && y < world.getHeight();
    }

    /**
     * Creates the vertices list based off the state of world
     */
    public void setVertices(WorldModel world) {
        // first populate the list of vertices
        CellState[][] worldArray = world.getWorldArray();
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                if (worldArray[x][y] == CellState.GRASS || worldArray[x][y] == null) {
                    WorldVertex vertex = new WorldVertex(world, x, y);
                    vertices.add(vertex);
                }
            }
        }

        // then, for each vertex, populate its edge list
        for (WorldVertex v : vertices) {
            System.out.println("populating edge list at " + v.getX() + ", " + v.getY());
            System.out.println("object at position " + v.getX() + ", " + v.getY() + worldArray[v.getX()][v.getY()]);
            v.setOutgoingEdges(calculateOutgoingEdges(v));
            System.out.println(v.outgoingEdges().size());
        }
    }

    /**
     * helper method for calculating outgoing edges from a vertex on this graph
     */
    private ArrayList<WorldEdge> calculateOutgoingEdges(WorldVertex vertex) {
        ArrayList<WorldEdge> edges = new ArrayList<>();
        assert vertex != null;
        int x = vertex.getX();
        int y = vertex.getY();
        assert x >= 0 && x < world.getWidth() && y >= 0 && y < world.getHeight();

        CellState[][] worldArray = vertex.getWorld().getWorldArray();
        List<Point> neighbors = world.squaresAround(new Point(x, y));

        for (Point p : neighbors) {
            int px = p.x;
            int py = p.y;
            if (validCoordinates(px, py)) {
                if (worldArray[px][py] == CellState.GRASS) {
                    WorldVertex neighbor = getVertex(vertexAt(px, py).getId());
                    WorldEdge outgoingEdge = new WorldEdge(vertex.getWorld(), false, vertex.getId(), neighbor.getId(), 1);
                    edges.add(outgoingEdge);
                }
            }
        }
        return edges;
    }

    /**
     * Updates the world graph
     */
    public void updateGraph() {
        vertices.clear();
        setVertices(world);
    }

}