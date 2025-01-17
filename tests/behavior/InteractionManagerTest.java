package behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Point;
import model.Critter;
import model.Critter.Orientation;
import controller.CritterFactory;
import model.Food;
import model.Water;
import model.WorldFactory;
import model.WorldModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class InteractionManagerTest {

    @DisplayName("WHEN a critter eats food,"
            + "THEN its hunger should replenish the same amount as the "
            + "food's quantity")
    @Test
    public void testEatFood() {
        CritterFactory factory = new CritterFactory();
        WorldFactory worldFactory = new WorldFactory();
        Critter critter = factory.generateCritter(new Point(0, 0), worldFactory.generateTestWorld());
        critter.setMaxHunger(100);
        critter.setHunger(50);
        Food food = new Food(new Point(1, 1), 40, 1);
        critter.eat(food);
        assertEquals(90, critter.getHunger());

        critter.eat(food);
        assertEquals(100, critter.getHunger());

    }

    @DisplayName("WHEN a critter drinks water, THEN its thirst should fully replenish")
    @Test
    public void testDrinkWater() {
        CritterFactory factory = new CritterFactory();
        WorldFactory worldFactory = new WorldFactory();
        Critter critter = factory.generateCritter(new Point(0, 0), worldFactory.generateTestWorld());
        Water water = new Water(new Point(1, 1), 1);
        critter.setMaxThirst(100);
        critter.drink(water);
        assertEquals(critter.getThirst(), critter.getMaxThirst());
    }

    @DisplayName("WHEN a critter rotates,"
            + "THEN its orientation should be updated"
            + "AND it's hunger should be depleted a small amount")
    @Test
    public void testRotate() {
        CritterFactory factory = new CritterFactory();
        WorldFactory worldFactory = new WorldFactory();
        Critter critter = factory.generateCritter(new Point(0, 0), worldFactory.generateTestWorld());
        critter.setMaxHunger(100);
        critter.setHunger(100);
        critter.setOrientation(Orientation.N);

        double size = critter.getSize();
        double hungerTester = critter.getHunger();

        critter.rotate(Orientation.N);
        assertEquals(100, critter.getHunger());
        assertEquals(Orientation.N, critter.getOrientation());

        critter.rotate(Orientation.S);
        assertEquals(hungerTester - (1.0 + 4.0 * (0.0004 * Math.pow(size, 2))), critter.getHunger());
        assertEquals(Orientation.S, critter.getOrientation());
        hungerTester = critter.getHunger();

        critter.rotate(Orientation.W);
        assertEquals(hungerTester -  (1.0 + 2.0 * (0.0004 * Math.pow(size, 2))), critter.getHunger());
        assertEquals(Orientation.W, critter.getOrientation());
        hungerTester = critter.getHunger();

        critter.rotate(Orientation.NE);
        assertEquals(hungerTester -  (1.0 + 3.0 * (0.0004 * Math.pow(size, 2))), critter.getHunger());
        assertEquals(Orientation.NE, critter.getOrientation());
        hungerTester = critter.getHunger();

        critter.rotate(Orientation.E);
        assertEquals(hungerTester -  (1 + 0.0004 * Math.pow(size, 2)), critter.getHunger());
        assertEquals(Orientation.E, critter.getOrientation());
        hungerTester = critter.getHunger();
    }

    @DisplayName("WHEN a critter moves"
            + "THEN its coordinates should be updated"
            + "AND its hunger should be decreased by a proportional amount")
    @Test
    public void testMove() {
        CritterFactory factory = new CritterFactory();
        WorldFactory worldFactory = new WorldFactory();

        Critter critter = factory.generateCritter(new Point(0, 0), worldFactory.generateTestWorld());
        critter.setMaxHunger(100);
        critter.setHunger(100);
        critter.setOrientation(Orientation.S);

        double size = critter.getSize();
        double hungerTester = critter.getHunger();

        critter.move(1);
        assertEquals(hungerTester - (1 + 0.002*Math.pow(size, 2)), critter.getHunger());
        assertEquals(new Point(0, 1), critter.getPosition());


        critter.setHunger(1);
        critter.move(2);
        assertEquals(0, critter.getHunger());
        assertEquals(new Point(0, 3), critter.getPosition());
    }

    @DisplayName("test for attack yay")
    @Test
    public void testAttack(){
        CritterFactory factory = new CritterFactory();
        WorldFactory worldFactory = new WorldFactory();

        Critter critter1 = factory.generateCritter(new Point(0, 0), worldFactory.generateTestWorld());
        critter1.setHealth(100);
        Critter critter2 = factory.generateCritter(new Point(0, 1), worldFactory.generateTestWorld());
        critter2.setHealth(100);

        double size1 = critter1.getSize();
        double size2 = critter2.getSize();
        double baseDamage = 25;
        double factor = 1.3;
        double offense1 = critter1.getOffense();
        double defense2 = critter2.getDefense();

        critter1.attack(critter2);
        assertEquals(100 - Math.pow(baseDamage*((double) (size1 * offense1) /(size2*defense2)), factor), critter2.getHealth());
    }

    @DisplayName("WHEN a critter reproduces, THEN it should create a child behind it"
            + "WHEN a critter attempts to reproduce and there are no empty squares around it,"
            + "THEN nothing should happen"
            + "WHEN a critter attempts to reproduce and the square behind it is non-empty"
            + "THEN the child should be placed at a random empty square around it")
    @Test
    public void testReproduce() {
        CritterFactory factory = new CritterFactory();
        WorldFactory worldFactory = new WorldFactory();
        WorldModel world = worldFactory.generateTestWorld();
        Critter critter = factory.generateCritter(new Point(0, 0), world);
        critter.setOrientation(Orientation.N);

        critter.reproduce();
        assertNotNull(world.getCritter(new Point(0, 1)));

        critter.reproduce();
        critter.reproduce();
        assertNotNull(world.getCritter(new Point(1, 1)));
        assertNotNull(world.getCritter(new Point(1, 0)));
    }

    @DisplayName("WHEN a critter dies, THEN it should be removed from the world's list of critters"
            + "AND it should be turned into food")
    @Test
    public void testDie() {
        CritterFactory factory = new CritterFactory();
        WorldFactory worldFactory = new WorldFactory();
        WorldModel world = worldFactory.generateTestWorld();
        Critter critter = factory.generateCritter(new Point(0, 0), world);

        world.addCritter(critter);

        int currsize = world.getCritters().size();

        critter.die();
        assertEquals(currsize - 1, world.getCritters().size());
        assertNotNull(world.getFood(new Point(0, 0)));
    }
}


