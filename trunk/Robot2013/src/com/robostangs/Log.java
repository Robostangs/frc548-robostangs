package com.robostangs;

import com.sun.squawk.microedition.io.FileConnection;
import edu.wpi.first.wpilibj.Timer;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;

/**
 *
 * @author Robostangs
 */
public class Log {
    private static Log instance = null;
    private static PrintStream output;
    private static Date date;
    
    private Log() {
        date = new Date();
        String d = date.toString();
        d = d.replace(':', '_');
        
        try {
            FileConnection connection = (FileConnection)
                    Connector.open("file:///" + d + Timer.getFPGATimestamp() + ".csv", Connector.READ_WRITE);
            if (!connection.exists()) {
                connection.create();
            }
            OutputStream out = connection.openOutputStream();
            output = new PrintStream(out);
        } catch (ConnectionNotFoundException error) {
            System.out.println("couldn't connect to file" + error);
        } catch (IOException error) {
            System.out.println("IOE" + error);
        }
    }
    
    public static Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        
        return instance;
    }
    
    public static void write(String data) {
        output.println(data);
    }
}
