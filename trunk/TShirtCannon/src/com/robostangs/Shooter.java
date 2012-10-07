/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robostangs;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author sky
 */
public class Shooter {
	//TODO: fix tabs
	private Solenoid shell, barrel, shoot;
	private Relay compressor;
	private DigitalInput loader, pressureSensor;
	private StopWatch timer;
	
	
	public Shooter(){
		shell = new Solenoid(Constants.SHOOTER_SHELL_SOLENOID_POS);
		barrel = new Solenoid(Constants.SHOOTER_BARREL_SOLENOID_POS);
		shoot = new Solenoid(Constants.SHOOTER_SHOOT_SOLENOID_POS);
		loader = new DigitalInput(Constants.SHOOTER_LIMIT_SWITCH_POS);
		compressor = new Relay(1, Constants.SHOOTER_COMPRESSOR_1);
		pressureSensor = new DigitalInput(Constants.SHOOTER_PRESSURE_SENSOR);
		timer = new StopWatch();
	}
	
	public boolean getShellCylinder(){
		return shell.get();
	}
	
	public boolean getBarrelCylinder(){
		return barrel.get();
	}
	
	public boolean getShootCylinder(){
		return shoot.get();
	}
	
	public void setShellCylinder(boolean x){
		shell.set(x);
	}
	
	public void setBarrelCylinder(boolean x){
		barrel.set(x);
	}
	
	public void setShootCylinder(boolean x){
		shoot.set(x);
	}
	
	public boolean isShellReady(){
		return loader.get();
	}
	
	public void shoot(){
		barrel.set(true);
		timer.start();
		if(timer.getSeconds() >= 0.25){
			shoot.set(true);
			timer.reset();
		}
		if(timer.getSeconds() >= 0.25){
			shoot.set(false);
			timer.reset();
		}
		if(timer.getSeconds() >= 0.75){
			barrel.set(false);
			timer.reset();
		}
		if(timer.getSeconds() >= 1){
			shell.set(true);
			timer.reset();
		}
		if(timer.getSeconds() >= 0.25){
			shell.set(false);
			timer.reset();
		}
		timer.reset();
		timer.stop();
		setAllOff();
	}
	
	public void setAllOff(){
		shell.set(false);
		barrel.set(false);
		shoot.set(false);
	}
	
	/*
	 * Ensure that the compressor has pressure
	 */
        public void checkPressure(){
	    if(!pressureSensor.get()){
	        //Turn on the compressor
	        compressor.set(Relay.Value.kReverse);
	    }else{
	        //Turn off the compressor
	        compressor.set(Relay.Value.kOn);
	    }
        }

        public void stopCompressor(){
            compressor.set(Relay.Value.kOn);
        }
}
