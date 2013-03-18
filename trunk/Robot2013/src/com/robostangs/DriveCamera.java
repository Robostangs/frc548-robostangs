package com.robostangs;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * Class that serves as a PIDsource for DTalign code
 * @author sky
 */
public class DriveCamera implements PIDSource{
    private static DriveCamera instance = null;
    private static NetworkTable dash;
    private static int xCenter;
    private static int imageWidth;
    private static int area;
    
    private DriveCamera() {
        dash = NetworkTable.getTable("camera");
        xCenter = 0;
        area = 0;
        imageWidth = (int) dash.getNumber("imageWidth", 480);
    }
    
    public static DriveCamera getInstance() {
        if (instance == null) {
            instance = new DriveCamera();
        }
        return instance;
    }
    
    public double pidGet() {
        update();
        return xCenter + Constants.CAM_X_OFFSET;
    }
    
    public void update() {
        xCenter = (int) dash.getNumber("xCenter", imageWidth / 2);
        area = (int) dash.getNumber("area", 0); //TODO: default cam area
    }
}
