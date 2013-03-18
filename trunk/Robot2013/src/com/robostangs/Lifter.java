package com.robostangs;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 * Lifter class
 * maintainer: Sam 
 * TODO: lifter: timers to always fully raise/lower lift
 */

public class Lifter {
  private static Lifter instance = null;
  private static CANJaguar lift;
  private static Timer timer;
  private static boolean atTop;
  private static boolean atBottom;
  private static boolean goingToTop;
  private static boolean goingToBottom;

  private Lifter() { 
      try{
        lift = new CANJaguar(Constants.LIFTER_JAG_POS);
      } catch (CANTimeoutException ex) {
          ex.printStackTrace();
      }
      timer = new Timer();
      timer.stop();
      timer.reset();
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
            lift.setX(-Constants.LIFTER_UP_POWER);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
  }

  /**
   * Lifter goes down
   */
  public static void lower() {
    try {
        lift.setX(Constants.LIFTER_DOWN_POWER);
    } catch (CANTimeoutException ex) {
        ex.printStackTrace();
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
      if(atBottom) {
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

  public static void constantDown() {
      try {
          lift.setX(0.2);
      } catch (CANTimeoutException ex) {
          ex.printStackTrace();
      }
  }

  /**
   * Move the lifter from bottom to top using a timer
   * @return 0 if running, 1 if done 
   *
  public static void timedUp() {
      timer.start();
      while (timer.get() <= Constants.LIFTER_UP_TIME || atTop) {
          raise();
      }
	  if (timer.get() >= Constants.LIFTER_UP_TIME || atTop) {
		  stop();
		  timer.stop();
		  timer.reset();
		  atTop = true;
	  } else {
          raise();
          System.out.println("UP TIMER:" + timer.get());
      }
  }*/

  /**
   * Move the lifter from bottom to top using a timer
   * @return 0 if running, 1 if done 
   *
  public static void timedDown() {
      timer.start();
      while (timer.get() <= Constants.LIFTER_DOWN_TIME) {
          raise();
      }
	  if (timer.get() >= Constants.LIFTER_DOWN_TIME || !atTop) {
		  stop();
		  timer.stop();
		  timer.reset();
		  atTop = false;
	  } else {
          lower();
          System.out.println("DOWN TIMER:" + timer.get());
      }
  }*/

  /**
   * @return 0 if in progress, 1 if done 
   *
  public static int switchUp() {
      if (topSwitch.get()) {
          raise();
          return 0;
      } else {
          stop();
          return 1;
      }
  }
  */
  //TODO: implement
  /*
  public static void switchDown(double speed) {
      if (!bottomSwitch.get() && speed < 0) {
          stop();
      } else {
          try {
              lift.setX(speed);
          } catch (CANTimeoutException ex) {
              ex.printStackTrace();
          }
      }
  }*/

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
