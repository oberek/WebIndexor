package linkfinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class PersistentArray {
    private static RandomAccessFile file = null;
    private static int arraySize;
    public PersistentArray(String arrayFileName){
        try {
            file = new RandomAccessFile(arrayFileName, "rws");
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        }
    }
    
    public static void initialize(String arrayFileName, int arraySize, long initialValue) throws IOException{
        PersistentArray.arraySize = arraySize;
        file = new RandomAccessFile(arrayFileName, "rws");
        
        for(int i = 0; i < arraySize; i++){
            try {
                file.writeLong(initialValue);
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }
        file.close();
    }
    
    public void set(int index, long value) throws IOException{
        file.seek(index*8);
        file.writeLong(value);
    }
    public long get(int index) throws IOException{
        
        file.seek(index * 8);
        return file.readLong();
        
        
    }
    public long getLength() throws IOException{
        return file.length()/8;
    }
    public void close() throws IOException{
        file.close();
    }
    public static boolean delete(String arrayFileName){
        File newFile = new File(arrayFileName);
        newFile.delete();
        return true;
    }
    
}
