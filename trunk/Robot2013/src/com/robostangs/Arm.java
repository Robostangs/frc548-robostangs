/**
 * Class to control the shooter arm
 * maintainer: Lauren
 */

package com.robostangs;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Arm {
    private static Arm instance = null;
    private static Potentiometer potA, potB;
    private static CANJaguar motor;
    private static PIDController pidA, pidB; 
    private static boolean useB = false;
    private static StopWatch timer;
    
    private Arm() {
        potA = new Potentiometer(Constants.POT_A_PORT);
        potB = new Potentiometer(Constants.POT_B_PORT);
        pidA = new PIDController(Constants.ARM_KP_A, Constants.ARM_KI_A, Constants.ARM_KD_A, potA, motor); 
        pidB = new PIDController(Constants.ARM_KP_B, Constants.ARM_KI_B, Constants.ARM_KD_B, potB, motor);
        timer = new StopWatch();
        try {
            motor = new CANJaguar(Constants.ARM_JAG_POS);
            motor.configFaultTime(Constants.JAG_CONFIG_TIME);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }   
        
        //configure PID
        pidA.setInputRange(Constants.POT_MIN_VALUE, Constants.POT_MAX_VALUE);
        pidA.setOutputRange(Constants.ARM_MIN_POWER, Constants.ARM_MAX_POWER);
        pidB.setInputRange(Constants.POT_MIN_VALUE, Constants.POT_MAX_VALUE);
        pidB.setOutputRange(Constants.ARM_MIN_POWER, Constants.ARM_MAX_POWER);
        disablePID();
    }
    
    public static Arm getInstance() {
        if (instance == null) {
            instance = new Arm();
        }        
        return instance;
    }
    
    /**
     * gets average value of pot A
     * @return potA.getAverageValue average value of pot A 
     */
    public static int getPotA() {
        return potA.getAverageValue(); 
    }    
    
    /**
     * gets average value of pot B
     * @return potB.getAverageValue average value of pot B
     */
    public static int getPotB() {
        return potB.getAverageValue(); 
    }
    
    /**
     * set angle equal to zero
     * retrieves value of getPotA or getPotB
     * subtracts it by the zero constant
     * multiplies everything by the constant that converts the values to degrees
     * @return angle angle of arm
     */
     public static double getAngle() {
        double angle = 0;
        if (!useB) {
            angle = (getPotA() - Constants.ARM_POT_ZERO) * Constants.POT_TO_DEGREES;
            return angle;
        } else {
            angle = (getPotB() - Constants.ARM_POT_ZERO) * Constants.POT_TO_DEGREES;
            return angle;
        }
     }
    
     /**
      * @param power power of arm jags
      * disables pid
      * sets the power of the arm jags
      */
    public static void setJags(double power) {
        if (pidEnabled()) {
            disablePID();
        }
        try {
            motor.setX(power);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * For manual control
     * @param power 
     */
    public static void coarseDrive(double power) {
        setJags(power);
    }
    
    /**
     * For fine manual control
     * @param power 
     */
    public static void fineDrive(double power) {
        setJags(power / 2.0);
    }
    /**
     * @param potValue value of pot 
     * disables the other pot
     * sets the value of the pot
     * enables the pot
     * @return 0 if in progress, 1 if done
     */

    public static int setPosition(double potValue) { 
        if (onTarget()) {
            return 1;
        }
        
        if (useB) {
            pidA.disable();
            pidB.setSetpoint(potValue);
            pidB.enable();
        } else {
            pidB.disable();
            pidA.setSetpoint(potValue);
            pidA.enable();
        }
        
        return 0;
    }
    
    /**
     * Uses PID to move to proper angle for pyramid shot
     * @return 0 if in progress, 1 if done
     */
    public static int underPyramidShotPos() {
        return setPosition(Constants.ARM_PYRAMID_POS);
    }
    
    /**
     * Uses PID to move to flat angle
     * @return 0 if in progress, 1 if done
     */
    public static int flatPos() {
        return setPosition(Constants.ARM_POT_ZERO);
    }
    
    /**
     * Uses PID to move to proper angle for feeding from station
     * @return 0 if in progress, 1 if done
     */
    public static int feedPos() {
        return setPosition(Constants.ARM_FEED_POS);
    }
    
    /**
     * TODO: Uses the camera to set arm pos
     */
    public static int camPos() {
        
        return 0;
    }
    
    /**
     * checks if either pid is enabled
     * @return true if either pid is enabled, false if neither is enabled
     */
    public static boolean pidEnabled() {
        return pidA.isEnable() || pidB.isEnable();   
    }
    
    /**
     * if either pid is enabled, it disables it
     */
    public static void disablePID() {
        if (pidA.isEnable()) {
            pidA.disable();
        }
        if (pidB.isEnable()) {
            pidB.disable();
        }
    }
    
    /**
     * 
     * @return true if either pid is on target, false if neither is
     */
    public static boolean onTarget() {
        return pidA.onTarget() || pidB.onTarget();
    }
    
    /**
     * stops the arm motor
     */
    public static void stop() {
        setJags(0.0);
    }
    
    /**
     * makes pot A be in use by pid
     */
    public static void usePotA() {
        useB = false;
    }
    
    /**
     * makes potB be in use by pid
     */
    public static void usePotB() {
        useB = true;
    }
    
    /**
     * switches the pot used by the pid
     */
    public static void switchPot() {
        useB = !useB;        
    }
    
    /**
     * sends value of angle to SmartDashboard
     */
    public static void sendAngle() {
        SmartDashboard.putNumber("Angle: ", getAngle());
    }
    
    /**
     * sends the pot data from both pots to SmartDashboard
     */
    public static void sendPotData() {
        SmartDashboard.putNumber("Pot A: ", getPotA());
        SmartDashboard.putNumber("Pot B: ", getPotB());
    }
    /**
     * sends which pot is in use by pid to SmartDashboard
     */
    public static void sendWhichPotInUse() {
        if (useB) {
            SmartDashboard.putString("CURRENT POT: ", "POT B");
        } else {
            SmartDashboard.putString("CURRENT POT: ", "POT A");
        }
    } 
    
    /**
     * checks if pot A is within range
     * @return true if pot A is within range, false if it isn't
     */
    public boolean isPotAFunctional() {
        return getPotA() >= Constants.POT_MIN_VALUE  && getPotA() <= Constants.POT_MAX_VALUE;
    }
    
    /**
     * checks if pot B is within range
     * @return true if pot B is within range, false if it isn't
     */
    public boolean isPotBFunctional() {
        return getPotB() >= Constants.POT_MIN_VALUE  && getPotB() <= Constants.POT_MAX_VALUE;
    }     
}
