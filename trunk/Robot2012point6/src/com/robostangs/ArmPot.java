package com.robostangs;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.PIDSource;

public class ArmPot implements PIDSource{
 
    public AnalogChannel pot;
    
    public ArmPot(){
        pot = new AnalogChannel(1, Constants.ARM_POT_POS);
    }
    public double pidGet() {
        return pot.getValue();
    }
    public double getPotentiometer(){
        return pot.getValue();
    }
    public double getPotVoltage(){
        return pot.getVoltage();
    }
    public void resetPotentiometer(){
        pot.resetAccumulator();
    }
}