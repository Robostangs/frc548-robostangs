/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

/*
 * Development group's code. please don't change anything unless you have the permission of the maintainer
 * Maintainer list:
 * @sky : RobotMain
 */
package com.robostangs;

/*
 * This class is maintained by @sky! (Sydney) 
 * Do not make any changes unless you have her explicit permission!
 */
import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotMain extends IterativeRobot {
    private XboxDriver driver;
    private XboxManip manip;
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        //avoid null pointers
        Arm.getInstance();
        Autonomous.getInstance();
        Camera.getInstance();
        DriveTrain.getInstance();
        FrisbeeTracker.getInstance();
        Loader.getInstance();
        Log.getInstance();
        Shooter.getInstance();
        driver = XboxDriver.getInstance();
        manip = XboxManip.getInstance();
    }
    
    public void autonomousInit() {
        
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Autonomous.run();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        sendDataToDash();
        
        //count frisbees
        FrisbeeTracker.count(Shooter.isFeedMode());
        
        /*
         * Ingestor/Loader System Control
         * Left Bumper: Run ingestor + conveyors
         * Right Bumper: Run lifter + shooter conveyor
         */
        if (!manip.leftTriggerButton() && !manip.lBumper() 
                && !manip.rBumper()) {
            //not using loading system, turn off
            Loader.allOff();
        } else {
            if (manip.lBumper()) {
                Loader.ingest();
            }
            if (manip.rBumper()) {
                Loader.loadShooter();
            }
        }
       
        
        /*
         * Shooter Control
         * Manip Right Trigger: Shoot
         * Manip Left Trigger: Feed
         */
        if (manip.rightTriggerButton()) {
            Shooter.shoot();
        } else if (manip.leftTriggerButton()) {
            Shooter.feed();
            Loader.feed();
        } else {
            Shooter.stop();
        }
        
        /*
         * Manipulator Arm Control
         * Left Stick: Coarse Manual
         * Right Stick: Fine Manual
         * A: Feeder Station
         * B: Flat
         * Y: Under the pyramind shot
         * X: Use Camera to auto-set angle
         */        
        if ((Math.abs(manip.leftStickYAxis()) == 0) 
                && Math.abs(manip.rightStickYAxis()) == 0) {
            //not using the joysticks to manual set, use PID
            if (manip.yButton()) {
                Arm.underPyramidShotPos();
            } else if (manip.aButton()) {
                Arm.flatPos();
            } else if (manip.bButton()) {
                Arm.feedPos();
            } else if (manip.xButton()) {
                //TODO: manip set arm pos based on camera
            } else {
                Arm.stop();
            }
        } else {
            if (Math.abs(manip.leftStickYAxis()) != 0) {
                //coarse control
                Arm.coarseDrive(manip.leftStickYAxis());
            } else if (Math.abs(manip.rightStickYAxis()) != 0) {
                //fine control
                Arm.fineDrive(manip.rightStickYAxis());
            } else {
                Arm.stop();
            }
        }
        
        /*
         * Manip Pot Controls
         * Start: Change Pot
         */
        if (manip.startButton()) {
            Arm.switchPot();
        }
                
        /*
         * Driver left trigger: Run the standalone climber
         */
        if (driver.leftTriggerButton()) {
            //TODO: enable solo climber
        }
        
        /*
         * Shifting between drive mode and climb mode
         * Driver a-button: enable climbing mode
         * Driver b-button: enable drive mode
         */
        if (driver.aButton()) {
            DriveTrain.enableClimbMode();
        } else if (driver.bButton()) {
            DriveTrain.enableDriveMode();
        }
        
        /*
         * TODO: Driver Right Bumper *OR* Manip Back: Take Picture
         */
        if (driver.rBumper() || manip.backButton()) {
            
        }
        
        /*
         *  TODO: If Driver Right Trigger, Enable Auto Align
         */
        if (driver.rightTriggerButton()) {
            
        } else {
            /*
             * Drive Slow if Left Bumper, otherwise drive normally
             */
            if (driver.lBumper()) {
                DriveTrain.driveSlow(driver.leftStickYAxis(), driver.rightStickYAxis());
            } else {
                DriveTrain.humanDrive(driver.leftStickYAxis(), driver.rightStickYAxis());
            }
        }
    }
    
    /**
     * Sends data to the dashboard
     */
    public void sendDataToDash() {
        Arm.sendWhichPotInUse();
        Arm.sendPotData();
        FrisbeeTracker.sendFrisbeeDataToDash();
    }
    
}
