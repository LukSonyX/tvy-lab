package com.example.tvylab.sandbox.logic;

import com.example.tvylab.sandbox.visual.LogicItem;

public class BufferGate extends Gate implements LogicItem {

    public BufferGate() {
        this.name = "Buffer";
        setupPins(1, 1);
        compute();
    }

    @Override
    protected void compute() {
        Pin input = inputPins.get(0);
        Pin output = outputPins.get(0);

        output.setState(input.getState());
    }

    @Override
    public String getName() {
        return this.name;
    }
}
