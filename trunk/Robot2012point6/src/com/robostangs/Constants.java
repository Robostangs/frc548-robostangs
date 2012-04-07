package com.robostangs;

public class Constants {
    
    /*
     * Enable Constants
     */
        public static final boolean USE_CAMERA = true;  //Enable camera
        public static final boolean USE_CAN = true;     //Enable CANJaguars
    /*
     * Camera
     */
        public static final double CAM_FOV_ANGLE = 15.91;
        
    /*
     * Arm
     */
        public static final float ARM_POWER_COEFFICIENT = 1.0f;
        //TODO: Check bottom and comment positions with angle
        public static final int ARM_MAX_ANGLE = 59;
        public static final int ARM_MIN_ANGLE = -57;
        
        /*public static final int ARM_LOW = 574;
        public static final int ARM_TOP = 786;
        public static final int ARM_MIDDLE = 745;
        public static final int ARM_BOTTOM = 352;*/
        public static final int ARM_LOW = 372;
        public static final int ARM_TOP = 582;
        public static final int ARM_MIDDLE = 562;
        public static final int ARM_BOTTOM = 190;
        public static final int ARM_POT_MAX = ARM_TOP + 2;
        public static final int ARM_POT_MIN = ARM_BOTTOM -2;
        
        //366, +125 = 269
        //566, -425 = 1231
        //652, -375, 2169
        //Bridge, 3572
        
        //PID constants, competetion
        public static final double ARM_BOTTOM_AKp = 0.023;
        public static final double ARM_BOTTOM_AKi = 0.000015;
        public static final double ARM_BOTTOM_AKd = 0.015;
        public static final double ARM_TOP_AKp = 0.01;
        public static final double ARM_TOP_AKi = 0.00001;
        public static final double ARM_TOP_AKd = 0.028;
        public static final double ARM_MIDDLE_AKp = 0.008;
        public static final double ARM_MIDDLE_AKi = 0.00001;
        public static final double ARM_MIDDLE_AKd = 0.025;
        public static final double ARM_LOW_AKp = 0.0165;
        public static final double ARM_LOW_AKi = 0.00001;
        public static final double ARM_LOW_AKd = 0.025;

        public static final int ARM_POT_POS = 2;
        public static final int ARM_JAG_A_POS = 3;
        public static final int ARM_JAG_B_POS = 9;

    /*
     * Shooter
     */
        public static final int SHOOTER_JAG_TOP_POS = 2;
        public static final int SHOOTER_JAG_BOTTOM_POS = 4;
        public static final int SHOOTER_TOP_ENC_A = 5;
        public static final int SHOOTER_TOP_ENC_B = 6;
        public static final int SHOOTER_BOTTOM_ENC_A = 7;
        public static final int SHOOTER_BOTTOM_ENC_B = 8;
        public static final int SHOOTER_CONVEYOR_VIC = 1;
        public static final int SHOOTER_INGESTOR_REL = 1;
        //PID constants
        public static final double SKp = .2;
        public static final double SKi = 1;
        public static final double SKd = 0;
        //TODO: Get values
        public static final double SHOOTER_FRONT_FENDER_RPM = 1162.49046;
        public static final double SHOOTER_SIDE_FENDER_RPM = 240;
        public static final double SHOOTER_FRONT_KEY_RPM = 1362.1674;
        public static final double SHOOTER_BACK_KEY_RPM = 1735.8208;
        //TODO: Check
        public static final int INGESTOR_ARM_MAX_ANGLE = -45;
    
    /*
     * Sensors
     */
        public static final int GYRO_POS = 1;
        public static final int ACCELERATIONX_CONSTANT = 0;
        public static final int ACCELERATIONY_CONSTANT = 0;

    /*
     * Drive train
     */
        public static final int DRIVE_LEFT_ENCODER_1 = 1;
        public static final int DRIVE_LEFT_ENCODER_2 = 2;
        public static final int DRIVE_RIGHT_ENCODER_1 = 3;
        public static final int DRIVE_RIGHT_ENCODER_2 = 4;        
        public static final double LEFT_DISTANCE_PER_TICK = -.0018419499;//-0.001829;
        public static final double RIGHT_DISTANCE_PER_TICK = .0018459978;//-0.001289;
        public static final int DRIVE_JAG_1_POS = 7;        //left drive train motor
        public static final int DRIVE_JAG_2_POS = 8;        //left drive train motor
        public static final int DRIVE_JAG_3_POS = 5;        //right drive train motor
        public static final int DRIVE_JAG_4_POS = 6;        //right drive train motor
        //PID constants
        public static final double DKp = .02;
        public static final double DKi = .009;
        public static final double DKd = .02;
    
    /*
     * Joysticks
     */
        public static final int XBOXDRIVER_PORT = 1;
        public static final int XBOXMANIP_PORT = 2;
        public static final int JOYSTICK_YAXIS_1 = 2;
        public static final int JOYSTICK_YAXIS_2 = 5;
        
    /*
     * Pneumatics
     */
        public static final int RAMPCYLINDER_SOLENOID1 = 0;
        public static final int RAMPCYLINDER_SOLENOID2 =0;
        public static final int COMPRESSOR_RELAY1 = 2;
        public static final int DIGITAL_INPUT1 = 14;
        public static final int GEARCYLINDER_SOLENOID1 = 2;
        public static final int INGESTCYLINDER_SOLENOID1 = 3;
        public static final boolean LOW_SPEED = false;           //Position of shifter for high gear
}
