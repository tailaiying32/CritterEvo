package controller;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import model.Critter;
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
     * Indicates if the simulation is running
     */
    private boolean isRunning;

    /**
     * Constructor for the WorldUpdate
     */
    public WorldUpdater(WorldModel world) {
        this.world = world;
    }

    /**
     * Starts the world
     */
    public void start() {
        while (isRunning) {
            tick();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gathers information from critters and updates each critter as well as the world
     */
    private void tick() {
        world.incrementTickCount();
        updateCritters();

        // update world
        world.update();
    }

    /**
     * updates the critters' state
     */
    private void updateCritters() {
        HashMap<Point, Critter> updatedCritters = new HashMap<>();
        for (Critter critter: world.getCritters().values()) {
            critter.updatePriority();
            critter.makeMove();
        }
    }

}
