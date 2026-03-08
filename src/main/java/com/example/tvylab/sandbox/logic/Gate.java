package com.example.tvylab.sandbox.logic;

import java.util.ArrayList;
import java.util.List;

public abstract class Gate {
    public String name;
    protected List<Pin> inputPins = new ArrayList<>();
    protected List<Pin> outputPins;

    public Gate() {
    }

    protected abstract void compute();

    public void update() {
        compute();
    }

    public List<Pin> getOutputPins() { return outputPins; }
    public List<Pin> getInputPins() { return inputPins; }
}