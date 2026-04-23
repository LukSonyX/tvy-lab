package com.example.tvylab.sandbox.logic;

public class SevenSegmentLogic extends Gate {
    public SevenSegmentLogic() {
        this.name = "7-Segment Display";
        setupPins(7, 0);
    }

    @Override
    protected void compute() {
    }
}