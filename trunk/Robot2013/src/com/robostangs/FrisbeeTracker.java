/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robostangs;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Counts frisbees, tracks location in robot
 * maintainer: Michael
 */
public class FrisbeeTracker {
    private static FrisbeeTracker instance = null;
    private static DigitalInput ingestSwitch, shootSwitch, liftSwitch;
    private static int numberOfFrisbees = 0;
    private static StopWatch ingestTimer, liftTimer, shootTimer;
    
    private FrisbeeTracker() {
        ingestSwitch = new DigitalInput(Constants.INGEST_SWITCH_POS);
        shootSwitch = new DigitalInput(Constants.SHOOT_SWITCH_POS);
        liftSwitch = new DigitalInput(Constants.LIFT_SWITCH_POS);
        ingestTimer = new StopWatch();
        liftTimer = new StopWatch();
        shootTimer = new StopWatch();
    
    }
    
    public static FrisbeeTracker getInstance() {
        if (instance == null) {
            instance = new FrisbeeTracker();
        }
        return instance;
    }
    
    /**
     * gets the number of discs in the robot
     * @return number of frisbees
     */
    public static int getNumberOfFrisbees() {
        return numberOfFrisbees;
    }
    
    /**
     * adds a count of discs when in feed mode
     * subtracts when shot
     * @param feedMode 
     */
    public static void count(boolean feedMode) {
        //if in feedMode, adds frisbees; if not, subtracts
        if (feedMode) {
            if (FrisbeeTracker.shotFrisbee()) {
                numberOfFrisbees++;
            }
        } else {
            if (FrisbeeTracker.shotFrisbee()) {
                numberOfFrisbees--;
            }
            //ingesting frisbees
        }
        if (FrisbeeTracker.ingestFrisbee()) {
            numberOfFrisbees++;
        }
    }
    
    /**
     * counts the time to ingest a frisbee
     * makes sure that the robot does not ingest 2 frisbees at the same time
     * @return true when the robot finishes ingesting
     */
    public static boolean ingestFrisbee() {
        if (ingestSwitch.get()) {
            //accounts for switch being pressed for a period of time
            ingestTimer.start();
            if (ingestTimer.getSeconds() >= Constants.INGEST_FRISBEE_TIMER) {
                return true;
            }           
        } else {
            ingestTimer.stop();
            ingestTimer.reset();
            return false;
        }
        return false;
    }
    
    /**
     * counts the time to lift a frisbee
     * @return true when lifting is done
     */
    public static boolean liftFrisbee() {
        if (liftSwitch.get()) {
             //accounts for switch being pressed for a period of time
            liftTimer.start();
            if (liftTimer.getSeconds() >= Constants.LIFT_FRISBEE_TIMER) {
                return true;
            }      
        } else {
            liftTimer.stop();
            liftTimer.reset();
            return false;
        }
        return false;
    }
    
    /**
     * counts the time to shoot
     * @return true when finish shooting
     */
    public static boolean shotFrisbee() {
        if (shootSwitch.get()) {
             //accounts for switch being pressed for a period of time
            shootTimer.start();
            if (shootTimer.getSeconds() >= Constants.SHOOT_FRISBEE_TIMER) {
                return true;
            }        
        } else {
            shootTimer.stop();
            shootTimer.reset();
            return false;
        }
        return false;
    }
    
    /**
     * sends frisbee data to dashboard
     */
    public static void sendFrisbeeDataToDash() {
        SmartDashboard.putNumber("Number of frisbees", numberOfFrisbees);
        if (numberOfFrisbees == 4) {
            SmartDashboard.putString("AT MAX FRISBEES", "");
        } else if (numberOfFrisbees > 4) {
            SmartDashboard.putString("TOO MANY FRISBEES!!1!", "DUMP " 
                    + (numberOfFrisbees - 4) + " STAT.");
        }
    }
}