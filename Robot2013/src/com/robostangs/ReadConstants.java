package com.robostangs;

import com.sun.squawk.io.BufferedReader;
import com.sun.squawk.microedition.io.FileConnection;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import javax.microedition.io.Connector;

/**
 *
 * @author sky
 */
public class ReadConstants {
    private static ReadConstants instance = null;
    private static String inputFileName = "constants.txt";
    private static Vector contents;
    private static String[] keys;
    private static double[] constants;

    private ReadConstants() {
        contents = new Vector();
        String line = "";
        try {
            FileConnection fc = (FileConnection) Connector.open("file:///" + inputFileName, Connector.READ); 
            BufferedReader in = new BufferedReader(new InputStreamReader(fc.openInputStream()));

            while ((line = in.readLine()) != null) {
                if (line.indexOf("=") != -1) { 
                    contents.addElement(line);
                }
            }

            fc.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        contents.trimToSize();
        keys = new String[contents.size()];
        constants = new double[contents.size()];

        processText();
    }

    public static void init() {
        if (instance == null) {
            instance = new ReadConstants();
        }
    }
    
    public static void processText() {
        int equalPos = 0;
        String line = "";
        for (int i = 0; i < contents.size(); i++) {
            line = (String) contents.elementAt(i);
            equalPos = line.indexOf("=");
            keys[i] = line.substring(0, equalPos);
            constants[i] = Double.parseDouble(line.substring(equalPos + 1, line.length()));
            System.out.println("key " + i + " " + keys[i]);
            System.out.println("constant " + i + " " + constants[i]);
        }
    }

    public static int findKey(String key) {
        try {
            for (int i = 0; i < keys.length; i++) {
                if (keys[i].equalsIgnoreCase(key)) {
                    return i;
                }
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            return -1;
        }

        return -1;
    }

    public static double findDouble(String key) {
        try {
            for (int i = 0; i < keys.length; i++) {
                if (keys[i].equalsIgnoreCase(key)) {
                    return constants[i];
                }
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            return -1;
        }

        return -1;
    }

    public static int findInt(String key) {
        try {
            for (int i = 0; i < keys.length; i++) {
                if (keys[i].equalsIgnoreCase(key)) {
                    return (int) constants[i];
                }
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            return -1;
        }

        return -1;
    }
    
    public static double getJagConfigTime() {
        double x = findDouble("JAG_CONFIG_TIME");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.JAG_CONFIG_TIME;
        }
    }

    public static double getBennettConstant() {
        double x = findDouble("BENNETT_CONSTANT");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.BENNETT_CONSTANT;
        }
    }

    public static double getTeleopDriveTime() {
        double x = findDouble("TELEOP_DRIVE_TIME");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.TELEOP_DRIVE_TIME;
        }
    }

    public static int getArmJagPos() {
        int x = findInt("ARM_JAG_POS");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.ARM_JAG_POS;
        }
    }

