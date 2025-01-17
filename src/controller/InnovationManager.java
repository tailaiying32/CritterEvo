package controller;

/**
 * Helps keep track of genes with the NEAT algorithm. Each world is initiated with one global InnovationManager
 */
public class InnovationManager {
    /**
     * The current innovation number for this world, representing the number of connection genes that have been discovered
     */
    private int innovation;

    /**
     * Returns the global innovationNumber
     */
    public int innovation() {
        return innovation;
    }

    /**
     * increments the innovationNumber
     * Should be called during the creation of a new undiscovered synapse
     */
    public void incrementInnovation() {
        innovation++;
    }
}
