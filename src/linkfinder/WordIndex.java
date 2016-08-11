package linkfinder;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WordIndex {
    private PersistentArray hashindex;
    private ListFile wordLists, urlLists;
    
    public class URLEntry{
        private String url;
        private long count;
        
        public URLEntry(String url, long count){
         this.url = url;
         this.count = count;
        }
        public String getURL(){
            return url;
        }
        public long getCount(){
            return count;
        }
    }
    
    public static void initialize(String indexName, long indexSize) throws IOException{
        PersistentArray.initialize("NumberArray"+indexName, (int)indexSize, -1);
        ListFile.initialize("URLOffset"+indexName);
        ListFile.initialize("URLCounter"+indexName);
    }
    public static void delete(String indexName){
        ListFile.delete("URLOffset"+indexName);
        ListFile.delete("URLCounter"+indexName);
        PersistentArray.delete("NumberArray"+indexName);
    }
    public WordIndex(String indexName){
        try {
            hashindex = new PersistentArray("NumberArray"+indexName);
            wordLists = new ListFile("URLOffset"+indexName);
            urlLists = new ListFile("URLCounter"+indexName);

        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
    public void close() throws IOException{
        hashindex.close();
        wordLists.close();
        urlLists.close();
    }
    public void add(String word, String url) throws IOException{
        Entry wordEntry, urlEntry = null;
        long wordEntryWordOffset = -1;
        System.out.println("HASH INDEX LENGTH: " +hashindex.getLength());
        // First hash the word and mod it by the size of the hashindex
        int hashedWordIndex = (int) (word.hashCode() % hashindex.getLength());
        // Check to see if the index at the hashed word is -1, meaning uninitialized
        // If it is, initialize it with a word entry
        long hashindexOffsetCheck = hashindex.get(hashedWordIndex);
        if(hashindexOffsetCheck == -1){
            wordEntry = new Entry(word, -1, -1);
            wordEntryWordOffset = wordLists.newEntry(wordEntry);
            hashindex.set(hashedWordIndex, wordEntryWordOffset);
        }
        else{
            wordEntry = wordLists.getEntry(hashindex.get(hashedWordIndex));
            while(!wordEntry.getString().equals(word)){
                if(wordEntry.getLink() != -1){
                    wordEntry = wordLists.getEntry(wordEntry.getLink());
                }
                //This means that the word isn't registered yet
                else{
                    Entry tempNewWordEntry = new Entry(word, -1, -1);
                    wordEntryWordOffset = wordLists.newEntry(tempNewWordEntry);
                    wordEntry.setLink(wordEntryWordOffset);
                    wordEntry = wordLists.getEntry(wordEntryWordOffset);
                }
            }
        }
        long urlEntryOffset = wordEntry.getValue();
        // If wordEntry does not have a url attached to it yet
        
            if(urlEntryOffset == -1){
                urlEntry = new Entry(url, 1, -1);
                urlEntryOffset = urlLists.newEntry(urlEntry);
                System.out.println("Linking " + wordEntry.getString() + " to " + url + " at " + urlEntryOffset);
                wordEntry.setValue(urlEntryOffset);
                System.out.println("WORD OFFSET: " + wordEntryWordOffset);
                wordLists.putEntry(wordEntryWordOffset, wordEntry);
            }
            else{
                urlEntry = urlLists.getEntry(urlEntryOffset);

                while(!urlEntry.getString().equals(url)){
                    if(urlEntry.getLink()!= -1){
                        urlEntryOffset = urlEntry.getLink();
                        urlEntry = urlLists.getEntry(urlEntryOffset);
                    }else{
                        Entry tempUrlEntry = new Entry(url, 0, -1);
                        long newEntryUrlPointer = urlLists.newEntry(tempUrlEntry);
                        urlEntry.setLink(newEntryUrlPointer);
                        urlLists.putEntry(urlEntryOffset, urlEntry);
                        urlEntryOffset = newEntryUrlPointer;
                        urlEntry = urlLists.getEntry(newEntryUrlPointer);
                    }
                }
                urlEntry.setValue(urlEntry.getValue()+1);
                urlLists.putEntry(urlEntryOffset, urlEntry);
            
        }
        
        // Otherwise, give check if that word entry is the right word entry
        
        // If not, check for a link 
        // If that link is -1, make a new word entry
        // and link the old word entry to this new one
        
        // Once you have the right word entry, check to see if there is a link to another offset
        // If the link is -1, create a new url entry and set its count to 1
        
        // If there is a link, go to it
        
        // If the url matches, increment count by one
        
        // Otherwise, check if there's a link
        // If there is a link, go to it and check Urls against each other
        // If not, create a new URL and link it to the old one
        
        
    }
    public Iterator<URLEntry> getURLs(String word) throws IOException{
        Iterator<URLEntry> URLEntryIterator;
        List<URLEntry> URLEntryList = new ArrayList<>(); 
        int hashIndexOffset = (int) (word.hashCode() % hashindex.getLength());
        
        System.out.println("Word: " + word + ", HashCode: " + hashIndexOffset);
        Entry wordEntry, urlEntry;
        
        wordEntry = wordLists.getEntry(hashindex.get(hashIndexOffset));
        
        while(!wordEntry.getString().equals(word)){
            if(wordEntry.getLink() != -1){
                wordEntry = wordLists.getEntry(wordEntry.getLink());
            }
            else{
                System.out.println("Internal Problem: Word Does Not Exist or Connection Issue");
            }
        }
        System.out.println("WORD ENTRY VALUE: " + wordEntry.getValue() + ", WORD: " + wordEntry.getString());
//        urlEntry = urlLists.getEntry(wordEntry.getValue());
//        do{
//            URLEntryList.add(new URLEntry(urlEntry.getString(), urlEntry.getValue()));
//            urlEntry = urlLists.getEntry(urlEntry.getLink());
//        }while(urlEntry.getLink() != -1);
        
        long offset = wordEntry.getValue();
        while(offset != -1){
            urlEntry = urlLists.getEntry(offset);
            URLEntryList.add(new URLEntry(urlEntry.getString(), urlEntry.getValue()));
            offset = urlEntry.getLink();
        }
        System.out.println("NUMBER OF ENTRIES: " + URLEntryList.size());

        URLEntryIterator = URLEntryList.iterator();
        return URLEntryIterator;
    }
}