    public static int getArmPotPos() {
        int x = findInt("ARM_POT_PORT");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.ARM_POT_PORT;
        }
    }

    public static int getArmPotMinValue() {
        int x = findInt("ARM_POT_MIN_VALUE");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.ARM_POT_MIN_VALUE;
        }
    }

    public static int getArmPotMaxValue() {
        int x = findInt("ARM_POT_MAX_VALUE");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.ARM_POT_MAX_VALUE;
        }
    }

    public static int getArmPotSlowValue() {
        int x = findInt("ARM_POT_SLOW_VALUE");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.ARM_POT_SLOW_VALUE;
        }
    }

    public static double getArmMinVoltage() {
        double x = findDouble("ARM_MIN_VOLTAGE");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.ARM_MIN_VOLTAGE;
        }
    }

    public static double getArmKPSmall() {
        double x = findDouble("ARM_KP_SMALL");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.ARM_KP_SMALL;
        }
    }

    public static double getArmKISmall() {
        double x = findDouble("ARM_KI_SMALL");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.ARM_KI_SMALL;
        }
    }

    public static double getArmKDSmall() {
        double x = findDouble("ARM_KD_SMALL");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.ARM_KD_SMALL;
        }
    }

    public static double getArmKPMed() {
        double x = findDouble("ARM_KP_MED");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.ARM_KP_MED;
        }
    }

    public static double getArmKIMed() {
        double x = findDouble("ARM_KI_MED");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.ARM_KI_MED;
        }
    }

    public static double getArmKDMed() {
        double x = findDouble("ARM_KD_MED");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.ARM_KD_MED;
        }
    }

    public static double getArmKPLarge() {
        double x = findDouble("ARM_KP_LARGE");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.ARM_KP_LARGE;
        }
    }

    public static double getArmKILarge() {
        double x = findDouble("ARM_KI_LARGE");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.ARM_KI_LARGE;
        }
    }

    public static double getArmKDLarge() {
        double x = findDouble("ARM_KD_LARGE");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.ARM_KD_LARGE;
        }
    }

    public static double getArmKPCam() {
        double x = findDouble("ARM_KP_CAM");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.ARM_KP_CAM;
        }
    }

    public static double getArmKICam() {
        double x = findDouble("ARM_KI_CAM");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.ARM_KI_CAM;
        }
    }

    public static double getArmKDCam() {
        double x = findDouble("ARM_KD_CAM");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.ARM_KD_CAM;
        }
    }

    public static double getArmMinPower() {
        double x = findDouble("ARM_MIN_POWER");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.ARM_MIN_POWER;
        }
    }

    public static double getArmMaxPower() {
        double x = findDouble("ARM_MAX_POWER");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.ARM_MAX_POWER;
        }
    }

    public static double getArmPIDPotMin() {
        double x = findDouble("ARM_PID_POT_MIN");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.ARM_PID_POT_MIN;
        }
    }

    public static double getArmPIDPotMax() {
        double x = findDouble("ARM_PID_POT_MAX");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.ARM_PID_POT_MAX;
        }
    }
    
    public static double getArmShootingPos() {
        double x = findDouble("ARM_SHOOTING_POS");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.ARM_SHOOTING_POS;
        }
    }

    public static double getArmStartPos() {
        double x = findDouble("ARM_START_POS");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.ARM_START_POS;
        }
    }

    public static double getAutonDrivePower() {
        double x = findDouble("AUTON_DRIVE_POWER");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.AUTON_DRIVE_POWER;
        }
    }

    public static double getAutonTurnPower() {
        double x = findDouble("AUTON_TURN_POWER");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.AUTON_TURN_POWER;
        }
    }

    public static int getClimberLeftServoPos() {
        int x = findInt("CLIMBER_LEFT_SERVO_POS");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.CLIMBER_LEFT_SERVO_POS;
        }
    }
    
    public static int getClimberRightServoPos() {
        int x = findInt("CLIMBER_RIGHT_SERVO_POS");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.CLIMBER_RIGHT_SERVO_POS;
        }
    }
    
    public static double getClimberLeftOutPos() {
        int x = findInt("CLIMBER_LEFT_OUT_POS");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.CLIMBER_LEFT_OUT_POS;
        }
    }
    
    public static double getClimberRightOutPos() {
        int x = findInt("CLIMBER_RIGHT_OUT_POS");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.CLIMBER_RIGHT_OUT_POS;
        }
    }

    public static double getClimberLeftInPos() {
        int x = findInt("CLIMBER_LEFT_IN_POS");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.CLIMBER_LEFT_IN_POS;
        }
    }
    
    public static double getClimberRightInPos() {
        int x = findInt("CLIMBER_RIGHT_IN_POS");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.CLIMBER_RIGHT_IN_POS;
        }
    }
    
    public static int getConvShootJagPos() {
        int x = findInt("CONV_SHOOT_JAG_POS");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.CONV_SHOOT_JAG_POS;
        }
    }

    public static int getConvIngestJagPos() {
        int x = findInt("CONV_INGEST_JAG_POS");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.CONV_INGEST_JAG_POS;
        }
    }

    public static double getConvShooterPower() {
        double x = findDouble("CONV_SHOOTER_POWER");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.CONV_SHOOTER_POWER;
        }
    }

    public static double getConvIngestPower() {
        double x = findDouble("CONV_INGEST_POWER");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.CONV_INGEST_POWER;
        }
    }

    public static int getCamYOffset() {
        int x = findInt("CAM_Y_OFFSET");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.CAM_Y_OFFSET;
        }
    }

    public static int getCamXOffset() {
        int x = findInt("CAM_X_OFFSET");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.CAM_X_OFFSET;
        }
    }

    public static int getDTLeftEncoderFront() {
        int x = findInt("DT_LEFT_ENCODER_FRONT");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.DT_LEFT_ENCODER_FRONT;
        }
    }

    public static int getDTLeftEncoderBack() {
        int x = findInt("DT_LEFT_ENCODER_BACK");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.DT_LEFT_ENCODER_BACK;
        }
    }

    public static int getDTRightEncoderFront() {
        int x = findInt("DT_RIGHT_ENCODER_FRONT");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.DT_RIGHT_ENCODER_FRONT;
        }
    }

    public static int getDTRightEncoderBack() {
        int x = findInt("DT_RIGHT_ENCODER_BACK");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.DT_RIGHT_ENCODER_BACK;
        }
    }

    public static int getDTGyroPos() {
        int x = findInt("DT_GYRO_POS");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.DT_GYRO_POS;
        }
    }

    public static int getDTJagLeftFrontPos() {
        int x = findInt("DT_JAG_LEFT_FRONT_POS");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.DT_JAG_LEFT_FRONT_POS;
        }
    }

    public static int getDTJagLeftMidPos() {
        int x = findInt("DT_JAG_LEFT_MID_POS");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.DT_JAG_LEFT_MID_POS;
        }
    }

    public static int getDTJagLeftBackPos() {
        int x = findInt("DT_JAG_LEFT_BACK_POS");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.DT_JAG_LEFT_BACK_POS;
        }
    }

    public static int getDTJagRightFrontPos() {
        int x = findInt("DT_JAG_RIGHT_FRONT_POS");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.DT_JAG_RIGHT_FRONT_POS;
        }
    }

    public static int getDTJagRightMidPos() {
        int x = findInt("DT_JAG_RIGHT_MID_POS");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.DT_JAG_RIGHT_MID_POS;
        }
    }

    public static int getDTJagRightBackPos() {
        int x = findInt("DT_JAG_RIGHT_BACK_POS");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.DT_JAG_RIGHT_BACK_POS;
        }
    }

    public static int getDTJagClimbPos() {
        int x = findInt("DT_JAG_CLIMB_POS");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.DT_JAG_CLIMB_POS;
        }
    }

    public static int getLifterJagPos() {
        int x = findInt("LIFTER_JAG_POS");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.LIFTER_JAG_POS;
        }
    }

    public static double getLifterDownPower() {
        double x = findDouble("LIFTER_DOWN_POWER");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.LIFTER_DOWN_POWER;
        }
    }

    public static double getLifterUpPower() {
        double x = findDouble("LIFTER_UP_POWER");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.LIFTER_UP_POWER;
        }
    }

    public static double getLifterUpTime() {
        double x = findDouble("LIFTER_UP_TIME");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.LIFTER_UP_TIME;
        }
    }

    public static double getLifterDownTime() {
        double x = findDouble("LIFTER_DOWN_TIME");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.LIFTER_DOWN_TIME;
        }
    }

    public static int getLifterTopProxDigitalPort() {
        int x = findInt("LIFTER_TOP_PROX_DIGITAL_PORT");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.LIFTER_TOP_PROX_DIGITAL_PORT;
        }
    }

    public static int getLifterTopProxSolenoidPort() {
        int x = findInt("LIFTER_TOP_PROX_SOLENOID_PORT");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.LIFTER_TOP_PROX_SOLENOID_PORT;
        }
    }

    public static int getLifterBottomProxDigitalPort() {
        int x = findInt("LIFTER_BOTTOM_PROX_DIGITAL_PORT");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.LIFTER_BOTTOM_PROX_DIGITAL_PORT;
        }
    }

    public static int getLifterBottomProxSolenoidPort() {
        int x = findInt("LIFTER_BOTTOM_PROX_SOLENOID_PORT");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.LIFTER_BOTTOM_PROX_SOLENOID_PORT;
        }
    }

    public static int getShooterJagOnePos() {
        int x = findInt("SHOOTER_JAG1_POS");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.SHOOTER_JAG1_POS;
        }
    }

    public static int getShooterJagTwoPos() {
        int x = findInt("SHOOTER_JAG2_POS");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.SHOOTER_JAG2_POS;
        }
    }

    public static int getShooterJagThreePos() {
        int x = findInt("SHOOTER_JAG3_POS");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.SHOOTER_JAG3_POS;
        }
    }

    public static double getShooterMaxPower() {
        double x = findDouble("SHOOTER_MAX_POWER");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.SHOOTER_MAX_POWER;
        }
    }

    public static double getShooterFeedPower() {
        double x = findDouble("SHOOTER_FEED_POWER");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.SHOOTER_FEED_POWER;
        }
    }

    public static double getShooterFullBatteryVoltage() {
        double x = findDouble("SHOOTER_FULL_BATTERY_VOLTAGE");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.SHOOTER_FULL_BATTERY_VOLTAGE;
        }
    }

    public static double getShooterVoltageTolerance() {
        double x = findDouble("SHOOTER_VOLTAGE_TOLERANCE");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.SHOOTER_VOLTAGE_TOLERANCE;
        }
    }

    public static double getShooterReadyCurrentMax() {
        double x = findDouble("SHOOTER_READY_CURRENT_MAX");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.SHOOTER_READY_CURRENT_MAX;
        }
    }

    public static double getShooterReadyCurrentMin() {
        double x = findDouble("SHOOTER_READY_CURRENT_MIN");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.SHOOTER_READY_CURRENT_MIN;
        }
    }

    public static double getXboxDriverDrift() {
        double x = findDouble("XBOX_DRIVER_DRIFT");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.XBOX_DRIVER_DRIFT;
        }
    }

    public static double getXboxManipDrift() {
        double x = findDouble("XBOX_MANIP_DRIFT");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.XBOX_MANIP_DRIFT;
        }
    }

    public static int getXboxDriverPort() {
        int x = findInt("XBOX_DRIVER_PORT");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.XBOX_DRIVER_PORT;
        }
    }

    public static int getXboxManipPort() {
        int x = findInt("XBOX_MANIP_PORT");
        if (x != -1) {
            return x;
        } else {
            return DefaultConstants.XBOX_MANIP_PORT;
        }
    }
}
