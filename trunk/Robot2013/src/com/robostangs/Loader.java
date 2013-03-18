package com.robostangs;

/**
 * Uses conveyors, lifter, ingestor to get frisbees to shooter
 * maintainer: Tejas
 */

public class Loader {
    private static Loader instance = null;
    private static boolean lifterMovingUp = false;
    
    private Loader() {
        Conveyors.getInstance();
        //Ingestor.getInstance();
        Lifter.getInstance();
    }
    
    public static Loader getInstance() {
        if (instance == null) {
            instance = new Loader();
        }
        return instance;
    }
    
    /**
     * turns everything under loader control off
     */
    public static void allOff(){
        Lifter.stop();
        Conveyors.stopBoth();
        //Ingestor.turnOff();
    }

    public static void stopShooterConveyor() {
        Conveyors.stopShooter();
    }
    
    /**
     * runs ingestor + ingestConveyor
     * TODO: automatically move lifter down
     */
    public static void ingest(){
        //Ingestor.turnOn();
        Conveyors.ingest();
    }
    
    /**
     * runs shooter conveyor
     * TODO: moves lifter to top pos if not there
     */
    public static void loadShooter(){
        Conveyors.loadShooter();
    }
    
    /**
     * reverses to feed from station
     * TODO: moves lifter to top pos if not there
     */
    public static void feed(){
        Conveyors.feedMode();
    }
    
    /**
     * Moves lifter to up position
     * TODO: implement/test
     */
    public static void liftUp() {
        if (Lifter.getSetSpeed() > 0) {
            lifterMovingUp = true;
        } else {
            lifterMovingUp = false;
        }
        Lifter.raise();
        Conveyors.ingest();
    }

    /**
     * turns off ingestor
     */
    public static void ingestorOff() {
        //Ingestor.turnOff();
        if (!lifterMovingUp) {
            Conveyors.stopIngest();
        }
    }
    
    public static void stopConveyors() {
        Conveyors.stopBoth();
    }

    public static void liftOff() {
        Lifter.stop();
    }
}
