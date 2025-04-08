package controller;

import brain.Brain;
import brain.Neuron;
import brain.Synapse;
import model.Critter;

/**
 * Creates a brain with only input and output neurons at the beginning of critter creation
 */
public class BrainFactory {
    /**
     * Constructor for the factory
     */
    public BrainFactory() {}

    /**
     * generates a brain
     */
    public Brain generateBrain(Critter critter) {
        Brain brain = new Brain(critter);

        // input neurons: health, hunger, thirst, peaceful critter density, angry critter density, food density
        Neuron healthNeuron = new Neuron(0, brain);
        brain.addNeuron(healthNeuron);
        Neuron hungerNeuron = new Neuron(0, brain);
        brain.addNeuron(hungerNeuron);
        Neuron thirstNeuron = new Neuron(0, brain);
        brain.addNeuron(thirstNeuron);

        Neuron populationDensityNeuron = new Neuron(0, brain);
        Neuron peacefulCrittersNeuron = new Neuron(0, brain);
        Neuron angryCrittersNeuron = new Neuron(0, brain);
        Neuron foodDensityNeuron = new Neuron(0, brain);
//        System.out.println(brain.getNeuronsByLayer(0).size());


//        brain.addNeuron(populationDensityNeuron);
//        brain.addNeuron(peacefulCrittersNeuron);
//        brain.addNeuron(angryCrittersNeuron);
//        brain.addNeuron(foodDensityNeuron);

        // output neurons: find food, find water, find critter to attack, reproduce, rest
        Neuron foodNeuron = new Neuron(-1, brain);
        brain.addNeuron(foodNeuron);

        Neuron waterNeuron = new Neuron(-1, brain);
        brain.addNeuron(waterNeuron);

        Neuron attackNeuron = new Neuron(-1, brain);
        brain.addNeuron(attackNeuron);

        Neuron reproduceNeuron = new Neuron(-1, brain);
        brain.addNeuron(reproduceNeuron);

        Neuron restNeuron = new Neuron(-1, brain);
        brain.addNeuron(restNeuron);

        // Add basic connections with meaningful initial weights

        // Health connections
        new Synapse(healthNeuron, foodNeuron, 0.2, true);
        new Synapse(healthNeuron, waterNeuron, 0.2, true);
        new Synapse(healthNeuron, attackNeuron, 0.1, true);
        new Synapse(healthNeuron, reproduceNeuron, 0.6, true); // Higher health -> more likely to reproduce
        new Synapse(healthNeuron, restNeuron, 0.3, true);

        // Hunger connections
        new Synapse(hungerNeuron, foodNeuron, 0.8, true);      // Higher hunger -> strongly seek food
        new Synapse(hungerNeuron, waterNeuron, 0.2, true);
        new Synapse(hungerNeuron, attackNeuron, 0.3, true);
        new Synapse(hungerNeuron, reproduceNeuron, -0.5, true); // Higher hunger -> less likely to reproduce
        new Synapse(hungerNeuron, restNeuron, -0.2, true);

        // Thirst connections
        new Synapse(thirstNeuron, foodNeuron, 0.1, true);
        new Synapse(thirstNeuron, waterNeuron, 0.9, true);      // Higher thirst -> strongly seek water
        new Synapse(thirstNeuron, attackNeuron, 0.1, true);
        new Synapse(thirstNeuron, reproduceNeuron, -0.5, true); // Higher thirst -> less likely to reproduce
        new Synapse(thirstNeuron, restNeuron, -0.2, true);

        return brain;
    }
}
