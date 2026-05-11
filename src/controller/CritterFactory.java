package controller;

import behavior.CritterAI;
import behavior.InteractionManager;
import java.awt.Point;
import java.util.concurrent.ThreadLocalRandom;
import model.Critter;
import model.Critter.Orientation;
import model.Critter.Sex;
import model.WorldModel;

/**
 * used for creating randomized critters at the start of a new world
 */
public class CritterFactory {

    /**
     * constructor for CritterFactory
     */
    public CritterFactory() {}

    /**
     * Creates and returns a critter with randomized attributes
     */
    public Critter generateCritter(Point p, WorldModel world) {
        CritterAI critterai = new CritterAI();

        // random number generator for attributes for critter construction
        Orientation[] orientations = Orientation.values();
        Orientation orientation = orientations[ThreadLocalRandom.current().nextInt(orientations.length)];

        // Floor every randomized trait so newborns are actually viable.
        int maxAge = 500 + (int) (Math.random() * 1500);          // 500–2000

        Sex[] sexes = Sex.values();
        Sex sex = sexes[ThreadLocalRandom.current().nextInt(sexes.length)];

        double size = 20 + (Math.random() * 80);                  // 20–100

        // Decouple maxHealth from size so small critters aren't one-shot.
        double maxHealth = 40 + (Math.random() * 60);             // 40–100

        double offense = 20 + (Math.random() * 80);               // 20–100
        double defense = 20 + (Math.random() * 80);               // 20–100

        double maxHunger = 50 + (Math.random() * 50);             // 50–100
        double maxThirst = 50 + (Math.random() * 50);             // 50–100

        double aggression = (Math.random() * 100);

        double mutationRate = (Math.random() / 10);

        int vision = 3 + (int) (Math.random() * 27);              // 3–29

        Critter critter = new Critter(
                critterai,
                new InteractionManager(),
                p,
                orientation,
                maxAge,
                maxHunger,
                maxThirst,
                maxHealth,
                sex,
                size,
                offense,
                defense,
                aggression,
                mutationRate,
                vision,
                world
        );

        return critter;
    }

}
