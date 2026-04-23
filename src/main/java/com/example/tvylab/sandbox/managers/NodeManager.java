package com.example.tvylab.sandbox.managers;

import com.example.tvylab.sandbox.logic.Pin;
import com.example.tvylab.sandbox.visual.*;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class NodeManager {
    private final Pane zoomGroup;
    private LogicItem selectedItem;

    public NodeManager(Pane zoomGroup) {
        this.zoomGroup = zoomGroup;
    }

    public void setSelected(LogicItem item) {
        this.selectedItem = item;
    }

    public void tryPlace(Point2D local) {
        if (selectedItem instanceof Node node) {
            node.setLayoutX(local.getX());
            node.setLayoutY(local.getY());
            zoomGroup.getChildren().add(node);
            selectedItem = null;
        }
    }

    public void clearSandbox(Line ghostWire) {
        zoomGroup.getChildren().clear();
        zoomGroup.getChildren().add(ghostWire);
    }

    public boolean isSandboxEmpty() {
        for (Node node : zoomGroup.getChildren()) {
            if (node instanceof PinNode || node instanceof GateNode) {
                return false;
            }
        }
        return true;
    }

    public void updateAllGates() {
        for (Node node : zoomGroup.getChildren()) {
            if (node instanceof GateNode gateNode) {
                gateNode.getLogic().update();
            }
        }
    }

    public void delete(Node toDelete) {
        if (toDelete instanceof Wire wire) {
            wire.disconnect();
            zoomGroup.getChildren().remove(wire);
        }
        if (toDelete instanceof PinNode pin) {
            for (Wire wire : new ArrayList<>(pin.getWires())) {
                wire.disconnect();
                zoomGroup.getChildren().remove(wire);
            }
            pin.getLogic().disconnectAll();
            zoomGroup.getChildren().remove(pin);
        }
        if (toDelete instanceof GateNode gate) {
            for (Pin pin : gate.getLogic().getInputPins()) pin.disconnectAll();
            for (Pin pin : gate.getLogic().getOutputPins()) pin.disconnectAll();
            removeConnections(gate);
            zoomGroup.getChildren().remove(gate);
        }
        if (toDelete instanceof SevenSegmentNode segmentDisplay) {
            for (Pin pin : segmentDisplay.getLogic().getInputPins()) {
                pin.disconnectAll();
            }
            removeConnections(segmentDisplay);
            zoomGroup.getChildren().remove(segmentDisplay);
        }
    }

    private void removeConnections(Node item) {
        for (Node node : new ArrayList<>(zoomGroup.getChildren())) {
            if (node instanceof Wire wire) {
                if (wire.getConnectedFrom().getParent() == item ||
                        wire.getConnectedTo().getParent() == item) {

                    wire.disconnect();
                    zoomGroup.getChildren().remove(wire);
                }
            }
        }
    }
}