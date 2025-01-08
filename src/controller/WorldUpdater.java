package controller;

import java.awt.Point;
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
        if (!isRunning) return;

        // Update model
        worldView.getWorldModel().incrementTickCount();
        updateCritters();
//        addFood();

        // Request GUI update
        SwingUtilities.invokeLater(() -> {
            worldView.revalidate();
            worldView.repaint();
        });
    }

    /**
     * Updates states of critters and refreshes data in world model
     * inv: critters cannot move out of bounds
     */
    private void updateCritters() {
        int width = worldView.getWorldModel().getWidth();
        int height = worldView.getWorldModel().getHeight();

        // Clear previous critter positions
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (worldView.getWorldModel().getWorldArray()[i][j] == 4) {
                    worldView.getWorldModel().getWorldArray()[i][j] = 0;
                }
            }
        }

        // Update critter positions
        HashMap<Point, Critter> updatedCritters = new HashMap<>();
        for (Critter critter : worldView.getWorldModel().getCritters().values()) {
            critter.updatePriority();
            critter.makeMove();

            Point pos = critter.getPosition();
            if (pos.x >= 0 && pos.x < width && pos.y >= 0 && pos.y < height) {
                worldView.getWorldModel().getWorldArray()[pos.x][pos.y] = 4;
                updatedCritters.put(pos, critter);
            } else {
                System.out.println("Warning: Critter tried to move out of bounds: " + pos);
            }
        }
        worldView.getWorldModel().setCritters(updatedCritters);
    }

    /**
     * Reseeds food on the world
     */
    private void addFood() {
        WorldModel world = worldView.getWorldModel();
        int numCritters = worldView.getWorldModel().getCritters().size();
        double random = Math.random() * numCritters;

        for (int i = 0; i < world.getWidth(); i++) {
            for (int j = 0; j < world.getHeight(); j++) {
                if (world.getWorldArray()[i][j] == 0) {
                    if (random <= 1) {
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
            System.out.println("Setting timer delay to " + delay);
            timer.setDelay(delay);
        }
    }

}