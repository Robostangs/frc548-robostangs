package com.robostangs;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 *
 * @author sky
 * Combines the 2 arm jags into one PID output
 * possibly defunct; there might only be one motor on the shooter-arm
 * if that is confirmed, this class will be deleted.
 */
public class ArmMotors implements PIDOutput {    
    private static ArmMotors instance = null;
    private static CANJaguar jag1, jag2;
    
    private ArmMotors() {
        //TODO: Constants
        try {
            jag1 = new CANJaguar(1);
            jag2 = new CANJaguar(2);
        } catch (CANTimeoutException ex) {

        }
    }

    public static ArmMotors getInstance() {
        if (instance == null) {
            instance = new ArmMotors();
        }

        return instance;
    }
    
    public static void set(double speed) {
        try {
            jag1.setX(speed);
            jag2.setX(speed);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public void pidWrite(double output) {
        try {
            jag1.setX(output);
            jag2.setX(output);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
}
