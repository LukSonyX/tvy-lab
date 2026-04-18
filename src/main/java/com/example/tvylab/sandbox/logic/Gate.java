package com.example.tvylab.sandbox.logic;

import java.util.ArrayList;
import java.util.List;

public abstract class Gate {
    public String name;
    protected ArrayList<Pin> inputPins = new ArrayList<>();
    protected ArrayList<Pin> outputPins = new ArrayList<>();

    public Gate() {
    }

    protected void setupPins(int inCount, int outCount) {
        for (int i = 0; i < inCount; i++) {
            Pin p = new Pin(true);
            p.setParentGate(this);
            inputPins.add(p);
        }
        for (int i = 0; i < outCount; i++) {
            Pin p = new Pin(false);
            p.setParentGate(this);
            outputPins.add(p);
        }
    }

    protected abstract void compute();

    public void update() {
        compute();
    }

    public List<Pin> getOutputPins() { return outputPins; }
    public List<Pin> getInputPins() { return inputPins; }
}