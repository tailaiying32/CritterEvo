package brain;

import brain.Neuron.Layer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The brain for each critter. Controls high level decision-making and priority selection
 */
public class Brain {
    /**
     * Map of all neurons in this brain, with the keys as id's and the values as neurons
     */
    private Map<Integer, Neuron> neurons;

    /**
     * List of all synapses in this brain
     */
    private List<Synapse> synapses;

    /**
     * Constructs a new empty brain
     */
    public Brain() {
        this.neurons = new HashMap<>();
        this.synapses = new ArrayList<>();
    }

    /**
     * Returns the number of neurons in this brain
     */
    public int neuronCount() {
        return this.neurons.size();
    }

    /**
     * Returns the neuron with id "id"
     */
    public Neuron getNeuron(int id) {
        return neurons.get(id);
    }

    /**
     * Adds a neuron to this brain
     */
    public void addNeuron(Neuron neuron) {
        neurons.put(neuron.getId(), neuron);
    }

    /**
     * Removes a neuron associated with "id" from this brain
     */
    public void removeNeuron(int id) {
        this.neurons.remove(id);
    }

    /**
     * forward propagation through the network
     */
    public double[] feedForward(double[] input) {
        throw new UnsupportedOperationException("Not supported yet.");
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
    private List<Neuron> getNeuronsByLayer(Layer layer) {
        List<Neuron> layeredNeurons = new ArrayList<>();
        for (Neuron neuron : neurons.values()) {
            if (layer == neuron.getLayer()) {
                layeredNeurons.add(neuron);
            }
        }
        return layeredNeurons;
    }
}
