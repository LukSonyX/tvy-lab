package com.example.tvylab.sandbox.bs.logic;

import java.util.List;

public class AndGate extends Gate {
    public AndGate() {
        inputs = List.of(new Pin(), new Pin());
        outputs = List.of(new Pin());
    }

    @Override
    public void evaluate() {
        outputs.get(0).setActive(inputs.get(0).isActive() && inputs.get(1).isActive());
    }
}
