package com.robostangs;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.PIDSource;

/**
 *
 * @author sky
 * Converts a potentiometer into a PID source
 */
public class Potentiometer extends AnalogChannel implements PIDSource {
    private double degreeConstant;
    
    public Potentiometer(int port) {
        super(port);  //calls AnalogChannel constructor
        if (port == Constants.ARM_POT_A_PORT) {
            degreeConstant = Constants.POT_A_TO_DEGREES;
    }
    }

    //TODO: check logic
    public double getAngle() {
        double currentPot = getAverageValue();
        double angle = currentPot * degreeConstant;
        return angle;
    }
    public double pidGet() {
        return getAverageValue();
    }
}
