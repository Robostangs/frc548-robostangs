package com.robostangs;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 * Controls the conveyor system 
 * maintainer: Sam
 */
public class Conveyors {
    private static Conveyors instance = null;
    private static CANJaguar ingestConveyor, shooterConveyor;
    private static boolean direction = false; //false is in, used for shake methods
    private static StopWatch timer;
    
    private Conveyors() {
        try {
            ingestConveyor = new CANJaguar(Constants.CONV_INGEST_JAG_POS);
            shooterConveyor = new CANJaguar(Constants.CONV_SHOOT_JAG_POS);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        timer = new StopWatch();
    }
    
    public static Conveyors getInstance() {
        if (instance == null) {
            instance = new Conveyors();
        }
        
        return instance;
    }
    
    /**
     * Ingestor conveyor goes forward
     */
    public static void ingestMode() {
        try {
            ingestConveyor.setX(Constants.CONV_POWER);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Ingestor conveyor goes in reverse
     */
    public static void reverseIngest(){
        try {
            ingestConveyor.setX(-Constants.CONV_POWER);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Ingestor conveyor stops
     */
    public static void stopIngest() {
        try {
            ingestConveyor.setX(0.0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Readies the shooter to shoot
     */
    public static void readyShooter() {
        try {
            shooterConveyor.setX(Constants.CONV_POWER);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Shooter conveyor goes in reverse at feeder station
     */
    public static void feedMode() { 
        try {
            shooterConveyor.setX(-Constants.CONV_POWER);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Stops the shooter
     */
    public static void stopShooter() { 
        try {
            shooterConveyor.setX(0.0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * The ingestor and the shooter both stop
     */
    public static void stopBoth() {
        stopIngest();
        stopShooter();
    }

    /**
     * Unjams the ingestor conveyor
     */
    public static void shakeIngest() {
        timer.start();
        while (timer.getSeconds() < 2) {
            if (direction) {
                try {
                    ingestConveyor.setX(Constants.CONV_POWER);
                } catch (CANTimeoutException ex) {
                    ex.printStackTrace();
                }           
            } else {
                try {
                    ingestConveyor.setX(-Constants.CONV_POWER);
                } catch (CANTimeoutException ex) {
                    ex.printStackTrace();
                }
            }
        }
        direction = !direction;
        timer.reset();
    }            

    /**
     * Unjams the shooter conveyor
     */
    public static void shakeShooter() {
        timer.start();
        while (timer.getSeconds() < 2) {
            if (direction) {
                try {
                    shooterConveyor.setX(Constants.CONV_POWER);
                } catch (CANTimeoutException ex) {
                    ex.printStackTrace();
                }           
            } else {
                try {
                    shooterConveyor.setX(-Constants.CONV_POWER);
                } catch (CANTimeoutException ex) {
                    ex.printStackTrace();
                }
            }
        }
        direction = !direction;
        timer.reset();
    }
}
