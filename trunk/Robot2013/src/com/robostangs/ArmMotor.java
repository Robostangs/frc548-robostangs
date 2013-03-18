package com.robostangs;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 *
 * @author sky
 * flips the set power for PID
 */
public class ArmMotor implements PIDOutput {    
    private static ArmMotor instance = null;
    private static CANJaguar jag1;
    
    private ArmMotor() {
        try {
            jag1 = new CANJaguar(Constants.ARM_JAG_POS);
        } catch (CANTimeoutException ex) {
            System.out.println("CAN EX @ ARM JAG");
            Log.write("CAN EX @ ARM JAG");
        }
    }

    public static ArmMotor getInstance() {
        if (instance == null) {
            instance = new ArmMotor();
        }
        return instance;
    }
    
    public static void setX(double speed) {
        try {
            jag1.setX(-speed);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public void pidWrite(double output) {
        try {
            jag1.setX(-output);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
}
