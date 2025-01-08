package model;

/**
 * Create a world, for testing purposes
 */
public class WorldFactory {

    /**
     * constructor for the world factory
     */
    public WorldFactory() {}

    /**
     * Creates an empty world with very large dimensions and random attributes
     */
    public WorldModel generateTestWorld() {
        int height = 999;
        int width = 999;
        double foodDensity = 0;
        double critterDensity = 0;
        double mutationRate = 0.1;
        double baseDamage = 25;
        double damageScaling = 1.2;
        // create the new world
        return new WorldModel(width, height, foodDensity, critterDensity, mutationRate, baseDamage, damageScaling);
    }

}
