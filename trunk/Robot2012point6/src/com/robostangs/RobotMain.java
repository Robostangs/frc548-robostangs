/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.robostangs;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.NIVisionException;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotMain extends IterativeRobot {
 
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    private Pneumatics air;
    private XBoxController xboxDriver;      //Driver Joystick
    private XBoxController xboxManip;       //Manipulator Joystick
    private DriveTrain drive;               //Tank drive
    private Arm arm;                        //Arm
    private Shooter shoot;                  //Shooter
    private Autonomous2 auton;
    private CustomDashboard dash;
    private boolean seekingTarget = false;
    private double voltage = 0;
    private int currentManipButton = 3; //0-3, arm pid config
    private double angleOffset = 0;     //used for centering toward target
    private boolean onTarget = false;
    private double lowestVoltage = 15.0f;
    private double lastVoltageTime = 0;
    private double lowestVoltageTime = 0;
    private boolean ingestorDebugMode = false;  //if true, dont drop while ingesting for human loading 
    
    public void robotInit() {
        air = new Pneumatics();
        xboxDriver = new XBoxController(Constants.XBOXDRIVER_PORT);
        xboxManip = new XBoxController(Constants.XBOXMANIP_PORT);
        drive = new DriveTrain();
        arm = new Arm();    
        shoot = new Shooter();
        auton = new Autonomous2(drive, shoot, arm, air);
        dash = new CustomDashboard();
        drive.resetEncoders();
    }
    public void autonomousInit(){
        auton.init();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        auton.run();
        //Log.getInstance().write(Timer.getFPGATimestamp() + " , " + drive.getRightCount() + " , " + drive.getLeftCount() + " , " + drive.getLeftEncoder() + " , " + drive.getRightEncoder());
    }
    
    public void disabledInit(){
        arm.stop();
        drive.stop();
        auton.init();
    }
    
    public void teleopInit(){
        arm.stop();
        arm.setOutRange(-1,1);
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        //System.out.println("Arm angle: " + arm.getAngle() + " pot: " + arm.getPotentiometer());// + " potV: " + arm.getPotVoltage() + " distance: " + drive.axisCam.getDistance() + " TargetRpm: " + shoot.getTargetRpm() + " offset: " + shoot.getRpmOffset());
        //System.out.println("LeftEncoder: " + drive.getLeftEncoder() + " Right Encoder: " + drive.getRightEncoder() + " gyro: " + drive.getGyro());
        //System.out.println("L: " + drive.getLeftCount() + " R: " + drive.getRightCount());
        //System.out.println("Bottom: " + shoot.getBottomRpm() + " " + " Top: " + shoot.getTopRpm());
        //System.out.println("Angle: " + drive.getGyro());
        //Log.getInstance().write(Double.toString(shoot.topVoltage()));
        //System.out.println("VoltageShooter:" + shoot.topVoltage());
        /*
         * Check the air pressure, turn on compressor if nessisary.
         */
        if(shoot.getTargetRpm() == 0){
            air.checkPressure();
        }else{
            air.stopCompressor();
        }
        
        /*
         * Check Shooter Jaguars
         */
        shoot.checkShooterJags();
                
        //get the battery voltage        
        voltage = DriverStation.getInstance().getBatteryVoltage();
        
        if(voltage < lowestVoltage){
            lowestVoltage = voltage;
            lowestVoltageTime = Timer.getFPGATimestamp();
        }
        
        //log the lowest recorded voltage every 5 seconds
        //if(Timer.getFPGATimestamp() > (lastVoltageTime + 5)){
        //    Log.getInstance().write("Lowest Voltage, " + lowestVoltage + " , Recorded at " + lowestVoltageTime);
        //    lastVoltageTime = Timer.getFPGATimestamp();
        //}
        
        //update dashboard
        dash.updateDashboard(drive.onTarget(), Double.toString((int)drive.axisCam.getDistance()), Double.toString((int)shoot.getTargetRpm()), Double.toString((int)shoot.getTopRpm()), Double.toString((int)shoot.getRpmOffset()), Double.toString(arm.getAngle()));
            
        /*
         * Driver increase rpms by 25
         */
        if(xboxDriver.backButton()){
            shoot.setRpmOffset(shoot.getRpmOffset()-25);
        }
        if(xboxDriver.startButton()){
            shoot.setRpmOffset(shoot.getRpmOffset()+25);
        }
        
        /*
         * Shooter Rpm Controls
         */
        if(xboxDriver.aButton()){           //Fender SHot
            shoot.fenderShot();
        }else if(xboxDriver.bButton()){     //Side key rpm
            shoot.setRpmBackspin(Constants.SHOOTER_SIDE_KEY_RPM);
        }else if(xboxDriver.xButton()){     //Front fender rpm
            shoot.setRpmBackspin(Constants.SHOOTER_FRONT_KEY_RPM);
        }else if(xboxDriver.yButton()){     //Front fender rpm
            shoot.fenderShot();
        }else{
            //Manipulator
            if(xboxManip.triggerAxis() < -.2){
                //Right Trigger, manual shoot
                shoot.setRpmBoth(-xboxManip.triggerAxis() * 2200);        //800-2000
            }else if(xboxManip.triggerAxis() > .2){
                //Left Trigger, shooter ingesting
                shoot.setRpmBoth(-xboxManip.triggerAxis() * 2300 + 440);        //900-2740
            }else{
                if(seekingTarget && drive.onTarget()){
                    shoot.setRpmFromDistance(drive.axisCam.getDistance(), voltage); 
                }
                if(xboxDriver.lBumper()){
                    shoot.setRpmFromDistance(drive.axisCam.getDistance(), voltage);    
                }else{
                    shoot.setRpmBoth(0);
                }
            }
        }

        /*
         * Shifting
         */
        if(xboxDriver.triggerAxis() > .5){
            //Left Trigger Pressed
            air.setGear(!Constants.LOW_SPEED);
        }else if (xboxDriver.triggerAxis() < -.5){
            //Right Trigger Pressed
            air.setGear(Constants.LOW_SPEED);
        }else if (xboxDriver.rBumper()){
            air.setGear(Constants.LOW_SPEED);
        }else{
            if(arm.getAngle()<-40){
                air.setGear(!Constants.LOW_SPEED);
            }else{
                air.setGear(Constants.LOW_SPEED);
            }
        }
        
        /*
         * Ingestor debug mode
         * Does not drop arm while ingesting if ingestorDebugMode
         * 
         * TODO: get R3 Button*/
        if(xboxManip.backButton()){
            System.out.println("debug off");
            ingestorDebugMode = false;
        }else if(xboxManip.startButton()){
            System.out.println("debug on");
            ingestorDebugMode = true;
        }
        
        /*
         * Arm position.
         * Checks to make sure ingestor is not out, where applicable.
         *
        if(xboxDriver.bButton()){                   
            //Driver Override
            if(!air.getIngestCylinder()){
                if(currentManipButton != 3){
                    arm.setPidTop();
                    currentManipButton = 3;
                }
                arm.setPosition(Constants.ARM_TOP);
            }
        }else */
        if(Math.abs(xboxManip.rightStickYAxis()) < 0.20){
            //Manipulator is not using the YAxis2 to manual set, use buttons
            if(xboxManip.bButton()){
                //Manipulator B Button, drop the arm to the bottom
                if(currentManipButton != 0){
                    arm.setPidBottom();
                    currentManipButton = 0;
                }
                arm.setPosition(Constants.ARM_BOTTOM);                
            }else if(xboxManip.aButton()){
                //Manipulator A Button, move arm to low position
                if(!air.getIngestCylinder()){
                    if(currentManipButton != 1){
                        arm.setPidBottom();
                        currentManipButton = 1;
                    }
                    arm.setPosition(Constants.ARM_LOW);
                }
            }
            else if(xboxManip.xButton()){
                //Manipulator X Button, move arm to middle position
                if(!air.getIngestCylinder()){
                    if(currentManipButton != 2){
                        arm.setPidMiddle();
                        currentManipButton = 2;
                    }
                    arm.setPosition(Constants.ARM_MIDDLE);
                }
            }
            else if(xboxManip.yButton()){
                //Manipulator Y Button, move arm to top position
                if(!air.getIngestCylinder()){
                    if(currentManipButton != 3){
                        arm.setPidTop();
                        currentManipButton = 3;
                    }
                    arm.setPosition(Constants.ARM_TOP);
                }
            }else if(xboxManip.triggerAxis() > .5){
                //Manipulator Left Trigger, keep arm at bottom while ingesting.
                if(!ingestorDebugMode){
                    if(currentManipButton != 4){
                        arm.setPidBottom();
                        currentManipButton = 4;
                    }
                    arm.setPosition(Constants.ARM_BOTTOM);  
                }
            }else{
                //No buttons pressed, stop the arm.
                arm.setSpeed(0);  
            }
        }else{
            //Manipulatory is using right y axis to move arm manually
            arm.setSpeed(-xboxManip.rightStickYAxis());
        }
        
        /*
         * Conveyor controls
         */
        if(xboxManip.rBumper()){
            Log.getInstance().write(Timer.getFPGATimestamp() + " , "  + Double.toString(shoot.getTopRpm()));
            if(arm.getAngle() > 52.5){
                //The arm is at the top, we are at the fender, expell quickly
                shoot.setConveyorSpeed(.7);
            }else{
                //Arm is not at top, don't shoot too quickly so shooter
                //wheels have time to spin up
                shoot.setConveyorSpeed(.6);
            }
        }else if(xboxManip.lBumper()){
            //Run the conveyor in reverse
            shoot.setConveyorSpeed(-1);
        }else if(xboxManip.triggerAxis() > .2){
            //Manipulator is using the ingest button
            shoot.setConveyorSpeed(-1);
        }else{
            //TODO: Check for a certain arm speed to retain balls -1
            shoot.setConveyorSpeed(0);
        }

        /*
         * Ingestor controls
         * Will only deploy ingestor if arm is low enough to not break perimeter
         */
        if(xboxManip.triggerAxis() > .2){
            //Left trigger is pressed
            if(arm.getAngle() < Constants.INGESTOR_ARM_MAX_ANGLE){
                air.setIngestCylinder(true);
                shoot.turnOnIngestor();
            }else{
                air.setIngestCylinder(false);
                shoot.turnOffIngestor();
            }
        }else{
            air.setIngestCylinder(false);
            shoot.turnOffIngestor();
        }
        
        /*
         * 'Stinger' Controls
         */
        if(xboxDriver.rightJoystickButton()){
            //TODO: Pneumatics deploy stinger
        }else if(xboxManip.leftJoystickButton()){
            //TODO: Pneumatics retract stinger
        }
        
        /*
         * lbumper sets distance to rpm, 
         * rbumper sets distance to rpm and attempts to center.
         */
        if(xboxDriver.lBumper()){
            if(!seekingTarget){
                seekingTarget = true;
                try {
                    drive.axisCam.getImage();
                } catch (AxisCameraException ex) {
                    ex.printStackTrace();
                } catch (NIVisionException ex) {
                    ex.printStackTrace();
                }     
            }
            drive.driveXbox(-xboxDriver.leftStickYAxis(), -xboxDriver.rightStickYAxis());
        }else if(xboxDriver.rBumper()){
            //TODO: Improve
            System.out.println("TargetXCenter: " + drive.axisCam.getTargetCenterX() + " TargetYCenter: " + drive.axisCam.getTargetCenterY() + " Heading: " + drive.axisCam.getHeading() + " Distance: " + drive.axisCam.getDistance());
            if(!seekingTarget){
                angleOffset = drive.getGyro();
                onTarget = false;
                seekingTarget = true;
                System.out.println("beginning seek");
                try {
                    drive.axisCam.getImage();
                } catch (AxisCameraException ex) {
                    ex.printStackTrace();
                } catch (NIVisionException ex) {
                    ex.printStackTrace();
                }
            }
            
            if(drive.onTarget()){
                System.out.println("On target!");
                onTarget = true;
                drive.info();
                drive.stop();
                drive.driveStraight(-xboxDriver.leftStickYAxis(), -xboxDriver.rightStickYAxis(), angleOffset);
            }else{
                System.out.println("Still looking");
                drive.info();
                drive.setPosition(drive.axisCam.getHeading() + angleOffset);
            }
        }else{
            //We are not rectangle tracking, drive normally
            seekingTarget = false;
            drive.driveXbox(-xboxDriver.leftStickYAxis(), -xboxDriver.rightStickYAxis());
        }
    }
}