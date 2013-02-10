/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robostangs;

import edu.wpi.first.wpilibj.Joystick;
import java.lang.Math;

/**
 * For manip's controller
 * should adjust for joystick drift
 * maintainer: Nicholas
 */
public class XboxManip extends XboxController {
    private static XboxManip instance = null;
    
    private XboxManip(int port) {
        super(port);
    }

    public static XboxManip getInstance() {
        if (instance == null) {
            instance = new XboxManip(Constants.XBOX_MANIP_PORT);
        }

        return instance;
    }
    
    public double leftStickXAxis() {
        double lsxa = getRawAxis(1);
        if (Math.abs(lsxa) < Constants.XBOX_MANIP_DRIFT) {
            lsxa = 0;
        }
        return lsxa;
    }
    
    public double leftStickYAxis(){
        double lsya = -getRawAxis(2);
        if(Math.abs(lsya) < Constants.XBOX_MANIP_DRIFT) {
            lsya = 0;
        }
        return lsya;
    }
       
    public double rightStickXAxis(){
        double rsxa = getRawAxis(4);
        if(Math.abs(rsxa) < Constants.XBOX_MANIP_DRIFT) {
            rsxa = 0;
        }
        return rsxa;
    }
    
    public double rightStickYAxis(){
        double rsya = -getRawAxis(5);
        if(Math.abs(rsya) < Constants.XBOX_MANIP_DRIFT) {
            rsya = 0;
        }
        return rsya;
    }
    
    
}
