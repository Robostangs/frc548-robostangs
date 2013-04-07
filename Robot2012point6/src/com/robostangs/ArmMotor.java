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
 * @author john
 * 
 * This class is used to combine two can jaguars into one PID output
 */
public class ArmMotor implements PIDOutput{
    private CANJaguar arm_jag_a, arm_jag_b;
    public ArmMotor(){
        try {
            arm_jag_a = new CANJaguar(Constants.ARM_JAG_A_POS);
            arm_jag_b = new CANJaguar(Constants.ARM_JAG_B_POS);
        } catch (CANTimeoutException ex) {
            
            System.out.println("\n\n\n\n****ARM FAILURE");
            ex.printStackTrace();
        }
    }
    public void pidWrite(double output) {
        try {
            
            arm_jag_a.setX(output);
            arm_jag_b.setX(-output);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    public void set(double value){
        try {
            arm_jag_a.setX(value);
            arm_jag_b.setX(-value);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    public double getPower(){
        try {
            return (arm_jag_a.getOutputVoltage() + arm_jag_b.getOutputVoltage())/2;
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        return 0;
    }
}