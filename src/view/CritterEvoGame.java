package view;

import controller.WorldUpdater;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Random;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import model.WorldModel;


/**
 * A graphical game to simulate evolution!
 */
public class CritterEvoGame {

    /**
     * The application window
     */
    private final JFrame frame;

    /**
     * The world this game contains
     */
    private WorldModel world;

    /**
     * the world panel
     */
    private WorldView worldView;

    /**
     * the world updater
     */
    private WorldUpdater worldUpdater;

    /**
     * Start, pause, and reset buttons
     */
    private JButton startButton, pauseButton, resetButton, generateWorldButton;

    /**
     * Stats label
     */
    private JLabel statsLabel;

    /**
     * User input for world parameters.
     */
    private JTextField widthField,
            heightField,
            foodDensityField,
            critterDensityField,
            mutationRateField,
            baseDamageField,
            damageScalingField,
            baseMoveCostField,
            moveCostField,
            baseRotateCostField,
            rotateCostField,
            sizeCostField,
            reproductionCostField,
            baseHungerExpenditureCostField,
            foodGenField,
            scaleField,
            seedField;

    /**
     * Slider for simulation speed
     */
    private JSlider simulationSpeedSlider;

    /**
     * tabs to switch between the simulation and stats screen
     */
    private JTabbedPane tabbedPane;

    /**
     * the stats screen
     */
    public StatisticsPanel statsPanel;



