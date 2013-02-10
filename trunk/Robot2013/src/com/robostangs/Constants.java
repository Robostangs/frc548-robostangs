/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robostangs;

/**
 *
 * @author sky
 * TODO: everything pretty much.
 */
public class Constants {
    /*
     * Used in multiple classes
     */
    public static final double JAG_CONFIG_TIME = 0.5;

    /*
     * Arm
     */
    public static final int ARM_JAG_POS = 10;
    public static final int POT_A_PORT = 0;
    public static final int POT_B_PORT = 0;
    public static final int POT_MIN_VALUE = 0;
    public static final int POT_MAX_VALUE = 0;
    public static final double ARM_POT_ZERO = 0.0;
    public static final double POT_TO_DEGREES = 0.0;
    
    // Arm PID
    public static final double ARM_KP_A = 0.0;
    public static final double ARM_KI_A = 0.0;
    public static final double ARM_KD_A = 0.0;
    public static final double ARM_KP_B = 0.0;
    public static final double ARM_KI_B = 0.0;
    public static final double ARM_KD_B = 0.0;
    public static final double ARM_PYRAMID_POS = 0.0;
    public static final double ARM_FEED_POS = 0.0;
    public static final double ARM_MIN_POWER = 0.0;
    public static final double ARM_MAX_POWER = 0.0;
    
    /*
     * Autonomous
     */
    public static final double AUTON_DRIVE_POWER = 0.5;
    public static final double AUTON_INGEST_DRIVE_POWER = 0.5;
    public static final double AUTON_TURN_POWER = 0.5;
    
    //Fallback Mode
    //also uses AUTON_DRIVE_POWER and AUTON_TURN_POWER
    public static final double AUTON_FALLBACK_DRIVE_TIME = 0.0;
    public static final double AUTON_DRIVE_ANGLE = 0.0;
    public static final double AUTON_FALLBACK_TURN_TIME = 0.0;
    public static final double AUTON_TURN_ANGLE = 0.0;
    public static final double AUTON_FALLBACK_ARM_MOVE_TIME = 0.0;
    public static final double AUTON_ARM_POS = 0.0;
    public static final double AUTON_FALLBACK_SHOOT_TIME = 0.0;
    public static final int AUTON_SHOOT_DISC_NUM = 0;
    
    /*
     * Conveyors
     */
    public static final int CONV_SHOOT_JAG_POS = 7;
    public static final int CONV_INGEST_JAG_POS = 8;
    public static final double CONV_POWER = 0.0;   

    /*
     * DriveTrain
     */
    //Positions
    public static final int DT_LEFT_ENCODER_FRONT = 0;
    public static final int DT_LEFT_ENCODER_BACK = 0;
    public static final int DT_RIGHT_ENCODER_FRONT = 0;
    public static final int DT_RIGHT_ENCODER_BACK = 0;
    public static final int DT_GYRO_POS = 0;
    public static final int DT_JAG_LEFT_FRONT_POS = 4; //left drive 1
    public static final int DT_JAG_LEFT_MID_POS = 5; //left drive 2
    public static final int DT_JAG_LEFT_BACK_POS = 6; //left drive 3
    public static final int DT_JAG_RIGHT_FRONT_POS = 12; //right drive 1
    public static final int DT_JAG_RIGHT_MID_POS = 13; // right drive 2
    public static final int DT_JAG_RIGHT_BACK_POS = 14; //right drive 3
    public static final int DT_JAG_CLIMB_POS = 9;
    
    
    //Drive Straight
    public static final double DT_STRAIGHT_LEFT_INC = 0.0;
    public static final double DT_STRAIGHT_LEFT_DEC = 0.0;
    public static final double DT_STRAIGHT_RIGHT_INC = 0.0;
    public static final double DT_STRAIGHT_RIGHT_DEC = 0.0;

    //Timer
    public static final double DT_CONV_VOLT_TO_M_PER_SEC = 0.0;
    public static final double DT_DELAY_TIME = 1.0;
    
    /*
     * FrisbeeTracker
     */
    //Positions
    public static final int INGEST_SWITCH_POS = 0;
    public static final int SHOOT_SWITCH_POS = 0;
    public static final int LIFT_SWITCH_POS = 0;
    
    //Timers
    public static final double INGEST_FRISBEE_TIMER = 0.0;
    public static final double LIFT_FRISBEE_TIMER = 0.0;
    public static final double SHOOT_FRISBEE_TIMER = 0.0;
    
    /*
     * Ingestor
     */
    public static final int INGEST_RELAY_POS = 0;
    
    /*
     * Lifter
     */
    public static final int LIFTER_JAG_POS = 11;
    public static final double LIFTER_POWER = 0.0;
        
    /*
     * Shooter
     */ 
    public static final int SHOOTER_JAG1_POS = 1;
    public static final int SHOOTER_JAG2_POS = 2;
    public static final int SHOOTER_JAG3_POS = 3;

    public static final double SHOOTER_MAX_POWER = 1.0;
    public static final double SHOOTER_FEED_POWER = 0.0;
    public static final double TIME_TO_SHOOT_ONE = 0.0;
    public static final double TIME_TO_SHOOT_TWO = 0.0;
    public static final double TIME_TO_SHOOT_THREE = 0.0;
    public static final double TIME_TO_SHOOT_FOUR = 0.0;
    
    /*
     * StopWatch
     */
    public static final double MICRO_TO_BASE = 0.0;
    
    /*
     * XboxController
     */
    //Driver
    public static final double XBOX_DRIVER_DRIFT = 0.1;
    public static final int XBOX_DRIVER_PORT = 0;
    
    //Manipulator
    public static final int XBOX_MANIP_PORT = 0;
    public static final double XBOX_MANIP_DRIFT = 0.1;
}
