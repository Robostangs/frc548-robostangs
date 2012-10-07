package com.robostangs;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.PIDSource;

/**
 *
 * @author sky
 */
public class ArmPot implements PIDSource{
    public AnalogChannel pot;
    
    public ArmPot(){
        pot = new AnalogChannel(1, Constants.ARM_POT_POS);
    }

    public double pidGet() {
        return pot.getAverageValue();
    }
    
    public double getAvgValue(){
        return pot.getAverageValue();
    }
    
    public double getValue(){
        return pot.getValue();
    }
    
    public void reset(){
        pot.resetAccumulator();
    }
    
}
