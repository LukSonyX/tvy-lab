package com.example.tvylab.sandbox.bs.logic;

import java.util.List;

public abstract class Gate {
    List<Pin> outputs;
    List<Pin> inputs;

    public abstract void evaluate();

    public List<Pin> getInputs() {
        return inputs;
    }

    public List<Pin> getOutputs() {
        return outputs;
    }
}
