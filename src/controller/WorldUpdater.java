package controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;
import model.Critter;
import model.Food;
import model.WorldModel;
import model.WorldModel.CellState;
import view.CritterEvoGame;
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
     * the game instance
     */
    private CritterEvoGame game;

    /**
     * Boolean representing whether the simulation is running or not
     */
    private boolean isRunning;

    /**
     * Timer for the simulation speed
     */
    private Timer timer;

    /**
     * The batch size for critters to be processed in
     */
    private final static int BATCH_SIZE = 50;

    /**
     * Constructor for worldUpdater
     */
    public WorldUpdater(WorldModel world, WorldView worldView, CritterEvoGame game) {
        this.worldView = worldView;
        this.worldModel = worldView.getWorldModel();
        this.game = game;
        // Create timer that calls tick() every 2000ms
        this.timer = new Timer(100, e -> tick());
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
            game.statsPanel.updateStats();
            game.statsPanel.repaint();
        }
    }

    /**
     * Updates states of critters in this world
     */
    private void updateCritters(WorldModel worldModel) {
        for (Critter critter : new ArrayList<>(worldModel.getCritters().values())) {
            critter.makeMove();

            if (critter.getHunger() <= 0) {
                critter.starve();
            }
            if (critter.getThirst() <= 0) {
                critter.starve();
            }

            if (critter.getHealth() <= 0 || critter.getAge() >= critter.getMaxAge()) {
                critter.die();
            }
        }
    }

    /**
     * Reseeds food on the world
     */
    private void addFood() {
        WorldModel world = worldView.getWorldModel();
        List<Point> spawnables = world.getSpawnableCells();
        if (spawnables == null || spawnables.isEmpty()) return;

        int numCritters = Math.max(1, world.getCritters().size());
        // Expected spawns = spawnables.size() / (numCritters * factor); use
        // probabilistic rounding so fractional budgets are handled fairly.
        double expected = (double) spawnables.size() / (numCritters * world.getFOOD_GENERATION_FACTOR());
        int budget = (int) expected;
        if (Math.random() < (expected - budget)) budget++;

        for (int i = 0; i < budget; i++) {
            Point p = spawnables.get((int) (Math.random() * spawnables.size()));
            if (world.getWorldArray()[p.x][p.y] == CellState.GRASS) {
                world.addFood(new Food(new Point(p.x, p.y), (int) (Math.random() * 40), 0));
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