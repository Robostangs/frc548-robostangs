/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robostangs;

import edu.wpi.first.wpilibj.PIDController;

/**
 *
 * @author sky
 */
public class Arm {
    private PIDController pid;
    private ArmPot pot;
    private ArmMotors motors;
    private double potToDegrees = 0;    //TODO: get value
    
    public Arm(){
        pid = new PIDController(Constants.ARM_PID_P, Constants.ARM_PID_I, Constants.ARM_PID_D, pot, motors);
        pot = new ArmPot();
        motors = new ArmMotors();
        pid.setOutputRange(-0.6, 0.6);
        pid.setInputRange(Constants.ARM_POT_MIN, Constants.ARM_POT_MAX);
        pid.disable();
    }
    
    public void stop(){
        if(pid.isEnable()){
            pid.disable();
        }
        motors.set(0);
    }
    public double getAngle(){
        return (pot.getAvgValue() - Constants.ARM_90) / potToDegrees;
    }
    
    public void setSpeed(double speed){
        if(pid.isEnable()){
            pid.disable();
        }
        
        if(speed>0){
            if(pot.getAvgValue() < Constants.ARM_POT_MAX){
                motors.set(speed);
            }else{
                motors.set(0);
            }
        }else{
            if(pot.getAvgValue() > Constants.ARM_POT_MIN){
                motors.set(speed);
            }else{
                motors.set(0);
            }
        }
    }
    
    public void setPosition(double value){
        if(value <= Constants.ARM_POT_MAX && value >= Constants.ARM_POT_MIN){
            pid.setSetpoint(value);
            pid.enable();
        }else{
            stop();
        }
    }

    public double getPot(){
        return pot.getAvgValue();
    }
}
