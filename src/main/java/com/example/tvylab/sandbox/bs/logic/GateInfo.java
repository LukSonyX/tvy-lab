package com.example.tvylab.sandbox.bs.logic;

public class GateInfo {
    GateType type;
    String customName;
    byte inputCount;
    byte outputCount;
    double x, y;

    public GateInfo(GateType type, String customName, byte inputCount, byte outputCount, double x, double y) {
        this.type = type;
        this.customName = customName;
        this.inputCount = inputCount;
        this.outputCount = outputCount;
        this.x = x;
        this.y = y;
    }
}
