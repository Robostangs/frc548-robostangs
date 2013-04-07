package com.robostangs;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * DT has 6 jags, encoder on each side, and a gyro
 * maintainer: Koshiro
 */
public class DriveTrain {
    private static DriveTrain instance = null;
    private static Encoder leftEncoder, rightEncoder;
    private static Timer timer;
    private static boolean climbMode;
    //private static PIDController pid;
    //private static Gyro gyro;
    
    private DriveTrain() {
        DriveMotors.getInstance();
        timer = new Timer();
        climbMode = false;
        
        /*
        leftEncoder = new Encoder (Constants.DT_LEFT_ENCODER_FRONT, Constants.DT_LEFT_ENCODER_BACK);
        rightEncoder = new Encoder (Constants.DT_RIGHT_ENCODER_FRONT, Constants.DT_RIGHT_ENCODER_BACK);
        resetEncoders();
        startEncoders();
        */

        
        //gyro = new Gyro (Constants.DT_GYRO_POS);
        
        /*
        pid = new PIDController(Constants.DT_PID_K_P, Constants.DT_PID_K_I, 
                Constants.DT_PID_K_D, DriveCamera.getInstance(), DriveMotors.getInstance());
        */
        
    }
    
    /**
     * singleton
     * @return instance of dt 
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
        /*
        if (pid.isEnable()) {
            pid.disable();
        }
        * */
        
        DriveMotors.set(leftPower, rightPower);
    }
    
    /**
     * turn around
     * @param power positive = CW, negative = CCW
     */
    public static void turn(double power) {
        drive(power, -power);
    }
    
    /**
     * Drive method optimized for humans 
     * @param leftPower
     * @param rightPower 
     */
    public static void humanDrive(double left, double right) {
        if ((left < -.3 && right > .3) || (left > .3 && right < -.3)) {
            right = right*right * (right / Math.abs(right));
            left = left*left * (left / Math.abs(left));
        }
        drive(left, right);
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
     * reset the timer
     */
    public static void resetTimer() {
        timer.reset();
    }
    
    /**
     * stop everything in DriveTrain
     */
    public static void stop() {
        //pid.disable();
        drive(0, 0);
    }
    
    /**
     * get distance from the left encoder
     * @return in meters
     *
    public static double getLeftEncoderDistance() {
        return leftEncoder.getDistance();
    }
    
    /**
     * get distance from the right encoder
     * @return in meters
     *
    public static double getRightEncoderDistance() {
        return rightEncoder.getDistance();
    }*/
    
    /**
     * get the angle using gyro
     * @return in degrees
     *
    public static double getAngle() {
        return gyro.getAngle();
    } */
    
    
    /**
     * resets all encoders
     *
    public static void resetEncoders() {
        leftEncoder.reset();
        rightEncoder.reset();
    }

    public static void startEncoders() {
        leftEncoder.start();
        rightEncoder.start();
    }

    public static void stopEncoders() {
        leftEncoder.stop();
        rightEncoder.stop();
    }*/
    
    /**
     * sends encoder status to SmartDashboard
     *
    public static void sendEncoders() {
        SmartDashboard.putData("Left Encoder: ", leftEncoder);
        SmartDashboard.putData("Rigth Encoder: ", rightEncoder);
    }*/
    
    /**
     * send gyro status to SmartDashboard
     *
    public static void sendGyro() {
        SmartDashboard.putData("Gyro: ", gyro);
    } */
    
    /*
    public static void enablePid() {
        pid.enable();
    }
    
    public static boolean isPidEnabled() {
        return pid.isEnable();
    } */
    /**
     * drive straight
     * @param power
     * @param angle
     *
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
    } */
    
    /**
     * drive straight for a certain distance
     * @param power
     * @param angle
     * @param distance
     * @return -1 when incomplete, 0 when in progress, 1 when complete
     *
    public static int driveStraight(double power, double angle, double distance) {
        timer.start();
        
        /*
         * timer stuff
         * change power(Volts) to speed(distance/time)
         * get projected time speed = volt --> distance/time;
         * if the actual time passes expected time +1sec, stop and return -1
         * 
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
    } */
    
    /**
     * turn around for angle
     * @param power
     * @param angle
     * @return -1 if incomplete, 0 when in progress, 1 if complete
     *
    public static int turn(double power, double angle) {
        //pid.enable();
        timer.start();
        
        /*
         * timer stuff
         * it's a circular motion omega = angle / time
         * t = angle / power
         * 
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
    } */
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
}
