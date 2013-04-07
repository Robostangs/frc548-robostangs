package com.robostangs;

/**
 *
 * @author sky
 * TODO: everything pretty much.
 */
public class Constants {
    private static Constants instance = null;

    private Constants() {
        ReadConstants.init();
    }

    public static void init() {
        if (instance == null) {
            instance = new Constants();
        }
    }
    /*
     * Used in multiple classes
     */
    public static final double JAG_CONFIG_TIME = ReadConstants.getJagConfigTime();
    public static final double BENNETT_CONSTANT = ReadConstants.getBennettConstant();
    public static final double TELEOP_DRIVE_TIME = ReadConstants.getTeleopDriveTime();

    /*
     * Arm
     */
    public static final int ARM_JAG_POS = ReadConstants.getArmJagPos();
    public static final int ARM_POT_PORT = ReadConstants.getArmPotPos();
    public static final int ARM_POT_MIN_VALUE = ReadConstants.getArmPotMinValue();
    public static final int ARM_POT_MAX_VALUE = ReadConstants.getArmPotMaxValue();
    public static final int ARM_POT_SLOW_VALUE = ReadConstants.getArmPotSlowValue();
    public static final double ARM_MIN_VOLTAGE = ReadConstants.getArmMinVoltage();
    // Arm PID
    public static final double ARM_KP_SMALL = ReadConstants.getArmKPSmall();
    public static final double ARM_KI_SMALL = ReadConstants.getArmKISmall();
    public static final double ARM_KD_SMALL = ReadConstants.getArmKDSmall();
    public static final double ARM_KP_MED = ReadConstants.getArmKPMed();
    public static final double ARM_KI_MED = ReadConstants.getArmKIMed();
    public static final double ARM_KD_MED = ReadConstants.getArmKDMed();
    public static final double ARM_KP_LARGE = ReadConstants.getArmKPLarge();
    public static final double ARM_KI_LARGE = ReadConstants.getArmKILarge();
    public static final double ARM_KD_LARGE = ReadConstants.getArmKDLarge();
    public static final double ARM_KP_CAM = ReadConstants.getArmKPCam();
    public static final double ARM_KI_CAM = ReadConstants.getArmKICam();
    public static final double ARM_KD_CAM = ReadConstants.getArmKDCam();
    public static final double ARM_MIN_POWER = ReadConstants.getArmMinPower();
    public static final double ARM_MAX_POWER = ReadConstants.getArmMaxPower();
    public static final double ARM_PID_POT_MIN = ReadConstants.getArmPIDPotMin();
    public static final double ARM_PID_POT_MAX = ReadConstants.getArmPIDPotMax();

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
    public static final double ARM_SHOOTING_POS = ReadConstants.getArmShootingPos();
    public static final double ARM_START_POS = ReadConstants.getArmStartPos();

    /*
     * Autonomous
     */
    public static final double AUTON_DRIVE_POWER = ReadConstants.getAutonDrivePower();
    public static final double AUTON_TURN_POWER = ReadConstants.getAutonTurnPower();
    public static final double AUTON_ARM_POS = ARM_SHOOTING_POS;
    
    /*
     * Climber
     */
    public static final int CLIMBER_LEFT_SERVO_POS = ReadConstants.getClimberLeftServoPos();
    public static final int CLIMBER_RIGHT_SERVO_POS = ReadConstants.getClimberRightServoPos();
    public static final double CLIMBER_LEFT_OUT_POS = ReadConstants.getClimberLeftOutPos();
    public static final double CLIMBER_RIGHT_OUT_POS = ReadConstants.getClimberRightOutPos();
    public static final double CLIMBER_LEFT_IN_POS = ReadConstants.getClimberLeftInPos();
    public static final double CLIMBER_RIGHT_IN_POS = ReadConstants.getClimberRightInPos();
    
    /*
     * Conveyors
     */
    public static final int CONV_SHOOT_JAG_POS = ReadConstants.getConvShootJagPos();
    public static final int CONV_INGEST_JAG_POS = ReadConstants.getConvIngestJagPos();
    public static final double CONV_SHOOTER_POWER = ReadConstants.getConvShooterPower();
    public static final double CONV_INGEST_POWER = ReadConstants.getConvIngestPower();
    
