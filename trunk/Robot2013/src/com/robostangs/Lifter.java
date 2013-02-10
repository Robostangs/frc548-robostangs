package com.robostangs;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 * Lifter class
 * maintainer: Sam 
 * TODO: lifter: timers to always fully raise/lower lift
 */

public class Lifter {
  private static Lifter instance = null;
  private static CANJaguar lift;

  private Lifter() { 
      try{
        lift = new CANJaguar(Constants.LIFTER_JAG_POS);
      } catch (CANTimeoutException ex) {
          ex.printStackTrace();
      }
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
  public static void enable() {
        try {
            lift.setX(Constants.LIFTER_POWER);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
  }

  /**
   * Lifter goes down
   */
  public static void reverse() {
        try {
            lift.setX(-Constants.LIFTER_POWER);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
  }

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

}
