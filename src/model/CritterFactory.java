package model;

import behavior.CritterAI;
import behavior.InteractionManager;
import java.awt.Point;
import java.util.concurrent.ThreadLocalRandom;
import model.Critter.Orientation;
import model.Critter.Sex;

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
        CritterAI critterai = new CritterAI(5);

        // random number generator for attributes for critter construction
        Orientation[] orientations = Orientation.values();
        Orientation orientation = orientations[ThreadLocalRandom.current().nextInt(orientations.length)];

        int maxAge = (int) (Math.random() * 2000);

        Sex[] sexes = Sex.values();
        Sex sex = sexes[ThreadLocalRandom.current().nextInt(sexes.length)];

        double size =  (Math.random() * 100);

        double maxHealth = size;

        double offense =  (Math.random() * 100);
//        double offense = size;

        double defense =  (Math.random() * 100);
//        double defense = size;
        double maxHunger = (Math.random() * 100);
        double maxThirst = (Math.random() * 100);

        double aggression = (Math.random() * 100);

        double mutationRate = (Math.random() / 10);

        int vision = (int) (Math.random() * 30);

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
