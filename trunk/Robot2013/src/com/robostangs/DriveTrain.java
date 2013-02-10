/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robostangs;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * DT has 6 jags, encoder on each side, and a gyro
 * maintainer: Koshiro
 * TODO: drive train: all methods must be static
 */
public class DriveTrain {
    private static DriveTrain instance = getInstance();
    private static CANJaguar leftFront, leftMid, leftBack, rightFront, rightMid, 
            rightBack, climber;
    private static Encoder leftEncoder, rightEncoder;
    private static Gyro gyro;
    private static StopWatch timer;
    private static boolean climbMode;
    
    private DriveTrain() {
        leftEncoder = new Encoder (Constants.DT_LEFT_ENCODER_FRONT, Constants.DT_LEFT_ENCODER_BACK);
        rightEncoder = new Encoder (Constants.DT_RIGHT_ENCODER_FRONT, Constants.DT_RIGHT_ENCODER_BACK);
        
        gyro = new Gyro (Constants.DT_GYRO_POS);
        
        timer = new StopWatch();
        
        try {
            //declare jags here, init jags method not necessary
            leftFront = new CANJaguar(Constants.DT_JAG_LEFT_FRONT_POS);
            leftMid = new CANJaguar(Constants.DT_JAG_LEFT_MID_POS);
            leftBack = new CANJaguar(Constants.DT_JAG_LEFT_BACK_POS);
            rightFront = new CANJaguar(Constants.DT_JAG_RIGHT_FRONT_POS);
            rightMid = new CANJaguar(Constants.DT_JAG_RIGHT_MID_POS);
            rightBack = new CANJaguar(Constants.DT_JAG_RIGHT_BACK_POS);
            climber = new CANJaguar (Constants.DT_JAG_CLIMB_POS);
            leftFront.configFaultTime(Constants.JAG_CONFIG_TIME);
            leftMid.configFaultTime(Constants.JAG_CONFIG_TIME);
            leftBack.configFaultTime(Constants.JAG_CONFIG_TIME);
            rightFront.configFaultTime(Constants.JAG_CONFIG_TIME);
            rightMid.configFaultTime(Constants.JAG_CONFIG_TIME);
            rightBack.configFaultTime(Constants.JAG_CONFIG_TIME);
            climber.configFaultTime(Constants.JAG_CONFIG_TIME);
        } catch (CANTimeoutException ex) {
            System.out.println("CAN JAG TIMEOUT EXCEPTION ON DRIVE TRAIN");
            Log.write("CAN JAG TIMEOUT EXCEPTION ON DRIVE TRAIN");
        }
        
        climbMode = false;
    }
    
    /**
     * singleton stuff
     * @return 
     */
    public static DriveTrain getInstance() {
        if (instance == null) {
            instance = new DriveTrain();
        }
        
        return instance;
    }
    
    /**
     * tank drive
     * @param leftPower
     * @param rightPower 
     */
    public static void drive(double leftPower, double rightPower) {
        try {
            leftFront.setX(leftPower);
            leftMid.setX(leftPower);
            leftBack.setX(leftPower);
            rightFront.setX(rightPower);
            rightMid.setX(rightPower);
            rightBack.setX(rightPower);
        } catch (CANTimeoutException ex) {
            System.out.println("CAN JAG TIMEOUT EXCEPTION ON DRIVE TRAIN");
            Log.write("CAN JAG TIMEOUT EXCEPTION ON DRIVE TRAIN");
        }
    }
    
    /**
     * turn around
     * @param power positive = CW, negative = CCW
     */
    public static void turn(double power) {
        drive(power, -power);
    }
    
    /**
     * 
     * @param leftPower
     * @param rightPower 
     */
    public static void humanDrive(double leftPower, double rightPower) {
        //TODO: to be determined
        drive (leftPower, rightPower);
    }
    
    /**
     * TODO: ???
     * @param power
     * @param angle 
     */
    public static void arcadeDrive(double power, double angle) {
        
    }
    
    /**
     * drive at a half speed
     * @param leftPower
     * @param rightPower 
     */
    public static void driveSlow(double leftPower, double rightPower) {
        drive(leftPower / 2, rightPower / 2);
    }
    
    /**
     * drive straight
     * @param power
     * @param angle 
     */
    public static void driveStraight(double power, double angle) {
        double leftPower = power;
        double rightPower = power;
        
        //TODO: which way does gyro increase?
        if (getAngle() > angle) {
            leftPower = leftPower * Constants.DT_STRAIGHT_LEFT_INC;
            rightPower = rightPower * Constants.DT_STRAIGHT_RIGHT_DEC;
        } else if (getAngle() < angle) {
            leftPower = leftPower * Constants.DT_STRAIGHT_LEFT_DEC;
            rightPower = rightPower * Constants.DT_STRAIGHT_RIGHT_INC;
        }
        
        drive(leftPower, rightPower);
    }
    
