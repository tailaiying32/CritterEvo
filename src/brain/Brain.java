package brain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import model.Critter;

/**
 * The brain for each critter, represented by a neural network. Controls high level decision-making and priority selection.
 */
public class Brain {
    /**
     * The critter that this brain belongs to
     */
    private final Critter critter;

    /**
     * Map of all neurons in this brain, with the keys as id's and the values as neurons
     */
    private Map<Integer, Neuron> neurons;

    /**
     * The number of hidden layers in this network
     */
    private int hiddenLayers;

    /**
     * Constructs a new empty brain belonging to Critter "critter"
     */
    public Brain(Critter critter) {
        this.critter = critter;
        this.neurons = new HashMap<>();
        this.hiddenLayers = 0;
    }

    /**
     * constructs a new empty brain with no owner for testing purposes
     */
    public Brain() {
        this.neurons = new HashMap<>();
        this.hiddenLayers = 0;
        this.critter = null;
    }

    /**
     * Returns the critter this brain belongs to
     */
    public Critter critter() { return critter; }

    /**
     * getters and setters for the number of hidden layers in this network
     */
    public int hiddenLayers() { return hiddenLayers; }
    public void setHiddenLayers(int num) { hiddenLayers = num; }

    /**
     * Returns the number of neurons in this brain
     */
    public int neuronCount() { return this.neurons.size(); }

    /**
     * Returns the neuron with id "id"
     */
    public Neuron getNeuron(int id) { return neurons.get(id); }

    /**
     * Adds a neuron to this brain
     */
    public void addNeuron(Neuron neuron) {
        neurons.put(neuron.getId(), neuron);
        if (neuron.getLayer() > hiddenLayers) {
            hiddenLayers = neuron.getLayer();
        }
    }

    /**
     * Removes a neuron associated with "id" from this brain
     */
    public void removeNeuron(int id) { neurons.remove(id); }

    /**
     * forward propagation through the network
     */
    public double[] feedForward(double[] input) {

        List<Neuron> inputNeurons = getNeuronsByLayer(0);
        List<Neuron> outputNeurons = getNeuronsByLayer(-1);
        double[] output = new double[outputNeurons.size()];


        // validate input size
        if (input.length != inputNeurons.size()) {
            throw new IllegalArgumentException("Input array length is not equal to neuron count. Input length: " + input.length + " Neuron count: " + inputNeurons.size());
        }

        // set input values
        for (int i = 0; i < inputNeurons.size(); i++) {
            inputNeurons.get(i).setActivation(input[i]);
        }

        // process hidden layers
        for (int i = 1; i <= hiddenLayers; i++) {
            List<Neuron> hiddenNeurons = getNeuronsByLayer(i);
            for (Neuron hiddenNeuron : hiddenNeurons) {
                hiddenNeuron.processNeuron();
            }
        }

        // process output layer
        for (int i = 0; i < outputNeurons.size(); i++) {
            output[i] = outputNeurons.get(i).processNeuron();
        }

        // return the output array
        return output;
    }

    /**
     * Mutates the network
     */
    public void mutate() {
        // needs to support adding/removing a hidden neuron, adding/removing a synapse, and changing the weight of a synapse

        // add a neuron (must be added on top of an already existing synapse)
        assert critter != null;
        int innovation = critter.getWorld().innovationManager().innovation();


    }

    /**
     * Mutation: adds a neuron
     * Creates a new neuron, disables the original synapse, and inserts the neuron in between where the old synapse was
     * Create two new synapses connecting the neuron to the original synapse's endpoints
     * Parameters: takes in the innovation number of the synapse to disable
     */
    public void addNeuronMutation(int innovation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Mutation: removes a neuron
     * Removes a neuron and the two outgoing synapses, and replaces it with one larger synapse
     * Parameters: takes in the id of the neuron to remove
     */
    public void removeNeuronMutation(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Mutation: adds a synapse connecting two random neurons
     * inv: neurons must be in different layers
     * Parameters: takes in the ids of the two endpoint neurons the synapse will be attached to
     */
    public void addSynapseMutation(int id1, int id2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Mutation: removes a random synapse
     * Parameters: takes in the innovation number of the to be disabled synapse
     */
    public void removeSynapseMutation(int innovation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Mutation: changes the weight of a synapse by up to 20%
     * Parameters: takes in the innovation number of the to be disabled synapse
     */
    public void changeWeightMutation(int innovation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * helper method to get neurons by layer
     */
    public List<Neuron> getNeuronsByLayer(int layer) {
        List<Neuron> layeredNeurons = new ArrayList<>();
        for (Neuron neuron : neurons.values()) {
            if (layer == neuron.getLayer()) {
                layeredNeurons.add(neuron);
            }
        }
        return layeredNeurons;
    }

    /**
     * Returns the id of the neuron with the largest id, for neuron creation
     */
    public int getDiscoveredNeuronCount() {
        int count = 0;
        for (Neuron neuron : neurons.values()) {
            if (neuron.getId() > count) {
                count = neuron.getId();
            }
        }
        return count;
    }
}
