package brain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A representation of a neuron within the critter's brain. Takes in the weighted sum of all incoming
 * neurons and spits out a corresponding output (activation)
 */
public class Neuron {
    /**
     * The id of this neuron. Must be a positive integer
     */
    private final int id;

    /**
     * enum for the type of neuron
     */
    public enum Layer {
        INPUT, OUTPUT, HIDDEN
    }

    /**
     * The type/layer of this neuron - input, output, or hidden
     */
    private final Layer layer;

    /**
     * The activation for this neuron. Must be in between 0.0 and 1.0, inclusive
     */
    private double activation;

    /**
     * All incoming and outgoing synapses from this neuron,
     * where keys are innovation numbers and values are the corresponding synapses
     */
    private List<Synapse> incomingSynapses;
    private List<Synapse> outgoingSynapses;

    /**
     * Constructs a new neuron
     */
    public Neuron(int id, int activation, Layer layer) {
        this.id = id;
        this.activation = activation;
        this.layer = layer;
        this.incomingSynapses = new ArrayList<>();
        this.outgoingSynapses = new ArrayList<>();
    }

    /**
     * Returns this neuron's id
     */
    public int getId() { return id; }

    /**
     * Returns this neuron's layer
     */
    public Layer getLayer() { return layer; }

    /**
     * Returns a list of incoming/outgoing synapses for this neuron
     */
    public List<Synapse> incomingSynapses() { return incomingSynapses; }
    public List<Synapse> outgoingSynapses() { return outgoingSynapses; }

    /**
     * Returns the synapse with innovation "innovation"
     */
    public Synapse getSynapse(int innovation) {
        return outgoingSynapses.get(innovation);
    }

    /**
     * Adds an outgoing synapse to this neuron
     */
    public void addSynapse(Synapse synapse) {
        outgoingSynapses.add(synapse);
    }

    /**
     * Processes incoming data from synapses to create an output
     */
    public double processNeuron() {
        throw new UnsupportedOperationException();
    }

}
