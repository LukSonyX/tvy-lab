package com.example.tvylab.sandbox.bs.logic;

import javafx.beans.property.SimpleBooleanProperty;

public class Pin {
    private final SimpleBooleanProperty active = new SimpleBooleanProperty(false);
    public void setActive(boolean value) { this.active.set(value); }
    public boolean isActive() { return active.get(); }
    public SimpleBooleanProperty activeProperty() { return active; }
}
