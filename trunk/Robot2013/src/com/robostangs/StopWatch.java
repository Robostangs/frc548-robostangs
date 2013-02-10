package com.robostangs;

import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author sky
 * exists to get timer in seconds
 */
public class StopWatch extends Timer {
    public StopWatch() {
        super();
    }
    
    public double getSeconds() {
        return get() * Constants.MICRO_TO_BASE;
    }
}
