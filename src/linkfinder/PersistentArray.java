package linkfinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class PersistentArray {
    private static RandomAccessFile file = null;
    
    public PersistentArray(String arrayFileName){
        try {
            file = new RandomAccessFile(arrayFileName, "rws");
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        }
    }
    
    public static void initialize(String arrayFileName, int arraySize, long initialValue) throws IOException{
        //Wait how does this work again?
        file = new RandomAccessFile(arrayFileName, "rws");
        
        for(int i = 0; i < arraySize; i++){
            try {
                file.writeLong(initialValue);
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }
       
        
    }
    
    public void set(int index, long value) throws IOException{
        file.seek(0);
        file.seek(index*8);
        file.writeLong(value);
    }
    public long get(int index) throws IOException{
        file.seek(0);
        file.seek(index*8);
        return file.readLong();
        
    }
    public long getLength() throws IOException{
        return file.length()/8;
    }
    public void close() throws IOException{
        file.close();
    }
    public static void delete(String arrayFileName){
        File newFile = new File(arrayFileName);
        newFile.delete();
    }
    
}
