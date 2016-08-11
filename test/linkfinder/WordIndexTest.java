/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package linkfinder;

import java.io.IOException;
import java.util.Iterator;
import linkfinder.WordIndex.URLEntry;
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
public class WordIndexTest {
    
    public WordIndexTest() {
    }

    @Test
    public void test() throws IOException {
        String wordIndexName = "wordIndex.bin";
        String sampleWord1 = "Cat";
        String sampleWord2 = "Dog";
        String sampleUrl1 = "http://www.google.com";
        String sampleUrl2 = "http://www.yahoo.com";
        String sampleUrl3 = "http://www.facebook.com";
        
        WordIndex.initialize(wordIndexName, 25);
        WordIndex wi = new WordIndex(wordIndexName);
        
            wi.add(sampleWord1, sampleUrl3);
            wi.add(sampleWord1, sampleUrl3);
            wi.add(sampleWord1, sampleUrl3);
            wi.add(sampleWord1, sampleUrl3);
            
            System.out.println();         
            wi.add(sampleWord2, sampleUrl2);   
            wi.add(sampleWord2, sampleUrl2);            
            wi.add(sampleWord2, sampleUrl3);
            wi.add(sampleWord2, sampleUrl1);            
            
            Iterator<URLEntry> test = wi.getURLs(sampleWord1);
            while(test.hasNext()){
                URLEntry urlEntry = test.next();
                System.out.println("URL: [" + urlEntry.getURL() + "]");
                System.out.println("URL COUNT: " + urlEntry.getCount()); 
                assertTrue(urlEntry.getURL().equals(sampleUrl3));
            }

            Iterator<URLEntry> test2 = wi.getURLs(sampleWord2);

            while(test2.hasNext()){
                URLEntry urlEntry = test2.next();
                
                System.out.println("URL: " + urlEntry.getURL());
                System.out.println("URL COUNT: " + urlEntry.getCount());
                assertTrue(urlEntry.getURL().equals(sampleUrl3) || urlEntry.getURL().equals(sampleUrl2) || urlEntry.getURL().equals(sampleUrl1));
            }
        
       
            wi.close();
            WordIndex.delete(wordIndexName);
        
    }
}
