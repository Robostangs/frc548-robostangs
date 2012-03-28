package com.robostangs;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.*;
import com.sun.squawk.util.MathUtils;

/**
 *
 * @author john
 */
public class Camera implements PIDSource{
    private AxisCamera camera;
    private CriteriaCollection cc;                  // the criteria for doing the particle filter operation
    private ParticleAnalysisReport[] reports;       //Array of particle reports
    private ParticleAnalysisReport lowestReport;    //BottomRectangle
    public boolean beginCalc = false;
    private boolean checkSize = false;              //enable size requirements if more than 4 results
    public double lastImageTime = 0;

    
    public Camera(){
        if(Constants.USE_CAMERA){
            camera = AxisCamera.getInstance("10.5.48.11");  // get an instance ofthe camera
            camera.writeResolution(AxisCamera.ResolutionT.k640x480);//.k160x120);
        }
        cc = new CriteriaCollection();      // create the criteria for the particle filter
        //cc.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 640, false);
        //cc.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 480, false);
        //camServo = new Servo(1,2);
    }
    
    /*
     * Get a fresh image
     */
    public void getImage() throws AxisCameraException, NIVisionException{
        ColorImage image = null;     
        if(Constants.USE_CAMERA){
            if(camera.freshImage()){
                try {
                    image = camera.getImage();
                } catch (AxisCameraException ex) {
                    ex.printStackTrace();
                } catch (NIVisionException ex) {
                    ex.printStackTrace();
                }

                BinaryImage thresholdImage = image.thresholdHSI(113,156,243,255,114,239);       //keep bright green
                BinaryImage convexHullImage = thresholdImage.convexHull(true);                  // fill in occluded rectangles
                BinaryImage bigObjectsImage = convexHullImage.removeSmallObjects(true, 4);      // remove small artifacts
                BinaryImage filteredImage = bigObjectsImage.particleFilter(cc);                 // find filled in rectangles

                reports = filteredImage.getOrderedParticleAnalysisReports();  // get list of results
                int biggestIndex = 0;
                if(reports.length > 0){
                    lowestReport = reports[0];
                }
                if(reports.length > 4){
                    checkSize = true;
                }
                for (int i = 0; i < reports.length; i++) {                                // print results
                    ParticleAnalysisReport r = reports[i];
                    if(r.center_mass_y > reports[biggestIndex].center_mass_y){
                        biggestIndex = i;
                        lowestReport = r;  
                    }
                }
                lastImageTime = Timer.getFPGATimestamp();
                filteredImage.free();
                convexHullImage.free();
                thresholdImage.free();
                image.free();  
                beginCalc = true;
            }
        }
    }
    
    /*
     * Return the x coordinate of the center of the target.
     */
    public int getTargetCenterX(){
        return lowestReport.center_mass_x;
    }
    
    /*
     * Return the y coordinate of the center of the target.
     */
    public int getTargetCenterY(){
        return lowestReport.center_mass_y;
    }
    
    /*
     * Return the widht of the target, in pixels
     */
    public int getTargetWidth(){
        return lowestReport.boundingRectWidth;
    }
    
    /*
     * Return the height of the target, in pixels
     */
    public int getTargetHeight(){
        return lowestReport.boundingRectHeight;
    }
    
    /*
     * Return the area of the target, in pixels
     */
    public double getTargetArea(){
        return lowestReport.particleArea;
    }
    
    /*
     * Return center X pixel, adjusting for camera not in center of robot.
     */
    public int getXCenter(){
        if(Constants.USE_CAMERA){
            if(reports.length > 0 && beginCalc){
                int z = getTargetCenterX() + (int)((.28*getTargetHeight()) / .44);
                return z;
            }
            else{
                return -1;
            }
        }else{
            return -1;
        }
    }
    

    /*
     * Return the distance to the center of the target, in meters.
     */
    public double getDistance(){
        if(Constants.USE_CAMERA){
            if(beginCalc){
                double targetHeightMeters=.4572;    //18in in meters
                double targetHeightPixels=lowestReport.boundingRectHeight;
                double fieldHeightMeters;           //Determined with mathematics
                double fieldHeightPixels= camera.getResolution().height;
                double theta = Math.toRadians(Constants.CAM_FOV_ANGLE);                //Axis M1011 angle/2
                double distance;

                fieldHeightMeters = (targetHeightMeters * fieldHeightPixels)/targetHeightPixels;

                fieldHeightMeters /= 2;         //Half the height, for right triangle

                distance = (fieldHeightMeters)/Math.tan(theta);
                
                double fixedDistance = distance;//-.0343*distance*distance + 1.1926*distance - 0.1909;
                fixedDistance *= 100; //to centimeters
                //double fixedDistance = .9567*distance - 10.166;
                //System.out.println("D: " + distance + " fd: " + fixedDistance);
                return fixedDistance;
            }else{
                return 0;
            }
        }else{
            return 0;
        }
    }
    
    /*
     * Return the required angle to turn to be centered toward the target
     * in degrees
     */
    public double getHeading(){
        double d = getDistance();   //m
        //double x = (320-(getTargetCenterX()+  (getTargetHeight()/.4572)*.254))*.55555;
        double x = ((getTargetCenterX() + (getTargetHeight()/.4572)*.245) -320) *.55555;
        //double x = getTargetCenterX() - 320;
        //System.out.println("xxxx:" + x);
        return MathUtils.atan(x/d) * 57.2957795;    //to degrees
    }
    
    /*
     * Return the current resolution setting of the camera.
     */
    public AxisCamera.ResolutionT getResolution(){
        if(Constants.USE_CAMERA){
            return camera.getResolution();
        }else{
            return AxisCamera.ResolutionT.k640x480;
        }
    }
    
    /*
     * Return the center pixel, for use in pid.
     */
    public double pidGet() {
        double x = getHeading();//getXCenter();
        return x;
    }
}