/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robostangs;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 *
 * @author sky
 */
public class DriveTrain {
	private CANJaguar jag1, jag2, jag3, jag4;
	private Encoder leftEncoder, rightEncoder, frontEncoder, backEncoder;
	private Gyro gyro;
	private double initGyro;
	private double xDist = 0;
	private double yDist = 0;
	
	public DriveTrain() {
		try {
			jag1 = new CANJaguar(Constants.DRIVE_JAG_1_POS);
			jag2 = new CANJaguar(Constants.DRIVE_JAG_2_POS);
			jag3 = new CANJaguar(Constants.DRIVE_JAG_3_POS);
			jag4 = new CANJaguar(Constants.DRIVE_JAG_4_POS);
			jag1.configFaultTime(.5);
			jag2.configFaultTime(.5);
			jag3.configFaultTime(.5);
			jag4.configFaultTime(.5);
		} catch (CANTimeoutException ex) {
			ex.printStackTrace();
		}
		leftEncoder = new Encoder(Constants.DRIVE_LEFT_ENCODER_1, Constants.DRIVE_LEFT_ENCODER_2);
		rightEncoder = new Encoder(Constants.DRIVE_RIGHT_ENCODER_1, Constants.DRIVE_RIGHT_ENCODER_2);
		frontEncoder = new Encoder(Constants.DRIVE_FRONT_ENCODER_1, Constants.DRIVE_FRONT_ENCODER_2);
		backEncoder = new Encoder(Constants.DRIVE_BACK_ENCODER_1, Constants.DRIVE_BACK_ENCODER_2);
		leftEncoder.setDistancePerPulse(Constants.LEFT_DISTANCE_PER_TICK);
		rightEncoder.setDistancePerPulse(Constants.RIGHT_DISTANCE_PER_TICK);
		frontEncoder.setDistancePerPulse(Constants.FRONT_DISTANCE_PER_TICK);
		backEncoder.setDistancePerPulse(Constants.BACK_DISTANCE_PER_TICK);
		gyro = new Gyro(Constants.DRIVE_GYRO_POS);
		initGyro = gyro.getAngle();
	}
	public double getLeftEncoder() {
		return leftEncoder.getDistance();
	}
	public double getRightEncoder() {
		return rightEncoder.getDistance();
	}
	public double getFrontEncoder() {
		return frontEncoder.getDistance();
	}
	public double getBackEncoder() {
		return backEncoder.getDistance();
	}
	public double getGyro(){
		return gyro.getAngle();
	}
	/**
	 * Drives the robot relative to the field.
	 * @param leftX left joystick X axis
	 * @param leftY left joystick Y axis
	 * @param angle left joystick angle
	 * @param rightY right joystick Y axis
	 */
	public void driveF(double leftX, double leftY, double angle, double rightY){
		try {
			double deltaTheta = getGyro() - initGyro;
			double wheel1;
			double wheel2;
			double wheel3;
			double wheel4;
			if(Math.abs(deltaTheta) <= 5){
				driveR(leftX, leftY, angle, rightY);
				return;
			}
			angle-=deltaTheta;
			if(Math.abs(rightY) < 0.1){
				wheel1 = leftY - leftX + angle * (xDist + yDist);
				wheel2 = leftY + leftX - angle * (xDist + yDist);
				wheel3 = leftY - leftX - angle * (xDist + yDist);
				wheel4 = leftY + leftX + angle * (xDist + yDist);
				jag1.setX(wheel1);
				jag2.setX(wheel2);
				jag3.setX(wheel3);
				jag4.setX(wheel4);
			}else{
				wheel1 = angle * (xDist + yDist);
				wheel2 = angle * (xDist + yDist);
				wheel3 = angle * (xDist + yDist);
				wheel4 = angle * (xDist + yDist);
				jag1.setX(wheel1);
				jag2.setX(wheel2);
				jag3.setX(wheel3);
				jag4.setX(wheel4);
			}
		} catch (CANTimeoutException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Drives the robot relative to the robot.
	 * @param leftX left joystick X axis
	 * @param leftY left joystick Y axis
	 * @param angle left joystick angle
	 * @param rightY right joystick Y axis
	 */
	public void driveR(double leftX, double leftY, double angle, double rightY){
		double wheel1;
		double wheel2;
		double wheel3;
		double wheel4;
		if(Math.abs(rightY) < 0.1){
			try {
				wheel1 = leftY - leftX + angle * (xDist + yDist);
				wheel2 = leftY + leftX - angle * (xDist + yDist);
				wheel3 = leftY - leftX - angle * (xDist + yDist);
				wheel4 = leftY + leftX + angle * (xDist + yDist);
				jag1.setX(wheel1);
				jag2.setX(wheel2);
				jag3.setX(wheel3);
				jag4.setX(wheel4);
			} catch (CANTimeoutException ex) {
				ex.printStackTrace();
			}
		}else{
			try {
				wheel1 = angle * (xDist + yDist);
				wheel2 = angle * (xDist + yDist);
				wheel3 = angle * (xDist + yDist);
				wheel4 = angle * (xDist + yDist);
				jag1.setX(wheel1);
				jag2.setX(wheel2);
				jag3.setX(wheel3);
				jag4.setX(wheel4);
			} catch (CANTimeoutException ex) {
				ex.printStackTrace();
			}
		}
	}
}
