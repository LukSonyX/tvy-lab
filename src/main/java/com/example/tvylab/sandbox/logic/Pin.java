package com.example.tvylab.sandbox.logic;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.List;

public class Pin {

    private final BooleanProperty state = new SimpleBooleanProperty(false);
    private final List<Pin> connections = new ArrayList<>();
    private final PinType type;
    private Gate parentGate;

    public Pin(PinType type) {
        this.type = type;
    }

    public void setParentGate(Gate parentGate) {
        this.parentGate = parentGate;
    }

    public PinType getType() {
        return type;
    }

    public boolean isInput() {
        return type == PinType.INPUT;
    }

    public boolean isOutput() {
        return type == PinType.OUTPUT;
    }

    public boolean isSource() {
        return type == PinType.SOURCE;
    }

    public void toggle() {
        if (type == PinType.SOURCE) {
            setState(!getState());
        }
    }

    public boolean connectTo(Pin other) {
        if (this == other) return false;
        if (this.parentGate == other.parentGate) return false;

        Pin from;
        Pin to;

        if ((this.isOutput() || this.isSource()) && other.isInput()) {
            from = this;
            to = other;
        } else if ((other.isOutput() || other.isSource()) && this.isInput()) {
            from = other;
            to = this;
        } else {
            return false;
        }

        if (!from.connections.contains(to)) {
            from.connections.add(to);
        }
        if (!to.connections.contains(from)) {
            to.connections.add(from);
        }
        to.receiveState(from.getState());
        return true;
    }

    public void disconnectFrom(Pin other) {
        connections.remove(other);
        other.connections.remove(this);

        if (other.isInput()) {
            other.receiveState(false);
        }
    }

    public void disconnectAll() {
        for (Pin pin : new ArrayList<>(connections)) {
            disconnectFrom(pin);
        }
    }

    public BooleanProperty stateProperty() {
        return state;
    }

    public boolean getState() {
        return state.get();
    }

    public void setState(boolean newState) {
        if (state.get() == newState) return;

        state.set(newState);

        if (isOutput() || isSource()) {
            for (Pin pin : connections) {
                if (pin.isInput()) {
                    pin.receiveState(newState);
                }
            }
        }
    }

    private void receiveState(boolean newState) {
        if (!isInput()) return;

        if (state.get() == newState) return;

        state.set(newState);

        if (parentGate != null) {
            parentGate.onInputChanged();
        }
    }
}