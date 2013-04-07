package com.robostangs;

/**
 *
 * @author sky
 */
public class DefaultConstants {
    /*
     * Used in multiple classes
     */
    public static final double JAG_CONFIG_TIME = 0.5;
    public static final double BENNETT_CONSTANT = 0.44;
    public static final double TELEOP_DRIVE_TIME = 1.5;

    /*
     * Arm
     */
    public static final int ARM_JAG_POS = 11;
    public static final int ARM_POT_PORT = 2;
    public static final int ARM_POT_MIN_VALUE = 47;
    public static final int ARM_POT_MAX_VALUE = 700;
    public static final int ARM_POT_SLOW_VALUE = 330;
    public static final double ARM_MIN_VOLTAGE = 0.07;
    // Arm PID
    public static final double ARM_KP_SMALL = 0.0128;
    public static final double ARM_KI_SMALL = 0.0000009;
    public static final double ARM_KD_SMALL = 0.0;
    public static final double ARM_KP_MED = 0.0157;
    public static final double ARM_KI_MED = 0.00000001;
    public static final double ARM_KD_MED = 0.0;
    public static final double ARM_KP_LARGE = 0.0161;
    public static final double ARM_KI_LARGE = 0.0;
    public static final double ARM_KD_LARGE = 0.0;
    public static final double ARM_KP_CAM = 0.0;
    public static final double ARM_KI_CAM = 0.0;
    public static final double ARM_KD_CAM = 0.0;
    public static final double ARM_MIN_POWER = -0.95;
    public static final double ARM_MAX_POWER = 0.95;
    public static final double ARM_PID_POT_MIN = 318;
    public static final double ARM_PID_POT_MAX = 725;

    /**
     * 0.015
     * 1.0 * 10 ^ -6
     * 0
     */
    
    /**
     * 0.0151
     * 1.0 * 10 ^ -8
     * 0
     */
    /**
     * 0.012
     * 1.0 * 10 ^ -6
     * 0
     */
    //positions
    public static final double ARM_SHOOTING_POS = 430;
    public static final double ARM_START_POS = 600; //TODO: get actual value

    /*
     * Autonomous
     */
    public static final double AUTON_DRIVE_POWER = 0.3;
    public static final double AUTON_TURN_POWER = 0.5;
    public static final double AUTON_ARM_POS = ARM_SHOOTING_POS;
    
    /*
     * Climber
     */
    public static final int CLIMBER_LEFT_SERVO_POS = 1;
    public static final int CLIMBER_RIGHT_SERVO_POS = 2;
    public static final double CLIMBER_LEFT_OUT_POS = 1.0;
    public static final double CLIMBER_RIGHT_OUT_POS = 1.0;
    public static final double CLIMBER_LEFT_IN_POS = 0.0;
    public static final double CLIMBER_RIGHT_IN_POS = 0.0;
    
    /*
     * Conveyors
     */
    public static final int CONV_SHOOT_JAG_POS = 12;
    public static final int CONV_INGEST_JAG_POS = 13;
    public static final double CONV_SHOOTER_POWER = 1.0;
    public static final double CONV_INGEST_POWER = 0.95;
    
    /*
     * Camera
     */
    public static final int CAM_Y_OFFSET = 0;
    public static final int CAM_X_OFFSET = 0;

    /*
     * DriveTrain
     */
    //Positions
    public static final int DT_LEFT_ENCODER_FRONT = 2;
    public static final int DT_LEFT_ENCODER_BACK = 3;
    public static final int DT_RIGHT_ENCODER_FRONT = 4;
    public static final int DT_RIGHT_ENCODER_BACK = 5;
    public static final int DT_GYRO_POS = 0;
    public static final int DT_JAG_LEFT_FRONT_POS = 4; //left drive 1
    public static final int DT_JAG_LEFT_MID_POS = 5; //left drive 2
    public static final int DT_JAG_LEFT_BACK_POS = 6; //left drive 3
    public static final int DT_JAG_RIGHT_FRONT_POS = 7; //right drive 1
    public static final int DT_JAG_RIGHT_MID_POS = 8; // right drive 2
    public static final int DT_JAG_RIGHT_BACK_POS = 9; //right drive 3
    public static final int DT_JAG_CLIMB_POS = 14;
    public static final int DT_SERVO_POS = 1;
    public static final int DT_CLIMB_POS = 180;
    public static final int DT_DRIVE_POS = 0;
    
    //Drive Straight
    public static final double DT_STRAIGHT_LEFT_INC = 0.0;
    public static final double DT_STRAIGHT_LEFT_DEC = 0.0;
    public static final double DT_STRAIGHT_RIGHT_INC = 0.0;
    public static final double DT_STRAIGHT_RIGHT_DEC = 0.0;

    //Timer
    public static final double DT_CONV_VOLT_TO_M_PER_SEC = 0.0;
    public static final double DT_DELAY_TIME = 1.0;
    
    //PID
    public static final double DT_PID_K_P = 0.0;
    public static final double DT_PID_K_I = 0.0;
    public static final double DT_PID_K_D = 0.0;
    
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
    public static final int LIFTER_JAG_POS = 10;
    public static final double LIFTER_UP_POWER = 0.4;
    public static final double LIFTER_DOWN_POWER = 0.8;
    public static final double LIFTER_UP_TIME = 2;
    public static final double LIFTER_DOWN_TIME = 1.6;
    public static final int LIFTER_TOP_PROX_DIGITAL_PORT = 2;
    public static final int LIFTER_TOP_PROX_SOLENOID_PORT = 2;
    public static final int LIFTER_BOTTOM_PROX_DIGITAL_PORT = 1;
    public static final int LIFTER_BOTTOM_PROX_SOLENOID_PORT = 1;
        
    /*
     * Shooter
     */ 
    public static final int SHOOTER_JAG1_POS = 2;
    public static final int SHOOTER_JAG2_POS = 1;
    public static final int SHOOTER_JAG3_POS = 3;

    public static final double SHOOTER_MAX_POWER = 1.0;
    public static final double SHOOTER_FEED_POWER = 0.22;
    public static final double TIME_TO_SHOOT_ONE = 5.0;
    public static final double TIME_TO_SHOOT_TWO = 0.0;
    public static final double TIME_TO_SHOOT_THREE = 0.0;
    public static final double TIME_TO_SHOOT_FOUR = 0.0;
    public static final double SHOOTER_FULL_BATTERY_VOLTAGE = 14.0;
    public static final double SHOOTER_VOLTAGE_TOLERANCE = 0.0;
    public static final double SHOOTER_READY_CURRENT_MAX = 0.0;
    public static final double SHOOTER_READY_CURRENT_MIN = 0.0; 
    //GOOD SHOOTING ANGLE: 247
    
    /*
     * StopWatch
     */
    public static final double MICRO_TO_BASE = 0.0;
    
    /*
     * XboxController
     */
    //Driver
    public static final double XBOX_DRIVER_DRIFT = 0.2;
    public static final int XBOX_DRIVER_PORT = 1;
    
    //Manipulator
    public static final int XBOX_MANIP_PORT = 2;
    public static final double XBOX_MANIP_DRIFT = 0.15;
}
