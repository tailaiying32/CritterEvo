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

//        Neuron populationDensityNeuron = new Neuron(0, brain);
//        brain.addNeuron(populationDensityNeuron);
//        Neuron peacefulCrittersNeuron = new Neuron(0, brain);
//        brain.addNeuron(peacefulCrittersNeuron);
//        Neuron angryCrittersNeuron = new Neuron(0, brain);
//        brain.addNeuron(angryCrittersNeuron);
//        Neuron foodDensityNeuron = new Neuron(0, brain);
//        brain.addNeuron(foodDensityNeuron);
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

        // initial connections (all random)
        Synapse health_food = new Synapse (healthNeuron, foodNeuron, randomWeight(), true);
        brain.addSynapse(health_food);
        Synapse health_water = new Synapse (healthNeuron, waterNeuron, randomWeight(), true);
        brain.addSynapse(health_water);
        Synapse health_attack = new Synapse (healthNeuron, attackNeuron, randomWeight(), true);
        brain.addSynapse(health_attack);
        Synapse health_reproduce = new Synapse (healthNeuron, reproduceNeuron, randomWeight(), true);
        brain.addSynapse(health_reproduce);
        Synapse health_rest = new Synapse (healthNeuron, restNeuron, randomWeight(), true);
        brain.addSynapse(health_rest);

        Synapse hunger_food = new Synapse (healthNeuron, foodNeuron, randomWeight(), true);
        brain.addSynapse(hunger_food);
        Synapse hunger_water = new Synapse (healthNeuron, waterNeuron, randomWeight(), true);
        brain.addSynapse(hunger_water);
        Synapse hunger_attack = new Synapse (healthNeuron, attackNeuron, randomWeight(), true);
        brain.addSynapse(hunger_attack);
        Synapse hunger_reproduce = new Synapse (healthNeuron, reproduceNeuron, randomWeight(), true);
        brain.addSynapse(hunger_reproduce);
        Synapse hunger_rest = new Synapse (healthNeuron, restNeuron, randomWeight(), true);
        brain.addSynapse(hunger_rest);

        Synapse thirst_food = new Synapse (thirstNeuron, foodNeuron, randomWeight(), true);
        brain.addSynapse(thirst_food);
        Synapse thirst_water = new Synapse (thirstNeuron, waterNeuron, randomWeight(), true);
        brain.addSynapse(thirst_water);
        Synapse thirst_attack = new Synapse (thirstNeuron, attackNeuron, randomWeight(), true);
        brain.addSynapse(thirst_attack);
        Synapse thirst_reproduce = new Synapse (thirstNeuron, reproduceNeuron, randomWeight(), true);
        brain.addSynapse(thirst_reproduce);
        Synapse thirst_rest = new Synapse (thirstNeuron, restNeuron, randomWeight(), true);
        brain.addSynapse(thirst_rest);

        return brain;
    }

    /**
     * Helper method to generate a random weight between 0.0 and 1.0
     */
    private double randomWeight() {
        return Math.random();
    }
}
