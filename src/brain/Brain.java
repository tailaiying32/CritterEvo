package brain;

import controller.InnovationManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import model.Critter;

// testing git


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
     * Map of all synapses in this brain, with the keys as innovation and the values as synapses
     */
    private Map<Integer, Synapse> synapses;
    public Map<Integer, Synapse> getSynapses() { return synapses; }
    public void addSynapse(Synapse synapse) { synapses.put(synapse.innovation(), synapse); }

    /**
     * The number of hidden layers in this network
     */
    private int hiddenLayers;

    /**
     * Constructs a new empty brain belonging to Critter "critter"
     * critter cannot be empty
     */
    public Brain(Critter critter) {
        this.critter = critter;
        this.neurons = new HashMap<>();
        this.synapses = new HashMap<>();
        this.hiddenLayers = 0;

        assertInv();
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
        double chance = critter.getWorld().getMutationRate() + critter.getMutationRate();

        // 1. WEIGHT MUTATION: change the weights of the synapses - each synapse's rate of mutation is based off the world's mutation rate plus the critter's mutation rate
        for (Synapse synapse : synapses.values()) {
            double weight = synapse.weight();
            if (chance > Math.random()) {
                double change = Math.random()/5;
                if (Math.random() > 0.5) {
                    synapse.setWeight(Math.min(1.0, weight + change));
                } else {
                    synapse.setWeight(Math.max(0.0, weight - change));
                }
            }
        }

        // 2. ADD SYNAPSE MUTATION: add a synapse
        if (chance > Math.random()) {
            // get all neurons in brain
            List<Neuron> allNeurons = new ArrayList<>(neurons.values());
            if (allNeurons.size() < 2)
                return; // return if there are less than two neurons, which really should not happen. if it does something massively fucked up lmao

            // make multiple attempts to find a valid connection (10 attempts)
            for (int attempts = 0; attempts < 10; attempts++) {
                int indexFrom = new Random().nextInt(allNeurons.size());
                int indexTo = new Random().nextInt(allNeurons.size());

                Neuron fromNeuron = allNeurons.get(indexFrom);
                Neuron toNeuron = allNeurons.get(indexTo);

                int id1 = fromNeuron.getId();
                int id2 = toNeuron.getId();

                // check if the connection would be valid
                if (checkEndNeurons(id1, id2) && id1 != id2) {
                    Synapse newSynapse = new Synapse(fromNeuron, toNeuron, 1.0, true);
                    break; // successfully added synapse
                }
            }
        }


        // 3. ADD NEURON  MUTATION: (must be added on top of an already existing synapse)
        if (chance > Math.random()) {
            int maxInnovation = critter.getWorld().innovationManager().innovation();
            if (maxInnovation <= 0) {
                addNeuronMutation(0);
            } else {
                List<Synapse> allSynapses = new ArrayList<>(synapses.values());
                if (allSynapses.isEmpty()) {
                    addNeuronMutation(0);
                }
                else {
                    int randomIndex = new Random().nextInt(allSynapses.size());
                    Synapse targetSynapse = allSynapses.get(randomIndex);
                    addNeuronMutation(targetSynapse.innovation());
                }

            }
        }
    }


    /**
     * Mutation: adds a neuron
     * Creates a new neuron, disables the original synapse, and inserts the neuron in between where the old synapse was
     * Create two new synapses connecting the neuron to the original synapse's endpoints
     * Parameters: takes in the innovation number of the synapse to disable
     * If innovation is less than or equal to 0, create a new neuron and choose
     * a random input and random output neuron to place the neuron between
     */
    public void addNeuronMutation(int innovation) {
        assert innovation >= 0;
        InnovationManager innovationManager = critter.getWorld().innovationManager();

        // choose a random input and output neuron if innovation = 0
        if (innovation == 0) {
            List<Neuron> inputNeurons = getNeuronsByLayer(0);
            List<Neuron> outputNeurons = getNeuronsByLayer(-1);

            int inputIndex = new Random().nextInt(inputNeurons.size());
            int outputIndex = new Random().nextInt(outputNeurons.size());

            Neuron inputNeuron = inputNeurons.get(inputIndex);
            Neuron outputNeuron = outputNeurons.get(outputIndex);

            Neuron newNeuron = new Neuron(1, this);
            addNeuron(newNeuron);
            Synapse startSynapse = new Synapse(inputNeuron, newNeuron, 1,true);
            Synapse endSynapse = new Synapse(newNeuron, outputNeuron, 1, true);
        } else { // else, follow the normal logic
            Synapse disabledSynapse = innovationManager.get(innovation);
            disabledSynapse.setEnabled(false); // disables the synapse

            // get the layers of the endpoint neurons to calculate the layer of the new neuron
            int layer1 = disabledSynapse.start().getLayer();
            int layer2 = disabledSynapse.end().getLayer();
            int newLayer;

            // handle edge case where the synapse connects an input neuron to an output neuron
            if (layer1 == 0 && layer2 == -1) {
                newLayer = 1;
            } else if (layer2 == -1) { // handle case where neuron is inserted right before the output layer - just 1 more than hiddenLayers
                newLayer = hiddenLayers + 1;
            } else { // else, newLayer is equal to the endpoint neuron's layer - the layer of every neuron connected to the right of the endpoint neuron is incremented by 1
                newLayer = layer2;
            }

            // construct the neuron with the appropriate layer
            Neuron newNeuron = new Neuron(newLayer, this);
            addNeuron(newNeuron);

            // add two new synapses to connect the new neuron to the original endpoint neurons
            // the source-new synapse takes on the weight of the original synapse, while the new-destination synapse take on a weight of 1.0, to preserve network function
            Synapse startSynapse = new Synapse(disabledSynapse.start(), newNeuron, disabledSynapse.weight(), true);
            Synapse endSynapse = new Synapse(newNeuron, disabledSynapse.end(), 1.0, true);

            // adjust the layer of all neurons to the right of the newly created neuron
            adjustAfterAdd(newNeuron);
        }
    }

    /**
     * helper method for addNeuronMutation to recursively adjust layers of all neurons to the right of a newly added neuron after the add mutation
     * takes in a neuron as a parameter as the leftmost neuron
     */
    private void adjustAfterAdd(Neuron neuron) {
        for (Synapse synapse : neuron.outgoingSynapses()) {
            Neuron endNeuron = synapse.end();
            if (endNeuron.getLayer() != -1) {
                endNeuron.setLayer(endNeuron.getLayer() + 1);
                if (endNeuron.getLayer() > hiddenLayers) {
                    setHiddenLayers(endNeuron.getLayer());
                }
                adjustAfterAdd(endNeuron);
            }
        }
    }

    /**
     * Mutation: removes a neuron
     * Removes a neuron and the incoming and outgoing synapses, and replaces it with a new singular connecting synapse
     * Parameters: takes in the id of the neuron to remove
     * Inv: the parameter neuron cannot be an input or output neuron
     */
