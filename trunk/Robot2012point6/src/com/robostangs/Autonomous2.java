/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robostangs;

import com.sun.squawk.io.BufferedReader;
import com.sun.squawk.microedition.io.FileConnection;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.image.NIVisionException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;

/**
 *
 * @author john
 */
public class Autonomous2{
    private int mode = 0;   //current autonomouse mode
    private int step = 0;   //Step of specific autonomous
    private boolean modeDone = false;   //finished autonomous;
    private DriveMotors dm;
    private DriveTrain dt;
    private Shooter sh;
    private Arm ar;
    private Pneumatics pn;
    private double speed = 0.5;
    
    public Autonomous2(DriveTrain d, Shooter s, Arm a, Pneumatics p){
        dt = d;
        dm = DriveMotors.getInstance();
        sh = s;
        ar = a;
        pn = p;
    }
    public void init(){
        dt.resetEncoders();
        step = 0;
        mode = Integer.parseInt(readMode());
        if(mode < 0 || mode > 11){
            mode = 0;
        }
        System.out.println("mode: " + mode); 
    }
    //make sure it's driving straight
    public void check(double sLeft, double sRight){
        if((Math.abs(dt.getRightCount()) - Math.abs(dt.getLeftCount()*.95)) >= 25){
            sRight = sRight*.90;
            sLeft = sLeft * 1.05;
            dt.drive(sLeft, sRight);
        }else if ((Math.abs(dt.getRightCount()) - Math.abs(dt.getLeftCount()*.95)) <= -25){
            sLeft = sLeft*.90;
            sRight = sRight*1.05;
            dt.drive(sLeft, sRight);
        }else{
            dt.drive(sLeft, sRight);
        }
        /*double spe = Math.abs(s);
        if((dt.getLeftCount() <= (dt.getRightCount() - 40))){
            s = s*.95;
            dm.set((s/Math.abs(s))*spe, (s/Math.abs(s))*spe, s, s);
        }else if ((dt.getRightCount() <= (dt.getLeftCount() - 40))){
            s = s*.95;
            dm.set(s, s, (s/Math.abs(s))*spe, (s/Math.abs(s))*spe);
        }else{
            dm.set((s/Math.abs(s))*s,(s/Math.abs(s))*s,(s/Math.abs(s))*s,(s/Math.abs(s))*s);
        }*/
    }
    
    public String readMode(){
        String sMode = "";
        try{
            FileConnection fconn = (FileConnection)
            Connector.open("file:///auton.txt" , Connector.READ);
            InputStream in = fconn.openInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while((strLine = br.readLine()) != null)
                sMode = strLine;
            in.close();
            fconn.close(); 
        }catch(ConnectionNotFoundException error){  
            System.out.println("connection Not Found " + error);
        }catch(IOException error){
            System.out.println("IOE");
            System.out.println(error);
        }
        return sMode;
    }

