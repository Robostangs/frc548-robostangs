package com.robostangs;

import edu.wpi.first.wpilibj.PIDController;

public class Arm
{
    public PIDController pid;
    private ArmPot apot;
    private ArmMotor output;

    public Arm()
    {
        output = new ArmMotor();
        apot = new ArmPot();
        pid = new PIDController(Constants.ARM_TOP_AKp, Constants.ARM_TOP_AKi, Constants.ARM_TOP_AKd, apot, output);
        pid.setOutputRange(-.7, .6);     //Do not decrease as quickly (gas strut)
        pid.setInputRange(Constants.ARM_POT_MIN, Constants.ARM_POT_MAX);
        pid.disable();
    }
    
    /*
     * Return the angle of the arm (degrees)
     */
    public double getAngle(){
        double angle = (apot.getPotentiometer() - Constants.ARM_BOTTOM) / 3.8;
        return angle;
    }
    
    /*
     * Manually set the speed of the arm motors.
     */
    public void setSpeed(double speed)
    {
        if(pid.isEnable()){
            pid.disable();    
        }
        if(speed > 0){
            //Do not pass top
            if(getAngle() < Constants.ARM_MAX_ANGLE){
                output.set(speed * Constants.ARM_POWER_COEFFICIENT);
            }else{
                output.set(0);
            }
        }else if(speed < 0){
            //Do not pass bottom
            if(getAngle() > Constants.ARM_MIN_ANGLE){
                output.set(speed * Constants.ARM_POWER_COEFFICIENT);
            }else{
                output.set(0);
            }
        }else{
            output.set(0);
        }
    }
    
    /*
     * Sets the pid position of the arm, takes pot value, not angle.
     */
    public void setPosition(double value)
    {
        if(value <= Constants.ARM_POT_MAX && value >= Constants.ARM_POT_MIN){
            pid.setSetpoint(value);
            pid.enable();
        }else{
            stop();
        }
    }
    
    public void resetPid(){
        pid.reset();
    }
    
    /*
     * Change the pid constants to match a specific arm positon.
     * Reset 
     */
    public void setPidTop(){
        pid.reset();
        pid.setPID(Constants.ARM_TOP_AKp, Constants.ARM_TOP_AKi, Constants.ARM_TOP_AKd);
    }
    public void setPidMiddle(){
        pid.reset();
        pid.setPID(Constants.ARM_MIDDLE_AKp, Constants.ARM_MIDDLE_AKi, Constants.ARM_MIDDLE_AKd);
    }
    public void setPidBottom(){
        pid.reset();
        pid.setPID(Constants.ARM_BOTTOM_AKp, Constants.ARM_BOTTOM_AKi, Constants.ARM_BOTTOM_AKd);
    }
    public void setPidZero(){
        pid.reset();
        pid.setPID(Constants.ARM_ZERO_AKp, Constants.ARM_ZERO_AKi, Constants.ARM_ZERO_AKd);
    }
    
    /*
     * Disable pid and stop the arm.
     */
    public void stop()
    {
        pid.disable();
        output.set(0.0);
    }
    
    /*
     * Get the value of the potentiometer (value, not angle)
     */
    public double getPotentiometer()
    {
        return apot.pidGet();
    }
    
    public double getPotVoltage(){
        return apot.getPotVoltage();
    }
    
    /*
     * Difference between setpoint and current position
     */
    public double getPidError()
    {
        return pid.getError();
    }

    /*
     * Returns true if pid is enabled
     */
    public boolean pidEnabled()
    {
        return pid.isEnable();
    }
}