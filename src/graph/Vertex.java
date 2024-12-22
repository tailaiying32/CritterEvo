package graph;

import java.util.LinkedList;

public interface Vertex {

    /**
     * Returns this vertex's ID in the graph
     */
    int id();

    /**
     * Returns a linked list of all edges connecting this vertex
     * to another in the graph
     */
    LinkedList<Edge> edges();
}