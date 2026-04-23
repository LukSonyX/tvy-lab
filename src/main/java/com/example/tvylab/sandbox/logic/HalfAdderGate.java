package com.example.tvylab.sandbox.logic;

public class HalfAdderGate extends Gate {
    public HalfAdderGate() {
        this.name = "Half Adder";
        setupPins(2, 2);
        compute();
    }

    @Override
    protected void compute() {
        boolean a = inputPins.get(0).getState();
        boolean b = inputPins.get(1).getState();

        outputPins.get(0).setState(a ^ b);
        outputPins.get(1).setState(a && b);
    }
}