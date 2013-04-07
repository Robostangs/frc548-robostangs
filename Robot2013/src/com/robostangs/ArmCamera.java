/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robostangs;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * Manages the two axis cameras and image processing
 * maintainer: @sky
 * (massively TODO)
 */
public class ArmCamera implements PIDSource {
    private static ArmCamera instance = null;
    private static NetworkTable dash; 
    private static int yCenter;
    private static int area;
    private static int imageHeight;
    private static boolean pyramidMode;
    
    private ArmCamera() {
        pyramidMode = false;
        dash = NetworkTable.getTable("camera");     
        yCenter = 0;
        area = 0;
        imageHeight = (int) dash.getNumber("imageHeight", 300);
    }
    
    public static ArmCamera getInstance() {
        if (instance == null) {
            instance = new ArmCamera();
        }
        return instance;
    }
    
    public static void update() {
        dash.putBoolean("pyramidMode", pyramidMode);
        yCenter = (int) dash.getNumber("yCenter", imageHeight / 2);
        area = (int) dash.getNumber("area", 0); //TODO: default cam area
    }
    
    public static double getTarget() {
        update();
        return yCenter + Constants.CAM_Y_OFFSET;
    }
    
    public double pidGet() {
        update();
        return yCenter + Constants.CAM_Y_OFFSET;
    }
    
    public static void enablePyramidMode() {
        pyramidMode = true;
    }
    
    public static void enableThreePointMode() {
        pyramidMode = false;
    }
    
    public static void switchMode() {
        pyramidMode = !pyramidMode;
    }
    
}
