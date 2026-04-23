package com.example.tvylab.sandbox.visual;

import com.example.tvylab.sandbox.logic.Gate;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.beans.binding.Bindings;

public class SevenSegmentNode extends Pane implements LogicItem {

    private final Gate logic;

    public SevenSegmentNode(Gate logic) {
        this.logic = logic;

        Rectangle segA = new Rectangle();
        Rectangle segB = new Rectangle();
        Rectangle segC = new Rectangle();
        Rectangle segD = new Rectangle();
        Rectangle segE = new Rectangle();
        Rectangle segF = new Rectangle();
        Rectangle segG = new Rectangle();

        segA.setX(15); segA.setY(5);   segA.setWidth(50); segA.setHeight(10);
        segB.setX(67); segB.setY(17);  segB.setWidth(10); segB.setHeight(50);
        segC.setX(67); segC.setY(79);  segC.setWidth(10); segC.setHeight(50);
        segD.setX(15); segD.setY(131); segD.setWidth(50); segD.setHeight(10);
        segE.setX(3);  segE.setY(79);  segE.setWidth(10); segE.setHeight(50);
        segF.setX(3);  segF.setY(17);  segF.setWidth(10); segF.setHeight(50);
        segG.setX(15); segG.setY(68);  segG.setWidth(50); segG.setHeight(10);

        Rectangle[] allSegments = {segA, segB, segC, segD, segE, segF, segG};

        for (int i = 0; i < 7; i++) {
            allSegments[i].setArcWidth(8);
            allSegments[i].setArcHeight(8);

            allSegments[i].fillProperty().bind(
                    Bindings.when(logic.getInputPins().get(i).stateProperty())
                            .then(Color.RED)
                            .otherwise(Color.web("#220000"))
            );
        }

        String[] letters = {"A", "B", "C", "D", "E", "F", "G"};

        for (int i = 0; i < 7; i++) {
            PinNode pinNode = new PinNode(logic.getInputPins().get(i), "Seg " + letters[i], i);
            pinNode.setSide(true);
            pinNode.setLayoutY(i * 22 + 12);
            pinNode.setLayoutX(-15);
            getChildren().add(pinNode);
        }

        getChildren().addAll(segA, segB, segC, segD, segE, segF, segG);

        setPrefWidth(85);
        setPrefHeight(150);

        setStyle("-fx-background-color: #A3A3A3; -fx-padding: 15; -fx-background-radius: 10;");
    }

    @Override
    public String getName() {
        return logic.getName();
    }

    public Gate getLogic() {
        return this.logic;
    }
}