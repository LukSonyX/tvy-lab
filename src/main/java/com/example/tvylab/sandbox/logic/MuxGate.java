package com.example.tvylab.sandbox.logic;

public class MuxGate extends Gate {
    public MuxGate() {
        this.name = "MUX 2:1";
        setupPins(3, 1);
        compute();
    }

    @Override
    protected void compute() {
        boolean a = inputPins.get(0).getState();
        boolean b = inputPins.get(1).getState();
        boolean sel = inputPins.get(2).getState();

        outputPins.get(0).setState(sel ? b : a);
    }
}