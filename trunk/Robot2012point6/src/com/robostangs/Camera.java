package com.robostangs;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.*;
/**
 * @author john
 */
public class Camera implements PIDSource{
    private AxisCamera camera;
    private CriteriaCollection cc;                  // the criteria for doing the particle filter operation
    private ParticleAnalysisReport[] reports;       //Array of particle reports
    private ParticleAnalysisReport lowestReport;    //Bottom Rectangle Target
    private ParticleAnalysisReport highestReport;   //Top Rectangle Target
    public boolean beginCalc = false;
    private boolean checkSize = false;              //enable size requirements if more than 4 results
    public double lastImageTime = 0;               
    public boolean searchHigh = false;               //Look at high or low targets

    
    public Camera(){
            camera = AxisCamera.getInstance("10.5.48.11");  // get an instance ofthe camera
            camera.writeResolution(AxisCamera.ResolutionT.k640x480);//.k160x120);
        cc = new CriteriaCollection();      // create the criteria for the particle filter
        //cc.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 640, false);
        //cc.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 480, false);
    }
    
    /*
     * Get a fresh image
     */
    public void getImage() throws AxisCameraException, NIVisionException{
        if(camera.freshImage()){
            ColorImage image = null;
            try {
                image = camera.getImage();
            } catch (AxisCameraException ex) {
                ex.printStackTrace();
            } catch (NIVisionException ex) {
                ex.printStackTrace();
            }

            BinaryImage thresholdImage = image.thresholdHSI(108, 141, 219, 255, 125, 255);       //keep bright green10
            BinaryImage bigObjectsImage = thresholdImage.removeSmallObjects(true, 2);      // remove small artifacts
            BinaryImage convexHullImage = bigObjectsImage.convexHull(true);                  // fill in occluded rectangles
            //BinaryImage filteredImage = bigObjectsImage.particleFilter(cc);                 // find filled in rectangles

            reports = convexHullImage.getOrderedParticleAnalysisReports();  // get list of results
            int lowestIndex = 0;
            int highestIndex = 0;

            if(reports.length > 0){
                    lowestReport = reports[0];
                    highestReport = reports[0];
            }
            if(reports.length > 4){
                checkSize = true;
            }
            for (int i = 0; i < reports.length; i++) {
                ParticleAnalysisReport r = reports[i];
                if(r.center_mass_y > reports[lowestIndex].center_mass_y){
                    lowestIndex = i;
                    lowestReport = r;  
                }
                if(r.center_mass_y < reports[highestIndex].center_mass_y){
                highestIndex = i;
                highestReport = r;
                }
            }
            lastImageTime = Timer.getFPGATimestamp();
            beginCalc = true;//ready to process images
            
            //write then free images
            System.out.println("Saving images");
            image.write("../../images/orig" + Timer.getFPGATimestamp() + ".jpg");
            convexHullImage.write("../../images/cvhi" + Timer.getFPGATimestamp() + ".jpg");
            Log.getInstance().write("Just attemped to save images at: " + Timer.getFPGATimestamp());
            
            image.free();
            thresholdImage.free();
            bigObjectsImage.free();
            convexHullImage.free();
            //filteredImage.free();
        }
    }
    
    /*
     * Return the x coordinate of the center of the target.
     */
    public int getTargetCenterX(){
	if(searchHigh){
		if(highestReport != null){
			return highestReport.center_mass_x;
		}else{
			return camera.getResolution().width/2;
		}
	}else{
	        if(lowestReport != null){
       			return lowestReport.center_mass_x;
        	}else{
            		return camera.getResolution().width/2;
        	}
	}
    }
    
    /*
     * Return the y coordinate of the center of the target.
     */
    public int getTargetCenterY(){
	if(searchHigh){
		if(highestReport != null){
			return highestReport.center_mass_y;
		}else{
			return camera.getResolution().height/2;
		}
	}else{
	        if(lowestReport != null){
       			return lowestReport.center_mass_y;
        	}else{
            		return camera.getResolution().height/2;
        	}
	}
    }
    
    /*
     * Return the width of the target, in pixels
     */
    public int getTargetWidth(){
	if(searchHigh){
		if(highestReport != null){
			return highestReport.boundingRectWidth;
		}else{
			return 10;
		}
	}else{
	        if(lowestReport != null){
       			return lowestReport.boundingRectWidth;
        	}else{
            		return 10; 
        	}
	}
    }
    
    /*
     * Return the height of the target, in pixels
     */
    public int getTargetHeight(){
	if(searchHigh){
		if(highestReport != null){
			return highestReport.boundingRectHeight;
		}else{
			return 10;
		}
	}else{
	        if(lowestReport != null){
       			return lowestReport.boundingRectHeight;
        	}else{
            		return 10; 
        	}
	}
    }
    
    /*
     * Return the area of the target, in pixels
     */
    public double getTargetArea(){
	if(searchHigh){
		if(highestReport != null){
			return highestReport.particleArea;
		}else{
			return 10;
		}
	}else{
	        if(lowestReport != null){
       			return lowestReport.particleArea;
        	}else{
            		return 10; 
        	}
	}
    }
    
    /*
     * Return the distance to the center of the target, in cm.
     */
    public double getDistance(){
        ParticleAnalysisReport report = null;
        if(searchHigh){
            report = highestReport;
        }else{
            report = lowestReport;
        }
        if(beginCalc && report != null){
            double targetHeightMeters=.4572;    					//18in in meters
            double targetHeightPixels=lowestReport.boundingRectHeight;
            double fieldHeightMeters;           					//Determined with mathematics
            double fieldHeightPixels= camera.getResolution().height;
            double theta = Math.toRadians(Constants.CAM_FOV_ANGLE);                	//Axis M1011 angle/2
            double distance;

            fieldHeightMeters = (targetHeightMeters * fieldHeightPixels)/targetHeightPixels;

            fieldHeightMeters /= 2;         //Half the height, for right triangle

            distance = (fieldHeightMeters)/Math.tan(theta);

            //TODO: Check for an equation
            double fixedDistance = distance;
            fixedDistance *= 100; //to centimeters
            return fixedDistance;
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
        double x = ((getTargetCenterX() + (getTargetHeight()/.4572)*.245) -320) *.55555;
        if(d != 0){
            return MathUtils.atan(x/d) * 57.2957795;    //to degrees
        }else{
            return 0;
        }
    }
    
    /*
     * Return the current resolution setting of the camera.
     */
    public AxisCamera.ResolutionT getResolution(){
        return camera.getResolution();
    }
    
    /*
     * Return the center pixel, for use in pid.
     */
    public double pidGet() {
        double x = getHeading();
        return x;
    }
}
