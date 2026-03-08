package com.example.tvylab.sandbox.logic;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;

public class Pin {
    SimpleBooleanProperty isOn = new SimpleBooleanProperty(false);
    ArrayList<Pin> connectedTo = new ArrayList<>();
    private final boolean isInput;

    public Pin(boolean isInput) {
        this.isInput = isInput;
    }

    public void toggle() {
        if (isInput) {
            this.isOn.set(!this.isOn.get());

            for (Pin connected : connectedTo) {
                connected.setState(this.isOn.get());
            }
        }
    }

    public boolean connectTo(Pin pin) {
        if (this.isInput == pin.isInput) return false;

        if (this.isInput) {
            if (!connectedTo.contains(pin)) {
                connectedTo.add(pin);
                this.setState(pin.getState());
            }
        }
        else {
            if (!pin.connectedTo.contains(this)) {
                pin.connectedTo.add(this);
                pin.setState(this.getState());
            }
        }
        return true;

    }

    public BooleanProperty isOnProperty() {
        return this.isOn;
    }

    public boolean getState() {
        return this.isOn.get();
    }

    public void setState(boolean state) {
        if (this.isOn.get() != state) {
            this.isOn.set(state);

            for (Pin pin : connectedTo) {
                pin.setState(state);
            }
        }
    }
}
