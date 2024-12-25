package controller;

import model.WorldModel;

/**
 * Updates the world based on information data from Interaction Manager and CritterAI
 */
public class WorldUpdater {

    /**
     * The world that the updater is acting on
     */
    private WorldModel world;

    /**
     * Constructor for the WorldUpdate
     */
    public WorldUpdater(WorldModel world) {
        this.world = world;
    }


}
