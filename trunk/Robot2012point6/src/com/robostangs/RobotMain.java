/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.robostangs;


import edu.wpi.first.wpilibj.Dashboard;
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
    private boolean manipulatorRpmControl = true;
    private double voltage = 0;
    private int currentManipButton = 3; //0-3, arm pid config
    private double angleOffset = 0;     //used for centering toward target
    private boolean onTarget = false;
    
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
        /*
         * Check the air pressure, turn on compressor if nessisary.
         */
        air.checkPressure();
        
        /*
         * Check Shooter Jaguars
         */
        shoot.checkShooterJags();
        
        //Log.getInstance().write(Timer.getFPGATimestamp() + " , " + drive.getRightCount() + " , " + drive.getLeftCount() + " , " + drive.getLeftEncoder() + " , " + drive.getRightEncoder());
        
        //get the battery voltage
        voltage = DriverStation.getInstance().getBatteryVoltage();
        
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
         * Driver manual rpm speeds
         */
        if(xboxDriver.aButton()){           //Front key rpm
            manipulatorRpmControl = false;
            shoot.setRpmBackspin(Constants.SHOOTER_FRONT_KEY_RPM);
        }else if(xboxDriver.bButton()){     //Front Fender rpm
            manipulatorRpmControl = false;
            shoot.fenderShot();
        }else if(xboxDriver.xButton()){     //Side fender rpm
            manipulatorRpmControl = false;
            shoot.setRpmBackspin(Constants.SHOOTER_SIDE_FENDER_RPM);
        }else if(xboxDriver.yButton()){     //Front fender rpm
            manipulatorRpmControl = false;
            shoot.fenderShot();
        }else{
            manipulatorRpmControl = true;
        }

        /*
         * Driver shifting
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
         * Manipulator arm position.
         * Checks to make sure ingestor is not out, where applicable.
         */
        if(xboxDriver.bButton()){
             if(!air.getIngestCylinder()){
                    if(currentManipButton != 3){
                        arm.setPidTop();
                        currentManipButton = 3;
                    }
                    arm.setPosition(Constants.ARM_TOP);
                }
        }else if(Math.abs(xboxManip.rightStickYAxis()) < 0.20){
            //Manipulator is not using the YAxis2 to manual set
            if(xboxManip.bButton()){
                if(currentManipButton != 0){
                    arm.setPidZero();
                    currentManipButton = 0;
                }
                arm.setPosition(Constants.ARM_ZEROPOSITION);                
            }
            else if(xboxManip.aButton()){
                if(!air.getIngestCylinder()){
                    if(currentManipButton != 1){
                        arm.setPidBottom();
                        currentManipButton = 1;
                    }
                    arm.setPosition(Constants.ARM_BOTTOM);
                }
            }
            else if(xboxManip.xButton()){
                if(!air.getIngestCylinder()){
                    if(currentManipButton != 2){
                        arm.setPidMiddle();
                        currentManipButton = 2;
                    }
                    arm.setPosition(Constants.ARM_MIDDLE);
                }
            }
            else if(xboxManip.yButton()){
                if(!air.getIngestCylinder()){
                    if(currentManipButton != 3){
                        arm.setPidTop();
                        currentManipButton = 3;
                    }
                    arm.setPosition(Constants.ARM_TOP);
                }
            }else if(xboxManip.triggerAxis() > .5){
                if(currentManipButton != 4){
                    currentManipButton = 4;
                    arm.setPidZero();
                }
                arm.setPosition(Constants.ARM_ZEROPOSITION);  
            }else{
                //Stay in place if no joystick or buttons
                //if(arm.pidEnabled()){
                //    arm.setPosition(arm.getPotentiometer());
                //}else{
                    arm.setSpeed(0);  
                //}
            }
        }else{
            //Set the arm to manual speed
            arm.setSpeed(-xboxManip.rightStickYAxis());
        }
        
        /*
         * Manipulator conveyor controls
         */
        if(xboxManip.rBumper()){
            shoot.setConveyorSpeed(.4);
        }else if(xboxManip.lBumper()){
            shoot.setConveyorSpeed(-1);
        }else{
            shoot.setConveyorSpeed(0);
        }
        

        /*
        * Manipulator ingesting and shooting
        */
        if(xboxManip.triggerAxis() > .2){
            //Left trigger is pressed
            if(arm.getAngle() < Constants.INGESTOR_ARM_MAX_ANGLE){
                air.setIngestCylinder(true);
                shoot.turnOnIngestor();
                shoot.setConveyorSpeed(-1);
            }
            if(manipulatorRpmControl){
                shoot.setRpmBoth(-xboxManip.triggerAxis() * 2200);        //800-2000
            }
        }else if(xboxManip.triggerAxis() < -.2){
            //Right trigger is pressed
            air.setIngestCylinder(false);
            if(manipulatorRpmControl){
                shoot.setRpmBackspin(-xboxManip.triggerAxis() * 2300 + 440);        //900-2740
            }
            shoot.turnOffIngestor();
        }else{
            air.setIngestCylinder(false);
            shoot.turnOffIngestor();
            if(seekingTarget && drive.onTarget()){  
                //Automatic shooting speed (if no triggers pressed)
                if(manipulatorRpmControl){
                    shoot.setRpmFromDistance(drive.axisCam.getDistance(), voltage);
                }
            }else if(seekingTarget && xboxDriver.lBumper()){
                //Override target seeking
                shoot.setRpmFromDistance(drive.axisCam.getDistance(), voltage);
                drive.stop();
            }else{
                if(manipulatorRpmControl){
                    if(shoot.getRpmOffset() == 0){
                        shoot.setRpmBoth(0);
                    }else{
                        shoot.setRpmBoth(0);
                    }
                }
            }
        }

        /*
         * Manipulator ramp controls, don't exist yet
         * TODO: Check for is it time to deploy ramps?
         *
        if(xboxManip.rBumper()){
            air.setRampCylinder(true);
        }else{
            air.setRampCylinder(false);
        }*/
        
        
        /*
         * lbumper sets distance to rpm, 
         * rbumper sets distance to rpm and attempts to center.
         */
        if(xboxDriver.lBumper()){
            seekingTarget = true;
            try {
                drive.axisCam.getImage();
                System.out.println("D: " + drive.axisCam.getDistance() + " xc: " +drive.axisCam.getXCenter() + " h " + drive.axisCam.getHeading());
            } catch (AxisCameraException ex) {
                ex.printStackTrace();
            } catch (NIVisionException ex) {
                ex.printStackTrace();
            }
            drive.driveXbox(-xboxDriver.leftStickYAxis(), -xboxDriver.rightStickYAxis());
        }else if(xboxDriver.rBumper()){
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
                manipulatorRpmControl = false;
            }
            
            if(drive.onTarget()){
                System.out.println("On target!");
                onTarget = true;
                drive.info();
                drive.stop();
                drive.driveStraight(-xboxDriver.leftStickYAxis(), -xboxDriver.rightStickYAxis(), angleOffset);
                                manipulatorRpmControl = false;

            }else if(onTarget){
                System.out.println("Aligned");
                onTarget = true;
                drive.info();
                drive.driveStraight(-xboxDriver.leftStickYAxis(), -xboxDriver.rightStickYAxis(), angleOffset);
                                manipulatorRpmControl = false;

            }else{
                System.out.println("Still looking");
                double target = drive.axisCam.getHeading() + angleOffset;
                drive.info();
                drive.setPosition(drive.axisCam.getHeading() + angleOffset);
                                manipulatorRpmControl = false;

            }
            
        }else{
            //We are not rectangle tracking, drive normally
            seekingTarget = false;
            drive.driveXbox(-xboxDriver.leftStickYAxis(), -xboxDriver.rightStickYAxis());
                            manipulatorRpmControl = true;

        }
    }
}
