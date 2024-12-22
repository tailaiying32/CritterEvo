package graph;

/**
 * Represents an edge between two vertices in a graph.
 * Does not provide weights (see weigher interface)
 */
public interface Edge {

    /**
     * Returns the ID of this edge's source vertex
     */
    int startId();

    /**
     * Returns the ID of this edge's destination vertex
     */
    int endId();
}