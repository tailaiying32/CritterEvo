package controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import model.Critter;
import model.Food;
import model.WorldModel;
import view.WorldView;

public class WorldUpdater {

    /**
     * The world view for this updater
     */
    private WorldView worldView;

    /**
     * The world model corresponding to the view
     */
    private WorldModel worldModel;


    /**
     * Boolean representing whether the simulation is running or not
     */
    private boolean isRunning;

    /**
     * Timer for the simulation speed
     */
    private Timer timer;

    /**
     * Constructor for worldUpdater
     */
    public WorldUpdater(WorldModel world, WorldView worldView) {
        this.worldView = worldView;
        this.worldModel = worldView.getWorldModel();
        // Create timer that calls tick() every 2000ms
        this.timer = new Timer(1000, e -> tick());
    }

    /**
     * Starts the simulation
     */
    public void start() {
        isRunning = true;
        timer.start();
    }

    /**
     * Stops the simulation
     */
    public void stop() {
        isRunning = false;
        timer.stop();
    }

    /**
     * If isRunning is true, increment the tick count and update the critters and world state
     */
    public void tick() {
        if (isRunning) {
            worldModel.updateWorldArray();
            updateCritters(worldModel);

            addFood();
            worldView.repaint();
//            statsPanel.updateState();
        }
//        System.out.println("critter list: " + worldModel.getCritters().size());
//        System.out.println("food list: " + worldModel.getFoods().size());
    }

    /**
     * Updates states of critters in this world
     */
    private void updateCritters(WorldModel worldModel) {
        for (Critter critter : new ArrayList<>(worldModel.getCritters().values())) {
            critter.updatePriority();
            critter.makeMove();

            if (critter.getHunger() <= 0) {
                critter.starve();
            }

            if (critter.getHealth() <= 0) {
                critter.die();
            }

            if (critter.getAge() >= critter.getMaxAge()) {
                critter.die();
            }
        }
    }

    /**
     * Reseeds food on the world
     */
    private void addFood() {
        WorldModel world = worldView.getWorldModel();
        int numCritters = worldView.getWorldModel().getCritters().size();

        for (int i = 0; i < world.getWidth(); i++) {
            for (int j = 0; j < world.getHeight(); j++) {
                if (world.getWorldArray()[i][j] == 0) {
                    // Generate random number between 0 and 1
                    double random = Math.random();
                    // Check if random number is less than 1/2N
                    if (random < 1.0 / (numCritters * 2)) {
                        world.getWorldArray()[i][j] = 4;
                        world.addFood(new Food(new Point(i, j), (int) (Math.random() * 40), 0));
                    }
                }
            }
        }
    }


    /**
     * Takes data from slider in gui to change simulation speed
     */
    public void setTimerDelay(int delay) {
        if (timer != null) {
            timer.setDelay(delay);
        }
    }

}