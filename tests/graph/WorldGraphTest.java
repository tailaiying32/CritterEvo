package graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.awt.Point;
import model.Critter;
import model.CritterFactory;
import model.WorldFactory;
import model.WorldModel;
import model.WorldModel.CellState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class WorldGraphTest {
//    @DisplayName("WHEN a world graph is created,"
//            + "THEN it should initialize the vertex graph complete with"
//            + "edge lists")
//    @Test
//    public void testConstruction() {
//        WorldModel world = new WorldModel(5, 5, 0,0,0,0,0);
//        for (int i = 0; i < 5; i++) {
//            for (int j = 0; j < 5; j++) {
//                world.getWorldArray()[i][j] = CellState.GRASS;
//            }
//        }
//        WorldGraph graph = new WorldGraph(world);
//        assertEquals(5 * 5, graph.vertexCount());
//
//        CritterFactory critterFactory = new CritterFactory();
//        Critter critter1 = critterFactory.generateCritter(new Point(0, 0), world);
//        world.addCritter(critter1);
//        assertEquals(5 * 5 - 1, graph.vertexCount());
//        assertNull(graph.getVertex(graph.calculateId(0, 0)));
//        assertEquals(7, graph.getVertex(graph.calculateId(1, 1)).outgoingEdges().size());
//    }
}
