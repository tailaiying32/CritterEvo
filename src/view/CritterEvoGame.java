package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
     * the world panel
     */
    private WorldView worldView;

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
    private JTextField widthField, heightField, foodDensityField, critterDensityField, mutationRateField, baseDamageField, damageScalingField;


    /**
     * Construct a new application instance. Initializes GUI components, so must be invoked on the
     * Swing Dispatch Thread. Does not show application (call start() to do so).
     */
    public CritterEvoGame() {
        // initialize application window
        frame = new JFrame("Critter Evo");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // set default area for the frame
        frame.setPreferredSize(new Dimension(1920, 1200));
        frame.setLayout(new BorderLayout());

        // TODO: Add world component (the grid), living on the left majority of frame

        // TODO: Add input fields for world paramters
        // Create input fields for world parameters



        // TODO: Add a control panel on right of frame, with play, pause, reset, and speed sliders
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        controlPanel.add(new JLabel("World Width:"));
        widthField = new JTextField("40");
        controlPanel.add(widthField);

        controlPanel.add(new JLabel("World Height:"));
        heightField = new JTextField("30");
        controlPanel.add(heightField);

        controlPanel.add(new JLabel("Initial Food Density (0-1):"));
        foodDensityField = new JTextField("0.2");
        controlPanel.add(foodDensityField);

        controlPanel.add(new JLabel("Initial Critter Density (0-1):"));
        critterDensityField = new JTextField("0.1");
        controlPanel.add(critterDensityField);

        controlPanel.add(new JLabel("Mutation Rate (0-1):"));
        mutationRateField = new JTextField("0.05");
        controlPanel.add(mutationRateField);

        controlPanel.add(new JLabel("Base Damage (0-100)"));
        baseDamageField = new JTextField("25");
        controlPanel.add(baseDamageField);

        controlPanel.add(new JLabel("Damage Scaling (1.0-2.0"));
        damageScalingField = new JTextField("1.3");
        controlPanel.add(damageScalingField);


        startButton = new JButton("Start");
        pauseButton = new JButton("Pause");
        resetButton = new JButton("Reset");
        generateWorldButton = new JButton("Generate");
        controlPanel.add(startButton);
        controlPanel.add(pauseButton);
        controlPanel.add(resetButton);
        controlPanel.add(generateWorldButton);
        frame.add(controlPanel, BorderLayout.EAST);


        // TODO: Add action listeners for buttons
        startButton.addActionListener(e -> {
            // start simulation
            throw new UnsupportedOperationException();
        });

        pauseButton.addActionListener(e -> {
            // pause simulation
            throw new UnsupportedOperationException();
        });

        resetButton.addActionListener(e -> {
            // reset simulation
            throw new UnsupportedOperationException();
        });

        generateWorldButton.addActionListener(e -> {
            // generate world based on user input
            generateWorld();
        });
    }

    /**
     * generate the world view based on user parameters
     */
    private void generateWorld() {
        // TODO: parse and validate the user inputs, use the inputs to construct a new world, and initialize or update the world view
        int width = Integer.parseInt(widthField.getText());
        int height = Integer.parseInt(heightField.getText());
        double foodDensity = Double.parseDouble(foodDensityField.getText());
        double critterDensity = Double.parseDouble(critterDensityField.getText());
        double mutationRate = Double.parseDouble(mutationRateField.getText());
        double baseDamage = Double.parseDouble(baseDamageField.getText());
        double damageScaling = Double.parseDouble(damageScalingField.getText());

        // TODO: assert that the parameters do not violate invariants

        // create the new world
        WorldModel world = new WorldModel(width, height, foodDensity, critterDensity, mutationRate, baseDamage, damageScaling);

        // TODO: using the created "world", create a world view
        // Initialize or update world view
        if (worldView != null) {
            frame.remove(worldView);
        }
        worldView = new WorldView(world, 20);
        frame.add(worldView, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
        // TODO: catch any exceptions thrown
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


