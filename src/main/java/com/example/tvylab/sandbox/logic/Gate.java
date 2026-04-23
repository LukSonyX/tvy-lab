package com.example.tvylab.sandbox.logic;

import java.util.ArrayList;
import java.util.List;

public abstract class Gate {

    protected String name;
    protected List<Pin> inputPins = new ArrayList<>();
    protected List<Pin> outputPins = new ArrayList<>();

    public Gate() {
    }

    protected void setupPins(int inCount, int outCount) {
        inputPins.clear();
        outputPins.clear();

        for (int i = 0; i < inCount; i++) {
            Pin p = new Pin(PinType.INPUT);
            p.setParentGate(this);
            inputPins.add(p);
        }

        for (int i = 0; i < outCount; i++) {
            Pin p = new Pin(PinType.OUTPUT);
            p.setParentGate(this);
            outputPins.add(p);
        }
    }

    protected abstract void compute();

    public void onInputChanged() {
        compute();
    }

    public void update() {
        compute();
    }

    public List<Pin> getOutputPins() {
        return outputPins;
    }

    public List<Pin> getInputPins() {
        return inputPins;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}