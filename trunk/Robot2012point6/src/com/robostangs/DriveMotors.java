package com.robostangs;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 *
 * @author john
 * 
 * This class is used to combine two CANJaguars into one PID output
 */
public class DriveMotors implements PIDOutput{
    public CANJaguar jag1, jag2, jag3, jag4;
    private static DriveMotors _instance = null;
    
    public DriveMotors(){
        try {
            jag1 = new CANJaguar(Constants.DRIVE_JAG_1_POS);
            jag2 = new CANJaguar(Constants.DRIVE_JAG_2_POS);
            jag3 = new CANJaguar(Constants.DRIVE_JAG_3_POS);
            jag4 = new CANJaguar(Constants.DRIVE_JAG_4_POS);
            jag1.configFaultTime(.5);
            jag2.configFaultTime(.5);
            jag3.configFaultTime(.5);
            jag4.configFaultTime(.5);
        } catch (CANTimeoutException ex) {
            Log.getInstance().write("Can exception, drive class: \n" + ex.getMessage());
        }
    }
    
    public static synchronized DriveMotors getInstance(){
        if(_instance == null){
            _instance = new DriveMotors();
        }
        return _instance;
    }
    
    /*
     * (Override)
     * Sets jaguars based on pid values.
     * Rotates drive train based on camera to target offset.
     */
    public void pidWrite(double output) {
        try { 
            jag1.setX(output);
            jag2.setX(output);
            jag3.setX(output);
            jag4.setX(output);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    /*
     * Set each jaguar to a different value.
     */
    public void set(double a, double b, double c, double d){
        try {
            jag1.setX(-a);
            jag2.setX(-b);
            jag3.setX(c);
            jag4.setX(d);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
    /*
     * Set all four jaguars to the same value.
     */
    public void set(double a){
        set(-a,-a,a,a);
    }
    
}