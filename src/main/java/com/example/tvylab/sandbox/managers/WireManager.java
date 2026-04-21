package com.example.tvylab.sandbox.managers;

import com.example.tvylab.sandbox.visual.PinNode;
import com.example.tvylab.sandbox.visual.Wire;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

public class WireManager {
    private final Pane zoomGroup;
    private final Line ghostWire;
    private PinNode connectFrom;

    public WireManager(Pane zoomGroup, Line ghostWire) {
        this.zoomGroup = zoomGroup;
        this.ghostWire = ghostWire;
    }

    public void handleRightClick(PinNode connectTo, Point2D local) {
        if (connectTo == null) {
            cancel();
            return;
        }

        connectTo.setStroke(Paint.valueOf("orange"));
        connectTo.setStrokeWidth(5);

        if (connectFrom == null) {
            connectFrom = connectTo;
            Point2D pinPos = getPinPosition(connectTo);
            ghostWire.setStartX(pinPos.getX());
            ghostWire.setStartY(pinPos.getY());
            ghostWire.setEndX(local.getX());
            ghostWire.setEndY(local.getY());
            ghostWire.setVisible(true);
        } else {
            ghostWire.setVisible(false);
            if (connectFrom.getLogic().connectTo(connectTo.getLogic())) {
                generateWire(connectFrom, connectTo);
            }
            connectFrom.setStroke(null);
            connectTo.setStroke(null);
            connectFrom = null;
        }
    }

    public void updateGhostWire(Point2D local) {
        if (ghostWire.isVisible()) {
            ghostWire.setEndX(local.getX());
            ghostWire.setEndY(local.getY());
        }
    }

    private void generateWire(PinNode start, PinNode end) {
        Wire wire = new Wire(start, end);
        start.addWire(wire);
        end.addWire(wire);
        start.localToSceneTransformProperty().addListener((o, oldV, newV) -> wire.update());
        end.localToSceneTransformProperty().addListener((o, oldV, newV) -> wire.update());
        zoomGroup.getChildren().add(0, wire);
        wire.update();
    }

    private Point2D getPinPosition(PinNode pin) {
        Point2D scene = pin.localToScene(0, 0);
        return zoomGroup.sceneToLocal(scene);
    }

    public void cancel() {
        ghostWire.setVisible(false);
        if (connectFrom != null) {
            connectFrom.setStroke(null);
            connectFrom = null;
        }
    }

}
