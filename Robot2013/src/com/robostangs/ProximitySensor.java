package com.robostangs;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DigitalInput;

public class ProximitySensor extends DigitalInput{
    private Solenoid power;

    public ProximitySensor(int digitalPort, int solenoidPort) {
        super(digitalPort);
        power = new Solenoid(solenoidPort);
        turnOn();
    }

    public void turnOn() {
        power.set(true);
    }

    public void turnOff() {
        power.set(false);
    }
}

