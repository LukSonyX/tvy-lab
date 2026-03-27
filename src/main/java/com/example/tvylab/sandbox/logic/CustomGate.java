package com.example.tvylab.sandbox.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomGate extends Gate {
    private Map<String, List<Boolean>> table;

    public CustomGate(String name, int inputs, int outputs,
                          Map<String, List<Boolean>> table) {
        this.name = name;
        this.table = table;
        setupPins(inputs, outputs);
    }

    @Override
    protected void compute() {
        String key = "";

        for (Pin p : inputPins) {
            key += p.getState() ? "1" : "0";
        }

        List<Boolean> results = table.getOrDefault(key, new ArrayList<>());

        for (int i = 0; i < outputPins.size(); i++) {
            boolean value = i < results.size() && results.get(i);
            outputPins.get(i).setState(value);
        }
    }
}