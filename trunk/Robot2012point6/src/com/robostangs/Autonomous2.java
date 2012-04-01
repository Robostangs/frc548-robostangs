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
    private double initGyro;
    private double tempGyro;
    private double dToTarget = 0;
    
    public Autonomous2(DriveTrain d, Shooter s, Arm a, Pneumatics p){
        dt = d;
        dm = DriveMotors.getInstance();
        sh = s;
        ar = a;
        pn = p;
    }
    public void init(){
        dt.resetEncoders();
        ar.setOutRange(-1f , 1f);
        ar.setPidTop();
        step = 0;
        mode = Integer.parseInt(readMode());
        if(mode < 0 || mode > 4){
            mode = 0;
        }
        System.out.println("mode: " + mode);
        initGyro = dt.getGyro();
    }

    /*
     * Attempt to drive straight by using the gyro.
     */
    public void check(double sLeft, double sRight){
        if(dt.getGyro() - initGyro < -0.5){
            sRight = sRight*.88;
            sLeft = sLeft * 1.06;
            dt.drive(sLeft, sRight);
        }else if (dt.getGyro() - initGyro > 0.5 ){
            sLeft = sLeft*.90;
            sRight = sRight*1.01;
            dt.drive(sLeft, sRight);
        }else{
            dt.drive(sLeft, sRight);
        }
    }
    
    /*
     * Get the autonomous mode from file on the cRio.
     */
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
        if(!modeDone){
            switch(mode){
                /*
                 * Shoot from key
                 */
                case 0:
                    switch(step){
                        case 0:
                            sh.setRpmBackspin(Constants.SHOOTER_FRONT_KEY_RPM);
                            step++;
                            break;
                        case 1:
                            ar.setPosition(Constants.ARM_TOP);
                            sh.setRpmBackspin(Constants.SHOOTER_FRONT_KEY_RPM);
                            Timer.delay(2.5);
                            step++;
                            break;
                        case 2:
                            ar.stop();
                            sh.setRpmBackspin(Constants.SHOOTER_FRONT_KEY_RPM);
                            Timer.delay(.2);
                            step++;
                            break;
                        case 3:
                            ar.setPosition(Constants.ARM_TOP);
                            sh.setRpmBackspin(Constants.SHOOTER_FRONT_KEY_RPM);
                            Timer.delay(6);
                            step++;
                            break;
                        case 4:
                            sh.setConveyorSpeed(1);
                            sh.setRpmBackspin(Constants.SHOOTER_FRONT_KEY_RPM);
                            Timer.delay(1);
                            step++;
                            break;
                        case 5:
                            sh.setConveyorSpeed(-1);
                            Timer.delay(1);
                            step++;
                            break;
                        case 6:
                            sh.setConveyorSpeed(1);
                            Timer.delay(1.5);
                            step++;
                            break;
                        case 7: 
                            sh.setConveyorSpeed(0);
                            sh.setRpmBoth(0);
                            ar.setPosition(Constants.ARM_ZEROPOSITION);
                            Timer.delay(4);
                            break;
                        default:
                            break;
                    }
                    break;
                /*
                 * PREALIGNED
                 * Drive straight to fender, shoot
                 */
                case 1:
                    switch(step){
                        case 0: //Drive forward
                            pn.setGear(Constants.LOW_SPEED);
                            check(.35,.35);
                            if((Math.abs(dt.getLeftEncoder()) >= 1.45)){
                                dm.set(0,0,0,0);
                                dt.resetEncoders();
                                step++;
                                break;
                            }
                            break;                        
                        case 1: //prepare
                            sh.fenderShot();
                            sh.setConveyorSpeed(0);
                            ar.setPosition(Constants.ARM_TOP);
                            Timer.delay(3.25);
                            step++;
                            break;
                        case 2:     //Shoot
                            sh.fenderShot();
                            sh.setConveyorSpeed(1);
                            Timer.delay(3);
                            step++;
                            break;
                        case 3:     //Reset
                            sh.setTopRpm(0);
                            sh.setBottomRpm(0);
                            ar.setPosition(Constants.ARM_ZEROPOSITION);
                            sh.setConveyorSpeed(0);
                            break;    
                            
                    }
                    break;
                 /*
                  * Drive to fender, turn, shoot
                  */
                 case 2:
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
                            sh.setRpmBoth(0);
                            ar.setPosition(Constants.ARM_ZEROPOSITION);
                            sh.setConveyorSpeed(0);
                            break;                            
                    }
                    break;
                /*
                 * Shoot from back of the key
                 */
                case 3:
                    switch(step){
                        case 0:
                            sh.setRpmBackspin(Constants.SHOOTER_BACK_KEY_RPM);
                            step++;
                            break;
                        case 1:
                            sh.setRpmBackspin(Constants.SHOOTER_BACK_KEY_RPM);                            
                            ar.setPosition(Constants.ARM_TOP);
                            Timer.delay(8);
                            step++;
                            break;
                        case 2:
                            sh.setRpmBackspin(Constants.SHOOTER_BACK_KEY_RPM);
                            sh.setConveyorSpeed(1);
                            Timer.delay(.75);
                            step++;
                            break;
                        case 3:
                            sh.setRpmBackspin(Constants.SHOOTER_BACK_KEY_RPM);
                            sh.setConveyorSpeed(-1);
                            Timer.delay(1);
                            step++;
                            break;
                        case 4:
                            sh.setRpmBackspin(Constants.SHOOTER_BACK_KEY_RPM+150);                            
                            sh.setConveyorSpeed(1);
                            Timer.delay(1.5);
                            step++;
                            break;
                        case 5: 
                            sh.setConveyorSpeed(0);
                            sh.setRpmBoth(0);
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
                case 4:
                    switch(step){
                        case 0:
                            break;
                        default:
                            break;
                    }
                    break;
                    
                default:
                    break;
                /*
                 * Drive forward to bridge, ingest, drive backward around key, aim, shoot
                 */
                case 5:
                    switch(step){
                        case 0:
                            /*
                             * Setup
                             */
                            sh.setRpmBoth(0);
                            pn.setGear(Constants.LOW_SPEED);
                            ar.setPidBottom();
                            ar.setPosition(Constants.ARM_BOTTOM);
                            step++;
                            break;
                        case 1:
                            /*
                             * Drive toward bridge
                             */
                            ar.setPosition(Constants.ARM_BOTTOM);
                            check(1,1);
                            if((Math.abs(dt.getLeftEncoder()) >= .2)){
                                dm.set(0,0,0,0);
                                dt.resetEncoders();
                                step++;
                                break;
                            }
                            break;
                        case 2:
                            /*
                             * Ingest
                             */
                            ar.setPosition(Constants.ARM_BOTTOM);
                            sh.turnOnIngestor();
                            sh.setRpmBoth(-1000);
                            sh.setConveyorSpeed(-1);
                            Timer.delay(.8);
                            step++;
                            break;
                        case 3:
                            /*
                             * Drive forward a bit, up on bridge
                             */
                            check(1,1);
                            if((Math.abs(dt.getLeftEncoder()) >= .15)){
                                dm.set(0,0,0,0);
                                dt.resetEncoders();
                                step++;
                                break;
                            }
                            sh.setConveyorSpeed(-1);
                            sh.setRpmBoth(-1000);
                            break;
                        case 4:
                            /*
                             * wait to ingest
                             */
                            
                            //Redundant, but here anyway
                            sh.setConveyorSpeed(-1);
                            sh.setRpmBoth(-1000);
                            
                            Timer.delay(3);
                            dt.resetEncoders();
                            step++;
                            break;
                        case 5:
                            /*backup a bit
                             * 
                             */
                            check(-1,-1);
                            if((Math.abs(dt.getLeftEncoder()) <= -.2)){
                                dm.set(0,0,0,0);
                                dt.resetEncoders();
                                sh.turnOffIngestor();
                                sh.setRpmBoth(0);
                                sh.setConveyorSpeed(0);
                                tempGyro = dt.getGyro();
                                step++;
                                break;
                            }
                            
                            //Keep ingestor wheels on while backing up
                            sh.setRpmBoth(-1000);
                            sh.setConveyorSpeed(-1);
                            break;
                        case 6:
                            /*
                             *Turn 60 cw to avoid key when driving backwards, then shift to high speed
                             */
                            dt.drive(1, -1);
                            if(dt.getGyro() - tempGyro >= 60){
                                pn.setGear(!Constants.LOW_SPEED);
                                step++;
                                break;
                            }
                            break;
                        case 7:
                            /*
                             * arc around key until parallel to sides of field
                             */
                            dt.drive(-1, -.75);
                            if(dt.getGyro() - tempGyro <= 0){
                                dt.resetEncoders();
                                step++;
                                break;
                            }
                            break;
                        case 8:
                            /*
                             * Continue reversing, then low speed
                             */
                            check(-1,-1);
                            if((Math.abs(dt.getLeftEncoder()) <= -1)){
                                dm.set(0,0,0,0);
                                dt.resetEncoders();
                                pn.setGear(Constants.LOW_SPEED);
                                
                                //Get angle before turn
                                tempGyro = dt.getGyro();
                                ar.setPidTop();
                                //Dont lose balls while raising arm
                                sh.setConveyorSpeed(-.5);
                                step++;
                                break;
                            }
                            break;
                        case 9:
                            /*
                             * turn about 135 degrees cw
                             */
                            ar.setPosition(Constants.ARM_TOP);
                            dt.drive(1, -1);
                            if(dt.getGyro() >= tempGyro + 160){
                                dm.set(0,0,0,0);
                                tempGyro = dt.getGyro();
                               
                                try {
                                    dt.axisCam.getImage();
                                } catch (AxisCameraException ex) {
                                    ex.printStackTrace();
                                } catch (NIVisionException ex) {
                                    ex.printStackTrace();
                                }
                                 //wait for arm,camera
                                Timer.delay(.7);
                                sh.setConveyorSpeed(0);
                                dToTarget = dt.axisCam.getDistance();
                                ar.stop();
                                step++;
                                break;
                            }
                            break;
                        case 10:
                            /*
                             * Track targets
                             */
                            dt.setPosition(dt.axisCam.getHeading() + tempGyro);
                            sh.setRpmFromDistance(dToTarget);
                            Timer.delay(2);

                            dt.stop();
                            step++;
                            break;
                        case 11:
                            //If distance makes sense, bet. 50 and 500 cm
                            if(dToTarget >= 50 && dToTarget <= 500){
                                sh.setRpmFromDistance(dt.axisCam.getDistance());
                                sh.setConveyorSpeed(.5);
                                Timer.delay(3);
                                ar.setPidBottom();
                                step++;
                                break;
                            }
                            break;
                        case 12:
                            ar.setPosition(Constants.ARM_BOTTOM);
                            sh.setRpmBoth(0);
                            sh.setConveyorSpeed(0);
                        default:
                            break;
                    }
                    break;
            }
        }
    }
}