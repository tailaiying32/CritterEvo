package brain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import controller.CritterFactory;
import java.awt.Point;
import model.Critter;
import model.WorldFactory;
import model.WorldModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BrainTest {
    @DisplayName("WHEN a neuron of layer 'i' is queried,"
            + "THEN return a list of all neurons in layer 'i'.")
    @Test
    void testGetNeuronsByLayer() {
        Brain brain = new Brain();
        Neuron neuron1 = new Neuron(0, brain);
        brain.addNeuron(neuron1);
        Neuron neuron2 = new Neuron(-1, brain);
        brain.addNeuron(neuron2);
        Neuron neuron3 = new Neuron(1, brain);
        brain.addNeuron(neuron3);

        assertEquals(neuron1, brain.getNeuronsByLayer(0).getFirst());
        assertEquals(neuron2, brain.getNeuronsByLayer(-1).getFirst());
        assertEquals(neuron3, brain.getNeuronsByLayer(1).getFirst());

        assertEquals(1, neuron1.getId());
        assertEquals(2, neuron2.getId());
        assertEquals(3, neuron3.getId());
    }

    @DisplayName("WHEN a new synapse is created,"
            + "THEN it should automatically add itself to the endpoints' synapse list"
            + "AND therefore to the brain's synapse list"
            + "AND its innovation number should be incremented from the global innovation number")
    @Test
    void testAddSynapse() {
        WorldFactory wf = new WorldFactory();
        WorldModel wm = wf.generateTestWorld();
        CritterFactory cf = new CritterFactory();
        Critter critter = cf.generateCritter(new Point(0, 0), wm);

        Brain brain = new Brain(critter);
        Neuron neuron1 = new Neuron(0,  0, brain);
        brain.addNeuron(neuron1);
        Neuron neuron2 = new Neuron(1,  -1, brain);
        brain.addNeuron(neuron2);
        Neuron neuron3 = new Neuron(2,  1, brain);
        brain.addNeuron(neuron3);

        Synapse synapse1 = new Synapse(neuron1, neuron2, 0.1, true);
        assertEquals(1, synapse1.innovation());
        assertEquals(1, neuron1.outgoingSynapses().size());
        assertEquals(1, neuron2.incomingSynapses().size());

        synapse1.setEnabled(false);

        Synapse synapse2 = new Synapse(neuron1, neuron3, 0.1, true);
        assertEquals(2, synapse2.innovation());
        assertEquals(1, neuron1.outgoingSynapses().size());
        assertEquals(1, neuron3.incomingSynapses().size());
        assertEquals(0, neuron3.outgoingSynapses().size());
        assertEquals(0, neuron2.incomingSynapses().size());

        Synapse synapse3 = new Synapse(neuron3, neuron2, 0.2, true);
        assertEquals(3, synapse3.innovation());
        assertEquals(1, neuron3.outgoingSynapses().size());
        assertEquals(1, neuron2.incomingSynapses().size());

        assertEquals(3, wm.innovationManager().innovation());
    }

    @DisplayName("WHEN an array of input is given,"
            + "THEN a correct output array should be returned")
    @Test
    void testFeedForward() {
        Brain brain = new Brain();

        // input layer
        Neuron neuron1 = new Neuron(0,  0, brain);
        Neuron neuron2 = new Neuron(1,  0, brain);
        brain.addNeuron(neuron1);
        brain.addNeuron(neuron2);

        // hidden layer
        Neuron neuron3 = new Neuron(2,  1, brain);
        brain.addNeuron(neuron3);
        Neuron neuron6 = new Neuron(5,  1, brain);
        brain.addNeuron(neuron6);

        // output layer
        Neuron neuron4 = new Neuron(3,  -1, brain);
        brain.addNeuron(neuron4);
        Neuron neuron5 = new Neuron(4,  -1, brain);
        brain.addNeuron(neuron5);

        // add connections
        Synapse synapse1 = new Synapse(neuron1, neuron3, 0.1, true, 1);
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

    @DisplayName("WHEN a neuron is added through mutation,"
            + "THEN the old synapse should be disabled and two new synapses should be created"
            + "AND all layers of the neurons connected to the right of the new neuron should be adjusted")
    @Test
    public void testAddNeuronMutation() {
        WorldFactory wf = new WorldFactory();
        WorldModel wm = wf.generateTestWorld();
        CritterFactory cf = new CritterFactory();
        Critter critter = cf.generateCritter(new Point(0, 0), wm);

        Brain brain = new Brain(critter);
        Neuron inputNeuron = new Neuron(0, brain);
        brain.addNeuron(inputNeuron);
        Neuron hidden1 = new Neuron(1, brain);
        brain.addNeuron(hidden1);
        Neuron hidden2 = new Neuron(2, brain);
        brain.addNeuron(hidden2);
        Neuron hidden3 = new Neuron(2, brain);
        brain.addNeuron(hidden3);
        Neuron outputNeuron = new Neuron(-1, brain);
        brain.addNeuron(outputNeuron);

        Synapse synapse1 = new Synapse(inputNeuron, hidden1, 1, true);
        Synapse synapse2 = new Synapse(hidden1, hidden2, 0.1, true);
        Synapse synapse3 = new Synapse(hidden1, hidden3, 0.2, true);
        Synapse synapse4 = new Synapse(hidden2, outputNeuron, 0.3, true);
        Synapse synapse5 = new Synapse(hidden3, outputNeuron, 0.4, true);

        assertEquals(5, wm.innovationManager().innovation());

        brain.addNeuronMutation(1);

        assertEquals(7, wm.innovationManager().innovation());
        assertEquals(1, brain.getNeuronsByLayer(1).size());
        assertEquals(1, brain.getNeuronsByLayer(2).size());
        assertEquals(2, brain.getNeuronsByLayer(3).size());
        assertEquals(0, brain.getNeuronsByLayer(4).size());
        assertEquals(1, brain.getNeuronsByLayer(-1).size());

        brain.addNeuronMutation(4);

        assertEquals(9, wm.innovationManager().innovation());
        assertEquals(1, brain.getNeuronsByLayer(1).size());
        assertEquals(1, brain.getNeuronsByLayer(2).size());
        assertEquals(2, brain.getNeuronsByLayer(3).size());
        assertEquals(1, brain.getNeuronsByLayer(4).size());
        assertEquals(1, brain.getNeuronsByLayer(-1).size());

        brain.addNeuronMutation(0);

        assertEquals(11, wm.innovationManager().innovation());
        assertEquals(2, brain.getNeuronsByLayer(1).size());
    }

    @DisplayName("WHEN a new synapse is added through mutation, "
            + "THEN the innovation manager should check whether the synapse exists first"
            + "THEN add to the brain")
    @Test
    public void testAddNewSynapseMutation() {
        WorldFactory wf = new WorldFactory();
        WorldModel wm = wf.generateTestWorld();
        CritterFactory cf = new CritterFactory();
        Critter critter = cf.generateCritter(new Point(0, 0), wm);

        Brain brain = new Brain(critter);
        Neuron inputNeuron = new Neuron(0, brain);
        brain.addNeuron(inputNeuron);
        Neuron hidden1 = new Neuron(1, brain);
        brain.addNeuron(hidden1);
        Neuron hidden2 = new Neuron(2, brain);
        brain.addNeuron(hidden2);
        Neuron hidden3 = new Neuron(2, brain);
        brain.addNeuron(hidden3);
        Neuron outputNeuron = new Neuron(-1, brain);
        brain.addNeuron(outputNeuron);

        Synapse synapse1 = new Synapse(inputNeuron, hidden1, 1, true);
        Synapse synapse2 = new Synapse(hidden1, hidden2, 0.1, true);
        Synapse synapse3 = new Synapse(hidden1, hidden3, 0.2, true);
        Synapse synapse4 = new Synapse(hidden2, outputNeuron, 0.3, true);
        Synapse synapse5 = new Synapse(hidden3, outputNeuron, 0.4, true);

        brain.addNeuronMutation(1);
        brain.addNeuronMutation(4);
        brain.addNeuronMutation(3);

        brain.addSynapseMutation(8, 7);

        assertEquals(2, brain.getNeuron(7).incomingSynapses().size());
        assertEquals(2, brain.getNeuron(8).outgoingSynapses().size());
        assertEquals(2, brain.getNeuronsByLayer(3).size());
        assertEquals(2, brain.getNeuronsByLayer(4).size());

        brain.critter().getWorld().innovationManager().get(12).setEnabled(false);

        assertEquals(1, brain.getNeuron(7).incomingSynapses().size());
        assertEquals(1, brain.getNeuron(8).outgoingSynapses().size());

        brain.addSynapseMutation(8, 7);
        assertEquals(12, brain.critter().getWorld().innovationManager().innovation());
        assertEquals(12, brain.getSynapses().size());
    }

}
