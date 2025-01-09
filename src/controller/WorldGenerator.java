package controller;

import model.WorldModel;
import model.WorldModel.CellState;

public class WorldGenerator {
    /**
     * controls the frequency of nose
     */
    private final double scale;

    /**
     * for reproducibility
     */
    private final long seed;

    /**
     * the noise generator
     */
    private final OpenSimplex2S noiseGenerator;

    /**
     * the constructor for the world generator
     */
    public WorldGenerator(double scale, long seed) {
        this.scale = scale;
        this.seed = seed;
        this.noiseGenerator = new OpenSimplex2S();
    }

    /**
     * generate terrain using the noise generator
     */
//    public void generateTerrain(WorldModel world) {
//        int width = world.getWidth();
//        int height = world.getHeight();
//        CellState[][] worldArray = world.getWorldArray();
//
//        for (int x = 0; x < width; x++) {
//            for (int y = 0; y < height; y++) {
//                double noiseValue = noiseGenerator.noise2_ImproveX(seed, x * scale, y * scale);
//
//                // map values to terrain types
//                if (noiseValue < 0.6) {
//                    worldArray[x][y] = CellState.WATER;
//                } else if (noiseValue >= 0.6 && noiseValue < 0.7) {
//                    worldArray[x][y] = CellState.GRASS;
//                } else {
//                    worldArray[x][y] = CellState.MOUNTAIN;
//                }
//            }
//        }
//    }
    public void generateTerrain(WorldModel world) {
        int width = world.getWidth();
        int height = world.getHeight();
        CellState[][] worldArray = world.getWorldArray();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Generate normalized noise value
                double noiseValue = 0;
                double amplitude = 1;
                double frequency = scale;
                double maxAmplitude = 0;

                for (int octave = 0; octave < 4; octave++) {  // 4 octaves
                    noiseValue += amplitude * OpenSimplex2S.noise2_ImproveX(seed, x * frequency, y * frequency);
                    maxAmplitude += amplitude;
                    amplitude *= 0.5;  // Persistence
                    frequency *= 2;    // Lacunarity
                }

                noiseValue = (noiseValue / maxAmplitude + 1) / 2.0;  // Normalize to [0, 1]
                for (int i = 0; i < width/10; i++) {
                    for (int j = 0; j < height/10; j++) {
                        System.out.println("x=" + i + " y=" + j + " noiseValue=" + noiseValue);
                    }
                }

                // Map to terrain types
                if (noiseValue < 0.3) {
                    worldArray[x][y] = CellState.WATER;
                } else if (noiseValue < 0.7) {
                    worldArray[x][y] = CellState.GRASS;
                } else {
                    worldArray[x][y] = CellState.MOUNTAIN;
                }
            }
        }
    }

}
