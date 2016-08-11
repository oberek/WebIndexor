/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package linkfinder;

import java.io.IOException;
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
public class ListFileTest {
    
    public ListFileTest() {
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
    // @Test
    // public void hello() {}
    @Test
    public void test(){
        try{
            String listFileName = "listFile.bin";
            ListFile.initialize(listFileName);
            ListFile lf = new ListFile(listFileName);
            
            Entry bob = new Entry("Bob", 2, 30);
            Entry susan = new Entry("Susan", 5, 45);

            long bobOffset      = lf.newEntry(bob);
            long susanOffset    = lf.newEntry(susan);
            
            assertTrue(lf.getEntry(bobOffset).getLink()==30);
            assertTrue(lf.getEntry(bobOffset).getString().equals("Bob"));
            assertFalse(lf.getEntry(bobOffset).getString().equals("Susan"));
            
            assertTrue(lf.getEntry(susanOffset).getString().equals("Susan"));
            assertTrue(lf.getEntry(susanOffset).getValue() == 5);
            assertFalse(lf.getEntry(susanOffset).getValue() == 30);
            
            assertTrue(lf.close());
            assertTrue(ListFile.delete(listFileName));
            
        }catch(IOException ex){
            System.out.println(ex);
        }
    }
}
