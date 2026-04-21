package com.example.tvylab.sandbox.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomGate extends Gate {
    private final Map<String, List<Boolean>> table;

    public CustomGate(String name, int inputs, int outputs,
                          Map<String, List<Boolean>> table) {
        setupPins(inputs, outputs);
        this.name = name;
        this.table = table;
        compute();
    }

    @Override
    protected void compute() {
        StringBuilder key = new StringBuilder();

        for (Pin p : inputPins) {
            key.append(p.getState() ? "1" : "0");
        }

        List<Boolean> results = table.getOrDefault(key.toString(), new ArrayList<>());

        for (int i = 0; i < outputPins.size(); i++) {
            boolean value = i < results.size() && results.get(i);
            outputPins.get(i).setState(value);
        }
    }
}