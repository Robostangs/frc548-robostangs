/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robostangs;

import edu.wpi.first.wpilibj.Servo;

/**
 *
 * @author wmd
 */
public class Climber {
	private static Servo leftServo;
	private static Servo rightServo;
	private static Climber instance = null;
	private Climber(){
		leftServo = new Servo(Constants.CLIMBER_LEFT_SERVO_POS);
		rightServo = new Servo(Constants.CLIMBER_RIGHT_SERVO_POS);
	}
	public static Climber getInstance() {
		if(instance == null) {
			instance = new Climber();
		}
		return instance;
	}
	public static void deploy() {
		leftServo.set(Constants.CLIMBER_LEFT_OUT_POS);
		rightServo.set(Constants.CLIMBER_RIGHT_OUT_POS);
	}
	public static void retract() {
		leftServo.set(Constants.CLIMBER_LEFT_IN_POS);
		rightServo.set(Constants.CLIMBER_RIGHT_IN_POS);
	}
}
