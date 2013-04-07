package com.robostangs;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 * Controls the conveyor system 
 * maintainer: Sam
 */
public class Conveyors {
    private static Conveyors instance = null;
    private static CANJaguar ingestConveyor, shooterConveyor;
    private static boolean direction = false; //false is in, used for shake methods
    private static Timer timer;
    
    private Conveyors() {
        try {
            ingestConveyor = new CANJaguar(Constants.CONV_INGEST_JAG_POS);
            shooterConveyor = new CANJaguar(Constants.CONV_SHOOT_JAG_POS);
        } catch (CANTimeoutException ex) {
            System.out.println("CAN TIMEOUT EXCEPTION ON CONVEYORS");
            Log.write("CANJag Timeout Exception on Conveyors");
        }
        timer = new Timer();
    }
    
    public static Conveyors getInstance() {
        if (instance == null) {
            instance = new Conveyors();
        }
        
        return instance;
    }
    
    /**
     * Ingestor conveyor goes in ingest dir
     */
    public static void ingest() {
        try {
            ingestConveyor.setX(-Constants.CONV_INGEST_POWER);
        } catch (CANTimeoutException ex) {
            System.out.println("CAN TIMEOUT EXCEPTION ON INGEST CONVEYOR");
            Log.write("CANJag Timeout Exception on Ingest Conveyor");
        }
    }
    
    /**
     * Ingestor conveyor goes in reverse
     */
    public static void exgest() {
        try {
            ingestConveyor.setX(Constants.CONV_INGEST_POWER);
        } catch (CANTimeoutException ex) {
            System.out.println("CAN TIMEOUT EXCEPTION ON INGEST CONVEYOR");
            Log.write("CANJag Timeout Exception on Ingest Conveyor");
        }
    }
    
    /**
     * Ingestor conveyor stops
     */
    public static void stopIngest() {
        try {
            ingestConveyor.setX(0.0);
        } catch (CANTimeoutException ex) {
            System.out.println("CAN TIMEOUT EXCEPTION ON INGEST CONVEYOR");
            Log.write("CANJag Timeout Exception on Ingest Conveyor");
        }
    }

    /**
     * Readies the shooter to shoot
     */
    public static void loadShooter() {
        try {
            shooterConveyor.setX(-Constants.CONV_SHOOTER_POWER);
        } catch (CANTimeoutException ex) {
            System.out.println("CAN TIMEOUT EXCEPTION ON SHOOTER CONVEYOR");
            Log.write("CANJag Timeout Exception on Shooter Conveyor");
        }
    }

    /**
     * Shooter conveyor goes in reverse at feeder station
     */
    public static void feedMode() { 
        try {
            shooterConveyor.setX(Constants.CONV_SHOOTER_POWER);
        } catch (CANTimeoutException ex) {
            System.out.println("CAN TIMEOUT EXCEPTION ON SHOOTER CONVEYOR");
            Log.write("CANJag Timeout Exception on Shooter Conveyor");
        }
    }

    /**
     * Stops the shooter
     */
    public static void stopShooter() { 
        try {
            shooterConveyor.setX(0.0);
        } catch (CANTimeoutException ex) {
            System.out.println("CAN TIMEOUT EXCEPTION ON SHOOTER CONVEYOR");
            Log.write("CANJag Timeout Exception on Shooter Conveyor");
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
        while (timer.get() < 2) {
            if (direction) {
                try {
                    ingestConveyor.setX(Constants.CONV_INGEST_POWER);
                } catch (CANTimeoutException ex) {
                    ex.printStackTrace();
                }           
            } else {
                try {
                    ingestConveyor.setX(-Constants.CONV_INGEST_POWER);
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
        while (timer.get() < 2) {
            if (direction) {
                try {
                    shooterConveyor.setX(Constants.CONV_SHOOTER_POWER);
                } catch (CANTimeoutException ex) {
                    ex.printStackTrace();
                }           
            } else {
                try {
                    shooterConveyor.setX(-Constants.CONV_SHOOTER_POWER);
                } catch (CANTimeoutException ex) {
                    ex.printStackTrace();
                }
            }
        }
        direction = !direction;
        timer.reset();
    }
}
