package com.example.tvylab.sandbox;
import java.util.List;
import java.util.Map;

public class GateDefinition {
    public String name;
    public int inputs;
    public int outputs;
    public Map<String, List<Boolean>> table;

    public GateDefinition() {}

    public GateDefinition(String name, int inputs, int outputs,
                          Map<String, List<Boolean>> table) {
        this.name = name;
        this.inputs = inputs;
        this.outputs = outputs;
        this.table = table;
    }
}