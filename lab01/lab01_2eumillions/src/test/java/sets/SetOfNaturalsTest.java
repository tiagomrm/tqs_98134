/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sets;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

/**
 * @author ico0
 */
public class SetOfNaturalsTest {
    private SetOfNaturals setA;
    private SetOfNaturals setB;
    private SetOfNaturals setC;
    private SetOfNaturals setD;

    @BeforeEach
    public void setUp() {
        setA = new SetOfNaturals();
        setB = SetOfNaturals.fromArray(new int[]{10, 20, 30, 40, 50, 60});

        setC = new SetOfNaturals();
        for (int i = 5; i < 50; i++) {
            setC.add(i * 10);
        }
        setD = SetOfNaturals.fromArray(new int[]{30, 40, 50, 60, 10, 20});
    }

    @AfterEach
    public void tearDown() {
        setA = setB = setC = setD = null;
    }

    @Test
    public void testAddElement() {

        setA.add(99);
        assertTrue(setA.contains(99), "add: added element not found in set.");
        assertEquals(1, setA.size());

        setB.add(11);
        assertTrue(setB.contains(11), "add: added element not found in set.");
        assertEquals(7, setB.size(), "add: elements count not as expected.");
    }

    @Test
    public void testAddBadArray() {
        int[] elems = new int[]{10, 20, -30};

        // must fail with exception
        assertThrows(IllegalArgumentException.class, () -> setA.add(elems));
    }


    @Test
    public void testIntersectForNoIntersection() {
        assertFalse(setA.intersects(setB), "no intersection but was reported as existing");

    }

    // ADDED TESTS

    @Test
    public void testIntersect() {
        assertTrue(setC.intersects(setD), "intersection but was reported as non existing");
    }


    @Test
    public void testEquals() {
        assertEquals(setB, setD, "equal sets are reported as different");
    }


    @Test
    public void testNotEquals() {
        assertNotEquals(setA, setD, "different sets are reported as equal");
    }


    @Test
    public void testEqualsSameInstance() {
        assertEquals(setC, setC, "set compared to itself reported as different");
    }


    @Test
    public void testNotEqualsObject() {
        assertNotEquals(setC, new Object(), "set compared to object of another class reported as true");
    }


    @Test
    public void testSize() {
        assertEquals(0, setA.size(), "empty set does not have size 0");
    }


    @Test
    public void testAddDuplicateElement() {
        assertThrows(IllegalArgumentException.class, ()-> setB.add(10),
                "adding duplicate elements does not throw IllegalArgumentException");
    }
}
