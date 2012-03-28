package com.robostangs;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.PIDController;

/*
 *
 * @author Josh
 */
public class DriveTrain {
    private PIDController camPid;
    private DriveMotors output;
    private Gyro gyro;
    public Encoder leftEncoder;
    public Encoder rightEncoder;
    public Camera axisCam;
    
    public DriveTrain(){
        
        axisCam = new Camera();
        gyro = new Gyro(Constants.GYRO_POS);
        output = new DriveMotors();
        
        leftEncoder = new Encoder(Constants.DRIVE_LEFT_ENCODER_1, Constants.DRIVE_LEFT_ENCODER_2);
        rightEncoder = new Encoder(Constants.DRIVE_RIGHT_ENCODER_1, Constants.DRIVE_RIGHT_ENCODER_2);

        leftEncoder.setDistancePerPulse(Constants.LEFT_DISTANCE_PER_TICK);
        rightEncoder.setDistancePerPulse(Constants.RIGHT_DISTANCE_PER_TICK);
        
        leftEncoder.start();
        rightEncoder.start();
        
        /*
         * Pid controller used to turn the robot toward a target when
         * we are tracking.  Turned off otherwise.
         */
        camPid = new PIDController(Constants.DKp, Constants.DKi, Constants.DKd, gyro, output);
        camPid.setOutputRange(-.5, .5);      //Allow range of turing motion.
        //camPid.setInputRange(0, 640);
        camPid.setContinuous(false);
        camPid.setTolerance(.6);
        camPid.disable();
    }
    
    public void driveXbox(double leftStick, double rightStick){
        if(Math.abs(leftStick) < .2){
            leftStick = 0;
        } 
        if(Math.abs(rightStick) < .2){
            rightStick = 0;
        }
         //If turning, y=x^1.4, retain sign;
        if((leftStick < -.3 && rightStick > .3) || (leftStick > .3 && rightStick < -.3)){
            rightStick = rightStick*rightStick * (rightStick / Math.abs(rightStick));
            leftStick = leftStick*leftStick * (leftStick / Math.abs(leftStick));
        }
        drive(leftStick, rightStick);
    }
    
    public void drive(double left, double right){
        /*
         * Disabling pid will set output to zero, we only want to
         * turn pid off it if it is actually on.
         */
        if(camPid.isEnable()){
            camPid.disable();      //Turn off pid, manuall control
        }
        
       
        output.set(left,left,right,right);
    }
    
    /*
     * Forces the robot in a straight direction,
     * used when we are facing a target.
     */
    public void driveStraight(double left, double right, double initGyro){
        if(getGyro() - initGyro < -0.5){
            right = right*.88;
            left = left * 1.06;
            drive(left,right);
        }else if(getGyro() - initGyro > 0.5){
            left = left*.90;
            right = right*1.01;
            drive(left,right);
        }else{
            drive(left,right);
        }
    }
    
    /*
     * Turns the robot.
     * True sets clockwise,
     * False sets counterclockwise.
     */
    public void turn(boolean dir){
        if(dir)
            output.set(0.5,0.5,-0.5,-0.5);
        else if(!dir)
            output.set(-0.5,-0.5,0.5,0.5);
        else
            output.set(0,0,0,0); 
    }
    
    /*
     * Set where the center of the target should be.
     * Parameter position is usually 320 (half of 640, so in the center of the image)
     * Will only seek a target if we are off by more than 20 pixels
     */
    
    public void setPosition(double position)
    {
        //System.out.println("PositiontoGo: " + position + " current: " + gyro.pidGet()) ;
        camPid.setSetpoint(position);
        camPid.enable();
    }
    
    /*
     * Disables pid, sets the output to zero.
     */
    public void stop()
    {
        camPid.disable();
        output.set(0.0);
    }
    
    /*
     * Returns the difference between the setpoint and the current position (pixels)
     */
    public double getPidError()
    {
        return camPid.getError();
    }
    
    /*
     * Returns true if camera centering pid is enabled.
     */
    public boolean pidEnabled()
    {
        return camPid.isEnable();
    }
    
    /*
     * Set both encoders to zero distance.
     */
    public void resetEncoders(){
        leftEncoder.reset();
        rightEncoder.reset();
    }
   
    /*
     * Return the angle of the gyro. (Unused, i.e. no gyro on robot)
     */
    public double getGyro(){
        return gyro.getAngle();
    }
    
    /*
     * Return the distance that the left encoder has traveled since last reset, in meters.
     */
    public double getLeftEncoder(){
        return -(leftEncoder.getDistance());
    }
    
    /*
     * Return the distance that the right encoder has traveled since last reset, in meters.
     */
    public double getRightEncoder(){
        return rightEncoder.getDistance();
    }
    
    /*
     * Return left encoder clicks, total since last reset.
     */
    public double getLeftCount(){
        return leftEncoder.get();
    }
    
    /*
     * Return right encoder clicks, total since last reset.
     */
    public double getRightCount(){
        return rightEncoder.get();
    }
    
    /*
     * Output some test data to the screen. (Debugging)
     */
    public void info(){
        if(axisCam.beginCalc){
            System.out.println("Enabled: " + camPid.isEnable() + " Setpoint: " + camPid.getSetpoint() + " current: " + gyro.pidGet() + " error: " + camPid.getError());
        }
    }
    
    /*
     * According to pid, are we centered with the square.
     */
    public boolean onTarget(){
        if(Math.abs(gyro.getAngle() - camPid.getSetpoint()) < 1){
            return true;
        }else{
            return false;
        }
    }
}