    /**
     * drive straight for a certain distance
     * @param power
     * @param angle
     * @param distance
     * @return -1 when incomplete, 0 when in progress, 1 when complete
     */
    public static int driveStraight(double power, double angle, double distance) {
        timer.start();
        
        /*
         * timer stuff
         * change power(Volts) to speed(distance/time)
         * get projected time speed = volt --> distance/time;
         * if the actual time passes expected time +1sec, stop and return -1
         */ 
        double speed = power * Constants.DT_CONV_VOLT_TO_M_PER_SEC;
        double expectedTime = distance / speed;
        if (timer.get() > (expectedTime + Constants.DT_DELAY_TIME)) {
            driveStraight(0, 0);
            timer.stop();
            resetTimer();
            resetEncoders();
            return -1;
        }
        
        if (getLeftEncoderDistance() < distance && getRightEncoderDistance() < distance) {
            driveStraight(power, angle);
            return 0;
        } else {
            driveStraight(0,0);
            timer.stop();
            resetTimer();
            resetEncoders();
            return 1;
        }
    }
    
    /**
     * turn around for angle
     * @param power
     * @param angle
     * @return -1 if incomplete, 0 when in progress, 1 if complete
     */
    public static int turn(double power, double angle) {
        timer.start();
        
        /*
         * timer stuff
         * it's a circular motion omega = angle / time
         * t = angle / power
         */ 
        double speed = power * Constants.DT_CONV_VOLT_TO_M_PER_SEC;
        double expectedTime = Math.toRadians(angle) / speed;
        if (timer.get() > (expectedTime + Constants.DT_DELAY_TIME)) {
            driveStraight(0, 0);
            timer.stop();
            resetTimer();
            resetEncoders();
            return -1;
        }
        
        if (angle >= 0) {
            if(getAngle() < angle) {
                turn(power, -power);
                return 0;
            } else {
                turn(0, 0);
                timer.stop();
                resetTimer();
                resetEncoders();
                return 1;
            }
        } else {
            if (getAngle() > angle) {
                turn (-power, power);
                return 0;
            } else {
                turn (0, 0);
                timer.stop();
                resetTimer();
                resetEncoders();
                return 1;
            }
        }
    }
    
    /**
     * drive the robot along a circular arc
     * @param power
     * @param x
     * @param y
     * @return 
     */
    public static int driveArc(double power, double x, double y) {
        return -1;
    }
    
    /**
     * TODO: drive to position (x,y)
     * @param power
     * @param x
     * @param y
     * @return 
     */
    public static int driveToPosition(double power, double x, double y) {
        return -1;
    }
    
    /**
     * get distance from the left encoder
     * @return in meters
     */
    public static double getLeftEncoderDistance() {
        return leftEncoder.getDistance();
    }
    
    /**
     * get distance from the right encoder
     * @return in meters
     */
    public static double getRightEncoderDistance() {
        return rightEncoder.getDistance();
    }
    
    /**
     * get the angle using gyro
     * @return in degrees
     */
    public static double getAngle() {
        return gyro.getAngle();
    }
    
    /**
     * reset the timer
     */
    public static void resetTimer() {
        timer.reset();
    }
    /**
     * resets all encoders
     */
    public static void resetEncoders() {
        leftEncoder.reset();
        rightEncoder.reset();
    }
    
    /**
     * sends encoder status to SmartDashboard
     */
    public static void sendEncoders() {
        SmartDashboard.putData("Left Encoder: ", leftEncoder);
        SmartDashboard.putData("Rigth Encoder: ", rightEncoder);
    }
    
    /**
     * send gyro status to SmartDashboard
     */
    public static void sendGyro() {
        SmartDashboard.putData("Gyro: ", gyro);
    }
    
    /**
     * stop everything in DriveTrain
     */
    public static void stop() {
        drive(0, 0);
    }
    
    /**
     * check the mode
     * @return climbMode
     */
    public static boolean getMode() {
        return climbMode;
    }
    
    /**
     * enable climb mode
     */
    public static void enableClimbMode() {
        climbMode = true;
    }
    
    /**
     * enable drive mode
     */
    public static void enableDriveMode() {
        climbMode = false;
    }
    
    /**
     * TODO: climbing stuff if climbMode = true
     */
}
