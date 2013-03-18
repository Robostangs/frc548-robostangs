package com.robostangs;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DigitalInput;

public class ProximitySensor extends DigitalInput {
    private Solenoid powerSource;

    public ProximitySensor(int digitalPort, int solenoidPort) {
        super(digitalPort);
        powerSource = new Solenoid(1, solenoidPort);
        powerSource.set(true);  //give the prox sensor power
    }
}

