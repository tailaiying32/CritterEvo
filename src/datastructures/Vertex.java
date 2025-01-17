package datastructures;

import java.util.ArrayList;
import java.util.LinkedList;

public interface Vertex<EdgeType extends Edge> {
    /**
     * Returns this vertex's ID in the graph
     */
    int getId();

    /**
     * Returns an iterable of all edges connecting this node
     * to another in the graph. This node serves as the "source" node for all outgoing edges
     */
     Iterable<EdgeType> outgoingEdges();
}