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
    public Critter generateCritter(Point p) {
        CritterAI critterai = new CritterAI();

        // random number generator for attributes for critter construction
        Orientation[] orientations = Orientation.values();
        Orientation orientation = orientations[ThreadLocalRandom.current().nextInt(orientations.length)];

//        double or = Math.random() * 4;
//        Orientation orientation = null;
//        if (or <= 1) {
//            orientation = Orientation.N;
//        } else if (or <= 2) {
//            orientation = Orientation.S;
//        } else if (or <= 3) {
//            orientation = Orientation.E;
//        } else if (or <= 4) {
//            orientation = Orientation.W;
//        }

        int maxAge = (int) (Math.random() * 100);

        int maxHealth = (int) (Math.random() * 100);

        Sex[] sexes = Sex.values();
        Sex sex = sexes[ThreadLocalRandom.current().nextInt(sexes.length)];
//        double sx = Math.random() * 2;
//        Sex sex = null;
//        if (sx <= 1) {
//            sex = Sex.MALE;
//        } else if (sx <= 2) {
//            sex = Sex.FEMALE;
//        }

        int size = (int) (Math.random() * 100);

        int offense = (int) (Math.random() * 100);

        int defense = (int) (Math.random() * 100);

        int aggression = (int) (Math.random() * 100);

        int mutationRate = (int) (Math.random() * 10);

        Critter critter = new Critter(
                critterai,
                new InteractionManager(),
                p,
                orientation,
                maxAge,
                100,
                100,
                maxHealth,
                sex,
                size,
                offense,
                defense,
                aggression,
                0,
                mutationRate
        );

        return critter;
    }

}
