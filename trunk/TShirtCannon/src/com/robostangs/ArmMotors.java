/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robostangs;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 *
 * @author sky
 * Combines two jaguars into one PID output
 */
public class ArmMotors implements PIDOutput{
    private CANJaguar leftJag, rightJag;

    public ArmMotors(){
        try {
            leftJag = new CANJaguar(Constants.ARM_JAG_1_POS);
            rightJag = new CANJaguar(Constants.ARM_JAG_2_POS);
            leftJag.configFaultTime(0.5);
            rightJag.configFaultTime(0.5);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
    public void pidWrite(double output) {
        try {
            leftJag.setX(output);
            rightJag.setX(output);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
    public void set(double speed){
        try {
            leftJag.setX(speed);
            rightJag.setX(speed);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
}
