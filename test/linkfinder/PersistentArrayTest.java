package linkfinder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import linkfinder.PersistentArray;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Maciej
 */
public class PersistentArrayTest {
    
    public PersistentArrayTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void hello() {
        try {
            String fileName = "testFile.bin";
            int arrayLength = 25;
            int initialValue = 25;
            PersistentArray.initialize(fileName, arrayLength, initialValue);
            PersistentArray pa = new PersistentArray(fileName);
            
            pa.set(3, 32);
            assertTrue(pa.get(3) == 32);
            assertTrue(pa.getLength() == arrayLength);
            pa.set(3, 12);
            assertTrue(pa.get(3) == 12);
            assertFalse(pa.get(3) == 32);
            assertTrue(pa.getLength() == arrayLength);
            assertTrue(PersistentArray.delete(fileName));
        } catch (IOException ex) {
            System.out.println("TEST FAILED: " + ex);
        }
         
     }
}
