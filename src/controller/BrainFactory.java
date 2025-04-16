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

        return brain;
    }
}
