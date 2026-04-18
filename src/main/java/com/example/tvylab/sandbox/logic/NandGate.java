package com.example.tvylab.sandbox.logic;

import com.example.tvylab.sandbox.visual.LogicItem;

public class NandGate extends Gate implements LogicItem {

    public NandGate() {
        this.name = "NAND";
        setupPins(2, 1);
        compute();
    }

    @Override
    protected void compute() {
        boolean result = false;
        for (Pin input : inputPins) {
            if (!input.getState()) {
                result = true;
                break;
            }
        }

        for (Pin output : outputPins) {
            output.setState(result);
        }
    }

    @Override
    public String getName() {
        return this.name;
    }
}