package com.example.tvylab.sandbox;

import com.example.tvylab.Launcher;
import com.example.tvylab.sandbox.logic.Pin;
import com.example.tvylab.sandbox.visual.LogicItem;
import com.example.tvylab.sandbox.visual.PinNode;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

import java.io.IOException;

public class SandboxSpace {
    // Load/Save schematic implementation
    // Sandbox area for playing around
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
    private VBox menuBox;

    @FXML
    public void initialize() {
        menuBox.getStyleClass().add("sandbox");
        sandboxPane.setStyle("-fx-background-color: gray;");

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
        pins.setOnAction(e -> {
            onComboBoxSelect(pins);
        });

        Tooltip toolt = new Tooltip("what");
        pins.setTooltip(toolt);


        Pin pint = new Pin(true);
        Pin pinus = new Pin(false);
        PinNode pinNodet = new PinNode(pint, "temp");
        PinNode pinda = new PinNode(pinus,  "temp");
        pinNodet.setLayoutX(50);
        pinNodet.setLayoutY(50);

        pinda.setLayoutX(100);
        pinda.setLayoutY(100);


        sandboxPane.setOnMouseClicked(this::handleMouseClick);
        sandboxPane.setOnMouseDragged(this::handleMouseDrag);
        sandboxPane.setOnMouseMoved(this::handleMouseMove);

        sandboxPane.getChildren().add(pinNodet);
        sandboxPane.getChildren().add(pinda);

    }

    private void handleMouseClick(MouseEvent e) {
        if (wasDragged) {
            wasDragged = false;
            return;
        };

        if (e.getButton() == MouseButton.PRIMARY) {
            System.out.println(e.getTarget());
            if (e.getTarget() instanceof PinNode inputPin) {
                inputPin.getLogic().toggle();
            }
            else if (selectedItem instanceof PinNode node){
                node.setLayoutX(e.getX());
                node.setLayoutY(e.getY());
                sandboxPane.getChildren().add(node);
                selectedItem = null;
            }
        }
        if (e.getButton() == MouseButton.SECONDARY) {
            if (e.getTarget() instanceof PinNode connectTo) {
                connectTo.setStroke(Paint.valueOf("orange"));
                connectTo.setStrokeWidth(5);
                if (connectFrom == null) {
                    connectFrom = connectTo;
                    ghostWire.startXProperty().bind(connectFrom.layoutXProperty());
                    ghostWire.startYProperty().bind(connectFrom.layoutYProperty());
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
        if (e.getTarget() instanceof PinNode node && e.getButton() == MouseButton.PRIMARY) {
            wasDragged = true;
            node.setLayoutX(e.getX());
            node.setLayoutY(e.getY());
            e.consume();
        }
    }

    private void handleMouseMove(MouseEvent e) {
        if (ghostWire != null) {
            ghostWire.setEndX(e.getX());
            ghostWire.setEndY(e.getY());
        }
    }

    public void onComboBoxSelect(ComboBox<String> comboBox) {
        // Platform runlate - protože ihned po zapnutí funkce a vykonání příkazu clearSelection se spustí funkce znovu s Null
        String selection = comboBox.getSelectionModel().getSelectedItem();
        if (selection == null) return;

        selectedItem = switch (selection) {
            case "1-Input"  -> new PinNode(new Pin(true), "INPUT");
            case "1-Output" -> new PinNode(new Pin(false), "OUTPUT");
            default -> null;
        };

        Platform.runLater(() -> {
            comboBox.getSelectionModel().clearSelection();
        });

        System.out.println(selection);
        System.out.println(selectedItem);

    }

    private void generateWire(PinNode start, PinNode end) {
        Line wire = new Line();
        wire.strokeProperty().bind(end.fillProperty());
        wire.setStrokeWidth(5);
        wire.startXProperty().bind(start.layoutXProperty());
        wire.startYProperty().bind(start.layoutYProperty());
        wire.endXProperty().bind(end.layoutXProperty());
        wire.endYProperty().bind(end.layoutYProperty());

        sandboxPane.getChildren().add(0, wire);
    }
}
