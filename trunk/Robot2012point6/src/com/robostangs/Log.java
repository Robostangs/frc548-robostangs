package com.robostangs;

import com.sun.squawk.microedition.io.FileConnection;
import edu.wpi.first.wpilibj.Timer;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;

/*
 * Singleton. could cause problems with multiple writes, probably bad practice.
 * just didnt feel like passing it.
 */

public class Log{
    private static Log _instance = null;
    private PrintStream output; 
    private Date current;
    
    private Log() throws IOException{
        current = new Date();
        String date = current.toString();
        date = date.replace(':','_');
        try 
        {
            FileConnection fconn = (FileConnection)
            Connector.open("file:///"+date+Timer.getFPGATimestamp()+".csv" , Connector.READ_WRITE);
            if (!fconn.exists())
            {
                fconn.create(); 
            }
            OutputStream out = fconn.openOutputStream();
            output = new PrintStream( out );
        }
        catch( ConnectionNotFoundException error )
        {  
            System.out.println("connection Not Found " + error);
        }
        catch( IOException error )
        {
            System.out.println("IOE");
            System.out.println(error);
        }
    }
    
    public static synchronized Log getInstance(){
        if(_instance == null){
            try {
                _instance = new Log();
            } catch (IOException ex) {
                System.out.println("Failed to create Log instance:\n");
                ex.printStackTrace();
            }
        }
        return _instance;
    }
    
    public void write(String data){
        output.println(data);
    }   
}