package com.robostangs;

import com.sun.squawk.io.BufferedReader;
import com.sun.squawk.microedition.io.FileConnection;
import edu.wpi.first.wpilibj.Timer;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import javax.microedition.io.Connector;

/**
 * All functionality is read from dash; if that fails, runs a fallback mode
 * @author sky
 */

public class Autonomous {
    private static Autonomous instance = null;    
    private static Timer timer, secondary;
    private static boolean forward = false;
    private static boolean back = false;
    private static boolean shooting = false;
    private static boolean turning = false;
    private static boolean delay = false;
    private static boolean armMoving = false;
    private static boolean fallbackMode = false;
    private static boolean stepInit = true;
    private static boolean done = false;
    private static String[] keys;
    private static double[] stepData;
    private static int step = 0;
    private static String inputFileName = "auton.txt";
    private static Vector contents = new Vector();
    private static String line = "";
    
    
    private Autonomous() {
        timer = new Timer();
        secondary = new Timer();
        try {
            getInfo();
        } catch (IOException ex) {
            ex.printStackTrace();
            fallbackMode = true;
        }
        printMode();
    }
    
    public static Autonomous getInstance() {
        if (instance == null) {
            instance = new Autonomous();
        }
        return instance;
    }

    /**
     * shoots a frisbee
     * does not stop shooter when finished as to maintain momentum
     */
    public static void shoot() {
        if (stepInit) {
            secondary.stop();
            secondary.reset();
            System.out.println("secondary init: " + secondary.get());
            secondary.start();
            stepInit = false;
        }
        System.out.println("step secondary: " + secondary.get() + " " + step);
        Shooter.shoot();

        if (secondary.get() > 2.5) {
            Loader.loadShooter();
        }

        if (secondary.get() > 4.0) {
            Loader.allOff();
            secondary.reset();
        }
    }

    public static void setAngle() {
        if (Arm.getPot() > (Constants.AUTON_ARM_POS + 5) 
                || Arm.getPot() < (Constants.AUTON_ARM_POS - 5)) {
            Arm.setPosition(Constants.AUTON_ARM_POS);
        } else {
            Arm.stop();
        }
	}

    /**
     * Reads autonomous mode from a txt file
     */
    public static void getInfo() throws IOException{
        FileConnection fc = (FileConnection) Connector.open("file:///" + inputFileName, Connector.READ);
        BufferedReader in = new BufferedReader(new InputStreamReader(fc.openInputStream()));
        int commaPos = 0;
        int semiPos = 0;
        
        while ((line = in.readLine()) != null) {
            contents.addElement(line);
        }

        fc.close();
        contents.trimToSize();
        keys = new String[contents.size()];
        stepData = new double[contents.size()];
        
        for (int i = 0; i < contents.size(); i++) {
            line = (String) contents.elementAt(i);
            commaPos = line.indexOf(",", i);
            semiPos = line.indexOf(";", i);            
            keys[i] = line.substring(0, commaPos);
            stepData[i] = Double.parseDouble(line.substring(commaPos + 1, semiPos));
        }
    }

    public static void printKeys() {
        for (int i = 0; i < keys.length; i++) {
            System.out.println(keys[i]);
        }
    }
    public static void printMode() {
        for (int i = 0; i < keys.length; i++) {
            System.out.println(keys[i] + ": " + stepData[i]);
        }
    }
    
    /**
     * Checks to see if data is valid
     * @return true if data is good
     */
    public static void checkInfo() {
        //TODO: more fallback mode checks?
        if (keys == null) {
            fallbackMode = true;
        } else if (keys.length < 2) {
            fallbackMode = true;
        }
    }
    
    /*
     * uses info from text file, but doesn't actually move robot
     */
    public static void runText() {
        if (!done) {
            for (int i = 0; i < keys.length; i++) {    
                forward = keys[i].endsWith("Forward");
                back = keys[i].endsWith("Back");
                turning = keys[i].endsWith("Turn");
                shooting = keys[i].startsWith("shoot");
                armMoving = keys[i].startsWith("arm");
                delay = keys[i].startsWith("delay");

                timer.start();

                if (forward) {
                    while (timer.get() < stepData[i]) {
                        System.out.println("Driving forward for: " + stepData[i] + "; " 
                                + timer.get());
                    }

                    DriveTrain.stop();
                    timer.stop();
                    timer.reset();
                } else if (back) {
                    while (timer.get() < stepData[i]) {
                        System.out.println("Driving back for: " + stepData[i] + "; " 
                                + timer.get());
                    }

                    DriveTrain.stop();
                    timer.stop();
                    timer.reset();
                } else if (turning) {
                    while (timer.get() < stepData[i]) {
                        System.out.println("Turning for: " + stepData[i] + "; " 
                                + timer.get());
                    }

                    DriveTrain.stop();
                    timer.stop();
                    timer.reset();
                } else if (shooting) {
                    while (timer.get() < stepData[i]) {
                        System.out.println("shooting for: " + stepData[i] + "; " 
                                + timer.get());
                    }

                    Loader.allOff();
                    Shooter.stop();
                    timer.stop();
                    timer.reset();
                } else if (armMoving) {
                    while (timer.get() < stepData[i]) {
                        System.out.println("arm moving for: " + stepData[i] + "; " 
                                + timer.get());
                    }

                    Arm.stop();
                    timer.stop();
                    timer.reset();
                } else if (delay) {
                    while (timer.get() < stepData[i]);
                    timer.stop();
                    timer.reset();
                }
                if (i == (keys.length - 1)) done = true;
            }
        }
    }
    
    public static void run() {
        if (!fallbackMode && !done) {
            for (int i = 0; i < keys.length; i++) {    
                forward = keys[i].endsWith("Forward");
                back = keys[i].endsWith("Back");
                turning = keys[i].endsWith("Turn");
                shooting = keys[i].startsWith("shoot");
                armMoving = keys[i].startsWith("arm");
                delay = keys[i].startsWith("delay");

                timer.start();

                if (forward) {
                    while (timer.get() < stepData[i]) {
                        DriveTrain.drive(Constants.AUTON_DRIVE_POWER, Constants.AUTON_DRIVE_POWER);
                    }

                    DriveTrain.stop();
                    timer.stop();
                    timer.reset();
                } else if (back) {
                    while (timer.get() < stepData[i]) {
                        DriveTrain.drive(-Constants.AUTON_DRIVE_POWER, -Constants.AUTON_DRIVE_POWER);
                    }

                    DriveTrain.stop();
                    timer.stop();
                    timer.reset();
                } else if (turning) {
                    while (timer.get() < stepData[i]) {
                        DriveTrain.turn(Constants.AUTON_TURN_POWER);
                    }

                    DriveTrain.stop();
                    timer.stop();
                    timer.reset();
                } else if (shooting) {
                    while (timer.get() < stepData[i]) {
                        shoot();
                    }

                    Loader.allOff();
                    Shooter.stop();
                    timer.stop();
                    timer.reset();
                } else if (armMoving) {
                    while (timer.get() < stepData[i]) {
                        setAngle();
                    }

                    Arm.stop();
                    timer.stop();
                    timer.reset();
                } else if (delay) {
                    while (timer.get() < stepData[i]);
                    timer.stop();
                    timer.reset();
                }
                if (i == (keys.length - 1)) done = true;
            }
        } else {
            shoot();
        }
    }
}
