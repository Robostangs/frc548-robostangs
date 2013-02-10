/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robostangs;

import edu.wpi.first.wpilibj.Relay;

/**
 * runs the ingestor
 * maintainer: Sam
 */
public class Ingestor {
    private static Ingestor instance = null;
    private static Relay ingest;
    private static boolean isOn;
    
    private Ingestor() {
        ingest = new Relay(Constants.INGEST_RELAY_POS);
    }
    
    public static Ingestor getInstance() {
        if (instance == null) {
            instance = new Ingestor();
        }
        return instance;
    }
    
    /**
     * Turn on ingestor
     */
    public static void turnOn() {
        isOn = true;
        ingest.set(Relay.Value.kForward);
    }
    
    /**
     * Turn off ingestor
     */
    public static void turnOff() {
        isOn = false;
        ingest.set(Relay.Value.kOn);
    }
    
    /**
     * Ingestor goes in reverse
     */
    public static void reverse() {
        isOn = true;
        ingest.set(Relay.Value.kReverse);
    }
    
    /**
     * return isOn
     * @return 
     */
    public static boolean getState() {
        return isOn;
    }
    
}
