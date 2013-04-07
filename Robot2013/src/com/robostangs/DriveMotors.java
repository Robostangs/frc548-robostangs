package com.robostangs;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 *
 * @author Kou
 */
public class DriveMotors implements PIDOutput{
    private static DriveMotors instance = getInstance();
    private static CANJaguar leftFront, leftMid, leftBack, rightFront, rightMid, 
            rightBack;
    
    private DriveMotors() {
        try {
            leftFront = new CANJaguar(Constants.DT_JAG_LEFT_FRONT_POS);
            leftMid = new CANJaguar(Constants.DT_JAG_LEFT_MID_POS);
            leftBack = new CANJaguar(Constants.DT_JAG_LEFT_BACK_POS);
            rightFront = new CANJaguar(Constants.DT_JAG_RIGHT_FRONT_POS);
            rightMid = new CANJaguar(Constants.DT_JAG_RIGHT_MID_POS);
            rightBack = new CANJaguar(Constants.DT_JAG_RIGHT_BACK_POS);
            leftFront.configFaultTime(Constants.JAG_CONFIG_TIME);
            leftMid.configFaultTime(Constants.JAG_CONFIG_TIME);
            leftBack.configFaultTime(Constants.JAG_CONFIG_TIME);
            rightFront.configFaultTime(Constants.JAG_CONFIG_TIME);
            rightMid.configFaultTime(Constants.JAG_CONFIG_TIME);
            rightBack.configFaultTime(Constants.JAG_CONFIG_TIME);
        } catch (CANTimeoutException ex) {
            System.out.println("CAN JAG TIMEOUT EXCEPTION ON DRIVE TRAIN");
            Log.write("CAN JAG TIMEOUT EXCEPTION ON DRIVE TRAIN");  
        }
    }
    
    /**
     * singleton
     * @return 
     */
    public static DriveMotors getInstance() {
        if (instance == null) {
            instance = new DriveMotors();
        }
        
        return instance;
    }
    
    public static void set(double leftPower, double rightPower) {
        try {
            leftFront.setX(leftPower);
            leftMid.setX(leftPower);
            leftBack.setX(leftPower);
            rightFront.setX(-rightPower);
            rightMid.setX(-rightPower);
            rightBack.setX(-rightPower);
        } catch (CANTimeoutException ex) {
            System.out.println("CAN JAG TIMEOUT EXCEPTION ON DRIVE TRAIN");
            Log.write("CAN JAG TIMEOUT EXCEPTION ON DRIVE TRAIN");
        }    
    }
    
    public void pidWrite(double output) {
        try {
            leftFront.setX(output);
            leftMid.setX(output);
            leftBack.setX(output);
            rightFront.setX(-output);
            rightMid.setX(-output);
            rightBack.setX(-output);
        } catch (CANTimeoutException ex) {
            System.out.println("CAN JAG TIMEOUT EXCEPTION ON DRIVE TRAIN");
            Log.write("CAN JAG TIMEOUT EXCEPTION ON DRIVE TRAIN");
        }
    }
}
