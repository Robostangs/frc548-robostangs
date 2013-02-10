/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robostangs;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Generic xbox controller class
 * maintainer: Nicholas
 */

//TODO: XBOXCONTROLLER: methods to use triggers as buttons

public class XboxController extends Joystick {
    
    public XboxController(int port) {
        super(port);
    }
    
    public boolean aButton(){
        return getRawButton(1);
    }
    
    public boolean bButton(){
        return getRawButton(2);
    }
    
    public boolean xButton(){
        return getRawButton(3);
    }
    
    public boolean yButton(){
        return getRawButton(4);
    }
    
    public boolean lBumper(){
        return getRawButton(5);
    }
    
    public boolean rBumper(){
        return getRawButton(6);
    }
    
    public boolean backButton(){
        return getRawButton(7);
    }
    
    public boolean startButton(){
        return getRawButton(8);
    }
    
    public boolean leftJoyButton(){
        return getRawButton(9);
    }
    
    public boolean rightJoyButton(){
        return getRawButton(10);
    }
    
    
    /**
     * Makes triggers behave as buttons for rightTriggerButton and leftTriggerButton
     */
    public boolean rightTriggerButton(){
        double rtb = triggerAxis();
        if (rtb < -0.5) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean leftTriggerButton() {
        double ltb = triggerAxis();
        if (ltb > 0.5) {
            return (true);
        } else {
            return false;
        }
    }
    
    public double leftStickXAxis() {
        double lsxa = getRawAxis(1);
        if (Math.abs(lsxa) < 0.1) {
            lsxa = 0;
        }
        return lsxa;
    }
    
    public double leftStickYAxis(){
        double lsya = -getRawAxis(2);
        if(Math.abs(lsya) < 0.1)
        {
            lsya = 0;
        }
        return lsya;
    }
    
    public double triggerAxis(){
        return getRawAxis(3);
    }
    
    public double rightStickXAxis(){
        double rsxa = getRawAxis(4);
        if(Math.abs(rsxa) < 0.1)
        {
            rsxa = 0;
        }
        return rsxa;
    }
    
    public double rightStickYAxis(){
        double rsya = -getRawAxis(5);
        if(Math.abs(rsya) < 0.1)
        {
            rsya = 0;
        }
        return rsya;
    }
    
    public double rightTriggerAxis(){
        double rta = getRawAxis(6);
        if(rta < 0)
        {
            rta = 0;
        }
        return rta;
    }
    
    public double leftTriggerAxis(){
        double lta = getRawAxis(7);
        if(lta > 0)
        {
            lta = 0;
        }
        return lta;
    }
}
