/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package linkfinder;

import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Maciej
 */
public class LinkFinder {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException {
        // TODO code application logic here
        Crawler crawler = null;
        try{
            crawler = new Crawler(new URL("http://shalladay-iis1.neumont.edu/"),30);
        }
        catch(MalformedURLException e){
            System.err.print(e);
        }
        crawler.crawl();
    }
    
}
