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
     * Integer representing the layer of this neuron: 0 for input layer, -1 for output layer,
     * and enumerating from 1 up for hidden layers, left-to-right or input-to-output
     */
    private final int layer;

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
    public Neuron(int id, double activation, int layer) {
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
    public int getLayer() { return layer; }

    /**
     * Returns a list of incoming synapses for this neuron
     */
    public List<Synapse> incomingSynapses() {
        List<Synapse> synapses = new ArrayList<>();
        for (Synapse synapse : incomingSynapses) {
            if (synapse.isEnabled()) {
                synapses.add(synapse);
            }
        }
        return synapses;
    }

    /**
     * Returns a list of outgoing synapses for this neuron
     */
    public List<Synapse> outgoingSynapses() {
        List<Synapse> synapses = new ArrayList<>();
        for (Synapse synapse : outgoingSynapses) {
            if (synapse.isEnabled()) {
                synapses.add(synapse);
            }
        }
        return synapses;
    }

    /**
     * Returns the synapse with innovation "innovation"
     */
    public Synapse getSynapse(int innovation) {
        return outgoingSynapses.get(innovation);
    }

    /**
     * Adds an incoming synapse to this neuron
     */
    public void addIncomingSynapse(Synapse synapse) {
        incomingSynapses.add(synapse);
    }

    /**
     * Adds an outgoing synapse to this neuron
     */
    public void addOutgoingSynapse(Synapse synapse) {
        outgoingSynapses.add(synapse);
    }

    /**
     * getters and setters for the neuron's activation
     */
    public double activation() { return activation; }
    public void setActivation(double activation) { this.activation = activation; }

    /**
     * Processes incoming data from synapses to create an output
     */
    public double processNeuron() {
        double activation = 0;
        for (Synapse synapse : incomingSynapses) {
            if (synapse.isEnabled()) {
                Neuron start = synapse.start();
                double weightedActivation = start.activation() * synapse.weight();
                activation += weightedActivation;
            }
        }
        double normalizedActivation = reLU(activation);
        setActivation(normalizedActivation);
        return normalizedActivation;
    }

    /**
     * The ReLU function, used for normalizing activations to [0.0, 1.0]
     */
    private double reLU(double activation) {
        return Math.max(0, activation);
    }
}
