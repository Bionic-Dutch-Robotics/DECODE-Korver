package org.firstinspires.ftc.teamcode.util;

import java.io.File;                    // already used in FTC SDK
import java.io.Writer;
import java.io.IOException;
import java.io.FileWriter;              // subclass of java.io.Writer
//import java.lang.ref.Cleaner;

@SuppressWarnings("all")
public class DataLogger {
    
    // Declare members.
    private Writer writer;              // contains write() method to store file
    private StringBuffer lineBuffer;    // its methods build each line (row) of data
    private long timeBase;              // time of instantiation (milliseconds)
    private long nsBase;                 // time of reset (nanoseconds)
    //private static Cleaner cleaner;
    public DataLogger (String fileName) {   // This constructor runs once, to initialize an instantiation of the class.
        
        // Build the path with the filename provided by the calling OpMode.
        String directoryPath    = "/sdcard/FIRST/java/src/Datalogs";
        String filePath         = directoryPath + "/" + fileName + ".txt";
        
        // src and any subfolder contents appear in OnBot Java (left side).
        // .txt files allow data display in OBJ.  Download as .csv files.
        
        new File(directoryPath).mkdir();  // create Datalogs folder if needed


        // Set up the file writer and line buffer.
        try {
            writer = new FileWriter(filePath);
            lineBuffer = new StringBuffer(128);     // initial length 128

            /*
            cleaner = Cleaner.create();
            cleaner.register(this, ()-> {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            });
            */

        }
        catch (IOException ignored) {}
        
        timeBase = System.currentTimeMillis();
        nsBase = System.nanoTime();
        addField("Time");               // first/default column label
        addField("d ms");               // second/default column label
        
    }   // end constructor


    // This *private* method is called by the *public* methods firstLine()
    // and newLine().
    private void flushLineBuffer(){

        try {
            lineBuffer.append('\n');                // end-of-line character
            writer.write(lineBuffer.toString());    // add line (row) to file
            lineBuffer.setLength(0);                // clear the line (row)
        }
        catch (IOException ignored) {
        }
        
    }   // end flushLineBuffer() method
    

    // This *private* method is called by the *public* method newLine().
    private void insertTimestamps(){
        
        long milliTime,nanoTime;

        // Update time for first two columns (cumulative and incremental time).
        milliTime   = System.currentTimeMillis();
        nanoTime    = System.nanoTime();

        // Insert timestamps at position 0, *before* the OpMode data fields.
        lineBuffer.insert
            
            (0, String.format("%.3f",(milliTime - timeBase) / 1000.0) + ','
              + String.format("%.3f",(nanoTime - nsBase) / 1.0E6) + ',');

        // Divide milliseconds by 1,000 to log seconds, in field named "Time".
        // Divide nanoseconds by 1,000,000 to log milliseconds, in "d ms".

        // The 1000.0 decimal and 1.0E6 scientific notation avoid a type error;
        // the expressions' variables are 'long'.

        nsBase      = nanoTime;         // reset for incremental time delta

    }   // end insertTimestamps() method
    

    // The OpMode calls this *public* method to complete the first row (labels).
    public void firstLine() {
        flushLineBuffer();
    }

    // The OpMode calls this *public* method to add timestamps and complete the
    // current line (row) of data.
    public void newLine() {
        insertTimestamps();
        flushLineBuffer();
    }
    
    
    // These two (overloaded) methods add a text field to the line (row),
    // preceded by a comma.  This creates the comma-separated values (CSV).
    
    public void addField(String s) {
        if (lineBuffer.length()>0) {
            lineBuffer.append(',');
        }
        lineBuffer.append(s);
    }

    public void addField(char c) {
        if (lineBuffer.length()>0) {
            lineBuffer.append(',');
        }
        lineBuffer.append(c);
    }
    // Checking the line length (before inserting a comma) is not needed when a 
    // default timestamp (and its comma) will be inserted before all data, as in
    // the current example. The check is here in case the default timestamp is removed.
    
    
    // The following (overloaded) method converts Boolean to text 
    // (Java type char) and adds it to the current line (row).
    
    public void addField(boolean b) {
        addField(b ? '1' : '0');
    }

    // These (overloaded) methods accept various numeric types,
    // all converted to type String for the method listed above.
    // Spreadsheet programs typically interpret these correctly as numbers.

    public void addField(byte b) {
        addField(Byte.toString(b));
    }

    public void addField(short s) {
        addField(Short.toString(s));
    }

    public void addField(long l) {
        addField(Long.toString(l));
    }

    public void addField(float f) {
        addField(Float.toString(f));
    }

    public void addField(double d) {
        addField(Double.toString(d));
    }

    // Any 'int' values are processed as 'long', through Java's implicit
    // type casting or type promotion.


    // The OpMode calls this method to allow optional reset of timers.
    public void resetTime() {
        timeBase = System.currentTimeMillis();
        nsBase = System.nanoTime();
    }


    // The OpMode must call this method when finished logging data.
    public void closeDataLogger() {
        try {
            writer.close();             // close the file
        }
        catch (IOException e) {
        }
    }


    // This method provides garbage collection and improves exception handling.
    @Override
    protected void finalize() throws Throwable {
        closeDataLogger();
        super.finalize();
        //cleaner.clean();
        //System.gc();
    }
    
}   // end class