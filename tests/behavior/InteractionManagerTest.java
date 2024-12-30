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
        critter.rotate(Orientation.N);
        assertEquals(100, critter.getHunger());

        critter.rotate(Orientation.S);
        assertEquals(critter.getHunger() - (int) ((0.0016 * Math.pow(size, 2))), critter.getHunger());

        critter.rotate(Orientation.W);
        assertEquals(critter.getHunger() - (int) ((0.0008 * Math.pow(size, 2))), critter.getHunger());

        critter.rotate(Orientation.NE);
        assertEquals(critter.getHunger() - (int) ((0.0012 * Math.pow(size, 2))), critter.getHunger());

        critter.rotate(Orientation.E);
        assertEquals(critter.getHunger() - (int) ((0.0004 * Math.pow(size, 2))), critter.getHunger());

    }
}


