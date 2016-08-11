package linkfinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ListFile {
    private static RandomAccessFile file = null;
    
    public static void initialize(String listFileName) throws FileNotFoundException, IOException{
        file = new RandomAccessFile(listFileName, "rws");
        file.close();
    }

    public static boolean delete(String listFileName){
        File newFile = new File(listFileName);
        newFile.delete();
        return true;
    }

    public ListFile(String listFileName){
        try {
            file = new RandomAccessFile(listFileName, "rws");
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    public boolean close(){
        try {
            file.close();
            return true;
        } catch (IOException ex) {
            System.out.println(ex);
            return false;
        }
    }

    public long newEntry(Entry entry) throws IOException{
        System.out.println("FILE LENGTH: " + file.length());
        long pointer = file.length();
        putEntry(pointer, entry);
        System.out.println("POINTER: " + pointer);
        return pointer;
    }

    public Entry getEntry(long offset) throws IOException{
        file.seek(offset);
        Entry entry = null;
        
        try{
            int stringLength = file.readInt();
            String s = "";
            for(int i = 0; i < stringLength; i++){
                char c = (char) file.read();
                s = s + c;
            }
            long value = file.readLong();
            long link = file.readLong();
            entry = new Entry(s,value, link);
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return entry;
    }

    public void putEntry(long offset, Entry entry) throws IOException{
        file.seek(offset);
        file.writeInt(entry.getString().length());
        file.writeBytes(entry.getString());
        file.writeLong(entry.getValue());
        file.writeLong(entry.getLink());
    }
}
