package behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Point;
import model.Critter;
import model.Critter.Orientation;
import model.CritterFactory;
import model.Food;
import model.Water;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class InteractionManagerTest {

    @DisplayName("WHEN a critter eats food,"
            + "THEN its hunger should replenish the same amount as the "
            + "food's quantity")
    @Test
    public void testEatFood() {
        CritterFactory factory = new CritterFactory();
        Critter critter = factory.generateCritter(new Point(0, 0));
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
        Critter critter = factory.generateCritter(new Point(0, 0));
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
        Critter critter = factory.generateCritter(new Point(0, 0));
        critter.setMaxHunger(100);
        critter.setHunger(100);
        critter.setOrientation(Orientation.N);

        int size = critter.getSize();
        int hungerTester = critter.getHunger();

        critter.rotate(Orientation.N);
        assertEquals(100, critter.getHunger());
        assertEquals(Orientation.N, critter.getOrientation());

        critter.rotate(Orientation.S);
        assertEquals(hungerTester - (int) (1 + 4 * (0.0004 * Math.pow(size, 2))), critter.getHunger());
        assertEquals(Orientation.S, critter.getOrientation());
        hungerTester = critter.getHunger();

        critter.rotate(Orientation.W);
        assertEquals(hungerTester - (int) (1 + 2 * (0.0004 * Math.pow(size, 2))), critter.getHunger());
        assertEquals(Orientation.W, critter.getOrientation());
        hungerTester = critter.getHunger();

        critter.rotate(Orientation.NE);
        assertEquals(hungerTester - (int) (1 + 3 * (0.0004 * Math.pow(size, 2))), critter.getHunger());
        assertEquals(Orientation.NE, critter.getOrientation());
        hungerTester = critter.getHunger();

        critter.rotate(Orientation.E);
        assertEquals(hungerTester - (int) (1 + 0.0004 * Math.pow(size, 2)), critter.getHunger());
        assertEquals(Orientation.E, critter.getOrientation());
        hungerTester = critter.getHunger();
    }

    @DisplayName("WHEN a critter moves"
            + "THEN its coordinates should be updated"
            + "AND its hunger should be decreased by a proportional amount")
    @Test
    public void testMove() {
        CritterFactory factory = new CritterFactory();
        Critter critter = factory.generateCritter(new Point(0, 0));
        critter.setMaxHunger(100);
        critter.setHunger(100);
        critter.setOrientation(Orientation.S);

        int size = critter.getSize();
        int hungerTester = critter.getHunger();

        critter.move(1);
        assertEquals(hungerTester - (int) (1 + 0.002*Math.pow(size, 2)), critter.getHunger());
        assertEquals(new Point(0, 1), critter.getPosition());


        critter.setHunger(1);
        critter.move(2);
        assertEquals(0, critter.getHunger());
        assertEquals(new Point(0, 3), critter.getPosition());
    }

    @Test
    public void testAttack(){
        CritterFactory factory = new CritterFactory();
        Critter critter1 = factory.generateCritter(new Point(0, 0));
        critter1.setHealth(100);
        Critter critter2 = factory.generateCritter(new Point(0, 1));
        critter2.setHealth(100);

        int size1 = critter1.getSize();
        int size2 = critter2.getSize();
        int baseDamage = 25;
        double factor = 1.3;
        int offense1 = critter1.getOffense();
        int defense2 = critter2.getDefense();

        critter1.attack(critter2);
        assertEquals((int) Math.pow(baseDamage*((size1*offense1)/(size2*defense2)), factor), critter2.getHealth());
    }
}


