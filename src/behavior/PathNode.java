package behavior;

import java.awt.Point;

/**
 * A node representing a traversable square on the graph
 */
public class PathNode implements Comparable<PathNode> {
    /**
     * the xy coordinate of this node
     */
    private Point position;

    /**
     * the parent of this node. Used for reconstructing the path for the critter to follow
     */
    private PathNode parent;

    /**
     * Boolean representing whether this node has been discovered or not. Used in A* algorithm
     */
    private boolean discovered;

    /**
     * The cost of the path from the start node to this node
     */
    private double gCost;

    /**
     * The heuristic estimate of the cost of the cheapest path from this node to the target node
     */
    private double hCost;

    /**
     * The total cost going through this node (gn + hn)
     */
    private double fCost;

    /**
     * Constructs a new pathNode.
     * Takes in position, parent, gCost, and hCost parameters
     */
    public PathNode(Point position, PathNode parent, double gn, double hn) {
        this.position = position;
        this.parent = parent;
        this.gCost = gn;
        this.hCost = hn;
        this.fCost = gn + hn;
    }

    /**
     * getters for position, parent, and costs
     */
    public Point getPosition() { return position; }
    public PathNode getParent() { return parent; }
    public boolean discovered() { return discovered; }
    public double getGCost() { return gCost; }
    public double getHCost() { return hCost; }
    public double getFCost() { return fCost; }

    /**
     * setters for parent, discovered, and gCost
     */
    public void setParent(PathNode parent) { this.parent = parent; }
    public void setDiscovered(boolean discovered) { this.discovered = discovered; }
    public void setGCost(double gCost) {
        this.gCost = gCost;
        this.fCost = gCost + hCost;
    }


    @Override
    public int compareTo(PathNode other) {
        int cmp = Double.compare(this.fCost, other.fCost);
        if (cmp == 0) {
            return Double.compare(this.hCost, other.hCost); // if both have the same cost, prefer the node closer to the target
        }
        return cmp;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj instanceof PathNode other) {
            return this.position.equals(other.position);
        }
        return false;
    }

}
