/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robostangs;

import edu.wpi.first.wpilibj.PIDSource;

/**
 * Class that serves as a PIDsource for DTalign code
 * @author sky
 */
public class DriveCamera implements PIDSource{
    private static DriveCamera instance = null;
    private Camera cam;
    
    private DriveCamera() {
        
    }
    
    public static DriveCamera getInstance() {
        if (instance == null) {
            instance = new DriveCamera();
        }
        
        return instance;
    }
    
    public double pidGet() {
        return Camera.getTargetHeading();
    }
}
