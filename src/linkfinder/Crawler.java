package linkfinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crawler implements VisitAction{
    List<URL> toVisit = new ArrayList<>();
    List<URL> alreadyVisited = new ArrayList<>();
    URL originalURL;
    int maxPagesToVisit;
    
    public Crawler(URL url, int maxPagesToVisit){
        toVisit.add(url);
        originalURL = url;
        this.maxPagesToVisit = maxPagesToVisit;
        System.out.println("Pages to visit for links: " + maxPagesToVisit);
        
    }
    public void crawl() throws MalformedURLException{  
        int counter = 0;
        while(!toVisit.isEmpty() && counter < maxPagesToVisit){
            Iterator it = visitUrl(toVisit.get(counter));
            while(it.hasNext()){
                URL temp = (URL) it.next();
                if(!toVisit.contains(temp) && !alreadyVisited.contains(temp)){
                    System.out.println("NEW LINK: " + temp);
                    toVisit.add(temp);
                }
                alreadyVisited.add(temp);
            }
            counter++;
        }
    }
    
    //HERE GOES
    @Override
    public Iterator<URL> visitUrl(URL url) {
        String html = null;
        Iterator iterator = null;
        Iterator wordIterator = null;
        try {
            html = getContent(url);
        } catch (IOException ex) {
            System.err.print(ex);
        }
        
        try {
            wordIterator = getWords(html);
            iterator = getLinks(html);
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return iterator;
    }

    private String getContent(URL url) throws IOException {
        
        String inputLine = "";
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()))) {
            String line;
            while ((line = in.readLine()) != null){    
                inputLine = inputLine + " " + line;
            }
        }
        return inputLine;
    }
    
    public Iterator<URL> getLinks(String input) throws UnsupportedEncodingException, IOException{     
        
        String patternString = "[Hh][Rr][Ee][Ff]\\s?=\\s?\"(.*?)\"";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(input);
        
        List<URL> links = new ArrayList<>();
        while(matcher.find()){
            String group = matcher.group();
            group = group.substring(6,group.length()-1);

            //Adds link to the link ArrayList
            if(!(group.contains(".png") || group.contains(".css")) && group.length() > 2){

                if(!group.contains("http"))
                    group = originalURL + "/" + group;
                if(group.contains("u//")){
                    group = group.replace("u///", "u/");
                }
                URL tempURL = null;
                if((group.contains(".html")||group.contains(".htm")))
                    tempURL = new URL(group);
                if(!links.contains(tempURL)){
                    //System.out.println(group);
                    links.add(tempURL); 
                }
            }
        }
        
        Iterator ir = links.iterator();
        return ir;
    }

    private Iterator<String> getWords(String html) {
        Iterator it = null;
        String htmlCopy = null;
        
        htmlCopy = html.replaceAll("<[^>]*>", "");
        htmlCopy = htmlCopy.replaceAll("\\W", " ");
        htmlCopy = htmlCopy.replaceAll("[\\d]", "");
        htmlCopy = htmlCopy.replaceAll("\\s+", " ");
        it= Arrays.asList(htmlCopy.split(" ")).iterator();
        //System.out.println(htmlCopy);
        return it;
    }
}
