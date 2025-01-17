package datastructures;

/**
 * Represents an edge between two vertices in a graph.
 */
public interface Edge {
    /**
     * Returns the ID of this edge's source vertex
     */
    int startId() ;

    /**
     * Returns the ID of this edge's destination vertex
     */
    int endId();

    /**
     * Returns the weight of this edge
     */
    double weight();
}