    /**
     * Construct a new application instance. Initializes GUI components, so must be invoked on the
     * Swing Dispatch Thread. Does not show application (call start() to do so).
     */
    public CritterEvoGame() {
        // initialize application window
        frame = new JFrame("Critter Evo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // get screen dimensions

        // set default area for the frame
        frame.setPreferredSize(new Dimension(1920, 1040));
        frame.setLayout(new BorderLayout());


        // Create tabbed pane
        tabbedPane= new JTabbedPane();

        // Create main game panel
        JPanel gamePanel = new JPanel(new BorderLayout());

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        controlPanel.add(new JLabel("World Width:"));
        widthField = new JTextField("120");
        controlPanel.add(widthField);

        controlPanel.add(new JLabel("World Height:"));
        heightField = new JTextField("90");
        controlPanel.add(heightField);

        controlPanel.add(new JLabel("Initial Food Density (0-1):"));
        foodDensityField = new JTextField("0.02");
        controlPanel.add(foodDensityField);

        controlPanel.add(new JLabel("Initial Critter Density (0-1):"));
        critterDensityField = new JTextField("0.01");
        controlPanel.add(critterDensityField);

        controlPanel.add(new JLabel("Size Cost"));
        sizeCostField = new JTextField("1.1");
        controlPanel.add(sizeCostField);

        controlPanel.add(new JLabel("Base Hunger Expenditure"));
        baseHungerExpenditureCostField = new JTextField("0.4");
        controlPanel.add(baseHungerExpenditureCostField);

        controlPanel.add(new JLabel("Base Movement Cost"));
        baseMoveCostField = new JTextField("0.5");
        controlPanel.add(baseMoveCostField);

        controlPanel.add(new JLabel("Movement Cost Factor"));
        moveCostField = new JTextField("0.001");
        controlPanel.add(moveCostField);

        controlPanel.add(new JLabel("Base Rotate Cost"));
        baseRotateCostField = new JTextField("1.1");
        controlPanel.add(baseRotateCostField);

        controlPanel.add(new JLabel("Rotate Cost Factor"));
        rotateCostField = new JTextField("0.0004");
        controlPanel.add(rotateCostField);

        controlPanel.add(new JLabel("Mutation Rate (0-1):"));
        mutationRateField = new JTextField("0.1");
        controlPanel.add(mutationRateField);

        controlPanel.add(new JLabel("Base Damage (0-100)"));
        baseDamageField = new JTextField("40");
        controlPanel.add(baseDamageField);

        controlPanel.add(new JLabel("Damage Scaling (1.0-2.0)"));
        damageScalingField = new JTextField("1.3");
        controlPanel.add(damageScalingField);

        controlPanel.add(new JLabel("Food Generation Rate (higher is lower)"));
        foodGenField = new JTextField("2.0");
        controlPanel.add(foodGenField);

        controlPanel.add(new JLabel("Terrain Scale:"));
        scaleField = new JTextField("0.03"); // Default scale
        controlPanel.add(scaleField);

//        controlPanel.add(new JLabel("Terrain Seed:"));
//        seedField = new JTextField(Long.toString(new Random().nextLong())); // Default random seed
//        controlPanel.add(seedField);

        controlPanel.add(new JLabel("Simulation Speed"));
        simulationSpeedSlider = new JSlider(0, 1000, 800 );
        controlPanel.add(simulationSpeedSlider);

        simulationSpeedSlider.addChangeListener(e -> {
            if (worldUpdater != null) {
                int delay = simulationSpeedSlider.getValue();
                worldUpdater.setTimerDelay(simulationSpeedSlider.getMaximum() - delay);
            }
        });

        startButton = new JButton("Start");
        pauseButton = new JButton("Pause");
        resetButton = new JButton("Reset");
        generateWorldButton = new JButton("Generate");
        controlPanel.add(startButton);
        controlPanel.add(pauseButton);
        controlPanel.add(resetButton);
        controlPanel.add(generateWorldButton);
//        frame.add(controlPanel, BorderLayout.EAST);


        gamePanel.add(controlPanel, BorderLayout.EAST);


        startButton.addActionListener(e -> {
            // start simulation
            if (worldUpdater == null) {
                worldUpdater = new WorldUpdater(worldView.getWorldModel(), worldView, this);
                worldUpdater.start();
                worldUpdater.tick();
            } else {
                worldUpdater.start();
                worldUpdater.tick();// Resume if paused
            }
        });

        pauseButton.addActionListener(e -> {
            // pause simulation
            worldUpdater.stop();
        });

        resetButton.addActionListener(e -> {
            // reset simulation, clear world
            gamePanel.remove(worldView);
            gamePanel.repaint();
        });
        // Modify the generate world button action
        generateWorldButton.addActionListener(e -> {
            generateWorld();
            statsPanel.setWorld(world);
        });

        // Create statistics panel
        statsPanel = new StatisticsPanel();


        // Add tabs
        tabbedPane.addTab("Simulation", gamePanel);
        tabbedPane.addTab("Statistics", statsPanel);


        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * generate the world view based on user parameters
     */
    private void generateWorld() {
        WorldModel world = createWorldModel();
        this.world = world;

        // Initialize or update world view
        if (worldView != null) {
            JPanel gamePanel = (JPanel) tabbedPane.getComponentAt(0);
            gamePanel.remove(worldView);
        }
        worldView = new WorldView(world);
        JPanel gamePanel = (JPanel) tabbedPane.getComponentAt(0);
        gamePanel.add(worldView, BorderLayout.CENTER);
        gamePanel.revalidate();
        gamePanel.repaint();

        // Reset the world updater
        worldUpdater = null;
    }

    /**
     * Constructs the world, helper method for generateWorld()
     */
    private WorldModel createWorldModel() {
        int height = Integer.parseInt(heightField.getText());
        int width = Integer.parseInt(widthField.getText());
        double foodDensity = Double.parseDouble(foodDensityField.getText());
        double critterDensity = Double.parseDouble(critterDensityField.getText());
        double moveCostFactor = Double.parseDouble(moveCostField.getText());
        double rotateCostFactor = Double.parseDouble(rotateCostField.getText());
        double baseMoveCostFactor = Double.parseDouble(baseMoveCostField.getText());
        double baseRotateCostFactor = Double.parseDouble(baseRotateCostField.getText());
        double sizeCost = Double.parseDouble(sizeCostField.getText());
        double mutationRate = Double.parseDouble(mutationRateField.getText());
        double baseDamage = Double.parseDouble(baseDamageField.getText());
        double damageScaling = Double.parseDouble(damageScalingField.getText());
        double baseHungerExpenditure = Double.parseDouble(baseHungerExpenditureCostField.getText());
        double foodGenRate = Double.parseDouble(foodGenField.getText());
        double scale = Double.parseDouble(scaleField.getText());
        long seed = new Random().nextLong();


        // create the new world
        return new WorldModel(
                width,
                height,
                foodDensity,
                critterDensity,
                moveCostFactor,
                rotateCostFactor,
                baseMoveCostFactor,
                baseRotateCostFactor,
                sizeCost,
                mutationRate,
                baseDamage,
                damageScaling,
                baseHungerExpenditure,
                foodGenRate,
                scale,
                seed
        );
    }

    public void start() {
        frame.pack();
        frame.setVisible(true);
    }

    /** Run an instance of CritterEvoGame. No program arguments are expected.
     */
    public static void main(String[] args) {
        CritterEvoGame game = new CritterEvoGame();
        game.start();
    }
}