/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robostangs;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author sky
 * Extends the timer class to convert to seconds
 */
public class StopWatch extends Timer{
        
    /**
     * Converts the timer to seconds
     * @return current number of seconds passed 
     */
    public double getSeconds(){
        return get() / MathUtils.pow(10, 6);
    }
}
