package com.robostangs;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

public class Shooter
{
    private Victor conveyor;
    private Relay ingestor;
    private CANJaguar bottomMotor, topMotor;
    private double rpmOffset = 0;
    private double targetRpm = 0;    
    
    public Shooter()
    {
        conveyor = new Victor(Constants.SHOOTER_CONVEYOR_VIC);
        ingestor = new Relay(Constants.SHOOTER_INGESTOR_REL);
        try {
            topMotor = new CANJaguar(Constants.SHOOTER_JAG_TOP_POS);
            bottomMotor = new CANJaguar(Constants.SHOOTER_JAG_BOTTOM_POS);   
        } catch (CANTimeoutException ex) {
            System.out.println("\n\n\n\n****CAN exception at shooter class.");
            ex.printStackTrace();
        }
      
        try {
            topMotor.changeControlMode(CANJaguar.ControlMode.kSpeed);
            topMotor.setPID(Constants.SKp, Constants.SKi, Constants.SKd);
            topMotor.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
            topMotor.configEncoderCodesPerRev(360);
           
            bottomMotor.changeControlMode(CANJaguar.ControlMode.kSpeed);
            bottomMotor.setPID(Constants.SKp, Constants.SKi, Constants.SKd);
            bottomMotor.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
            bottomMotor.configEncoderCodesPerRev(360);
            
            topMotor.enableControl();
            bottomMotor.enableControl();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    /*
     * Set the speed of the conveyor belt.
     */
    public void setConveyorSpeed(double speed){
        conveyor.set(speed);
    }
    
    /*
     * Get the speed of the conveyor belt.
     */
    public double getConveyorSpeed(){
        return conveyor.get();
    }
    
    /*
     * Sets the conveyor forward if rpms are correct, otherwise reverses 
     */
    public boolean conveyorDispense(){
        if(Math.abs(getAverageRpm() - targetRpm) > 70){
            setConveyorSpeed(-1);
            return(false);
        }else{
            setConveyorSpeed(1);
            return(true);
        }
    }
    
    /*
     * Set the speed of the ingestor rollers.
     */
    public void turnOnIngestor(){
        ingestor.set(Relay.Value.kForward);
    }
    
    /*
     * Get the speed of the ingestor rollers.
     */
    public void turnOffIngestor(){
        ingestor.set(Relay.Value.kOn);
    }    
    
    public void fenderShot(){
        try{ 
            topMotor.setX(0);
            bottomMotor.setX(3100);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
    /*
     * Using jaguar pid, set the rpm of the shooter wheels.
     */
    public void setRpm(double rpm){
        targetRpm = rpm+rpmOffset;
        try{ 
            topMotor.setX(targetRpm);
            bottomMotor.setX(targetRpm);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
    /*
     * Add an offset to the shooter wheel rpms.
     */
    public void setRpmOffset(double rpm){
        rpmOffset = rpm;
    }
    
    /*
     * Return the current shooter wheel rpm offset.
     */
    public double getRpmOffset(){
        return rpmOffset;
    }

    /*
     * Turn off jaguar shooter wheel pid.
     */
    public void disablePid(){
        try {
            topMotor.disableControl();
            bottomMotor.disableControl();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
    /*
     * Return the rpm of the top shooter wheel.
     */
    public double getTopRpm(){
        double x = 0;
        try {
            x = (topMotor.getSpeed());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        return x;
    }
    
    /*
     * Return the rpm of the bottom shooter wheel.
     */
    public double getBottomRpm(){
        double x = 0;
        try {
            x = (bottomMotor.getSpeed());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        return x;
    }
    
    /*
     * Return the average shooter wheel rpm.
     */
    public double getAverageRpm(){
        double x = 0;
        try {
            x = (topMotor.getSpeed() + bottomMotor.getSpeed()) /2;
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        return x;
    }
    
    public double getTargetRpm(){
        return targetRpm;
    }
    
    /*
     * Set wheel rpm based on the distance to the target and battery voltage. 
     */
    public void setRpmFromDistance(double distance, double voltage)
    {   
        double d = distance;
        double rpm = 0;
        if(voltage < 11.5){    
            //Very low battery or not working
            rpm = (.0000000753798)*(d*d*d*d) - (.0000812944)*(d*d*d) + (.0282062)*(d*d) - (1.06457)*d + 593.939;
        //rpm = (-.0000000785614)*(d*d*d*d)+(.000142)*(d*d*d)-(.0847712)*(d*d)+(22.802)*(d)-1077.34;
            rpm -= 20;
        }else if(voltage >= 11.5 && voltage <= 13.5){
            //Low battery
            rpm = (.0000000753798)*(d*d*d*d) - (.0000812944)*(d*d*d) + (.0282062)*(d*d) - (1.06457)*d + 593.939;
            rpm += (12.7-voltage)*28.6;
        }else{
            //voltage sensor not working
            rpm = (.0000000753798)*(d*d*d*d) - (.0000812944)*(d*d*d) + (.0282062)*(d*d) - (1.06457)*d + 593.939;

        }
        targetRpm = rpm;
        setRpm(targetRpm);
    }
    
    /*
     * Set wheel rpm based on the distance to the target. 
     */
    public void setRpmFromDistance(double distance)
    {   
        double d = distance;
        double rpm = 0;
        //Assume normal battery
        rpm = (-.0000000785614)*(d*d*d*d)+(.000142)*(d*d*d)-(.0847712)*(d*d)+(22.802)*(d)-1077.34;
        targetRpm = rpm;
        setRpm(targetRpm);
    }
    
    /*
     * Set the shooter wheels to zero speed.
     */
    public void stop()
    {
        try {
            topMotor.setX(0.0);
            bottomMotor.setX(0.0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
    /*
     * Output the shooter wheel rpm to the log file
     */
    public void writeInfo(){
        try {
            //time,topRate,bottomRate
            Log.getInstance().write(Timer.getFPGATimestamp() + " , " + topMotor.getSpeed() + " , " + bottomMotor.getSpeed());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }

    }
    
}
