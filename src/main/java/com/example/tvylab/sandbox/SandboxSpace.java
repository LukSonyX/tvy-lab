package com.example.tvylab.sandbox;

import com.example.tvylab.Launcher;
import com.example.tvylab.sandbox.logic.NandGate;
import com.example.tvylab.sandbox.logic.Pin;
import com.example.tvylab.sandbox.visual.GateNode;
import com.example.tvylab.sandbox.visual.LogicItem;
import com.example.tvylab.sandbox.visual.PinNode;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

import java.io.IOException;

public class SandboxSpace {

    public void onBackPressed() throws IOException { Launcher.changeScene("main-menu.fxml"); }

    @FXML
    private Pane sandboxPane;
    Line ghostWire = new Line();
    boolean wasDragged = false;

    LogicItem selectedItem;
    PinNode connectFrom;

    @FXML
    private ComboBox<String> pins;
    @FXML
    private ComboBox<String> gates;
    @FXML
    private VBox menuBox;
    @FXML
    private CheckBox deleteToggle;

    @FXML
    public void initialize() {
        sandboxPane.setStyle("-fx-background-color: gray;");
        menuBox.getStyleClass().add("sandbox");

        ghostWire.setVisible(false);
        ghostWire.setStroke(Paint.valueOf("red"));
        ghostWire.setStrokeWidth(5);

        sandboxPane.getChildren().add(ghostWire);

        pins.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText("IO");
            }
        });

        pins.setPromptText("IO");
        pins.getItems().addAll("1-Input", "1-Output");
        pins.setOnAction(e -> onComboBoxSelect(pins));

        gates.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText("GATES");
            }
        });

        gates.setPromptText("GATES");
        gates.getItems().addAll("NAND");
        gates.setOnAction(e -> onComboBoxSelect(gates));

        sandboxPane.setOnMouseClicked(this::handleMouseClick);
        sandboxPane.setOnMouseDragged(this::handleMouseDrag);
        sandboxPane.setOnMouseMoved(this::handleMouseMove);
    }

    private void handleMouseClick(MouseEvent e) {
        if (wasDragged) {
            wasDragged = false;
            return;
        }

        Node item = findTopItem((Node) e.getTarget());

        if (e.getButton() == MouseButton.PRIMARY) {
            if (deleteToggle.isSelected()) {
                Node nodeToDelete = findTopItem((Node) e.getTarget());
                sandboxPane.getChildren().remove(nodeToDelete);
            }
            if (item instanceof PinNode inputPin) {
                inputPin.getLogic().toggle();
            }
            else if (selectedItem instanceof Node node){
                node.setLayoutX(e.getX());
                node.setLayoutY(e.getY());
                sandboxPane.getChildren().add(node);
                selectedItem = null;
            }
        }

        if (e.getButton() == MouseButton.SECONDARY) {
            PinNode connectTo = findPin((Node) e.getTarget());

            if (connectTo != null) {
                connectTo.setStroke(Paint.valueOf("orange"));
                connectTo.setStrokeWidth(5);

                if (connectFrom == null) {
                    connectFrom = connectTo;

                    // FIX: correct start position
                    Point2D p = getPinPosition(connectFrom);
                    ghostWire.setStartX(p.getX());
                    ghostWire.setStartY(p.getY());

                    ghostWire.setEndX(e.getX());
                    ghostWire.setEndY(e.getY());
                    ghostWire.setVisible(true);
                }
                else {
                    ghostWire.setVisible(false);

                    if (connectFrom.getLogic().connectTo(connectTo.getLogic())) {
                        generateWire(connectFrom, connectTo);
                    }

                    connectFrom.setStroke(null);
                    connectTo.setStroke(null);
                    connectFrom = null;
                }
            }
            else {
                ghostWire.setVisible(false);
                if (connectFrom != null) {
                    connectFrom.setStroke(null);
                    connectFrom = null;
                }
            }
        }
    }

    private void handleMouseDrag(MouseEvent e) {
        Node item = findTopItem((Node) e.getTarget());
        if (item instanceof Line) return;

        if (e.getButton() == MouseButton.PRIMARY && item != null) {
            wasDragged = true;

            item.setLayoutX(e.getX());
            item.setLayoutY(e.getY());

            e.consume();
        }
    }

    private void handleMouseMove(MouseEvent e) {
        if (ghostWire != null) {
            ghostWire.setEndX(e.getX());
            ghostWire.setEndY(e.getY());
        }
    }

    private Node findTopItem(Node node) {
        if (node == sandboxPane) return null;
        while (node != null && node.getParent() != sandboxPane) {
            node = node.getParent();
        }
        return node;
    }

    private PinNode findPin(Node node) {
        while (node != null && !(node instanceof PinNode)) {
            node = node.getParent();
        }
        return (PinNode) node;
    }

    public void onComboBoxSelect(ComboBox<String> comboBox) {
        String selection = comboBox.getSelectionModel().getSelectedItem();
        if (selection == null) return;

        selectedItem = switch (selection) {
            case "1-Input"  -> new PinNode(new Pin(true), "INPUT");
            case "1-Output" -> new PinNode(new Pin(false), "OUTPUT");
            case "NAND" -> new GateNode(new NandGate());
            default -> null;
        };

        Platform.runLater(() -> comboBox.getSelectionModel().clearSelection());
    }

    private void generateWire(PinNode start, PinNode end) {
        Line wire = new Line();
        wire.strokeProperty().bind(end.fillProperty());
        wire.setStrokeWidth(5);

        Runnable update = () -> {
            Point2D p1 = getPinPosition(start);
            Point2D p2 = getPinPosition(end);

            wire.setStartX(p1.getX());
            wire.setStartY(p1.getY());
            wire.setEndX(p2.getX());
            wire.setEndY(p2.getY());
        };

        start.localToSceneTransformProperty().addListener((obs, o, n) -> update.run());
        end.localToSceneTransformProperty().addListener((obs, o, n) -> update.run());

        update.run();

        sandboxPane.getChildren().add(0, wire);
    }

    private Point2D getPinPosition(PinNode pin) {
        Point2D scene = pin.localToScene(0, 0);
        return sandboxPane.sceneToLocal(scene);
    }
}