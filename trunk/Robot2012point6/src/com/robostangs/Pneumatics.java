package com.robostangs;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;

/**
 *
 * @author sydney
 * Various pneumatics methods.
 */
public class Pneumatics {
    private Relay compressor;
    private DigitalInput pressureSensor;
    private Solenoid rampCylinder;
    private Solenoid gearCylinder;
    private Solenoid ingestCylinder;

    public Pneumatics(){
        
            compressor = new Relay(1, Constants.COMPRESSOR_RELAY1);
            pressureSensor = new DigitalInput(1, Constants.DIGITAL_INPUT1);
            //rampCylinder = new Solenoid(Constants.RAMPCYLINDER_SOLENOID1, Constants.RAMPCYLINDER_SOLENOID2);        
            gearCylinder = new Solenoid(Constants.GEARCYLINDER_SOLENOID1);
            ingestCylinder = new Solenoid(Constants.INGESTCYLINDER_SOLENOID1);
        
    }
    
    /*
     * Ensure that the compressor has pressure
     */
    public void checkPressure(){
        if(!pressureSensor.get()){
            //Turn on the compressor
            compressor.set(Relay.Value.kReverse);
        }else{
            //Turn off the compressor
            compressor.set(Relay.Value.kOn);
        }
    }
   
    /*
     * Enable/disable ramps
     */
    public void setRampCylinder(boolean x){
        /*
        rampCylinder.set(x);
        */
    }       

    /*
     * Switch the drive train gear
     */
    public void swapGear(){
        gearCylinder.set(!gearCylinder.get());
    }

    /*
     * Set the drive train gear
     * High: true
     * Low: false
     */
    public void setGear(boolean x){
        gearCylinder.set(x);
    }

    /*
     * Return current drive train gear
     */
    public boolean getGear(){
        return gearCylinder.get();
    }

    /*
     * Enable/disable ingestor
     */
    public void setIngestCylinder(boolean x){
        ingestCylinder.set(x);
    }

    /*
     * Return current ingestor state
     */
    public boolean getIngestCylinder(){
        return ingestCylinder.get();
    }
}