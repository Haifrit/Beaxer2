package com.example.jwolter.beaxernovcs;

import java.math.BigDecimal;

/**
 * Ein MyBleDevice sollte immer ein Beacon sein.
 * In dieser Datenstrucktur wird die eindeutige Addresse des Beacons und die Signalstärke gespeichert
 */
public class MyBleDevice {

    private BigDecimal signal_strenght;
    private String adress;

    public MyBleDevice(BigDecimal signal_strenght, String adress) {
        this.signal_strenght = signal_strenght;
        this.adress = adress;
    }

    public BigDecimal getSignal_strenght() {
        return signal_strenght;
    }

    public String getAdress() {
        return adress;
    }
}
