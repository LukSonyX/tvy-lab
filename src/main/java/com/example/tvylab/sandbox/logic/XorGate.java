package com.example.tvylab.sandbox.logic;

import com.example.tvylab.sandbox.visual.LogicItem;

public class XorGate extends Gate implements LogicItem {
    public XorGate() {
        this.name = "XOR";
        setupPins(2, 1);
        compute();
    }
    @Override
    protected void compute() {
        outputPins.get(0).setState(inputPins.get(0).getState() ^ inputPins.get(1).getState());
    }

    @Override
    public String getName() {
        return this.name;
    }
}