    /*
     * Camera
     */
    public static final int CAM_Y_OFFSET = ReadConstants.getCamYOffset();
    public static final int CAM_X_OFFSET = ReadConstants.getCamXOffset();

    /*
     * DriveTrain
     */
    //Positions
    public static final int DT_LEFT_ENCODER_FRONT = ReadConstants.getDTLeftEncoderFront();
    public static final int DT_LEFT_ENCODER_BACK = ReadConstants.getDTLeftEncoderBack();
    public static final int DT_RIGHT_ENCODER_FRONT = ReadConstants.getDTRightEncoderFront();
    public static final int DT_RIGHT_ENCODER_BACK = ReadConstants.getDTRightEncoderBack();
    public static final int DT_GYRO_POS = ReadConstants.getDTGyroPos();
    public static final int DT_JAG_LEFT_FRONT_POS = ReadConstants.getDTJagLeftFrontPos();
    public static final int DT_JAG_LEFT_MID_POS = ReadConstants.getDTJagLeftMidPos();
    public static final int DT_JAG_LEFT_BACK_POS = ReadConstants.getDTJagLeftBackPos();
    public static final int DT_JAG_RIGHT_FRONT_POS = ReadConstants.getDTJagRightFrontPos();
    public static final int DT_JAG_RIGHT_MID_POS = ReadConstants.getDTJagRightMidPos();
    public static final int DT_JAG_RIGHT_BACK_POS = ReadConstants.getDTJagRightBackPos();
    
    /*
     * Ingestor
     */
    public static final int INGEST_RELAY_POS = 0;
    
    /*
     * Lifter
     */
    public static final int LIFTER_JAG_POS = ReadConstants.getLifterJagPos();
    public static final double LIFTER_UP_POWER = ReadConstants.getLifterUpPower();
    public static final double LIFTER_DOWN_POWER = ReadConstants.getLifterDownPower();
    public static final double LIFTER_UP_TIME = ReadConstants.getLifterUpTime();
    public static final double LIFTER_DOWN_TIME = ReadConstants.getLifterDownTime();
    public static final int LIFTER_TOP_PROX_DIGITAL_PORT = ReadConstants.getLifterTopProxDigitalPort();
    public static final int LIFTER_TOP_PROX_SOLENOID_PORT = ReadConstants.getLifterTopProxSolenoidPort();
    public static final int LIFTER_BOTTOM_PROX_DIGITAL_PORT = ReadConstants.getLifterBottomProxDigitalPort();
    public static final int LIFTER_BOTTOM_PROX_SOLENOID_PORT = ReadConstants.getLifterBottomProxSolenoidPort();
        
    /*
     * Shooter
     */ 
    public static final int SHOOTER_JAG1_POS = ReadConstants.getShooterJagOnePos();
    public static final int SHOOTER_JAG2_POS = ReadConstants.getShooterJagTwoPos();
    public static final int SHOOTER_JAG3_POS = ReadConstants.getShooterJagThreePos();

    public static final double SHOOTER_MAX_POWER = ReadConstants.getShooterMaxPower();
    public static final double SHOOTER_FEED_POWER = ReadConstants.getShooterFeedPower();
    public static final double SHOOTER_FULL_BATTERY_VOLTAGE = ReadConstants.getShooterFullBatteryVoltage();
    public static final double SHOOTER_VOLTAGE_TOLERANCE = ReadConstants.getShooterVoltageTolerance();
    public static final double SHOOTER_READY_CURRENT_MAX = ReadConstants.getShooterReadyCurrentMax();
    public static final double SHOOTER_READY_CURRENT_MIN = ReadConstants.getShooterReadyCurrentMin();
    //GOOD SHOOTING ANGLE: 247
    
    /*
     * XboxController
     */
    //Driver
    public static final double XBOX_DRIVER_DRIFT = ReadConstants.getXboxDriverDrift();
    public static final int XBOX_DRIVER_PORT = ReadConstants.getXboxDriverPort();
    
    //Manipulator
    public static final int XBOX_MANIP_PORT = ReadConstants.getXboxManipPort();
    public static final double XBOX_MANIP_DRIFT = ReadConstants.getXboxManipDrift();
}
