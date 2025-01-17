package brain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The brain for each critter, represented by a neural network. Controls high level decision-making and priority selection.
 */
public class Brain {
    /**
     * Map of all neurons in this brain, with the keys as id's and the values as neurons
     */
    private Map<Integer, Neuron> neurons;

    /**
     * List of all synapses in this brain
     */
//    private List<Synapse> synapses;

    /**
     * The number of hidden layers in this network
     */
    private int hiddenLayers;

    /**
     * Constructs a new empty brain
     */
    public Brain() {
        this.neurons = new HashMap<>();
//        this.synapses = new ArrayList<>();
        this.hiddenLayers = 0;
    }

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
            throw new IllegalArgumentException("Input array length is not equal to neuron count");
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
}
