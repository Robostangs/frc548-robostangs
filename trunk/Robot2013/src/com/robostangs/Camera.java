/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robostangs;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;

/**
 *
 * @author Robostangs
 */
public class Camera {
   private static AxisCamera cam;
   private static Camera instance = null;
   
   private Camera() {
       cam = AxisCamera.getInstance("10.5.48.19");
   }

   public static Camera getInstance() {
       if (instance == null) {
           instance = new Camera();
       }
       return instance;
   }

   public static void saveImage() {
       ColorImage image = null;
       try {
           image = cam.getImage();
           image.write("../../images" + Timer.getFPGATimestamp() + ".jpg");
           image.free();
       } catch (AxisCameraException ex) {
           ex.printStackTrace();
       } catch (NIVisionException ex) {
           ex.printStackTrace();
       }
   }
}
