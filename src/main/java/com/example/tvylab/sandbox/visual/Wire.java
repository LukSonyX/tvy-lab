package com.example.tvylab.sandbox.visual;

import javafx.geometry.Point2D;
import javafx.scene.shape.Line;

import java.awt.*;

public class Wire extends Line {
    private final PinNode connectedFrom;
    private final PinNode connectedTo;

    public Wire(PinNode connectedFrom, PinNode connectedTo) {
        this.connectedFrom = connectedFrom;
        this.connectedTo = connectedTo;

        setStrokeWidth(5);
        strokeProperty().bind(connectedFrom.fillProperty());
    }


    public void update() {
        Point2D findStartPos = connectedFrom.localToScene(0, 0);
        Point2D findEndPos = connectedTo.localToScene(0,0);

        Point2D startPos = sceneToLocal(findStartPos);
        Point2D endPos = sceneToLocal(findEndPos);

        this.setStartX(startPos.getX());
        this.setStartY(startPos.getY());
        this.setEndX(endPos.getX());
        this.setEndY(endPos.getY());
    }

    public void disconnect() {
        connectedFrom.getLogic().disconnectFrom(connectedTo.getLogic());
        connectedTo.getLogic().disconnectFrom(connectedFrom.getLogic());

        connectedTo.removeWire(this);
        connectedFrom.removeWire(this);
    }

    public PinNode getConnectedFrom() {
        return connectedFrom;
    }

    public PinNode getConnectedTo() {
        return connectedTo;
    }
}
