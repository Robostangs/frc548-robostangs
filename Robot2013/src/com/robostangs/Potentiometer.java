package com.robostangs;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.PIDSource;

/**
 *
 * @author sky
 * Converts a potentiometer into a PID source
 */
public class Potentiometer extends AnalogChannel implements PIDSource {
    
    public Potentiometer(int port) {
        super(port);  //calls AnalogChannel constructor
    }

    public double pidGet() {
        return getAverageValue();
    }
}
