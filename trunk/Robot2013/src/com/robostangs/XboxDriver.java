/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robostangs;

import edu.wpi.first.wpilibj.Joystick;

/**
 * For driver's controller
 * should adjust for joystick drift
 * maintainer: Nicholas
 */
public class XboxDriver extends XboxController {
    private static XboxDriver instance = getInstance();
    
    private XboxDriver(int port) {
        super(port);
    }

    public static XboxDriver getInstance() {
        if (instance == null) {
            instance = new XboxDriver(Constants.XBOX_DRIVER_PORT);
        }

        return instance;
    }
    
    public double leftStickXAxis() {
        double lsxa = super.leftStickXAxis();
        if (Math.abs(lsxa) < Constants.XBOX_DRIVER_DRIFT) {
            lsxa = 0;
        }
        return lsxa;
    }
    
    public double leftStickYAxis(){
        double lsya = -getRawAxis(2);
        if(Math.abs(lsya) < Constants.XBOX_DRIVER_DRIFT) {
            lsya = 0;
        }
        return lsya;
    }
       
    public double rightStickXAxis(){
        double rsxa = getRawAxis(4);
        if(Math.abs(rsxa) < Constants.XBOX_DRIVER_DRIFT) {
            rsxa = 0;
        }
        return rsxa;
    }
    
    public double rightStickYAxis(){
        double rsya = -getRawAxis(5);
        if(Math.abs(rsya) < Constants.XBOX_DRIVER_DRIFT) {
            rsya = 0;
        }
        return rsya;
    }
}
