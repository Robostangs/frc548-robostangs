/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robostangs;

/**
 *
 * @author sky
 */
public class Constants {
	//TODO: get actual values when robot exists
	
	/*
	 * Drivetrain
	 */
	public static final int DRIVE_LEFT_ENCODER_1 = 1;
        public static final int DRIVE_LEFT_ENCODER_2 = 2;
        public static final int DRIVE_RIGHT_ENCODER_1 = 3;
        public static final int DRIVE_RIGHT_ENCODER_2 = 4;
	public static final int DRIVE_FRONT_ENCODER_1 = 1;
        public static final int DRIVE_FRONT_ENCODER_2 = 2;
        public static final int DRIVE_BACK_ENCODER_1 = 3;
        public static final int DRIVE_BACK_ENCODER_2 = 4;    
        public static final double LEFT_DISTANCE_PER_TICK = -.0018419499;//-0.001829;
        public static final double RIGHT_DISTANCE_PER_TICK = .0018459978;//-0.001289;
	public static final double FRONT_DISTANCE_PER_TICK = -.0018419499;//-0.001829;
        public static final double BACK_DISTANCE_PER_TICK = .0018459978;//-0.001289;
        public static final int DRIVE_JAG_1_POS = 7;        //left drive train motor
        public static final int DRIVE_JAG_2_POS = 8;        //left drive train motor
        public static final int DRIVE_JAG_3_POS = 5;        //right drive train motor
        public static final int DRIVE_JAG_4_POS = 6;        //right drive train motor
	public static final int DRIVE_GYRO_POS = 0;
	
	/*
	 * Shooter
	 */
	public static final int SHOOTER_SHELL_SOLENOID_POS = 0;
	public static final int SHOOTER_BARREL_SOLENOID_POS = 0;
	public static final int SHOOTER_SHOOT_SOLENOID_POS = 0;
	public static final int SHOOTER_LIMIT_SWITCH_POS = 0;
	public static final int SHOOTER_COMPRESSOR_1 = 0;
	public static final int SHOOTER_PRESSURE_SENSOR = 0;
	
	/*
	 * Xbox Controller
	 */
	public static final int XBOX_PORT = 1;
	
	/*
	 * Arm
	 */
       public static final int ARM_JAG_1_POS = 0;
       public static final int ARM_JAG_2_POS = 0;
       public static final int ARM_POT_POS = 0;
       public static final double ARM_PID_P = 0;
       public static final double ARM_PID_I = 0;
       public static final double ARM_PID_D = 0;
       public static final int ARM_POT_MAX = 0;
       public static final int ARM_POT_MIN = 0;
       public static final int ARM_LOW = 0;
       public static final int ARM_90 = 0;
       public static final int ARM_MID = 0;
       public static final int ARM_HIGH = 0;
}
