package com.robostangs;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 * Lifter class
 * maintainer: Sam 
 */

public class Lifter {
    private static Lifter instance = null;
    private static CANJaguar lift;
    private static Timer timer;
    private static boolean atTop;
    private static boolean atBottom;
    private static boolean goingToTop;
    private static boolean goingToBottom;
    private static int downcount = 0;
    //private static ProximitySensor topProx;
    //private static ProximitySensor bottomProx;

    private Lifter() { 
        try{
            lift = new CANJaguar(Constants.LIFTER_JAG_POS);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        timer = new Timer();
        timer.stop();
        timer.reset();
        /*
        topProx = new ProximitySensor(Constants.LIFTER_TOP_PROX_DIGITAL_PORT, 
                  Constants.LIFTER_TOP_PROX_SOLENOID_PORT);
        bottomProx = new ProximitySensor(Constants.LIFTER_BOTTOM_PROX_DIGITAL_PORT, 
                  Constants.LIFTER_BOTTOM_PROX_SOLENOID_PORT);
        */
        atTop = false;
        atBottom = true;
        goingToBottom = false;
        goingToTop = false;
    }
  
    public static Lifter getInstance() {
        if (instance == null) {
            instance = new Lifter();
        }
        return instance;
    }

   /**
    * Lifter goes up
    */
    public static void raise() {
        try {
            lift.setX(Constants.LIFTER_UP_POWER);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

   /**
    * Lifter goes down
    */
    public static void lower() {
        try {
            lift.setX(-Constants.LIFTER_DOWN_POWER);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public static void constantDown() {
        try {
            lift.setX(-0.15);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @return the absolute value of the current on the lifter jag
     */
    public static double getJagCurrent() {
        try {
            return Math.abs(lift.getOutputCurrent());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public static void currentDown() {
        if(atTop) {
          atTop = false;
          lower();
        }

        System.out.println("jag current: " + getJagCurrent());
        if ((getJagCurrent() >= 1.7 && !atBottom) || downcount < 20) {
          lower();
          goingToBottom = true;
            downcount++;
        } else {
          stop();
          atBottom = true;
          goingToBottom = false;
          downcount = 0;
        }

    }

  public static void timedDown() {
      if(atTop) {
          timer.stop();
          timer.reset();
          timer.start();
          atTop = false;
      }

      if (timer.get() <= Constants.LIFTER_DOWN_TIME && !atBottom) {
          lower();
          goingToBottom = true;
      } else {
          stop();
          atBottom = true;
          goingToBottom = false;
      }
  }


  public static void timedUp() {
      if (atBottom) {
          timer.stop();
          timer.reset();
          timer.start();
          atBottom = false;
      }

      if (timer.get() <= Constants.LIFTER_UP_TIME && !atTop) {
          goingToTop = true;
          raise();
      } else {
          timer.stop();
          stop();
          atTop = true;
          goingToTop = false;
      }
  }

  /*
  public static boolean getTopSensor() {
      return topProx.get();
  }

  public static boolean getBottomSensor() {
      return bottomProx.get();
  }

  public static void sensorUp() {
      if (topProx.get()) {
          raise();
      } else {
          stop();
      }
  }

  public static void sensorDown() { 
      if (bottomProx.get()) {
          lower();
      } else {
          stop();
      }
  } */

  /**
   * Stops the lifter
   */
  public static void stop() {
    try {
        lift.setX(0.0);
    } catch (CANTimeoutException ex) {
        ex.printStackTrace();
    }
  }

  /**
   * @return true if at top, false if at bottom
   */
  public static boolean atBottom() {
      return atBottom;
  }

  public static void manual(double speed) {
      timer.stop();
      if (goingToBottom || atBottom) {
          atTop = false;
      } else if (goingToTop || atTop) {
          atBottom = false;
      }
      try {
          lift.setX(speed);
      } catch (CANTimeoutException ex) {
          ex.printStackTrace();
      }
  }

  public static double getSetSpeed() {
      try {
          return lift.getX();
      } catch (CANTimeoutException ex) {
          ex.printStackTrace();
      } finally {
          return 0;
      }
  }

}
