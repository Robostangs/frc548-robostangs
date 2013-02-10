/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robostangs;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Manages the two axis cameras and image processing
 * maintainer: @sky
 * (massively TODO)
 */
public class Camera implements PIDSource {
    private static Camera instance = null;
    private AxisCamera armCam, ingestCam;
    private boolean pyramidMode;
    
    private Camera() {
        
    }
    
    public static Camera getInstance() {
        if (instance == null) {
            instance = new Camera();
        }
        
        return instance;
    }
    
    public static void getImage() {
        
    }
    
    public static double getTargetAngle() {
        return 0;
    }
    
    public static double getTargetHeading() {
        return 0;
    }

    public double pidGet() {
        return 0;
    }
    
    public static void enablePyramidMode() {
        
    }
    
    public static void enableThreePointMode() {
        
    }
    
}
