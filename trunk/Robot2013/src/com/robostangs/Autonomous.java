/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robostangs;

import edu.wpi.first.wpilibj.Preferences;
import java.util.Vector;

/**
 * All functionality is read from dash; if that fails, runs a fallback mode
 * @author sky
 */

public class Autonomous {
    private static Autonomous instance = null;    
    private static Preferences pref;
    private static boolean driving = false;
    private static boolean ingesting = false;
    private static boolean shooting = false;
    private static boolean turning = false;
    private static boolean delay = false;
    private static boolean armMoving = false;
    private static boolean fallbackMode = false;
    private static String[] keys;
    private static double timeForStep = 0.0;
    private static StopWatch timer;
    private static int status = 0;
    private static boolean gyroReady = false;
    private static double angle = 0;
    private static int step = 0;
    
    
    private Autonomous() {
        pref = Preferences.getInstance();
        timer = new StopWatch();
        getInfo();
        checkInfo();
    }
    
    public static Autonomous getInstance() {
        if (instance == null) {
            instance = new Autonomous();
        }
        return instance;
    }

    /**
     * Sets up the keys array with all the keys from the dash
     * Also sets variables not related to steps
     */
    public static void getInfo() {
        Vector vKeys = pref.getKeys();
        vKeys.trimToSize();  //for proper array length
        keys = new String[vKeys.capacity()];
        vKeys.copyInto(keys);
        
        timeForStep = pref.getDouble("timeForStep", 0);
    }
    
    /**
     * Checks to see if data read from dash is valid
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
    
    public static void run() {
        double stepData = 0;
        int numberToShoot = 0;
        String armPos = "";
        if (!fallbackMode) {
            for (int i = 0; i < keys.length; i++) {    
                driving = keys[i].endsWith("Distance");
                turning = keys[i].endsWith("Turn");
                ingesting = keys[i].startsWith("ingest");
                shooting = keys[i].startsWith("shoot");
                armMoving = keys[i].startsWith("arm");
                delay = keys[i].startsWith("delay");

                try {
                    if (shooting) {
                        numberToShoot = pref.getInt(keys[i], 0);
                    } else {
                        stepData = pref.getDouble(keys[i], 0);
                    }
                } catch (Preferences.IncompatibleTypeException ex) {
                    armPos = pref.getString(keys[i], "");
                }

                status = 0;
                determineAngle();
                timer.start();

                if (driving && !ingesting) {
                    while (status == 0 && timer.getSeconds() < timeForStep) {
                        status = DriveTrain.driveStraight(Constants.AUTON_DRIVE_POWER, angle, 
                                stepData);
                    }

                    gyroReady = false;
                    DriveTrain.stop();
                    timer.stop();
                    timer.reset();

                    if (status != 1) {
                        Log.write("Auton Step Incomplete " + keys[i] + "Return code: " + status);
                    }

                } else if (driving && ingesting) {
                    while (status == 0 && timer.getSeconds() < timeForStep) {
                        status = DriveTrain.driveStraight(Constants.AUTON_DRIVE_POWER, angle, 
                                stepData);
                        Loader.ingest();
                    }

                    Loader.allOff();
                    DriveTrain.stop();
                    gyroReady = false;
                    timer.stop();
                    timer.reset();

                    if (status != 1) {
                        Log.write("Auton Step Incomplete " + keys[i] + "Return code: " + status);
                    }

                } else if (turning) {
                    while (status == 0 && timer.getSeconds() < timeForStep) {
                        status = DriveTrain.turn(Constants.AUTON_TURN_POWER, stepData);
                    }

                    gyroReady = false;
                    DriveTrain.stop();
                    timer.stop();
                    timer.reset();

                    if (status != 1) {
                        Log.write("Auton Step Incomplete " + keys[i] + "Return code: " + status);
                    }

                } else if (shooting) {
                    while (status == 0 && timer.getSeconds() < timeForStep) {
                        status = Shooter.shoot(numberToShoot);   
                        Loader.loadShooter();
                    }

                    gyroReady = false;
                    Loader.allOff();
                    Shooter.stop();
                    timer.stop();
                    timer.reset();

                    if (status != 1) { 
                        Log.write("Auton Step Incomplete " + keys[i] + "Return code: " + status);
                    }

                } else if (armMoving) {
                    while (status == 0 && timer.getSeconds() < timeForStep) {
                        if (armPos.equalsIgnoreCase("zero")) {
                            status = Arm.flatPos();
                        } else if (armPos.equalsIgnoreCase("pyramid")) {
                            status = Arm.underPyramidShotPos();
                        } else if (stepData != 0 && stepData != -1) {
                            status = Arm.setPosition(stepData);
                        } else if (stepData == -1) {
                            status = Arm.camPos();
                        }
                    }

                    Arm.stop();
                    timer.stop();
                    timer.reset();

                    if (status != 1) { 
                        Log.write("Auton Step Incomplete " + keys[i] + "Return code: " + status);
                    }

                } else if (delay) {
                    while (timer.getSeconds() < stepData) { }

                    timer.stop();
                    timer.reset();
                }

            }
        } else {
            fallbackMode();
        }
    }
    
    /**
     * determine the angle using gyro
     */
    public static void determineAngle() {
        if (!gyroReady) {
            angle = DriveTrain.getAngle();
            gyroReady = true;
        }
    }
    
    /**
     * if fall to get info from the driver's station, backup plan
     */
    public static void fallbackMode() {
        //drive, turn, arm, shoot
        switch (step) {
            case 0:
                timer.start();
                while (timer.getSeconds() < Constants.AUTON_FALLBACK_DRIVE_TIME) {
                    DriveTrain.driveStraight(Constants.AUTON_DRIVE_POWER, Constants.AUTON_DRIVE_ANGLE);
                }
                
                DriveTrain.stop();
                timer.stop();
                timer.reset();
                step++;
                break;
            case 1:
                timer.start();
                while (timer.getSeconds() < Constants.AUTON_FALLBACK_TURN_TIME) {
                    DriveTrain.turn(Constants.AUTON_TURN_POWER, Constants.AUTON_TURN_ANGLE);
                }
                
                DriveTrain.stop();
                timer.stop();
                timer.reset();
                step++;
                break;
            case 2:
                timer.start();
                while (timer.getSeconds() < Constants.AUTON_FALLBACK_ARM_MOVE_TIME) {
                    Arm.setPosition(Constants.AUTON_ARM_POS);
                }
                
                Arm.stop();
                timer.stop();
                timer.reset();
                step++;
                break;
            case 3:
                timer.start();
                while (timer.getSeconds() < Constants.AUTON_FALLBACK_SHOOT_TIME) {
                    Shooter.shoot(Constants.AUTON_SHOOT_DISC_NUM);
                }
                
                Shooter.stop();
                timer.stop();
                timer.reset();
                break;
            default:
                break;
        }
    }
}
