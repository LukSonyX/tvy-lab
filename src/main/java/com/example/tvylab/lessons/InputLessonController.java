package com.example.tvylab.lessons;

import com.example.tvylab.sandbox.logic.BufferGate;
import com.example.tvylab.sandbox.logic.Gate;
import com.example.tvylab.sandbox.logic.Pin;
import com.example.tvylab.sandbox.managers.WireManager;
import com.example.tvylab.sandbox.visual.PinNode;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class InputLessonController extends BaseLessonController {

    @FXML
    private Pane simulationPane;

    @FXML
    private ToggleGroup quizGroup;

    private WireManager wireManager;

    public void initialize() {
        Gate invisibleGate = new BufferGate();

        Line ghostWire = new Line();
        ghostWire.setVisible(false);
        simulationPane.getChildren().add(ghostWire);

        wireManager = new WireManager(simulationPane, ghostWire);

        PinNode input = new PinNode(new Pin(true), "Vstup", 0);
        input.setTranslateX(80);
        input.setTranslateY(120);
        input.setTextColor("black");

        input.getLogic().setParentGate(invisibleGate);

        PinNode output = new PinNode(new Pin(false), "Výstup", 1);
        output.setTranslateX(250);
        output.setTranslateY(120);
        output.setTextColor("black");

        input.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                input.getLogic().toggle();
            }
            else {
                wireManager.handleRightClick(input, new Point2D(e.getX(), e.getY()));
            }
        });
        output.setOnMouseClicked(e -> wireManager.handleRightClick(output, new Point2D(e.getX(), e.getY())));

        simulationPane.setOnMouseMoved(e ->
                wireManager.updateGhostWire(new Point2D(e.getX(), e.getY()))
        );

        simulationPane.getChildren().addAll(input, output);
    }

    @FXML
    private void onCheckAnswer() {
        RadioButton selected = (RadioButton) quizGroup.getSelectedToggle();

        if (selected == null) return;

        if (selected.getText().equals("OUTPUT → INPUT")) {
            System.out.println("Správně!");
        } else {
            System.out.println("Špatně!");
        }
    }
}