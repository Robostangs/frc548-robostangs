/**
 * Class to control the shooter arm
 * maintainer: Lauren
 */

package com.robostangs;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Arm {
    private static Arm instance = null;
    private static Potentiometer potA;
    private static ArmMotor motor;
    private static PIDController pidA; // pidCam;
    private static Timer timer;
    
    private Arm() {
        potA = new Potentiometer(Constants.ARM_POT_A_PORT);
        timer = new Timer();
        motor = ArmMotor.getInstance();
        pidA = new PIDController(Constants.ARM_KP_A, Constants.ARM_KI_A, Constants.ARM_KD_A, potA, motor);
        //pidCam = new PIDController(Constants.ARM_KP_CAM, Constants.ARM_KI_CAM, Constants.ARM_KD_CAM, ArmCamera.getInstance(), motor);
        
        //configure PID
        pidA.setInputRange(Constants.ARM_POT_A_MIN_VALUE, Constants.ARM_POT_A_MAX_VALUE);
        pidA.setOutputRange(Constants.ARM_MIN_POWER, Constants.ARM_MAX_POWER);
        pidA.setAbsoluteTolerance(0);
        //pidCam.setInputRange(Constants.POT_A_MIN_VALUE, Constants.POT_A_MAX_VALUE);
        //pidCam.setOutputRange(Constants.ARM_MIN_POWER, Constants.ARM_MAX_POWER);
        
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
    public static double getPotA() {
        return potA.getAverageValue();
    }    

    /**
     * set angle equal to zero
     * retrieves value of getPotA or getPotB
     * subtracts it by the zero constant
     * multiplies everything by the constant that converts the values to degrees
     * @return angle of arm
     */
     public static double getAngle() {
        double angle = 0;
            angle = (getPotA() - Constants.ARM_POT_A_ZERO) * Constants.POT_A_TO_DEGREES;
            return angle;
     }
    
     /**
      * @param power of arm jags
      * disables pid
      * sets the power of the arm jags
      */
     public static void setJags(double power) {
        if (pidEnabled()) {
            disablePID();
        }
        motor.setX(power);
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
            //this might not be an intelligent idea, maybe get rid of disable
            disablePID();
            return 1;
        }

        pidA.setSetpoint(potValue);
        pidA.enable();
        
        return 0;
    }
    
    
    /**
     * Uses PID to move to lowest angle
     * @return 0 if in progress, 1 if done
     */
    public static int lowestPos() {
        return setPosition(Constants.ARM_POT_A_MIN_VALUE);
    }
    
    /**
     * Uses PID to move to proper angle for feeding from station
     * @return 0 if in progress, 1 if done
     */
    public static int feedPos() {
        return setPosition(Constants.ARM_FEED_POS_A);
    }

    /**
     * Uses PID to move to proper angle for shooting from front of pyramid
     * @return 0 if in progress, 1 if done
     */
    public static int frontPyramidPos() {
        return setPosition(Constants.ARM_FRONT_PYRAMID_POS);
    }

    /**
     * Uses PID to move to proper angle for shooting from back of pyramid
     * @return 0 if in progress, 1 if done
     */
    public static int backPyramidPos() {
        return setPosition(Constants.ARM_BACK_PYRAMID_POS);
    }

    /**
     * Uses PID to move to proper angle for shooting from side of pyramid
     * @return 0 if in progress, 1 if done
     */
    public static int sidePyramidPos() {
        return setPosition(Constants.ARM_SIDE_PYRAMID_POS);
    }

    /**
     * Uses PID to hold the current position
     */
    public static void pidHoldPos() {
        setPosition(getPotA());
    }

    /**
     * Enables the pid
     */
    public static void enablePID() {
        pidA.enable();
    }
    
    /**
     * checks if either pid is enabled
     * @return true if either pid is enabled, false if neither is enabled
     */
    public static boolean pidEnabled() {
        return pidA.isEnable(); // || pidCam.isEnable();   
    }
    
    /**
     * if either pid is enabled, it disables it
     */
    public static void disablePID() {
        if (pidA.isEnable()) {
            pidA.disable();
        }
        /*
        if (pidCam.isEnable()) {
            pidCam.disable();
        }
        * */
    }
    
    /**
     * @return true if either pid is on target, false if neither is
     */
    public static boolean onTarget() {
        return pidA.onTarget(); // || pidCam.onTarget();
    }
    
    /**
     * stops the arm motor
     */
    public static void stop() {
        setJags(0.0);
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
    }

    /**
     * sends which pot is in use by pid to SmartDashboard
     */
    public static void sendWhichPotInUse() {
        SmartDashboard.putString("CURRENT POT: ", "POT A");
    } 
    
    /**
     * checks if pot A is within range
     * @return true if pot A is within range, false if it isn't
     */
    public static boolean isPotAFunctional() {
        return getPotA() >= Constants.ARM_POT_A_MIN_VALUE  && getPotA() <= Constants.ARM_POT_A_MAX_VALUE;
    }

    public static void getPIDFromDash() {
        pidA.startLiveWindowMode();
        SmartDashboard.putData("PID: ", pidA);
    }

    public static void outputPIDConstants() {
        System.out.println("KP: " + pidA.getP());
        System.out.println("KI: " + pidA.getI());
        System.out.println("KD: " + pidA.getD());
        System.out.println("setpoint:" + pidA.getSetpoint());
    }

    /**
     * @param power of arm jags
     * disables pid
     * sets the power of the arm jags
     * this method for use when we have a working pot
     *
    public static void setJags(double power) {
        double currentPot = getPotA();
        //add a buffer
        if (power > 0) {
            //arm is going up, give it a pot val one higher
            currentPot++;
        } else {
            //arm is going down, give it a pot val one lower
            currentPot--;
        }

        if (pidEnabled()) {
            disablePID();
        }

        if (currentPot <= (Constants.POT_A_SLOW_VALUE + 10)) {
            //needs to go slow because of gas strut, go min speed and retain sign
            power = Constants.ARM_MIN_VOLTAGE * (power / Math.abs(power));
        }

        if (currentPot >= Constants.POT_A_MAX_VALUE && power > 0) {
            //at max height, move down slightly
            System.out.println("AT MAX");
            power = -0.1;
        } else if (currentPot <= Constants.POT_A_MIN_VALUE && power < 0) {
            //at min height, move up slightly
            System.out.println("AT MIN");
            power = 0.1;
        }

        motor.setX(power);
    } */

    /**
     * Uses the camera to set arm pos
     * for use when camera has been tuned
     * @return 0 if in progress, 1 if done
     *
    public static int camPos() {
        if (pidCam.onTarget()) {
            return 1;
        }
        if (pidA.isEnable()) {
            pidA.disable();
        }
        pidCam.setSetpoint(ArmCamera.getTarget());
        pidCam.enable();
        return 0;
    }*/
}