//    public void removeNeuronMutation(int id) {
//        if (getNeuron(id).getLayer() == 0 || getNeuron(id).getLayer() == -1) {
//            throw new IllegalArgumentException("Cannot remove an input or output neuron!");
//        }
//        Neuron disabledNeuron = getNeuron(id);
//
//        for (Synapse synapse : disabledNeuron.outgoingSynapses()) {
//            synapse.setEnabled(false);
//        }
//
//        for (Synapse synapse: disabledNeuron.incomingSynapses()) {
//            synapse.setEnabled(false);
//        }
//
//        adjustAfterRemove(getNeuron(id));
//        removeNeuron(id);
//    }

    /**
     * private helper method for adjusting layer of neurons to the right of the removed neurons
     * Recursively subtracts one from the layer
     */
//    private void adjustAfterRemove(Neuron neuron) {
//        for (Synapse synapse : neuron.outgoingSynapses()) {
//            Neuron endNeuron = synapse.end();
//            if (endNeuron.getLayer() != -1) {
//                endNeuron.setLayer(endNeuron.getLayer() - 1);
//                adjustAfterRemove(endNeuron);
//            }
//        }
//
//        int maxHiddenLayer = 0;
//        for (Neuron hiddenNeuron : neurons.values()) {
//            if (hiddenNeuron.getLayer() > maxHiddenLayer) {
//                maxHiddenLayer = hiddenNeuron.getLayer();
//            }
//        }
//
//        if (maxHiddenLayer < hiddenLayers) {
//            hiddenLayers = maxHiddenLayer;
//        }
//    }

    /**
     * Mutation: adds a synapse connecting two random neurons
     * inv: neurons must be in different layers, and the start layer must be one less than the end layer
     * Parameters: takes in the ids of the two endpoint neurons the synapse will be attached to
     */
    public void addSynapseMutation(int id1, int id2) {
        InnovationManager innovationManager = critter.getWorld().innovationManager();
        for (int i = 1; i <= innovationManager.innovation(); i++) {
            Synapse synapse = innovationManager.get(i);
            if (synapse.start().getId() == id1 && synapse.end().getId() == id2) {
                synapse.setEnabled(true);
                return;
            }
        }
        Synapse newSynapse = new Synapse(getNeuron(id1), getNeuron(id2), 1.0, true);
    }

    /**
     * helper method for checking the endpoints neurons of a new synapse are valid
     */
    private boolean checkEndNeurons(int id1, int id2) {
        Neuron startNeuron = getNeuron(id1);
        Neuron endNeuron = getNeuron(id2);

        if (startNeuron.getLayer() == 0 && endNeuron.getLayer() == -1) {
            List<Synapse> startOutgoing = startNeuron.outgoingSynapses();
            for (Synapse synapse : startOutgoing) {
                if (synapse.end().getLayer() > 0) {
                    return false;
                }
            }

            List<Synapse> endIncoming = endNeuron.incomingSynapses();
            for (Synapse synapse : endIncoming) {
                if (synapse.end().getLayer() > 0) {
                    return false;
                }
            }

        }
        return startNeuron.getLayer() == endNeuron.getLayer() - 1;
    }

//    /**
//     * Mutation: removes a random synapse
//     * Parameters: takes in the innovation number of the to be disabled synapse
//     */
//    public void removeSynapseMutation(int innovation) {
//        InnovationManager innovationManager = critter.getWorld().innovationManager();
//        innovationManager.get(innovation).setEnabled(false);
//    }

    /**
     * Mutation: changes the weight of a synapse by up to 0.2
     * Parameters: takes in the innovation number of the to be changed synapse
     */
    public void changeWeightMutation(int innovation) {
        InnovationManager innovationManager = critter.getWorld().innovationManager();
        Synapse synapse = innovationManager.get(innovation);

        double weight = synapse.weight();
        double randomChange = Math.random()/5;
        double addOrSubtract = Math.random()/2;

        if (addOrSubtract < 0.5) {
            synapse.setWeight(Math.max(weight - randomChange, 0));
        } else {
            synapse.setWeight(Math.min(weight + randomChange, 1.0));
        }
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

    /**
     * asserts class invariant
     */
    private void assertInv() {
        assert critter != null;
    }
}
