package com.robostangs;

public class Constants {
    /*
     * Used in multiple classes
     */
    public static final double JAG_CONFIG_TIME = 0.5;
    public static final double BENNETT_CONSTANT = 0.44;

    /*
     * Arm
     */
    public static final int ARM_JAG_POS = 11;
    public static final double ARM_MIN_POWER = -0.95;
    public static final double ARM_MAX_POWER = 0.95;

    // Pot A Values
    public static final int ARM_POT_A_PORT = 1;
    public static final double ARM_POT_A_MIN_VALUE = 0;
    public static final double ARM_POT_A_MAX_VALUE = 0;
    public static final double ARM_POT_A_ZERO = 0.0;
    public static final double ARM_FEED_POS_A = 0.0;
    public static final double ARM_BACK_PYRAMID_POS = 233;
    public static final double ARM_FRONT_PYRAMID_POS = 237;
    public static final double ARM_SIDE_PYRAMID_POS = 237;
    public static final double ARM_POT_45_A = 0;
    public static final double POT_A_TO_DEGREES = 45.0 / ARM_POT_45_A;
    public static final double ARM_KP_A = 0.02;
    public static final double ARM_KI_A = 0.000021;
    public static final double ARM_KD_A = 0.0;
    
    // Pot B Values
    public static final int ARM_POT_B_PORT = 0;
    public static final double ARM_POT_B_MIN_VALUE = 0;
    public static final double ARM_POT_B_MAX_VALUE = 0;
    public static final double ARM_POT_B_ZERO = 0.0;
    public static final double ARM_POT_B_TO_DEGREES = 0.0;
    public static final double ARM_PYRAMID_POS_B = 0.0;
    public static final double ARM_FEED_POS_B = 0.0;
    public static final double ARM_KP_B = 0.0;
    public static final double ARM_KI_B = 0.0;
    public static final double ARM_KD_B = 0.0;

    // Camera PID
    public static final double ARM_KP_CAM = 0.0;
    public static final double ARM_KI_CAM = 0.0;
    public static final double ARM_KD_CAM = 0.0;
    
    /*
     * Autonomous
     */
    public static final double AUTON_DRIVE_POWER = 0.5;
    public static final double AUTON_INGEST_DRIVE_POWER = 0.5;
    public static final double AUTON_TURN_POWER = 0.5;
    public static final double AUTON_ARM_LOW_POS = 0;
    public static final double AUTON_ARM_UNDER_PYRAMID_POS= 0;
    
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

    //servo
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
    public static final double LIFTER_TOP_PROX_SENSOR_POWER_POS = 0;
    public static final double LIFTER_TOP_PROX_SENSOR_POS = 0;
        
    /*
     * Shooter
     */ 
    public static final int SHOOTER_JAG1_POS = 1;
    public static final int SHOOTER_JAG2_POS = 3;
    public static final int SHOOTER_JAG3_POS = 2;

    public static final double SHOOTER_MAX_POWER = 1.0;
    public static final double SHOOTER_FEED_POWER = 0.22;
    public static final double TIME_TO_SHOOT_ONE = 5.0;
    public static final double TIME_TO_SHOOT_TWO = 0.0;
    public static final double TIME_TO_SHOOT_THREE = 0.0;
    public static final double TIME_TO_SHOOT_FOUR = 0.0;
    
    /*
     * XboxController
     */
    //Driver
    public static final double XBOX_DRIVER_DRIFT = 0.2;
    public static final int XBOX_DRIVER_PORT = 1;
    
    //Manipulator
    public static final int XBOX_MANIP_PORT = 2;
    public static final double XBOX_MANIP_DRIFT = 0.3;
}
