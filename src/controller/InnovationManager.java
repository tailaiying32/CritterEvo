package controller;

import brain.Synapse;
import java.util.HashMap;
import java.util.Map;

/**
 * Helps keep track of genes with the NEAT algorithm. Each world is initiated with one global InnovationManager
 */
public class InnovationManager {
    /**
     * A Map of all innovation numbers to synapses that have appeared so far
     */
    private Map<Integer, Synapse> discovered;

    /**
     * Constructs a new innovation manager
     */
    public InnovationManager() {
        this.discovered = new HashMap<>();
    }

    /**
     * Returns the global innovationNumber
     */
    public int innovation() {
        return discovered.size();
    }

    /**
     * Returns the synapse associated with this innovation number
     */
    public Synapse get(int innovation) {
        assert innovation <= discovered.size();
        return discovered.get(innovation);
    }

    /**
     * Adds a synapse to the discovered list
     */
    public void addSynapse(Synapse synapse) {
        assert synapse != null;
        if (discovered.containsValue(synapse)) {
            throw new IllegalArgumentException("Synapse already discovered!");
        }
        discovered.put(synapse.innovation(), synapse);
    }
}