    public void run(){
        if(!modeDone){      //Autonomous is not finished yet. continue stepping
            switch(mode){
                /*
                 * Hit bridge, drive straight to middle of key, shoot
                 */
                case 0:     
                    switch(step){
                        case 0:     //Drive backwards
                           //TODO: Set High Gear 
                           //dm.set(-speed, -speed, -speed, -speed);
                           check(-speed, -speed);
                           //TODO: Check distance
                           if((dt.getLeftEncoder() + dt.getRightEncoder())/2 <= -1.44){
                                dm.set(0,0,0,0);
                                dt.resetEncoders();
                                step++;
                                break;
                            }
                            break;
                        case 1: //Wait at bridge
                            //TODO: Check appropriate time
                            Timer.delay(3);
                            step++;
                            break;
                        case 2:     //Drive forward
                            //dm.set(speed, speed, speed, speed);
                            check(speed, speed);
                            //TODO: Check distance
                            if((dt.getLeftEncoder() + dt.getRightEncoder())/2 >= 3.9){
                                dm.set(0);
                                dt.resetEncoders();
                                Timer.delay(.2);
                                step++;
                                break;
                            }
                            break;
                        case 3:     //Prepare
                            //TODO: Check rpm
                            sh.setRpmFromDistance(dt.axisCam.getDistance());
                            try {
                                ar.setPosition(Constants.ARM_TOP);
                                dt.axisCam.getImage();
                            } catch (AxisCameraException ex) {
                                ex.printStackTrace();
                            } catch (NIVisionException ex) {
                                ex.printStackTrace();
                            }
                            Timer.delay(.8);
                            step++;
                            break;
                        case 4:     //align with target
                            try {
                                ar.setPosition(Constants.ARM_TOP);
                                dt.axisCam.getImage();
                            } catch (AxisCameraException ex) {
                                ex.printStackTrace();
                            } catch (NIVisionException ex) {
                                ex.printStackTrace();
                            }
                            System.out.println("xcenter: " + dt.axisCam.getXCenter() + " distance: " + dt.axisCam.getDistance());
                            if(dt.setPosition(320)){
                                System.out.println("Going to next step");
                                step++;
                                break;
                            }
                            break;
                        case 5:     //Shoot
                            System.out.println("Step 5");
//Continue if rpms correct during dispense
                            if(sh.conveyorDispense()){
                                Timer.delay(2);
                                step++;
                                break;
                            }else{
                                break;
                            }
                        case 6:     //Reset
                            sh.setRpm(0);
                            ar.setPosition(Constants.ARM_ZEROPOSITION);
                            sh.setConveyorSpeed(0);
                            break;
                    }
                    break;
                /*
                 * Go right around the key, go to right corner of basket area and shoot
                 */
                case 1:
                    switch(step){
                        case 0: //turn some angle
                           //TODO: define angle
                           dt.turn(true);
                           if(dt.getLeftCount() > -151.125 || dt.getRightCount() > -126.725){
                                dm.set(0,0,0,0);
                                dt.resetEncoders();
                                Timer.delay(.1);
                                step++;
                                break;
                            }
                            break;
                        case 1: //drive some distance
                            //TODO: define distance
                            //dm.set(speed, speed, speed, speed);
                            check(speed, speed);
                            if((dt.getLeftEncoder() + dt.getRightEncoder())/2 >= 832834){
                                dm.set(0);
                                dt.resetEncoders();
                                Timer.delay(.1);
                                step++;
                                break;
                            }
                            break;
                        case 2: //turn some angle
                            //TODO: define angle
                            dt.turn(false);
                            if(dt.getLeftCount() < 83283 || dt.getRightCount() < 78667){
                                dm.set(0,0,0,0);
                                dt.resetEncoders();
                                Timer.delay(.2);
                                step++;
                                break;
                            }
                            break;
                        case 3: //drive some distance
                            //TODO: define distance
                            //dm.set(speed, speed, speed, speed);
                            check(speed, speed);
                            if((dt.getLeftEncoder() + dt.getRightEncoder())/2 >= 832834){
                                dm.set(0);
                                dt.resetEncoders();
                                Timer.delay(.2);
                                step++;
                                break;
                            }
                            break;
                        case 4: //prepare
                             //TODO: Check rpm
                            sh.setRpm(1400);
                            try {
                                ar.setPosition(Constants.ARM_TOP);
                                dt.axisCam.getImage();
                            } catch (AxisCameraException ex) {
                                ex.printStackTrace();
                            } catch (NIVisionException ex) {
                                ex.printStackTrace();
                            }
                            Timer.delay(.8);
                            step++;
                            break;
                        case 5: //align
                            try {
                                ar.setPosition(Constants.ARM_TOP);
                                dt.axisCam.getImage();
                            } catch (AxisCameraException ex) {
                                ex.printStackTrace();
                            } catch (NIVisionException ex) {
                                ex.printStackTrace();
                            }
                            System.out.println("xcenter: " + dt.axisCam.getXCenter() + " distance: " + dt.axisCam.getDistance());
                            if(dt.setPosition(320)){
                                System.out.println("Going to next step");
                                step++;
                                break;
                            }
                            break;
                        case 6:     //Shoot
                            System.out.println("Step 5");
                            sh.setConveyorSpeed(1);
                            Timer.delay(2);
                            step++;
                            break;
                        case 7:     //Reset
                            sh.setRpm(0);
                            ar.setPosition(Constants.ARM_ZEROPOSITION);
                            sh.setConveyorSpeed(0);
                            break;                            
                    }
                    break;
                /*
                 * Go left around the key, go to left corner of basket area and shoot
                 */
                case 2:
                    switch(step){
                        case 0: //turn some angle
                           //TODO: define angle
                           dt.turn(false);
                           if(dt.getLeftCount() > 8745387 || dt.getRightCount() > 8743843){
                                dm.set(0,0,0,0);
                                dt.resetEncoders();
                                Timer.delay(.2);
                                step++;
                                break;
                            }
                            break;
                        case 1: //drive some distance
                            //TODO: define distance
                            //dm.set(speed, speed, speed, speed);
                            check(speed, speed);
                            if((dt.getLeftEncoder() + dt.getRightEncoder())/2 >= 832834){
                                dm.set(0);
                                dt.resetEncoders();
                                Timer.delay(.2);
                                step++;
                                break;
                            }
                            break;
                        case 2: //turn some angle
                            //TODO: define angle
                               dt.turn(false);
                            if(dt.getLeftCount() < 83283 || dt.getRightCount() < 78667){
                                dm.set(0,0,0,0);
                                dt.resetEncoders();
                                Timer.delay(.2);
                                step++;
                                break;
                            }
                            break;
                        case 3: //drive some distance
                            //TODO: define distance
                            //dm.set(speed, speed, speed, speed);
                            check(speed, speed);
                            if((dt.getLeftEncoder() + dt.getRightEncoder())/2 >= 832834){
                                dm.set(0);
                                dt.resetEncoders();
                                Timer.delay(.2);
                                step++;
                                break;
                            }
                            break;
                        case 4: //prepare
                             //TODO: Check rpm
                            sh.setRpm(1400);
                            try {
                                ar.setPosition(Constants.ARM_TOP);
                                dt.axisCam.getImage();
                            } catch (AxisCameraException ex) {
                                ex.printStackTrace();
                            } catch (NIVisionException ex) {
                                ex.printStackTrace();
                            }
                            Timer.delay(.8);
                            step++;
                            break;
                        case 5: //align
                            try {
                                ar.setPosition(Constants.ARM_TOP);
                                dt.axisCam.getImage();
                            } catch (AxisCameraException ex) {
                                ex.printStackTrace();
                            } catch (NIVisionException ex) {
                                ex.printStackTrace();
                            }
                            System.out.println("xcenter: " + dt.axisCam.getXCenter() + " distance: " + dt.axisCam.getDistance());
                            if(dt.setPosition(320)){
                                System.out.println("Going to next step");
                                step++;
                                break;
                            }
                            break;
                        case 6:     //Shoot
                            System.out.println("Step 5");
                            sh.setConveyorSpeed(1);
                            Timer.delay(2);
                            step++;
                            break;
                        case 7:     //Reset
                            sh.setRpm(0);
                            ar.setPosition(Constants.ARM_ZEROPOSITION);
                            sh.setConveyorSpeed(0);
                            break; 
                    }
                    break;
                /*
                 * Go right around the key, go to center of basket area and shoot
                 */
                case 3:
                    switch(step){
                        case 0: //turn some angle
                           //TODO: define angle
                           dt.turn(true);
                           if(dt.getLeftCount() > -151.125 || dt.getRightCount() > -126.725){
                                dm.set(0,0,0,0);
                                dt.resetEncoders();
                                Timer.delay(.2);
                                step++;
                                break;
                            }
                            break;
                        case 1: //drive some distance
                            //TODO: define distance
                            //dm.set(speed, speed, speed, speed);
                            check(speed, speed);
                            if((dt.getLeftEncoder() + dt.getRightEncoder())/2 >= 832834){
                                dm.set(0);
                                dt.resetEncoders();
                                Timer.delay(.2);
                                step++;
                                break;
                            }
                            break;
                        case 2: //turn some angle
                            //TODO: define angle
                            dt.turn(false);
                            if(dt.getLeftCount() < 83283 || dt.getRightCount() < 78667){
                                dm.set(0,0,0,0);
                                dt.resetEncoders();
                                Timer.delay(.2);
                                step++;
                                break;
                            }
                            break;
                        case 3: //drive some distance
                            //TODO: define distance
                            //dm.set(speed, speed, speed, speed);
                            check(speed, speed);
                            if((dt.getLeftEncoder() + dt.getRightEncoder())/2 >= 832834){
                                dm.set(0);
                                dt.resetEncoders();
                                Timer.delay(.2);
                                step++;
                                break;
                            }
                            break;
                        case 4: //prepare
                             //TODO: Check rpm
                            sh.setRpm(1400);
                            try {
                                ar.setPosition(Constants.ARM_TOP);
                                dt.axisCam.getImage();
                            } catch (AxisCameraException ex) {
                                ex.printStackTrace();
                            } catch (NIVisionException ex) {
                                ex.printStackTrace();
                            }
                            Timer.delay(.8);
                            step++;
                            break;
                        case 5: //align
                            try {
                                ar.setPosition(Constants.ARM_TOP);
                                dt.axisCam.getImage();
                            } catch (AxisCameraException ex) {
                                ex.printStackTrace();
                            } catch (NIVisionException ex) {
                                ex.printStackTrace();
                            }
                            System.out.println("xcenter: " + dt.axisCam.getXCenter() + " distance: " + dt.axisCam.getDistance());
                            if(dt.setPosition(320)){
                                System.out.println("Going to next step");
                                step++;
                                break;
                            }
                            break;
                        case 6:     //Shoot
                            System.out.println("Step 5");
                            sh.setConveyorSpeed(1);
                            Timer.delay(2);
                            step++;
                            break;
                        case 7:     //Reset
                            sh.setRpm(0);
                            ar.setPosition(Constants.ARM_ZEROPOSITION);
                            sh.setConveyorSpeed(0);
                            break;                            
                    }
                    break;
                /*
                 * Go left around the key, go to center of basket area and shoot
                 */
                case 4:
                    switch(step){
                        case 0: //turn some angle
                           //TODO: define angle
                           dt.turn(false);
                           if(dt.getLeftCount() > -151.125 || dt.getRightCount() > -126.725){
                                dm.set(0,0,0,0);
                                dt.resetEncoders();
                                Timer.delay(.2);
                                step++;
                                break;
                            }
                            break;
                        case 1: //drive some distance
                            //TODO: define distance
                            //dm.set(speed, speed, speed, speed);
                            check(speed, speed);
                            if((dt.getLeftEncoder() + dt.getRightEncoder())/2 >= 832834){
                                dm.set(0);
                                dt.resetEncoders();
                                Timer.delay(.2);
                                step++;
                                break;
                            }
                            break;
                        case 2: //turn some angle
                            //TODO: define angle
                            dt.turn(true);
                            if(dt.getLeftCount() < 83283 || dt.getRightCount() < 78667){
                                dm.set(0,0,0,0);
                                dt.resetEncoders();
                                Timer.delay(.2);
                                step++;
                                break;
                            }
                            break;
                        case 3: //drive some distance
                            //TODO: define distance
                            //dm.set(speed, speed, speed, speed);
                            check(speed, speed);
                            if((dt.getLeftEncoder() + dt.getRightEncoder())/2 >= 832834){
                                dm.set(0);
                                dt.resetEncoders();
                                Timer.delay(.2);
                                step++;
                                break;
                            }
                            break;
                        case 4: //prepare
                             //TODO: Check rpm
                            sh.setRpm(1400);
                            try {
                                ar.setPosition(Constants.ARM_TOP);
                                dt.axisCam.getImage();
                            } catch (AxisCameraException ex) {
                                ex.printStackTrace();
                            } catch (NIVisionException ex) {
                                ex.printStackTrace();
                            }
                            Timer.delay(.8);
                            step++;
                            break;
                        case 5: //align
                            try {
                                ar.setPosition(Constants.ARM_TOP);
                                dt.axisCam.getImage();
                            } catch (AxisCameraException ex) {
                                ex.printStackTrace();
                            } catch (NIVisionException ex) {
                                ex.printStackTrace();
                            }
                            System.out.println("xcenter: " + dt.axisCam.getXCenter() + " distance: " + dt.axisCam.getDistance());
                            if(dt.setPosition(320)){
                                System.out.println("Going to next step");
                                step++;
                                break;
                            }
                            break;
                        case 6:     //Shoot
                            System.out.println("Step 5");
                            sh.setConveyorSpeed(1);
                            Timer.delay(2);
                            step++;
                            break;
                        case 7:     //Reset
                            sh.setRpm(0);
                            ar.setPosition(Constants.ARM_ZEROPOSITION);
                            sh.setConveyorSpeed(0);
                            break;  
                    }
                    break;
                /*
                 * Go left around the key, go to left side of basket area and shoot
                 */
                case 5:
                    switch(step){
                        case 0: //turn some angle
                           //TODO: define angle
                           dt.turn(false);
                           if(dt.getLeftCount() > -151.125 || dt.getRightCount() > -126.725){
                                dm.set(0,0,0,0);
                                dt.resetEncoders();
                                Timer.delay(.2);
                                step++;
                                break;
                            }
                            break;
                        case 1: //drive some distance
                            //TODO: define distance
                            //dm.set(speed, speed, speed, speed);
                            check(speed, speed);
                            if((dt.getLeftEncoder() + dt.getRightEncoder())/2 >= 832834){
                                dm.set(0);
                                dt.resetEncoders();
                                Timer.delay(.2);
                                step++;
                                break;
                            }
                            break;
                        case 2: //turn some angle
                            //TODO: define angle
                            dt.turn(true);
                            if(dt.getLeftCount() < 83283 || dt.getRightCount() < 78667){
                                dm.set(0,0,0,0);
                                dt.resetEncoders();
                                Timer.delay(.2);
                                step++;
                                break;
                            }
                            break;
                        case 3: //drive some distance
                            //TODO: define distance
                            //dm.set(speed, speed, speed, speed);
                            check(speed, speed);
                            if((dt.getLeftEncoder() + dt.getRightEncoder())/2 >= 832834){
                                dm.set(0);
                                dt.resetEncoders();
                                Timer.delay(.2);
                                step++;
                                break;
                            }
                            break;
                        case 4: //prepare
                             //TODO: Check rpm
                            sh.setRpm(1400);
                            try {
                                ar.setPosition(Constants.ARM_TOP);
                                dt.axisCam.getImage();
                            } catch (AxisCameraException ex) {
                                ex.printStackTrace();
                            } catch (NIVisionException ex) {
                                ex.printStackTrace();
                            }
                            Timer.delay(.8);
                            step++;
                            break;
                        case 5: //align
                            try {
                                ar.setPosition(Constants.ARM_TOP);
                                dt.axisCam.getImage();
                            } catch (AxisCameraException ex) {
                                ex.printStackTrace();
                            } catch (NIVisionException ex) {
                                ex.printStackTrace();
                            }
                            System.out.println("xcenter: " + dt.axisCam.getXCenter() + " distance: " + dt.axisCam.getDistance());
                            if(dt.setPosition(320)){
                                System.out.println("Going to next step");
                                step++;
                                break;
                            }
                            break;
                        case 6:     //Shoot
                            System.out.println("Step 5");
                            sh.setConveyorSpeed(1);
                            Timer.delay(2);
                            step++;
                            break;
                        case 7:     //Reset
                            sh.setRpm(0);
                            ar.setPosition(Constants.ARM_ZEROPOSITION);
                            sh.setConveyorSpeed(0);
                            break;  
                    }
                    break;
                /*
                 * Go right around the key, go to right side of basket area and shoot
                 */
                case 6:
                    switch(step){
                        case 0: //turn some angle
                           //TODO: define angle
                           dt.turn(true);                           
                           if(dt.getLeftCount() > -151.125 || dt.getRightCount() > -126.725){
                                dm.set(0,0,0,0);
                                dt.resetEncoders();
                                Timer.delay(.2);
                                step++;
                                break;
                            }
                            break;
                        case 1: //drive some distance
                            //TODO: define distance
                            //dm.set(speed, speed, speed, speed);
                            check(speed, speed);
                            if((dt.getLeftEncoder() + dt.getRightEncoder())/2 >= 832834){
                                dm.set(0);
                                dt.resetEncoders();
                                Timer.delay(.2);
                                step++;
                                break;
                            }
                            break;
                        case 2: //turn some angle
                            //TODO: define angle
                            dt.turn(false);
                            if(dt.getLeftCount() < 83283 || dt.getRightCount() < 78667){
                                dm.set(0,0,0,0);
                                dt.resetEncoders();
                                Timer.delay(.2);
                                step++;
                                break;
                            }
                            break;
                        case 3: //drive some distance
                            //TODO: define distance
                            //dm.set(speed, speed, speed, speed);
                            check(speed, speed);
                            if((dt.getLeftEncoder() + dt.getRightEncoder())/2 >= 832834){
                                dm.set(0);
                                dt.resetEncoders();
                                Timer.delay(.2);
                                step++;
                                break;
                            }
                            break;
                        case 4: //prepare
                             //TODO: Check rpm
                            sh.setRpm(1400);
                            try {
                                ar.setPosition(Constants.ARM_TOP);
                                dt.axisCam.getImage();
                            } catch (AxisCameraException ex) {
                                ex.printStackTrace();
                            } catch (NIVisionException ex) {
                                ex.printStackTrace();
                            }
                            Timer.delay(.8);
                            step++;
                            break;
                        case 5: //align
                            try {
                                ar.setPosition(Constants.ARM_TOP);
                                dt.axisCam.getImage();
                            } catch (AxisCameraException ex) {
                                ex.printStackTrace();
                            } catch (NIVisionException ex) {
                                ex.printStackTrace();
                            }
                            System.out.println("xcenter: " + dt.axisCam.getXCenter() + " distance: " + dt.axisCam.getDistance());
                            if(dt.setPosition(320)){
                                System.out.println("Going to next step");
                                step++;
                                break;
                            }
                            break;
                        case 6:     //Shoot
                            System.out.println("Step 5");
                            sh.setConveyorSpeed(1);
                            Timer.delay(2);
                            step++;
                            break;
                        case 7:     //Reset
                            sh.setRpm(0);
                            ar.setPosition(Constants.ARM_ZEROPOSITION);
                            sh.setConveyorSpeed(0);
                            break;                            
                    }
                    break;
                /*
                 * Drive straight to fender, shoot
                 */
                case 7:
                    switch(step){
                        case 0: //Drive forward
                            pn.setGear(Constants.LOW_SPEED);
                            check(.35,.35);
                            if((Math.abs(dt.getLeftEncoder()) >= .1)){
                                dm.set(0,0,0,0);
                                dt.resetEncoders();
                                step++;
                                break;
                            }
                            break;
                        case 1: //Turn
                            pn.setGear(Constants.LOW_SPEED);
                            check(-.43,.43);
                            if((Math.abs(dt.getLeftEncoder()) >= .1)){
                                dm.set(0,0,0,0);
                                dt.resetEncoders();
                                step++;
                                break;
                            }
                            break;
                        case 2:
                            step++;
                            break;
                        case 3:
                             //TODO: write distance, rpms
                           pn.setGear(Constants.LOW_SPEED);
                           check(.5, .5);
                           if(Math.abs(dt.getLeftEncoder()) >= 1.55){
                                dm.set(0,0,0,0);
                                dt.resetEncoders();
                                sh.fenderShot();
                                step++;
                                break;
                            }
                            break;
                        case 4: //prepare
                             //TODO: Check rpm
                            sh.fenderShot();
                            sh.setConveyorSpeed(0);
                            ar.setPosition(Constants.ARM_TOP);
                            Timer.delay(2.5);
                            step++;
                            break;
                        case 5:     //Shoot
                            sh.fenderShot();
                            sh.setConveyorSpeed(1);
                            Timer.delay(3);
                            step++;
                            break;
                        case 6:     //Reset
                            sh.setRpm(0);
                            ar.setPosition(Constants.ARM_ZEROPOSITION);
                            sh.setConveyorSpeed(0);
                            break;                            
                    }
                    break;
                /*
                 * Shoot from front of key
                 */
                case 8:
                    switch(step){
                        case 0:
                            sh.setRpm(Constants.SHOOTER_FRONT_KEY_RPM);
                            step++;
                            break;
                        case 1:
                            ar.setPosition(Constants.ARM_TOP);
                            Timer.delay(8);
                            sh.setRpm(Constants.SHOOTER_FRONT_KEY_RPM);
                            step++;
                            break;
                        case 2:
                            sh.setConveyorSpeed(1);
                            sh.setRpm(Constants.SHOOTER_FRONT_KEY_RPM);
                            Timer.delay(1);
                            step++;
                            break;
                        case 3:
                            sh.setConveyorSpeed(-1);
                            Timer.delay(1);
                            step++;
                            break;
                        case 4:
                            sh.setConveyorSpeed(1);
                            Timer.delay(1.5);
                            step++;
                            break;
                        case 5: 
                            sh.setConveyorSpeed(0);
                            sh.setRpm(0);
                            ar.setPosition(Constants.ARM_ZEROPOSITION);
                            Timer.delay(4);
                            break;
                        default:
                            break;
                    }
                    break;
                /*
                 * Shoot from back of the key
                 */
                case 9:
                    switch(step){
                        case 0:
                            sh.setRpm(Constants.SHOOTER_BACK_KEY_RPM+150);
                            step++;
                            break;
                        case 1:
                            sh.setRpm(Constants.SHOOTER_BACK_KEY_RPM+150);                            
                            ar.setPosition(Constants.ARM_TOP);
                            Timer.delay(8);
                            step++;
                            break;
                        case 2:
                            sh.setRpm(Constants.SHOOTER_BACK_KEY_RPM+150);
                            sh.setConveyorSpeed(1);
                            Timer.delay(.75);
                            step++;
                            break;
                        case 3:
                            sh.setRpm(Constants.SHOOTER_BACK_KEY_RPM+150);
                            sh.setConveyorSpeed(-1);
                            Timer.delay(1);
                            step++;
                            break;
                        case 4:
                            sh.setRpm(Constants.SHOOTER_BACK_KEY_RPM+150);                            
                            sh.setConveyorSpeed(1);
                            Timer.delay(1.5);
                            step++;
                            break;
                        case 5: 
                            sh.setConveyorSpeed(0);
                            sh.setRpm(0);
                            ar.setPosition(Constants.ARM_ZEROPOSITION);
                            Timer.delay(4);
                            break;
                        default:
                            break;
                    }
                    break;
                /*
                 * shoot from any spot on key based on camera
                 */
                case 10:
                    switch(step){
                        case 0:
                            try {
                                dt.axisCam.getImage();
                            } catch (AxisCameraException ex) {
                                ex.printStackTrace();
                            } catch (NIVisionException ex) {
                                ex.printStackTrace();
                            }
                            Timer.delay(1   );
                            sh.setRpmFromDistance(dt.axisCam.getDistance()+100);
                            step++;
                            break;
                        case 1:
                            sh.setRpmFromDistance(dt.axisCam.getDistance()+100);                        
                            ar.setPosition(Constants.ARM_TOP);
                            Timer.delay(7.75);
                            step++;
                            break;
                        case 2:
                            sh.setRpmFromDistance(dt.axisCam.getDistance()+100);
                            sh.setConveyorSpeed(1);
                            Timer.delay(.5);
                            step++;
                            break;
                        case 3:
                            sh.setRpmFromDistance(dt.axisCam.getDistance()+100);
                            sh.setConveyorSpeed(-1);
                            Timer.delay(1.2);
                            step++;
                            break;
                        case 4:
                            sh.setRpmFromDistance(dt.axisCam.getDistance()+100);                        
                            sh.setConveyorSpeed(1);
                            Timer.delay(1.5);
                            step++;
                            break;
                        case 5: 
                            sh.setConveyorSpeed(0);
                            sh.setRpm(0);
                            ar.setPosition(Constants.ARM_ZEROPOSITION);
                            Timer.delay(4);
                            break;
                        default:
                            break;
                    }
                    break;
                /*
                 * Do nothing
                 */
                case 11:
                    switch(step){
                        case 0:
                            break;
                        default:
                            break;
                    }
                    break;
                    
                default:
                    break;
            }
        }
    }   
}
