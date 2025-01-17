package brain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BrainTest {
    @DisplayName("WHEN a neuron of layer 'i' is queried,"
            + "THEN return a list of all neurons in layer 'i'.")
    @Test
    void testGetNeuronsByLayer() {
        Brain brain = new Brain();
        Neuron neuron1 = new Neuron(0, 0, 0);
        brain.addNeuron(neuron1);
        Neuron neuron2 = new Neuron(1, 0, -1);
        brain.addNeuron(neuron2);
        Neuron neuron3 = new Neuron(2, 0, 1);
        brain.addNeuron(neuron3);

        assertEquals(neuron1, brain.getNeuronsByLayer(0).getFirst());
        assertEquals(neuron2, brain.getNeuronsByLayer(-1).getFirst());
        assertEquals(neuron3, brain.getNeuronsByLayer(1).getFirst());
    }

    @DisplayName("WHEN a new synapse is created,"
            + "THEN it should automatically add itself to the endpoints' synapse list"
            + "AND therefore to the brain's synapse list")
    @Test
    void testAddSynapse() {
        Brain brain = new Brain();
        Neuron neuron1 = new Neuron(0, 0, 0);
        brain.addNeuron(neuron1);
        Neuron neuron2 = new Neuron(1, 0, -1);
        brain.addNeuron(neuron2);
        Neuron neuron3 = new Neuron(2, 0, 1);
        brain.addNeuron(neuron3);

        Synapse synapse1 = new Synapse(neuron1, neuron2, 0.1, true, 0);
        assertEquals(1, neuron1.outgoingSynapses().size());
        assertEquals(1, neuron2.incomingSynapses().size());

        synapse1.setEnabled(false);

        Synapse synapse2 = new Synapse(neuron1, neuron3, 0.1, true, 1);
        assertEquals(1, neuron1.outgoingSynapses().size());
        assertEquals(1, neuron3.incomingSynapses().size());
        assertEquals(0, neuron3.outgoingSynapses().size());
        assertEquals(0, neuron2.incomingSynapses().size());

        Synapse synapse3 = new Synapse(neuron3, neuron2, 0.2, true, 2);
        assertEquals(1, neuron3.outgoingSynapses().size());
        assertEquals(1, neuron2.incomingSynapses().size());
    }

    @DisplayName("WHEN an array of input is given,"
            + "THEN a correct output array should be returned")
    @Test
    void testFeedForward() {
        Brain brain = new Brain();

        // input layer
        Neuron neuron1 = new Neuron(0, 0, 0);
        Neuron neuron2 = new Neuron(1, 0, 0);
        brain.addNeuron(neuron1);
        brain.addNeuron(neuron2);

        // hidden layer
        Neuron neuron3 = new Neuron(2, 0, 1);
        brain.addNeuron(neuron3);
        Neuron neuron6 = new Neuron(5, 0, 1);
        brain.addNeuron(neuron6);

        // output layer
        Neuron neuron4 = new Neuron(3, 0, -1);
        brain.addNeuron(neuron4);
        Neuron neuron5 = new Neuron(4, 0, -1);
        brain.addNeuron(neuron5);

        // add connections
        Synapse synapse1 = new Synapse(neuron1, neuron3, 0.1, true, 0);
        Synapse synapse2 = new Synapse(neuron2, neuron3, 0.2, true, 1);
        Synapse synapse3 = new Synapse(neuron3, neuron4, 0.3, true, 2);
        Synapse synapse4 = new Synapse(neuron3, neuron5, 0.4, true, 3);
        Synapse synapse5 = new Synapse(neuron1, neuron6, 0.2, true, 4);
        Synapse synapse6 = new Synapse(neuron2, neuron6, 0.1, true, 5);
        Synapse synapse7 = new Synapse(neuron6, neuron4, 0.4, true, 6);
        Synapse synapse8 = new Synapse(neuron6, neuron5, 0.3, true, 7);

        double[] input = {0.3, 0.5};
        assertEquals(0.083, brain.feedForward(input)[0]);
        assertEquals(0.085, brain.feedForward(input)[1]);

    }
}
