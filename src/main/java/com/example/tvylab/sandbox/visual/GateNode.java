package com.example.tvylab.sandbox.visual;

import com.example.tvylab.sandbox.logic.Gate;
import com.example.tvylab.sandbox.logic.Pin;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class GateNode extends Pane implements LogicItem {
    private final Gate logic;
    private String name;
    private final Label label;

    private static final double WIDTH = 90;
    private static final double PIN_SPACING = 25;
    private static final double TOP_PADDING = 20;
    private static final double SIDE_OFFSET = 6;

    public GateNode(Gate logic) {
        this.logic = logic;
        this.name = logic.name;

        setPickOnBounds(false);
        this.setStyle("-fx-font-size: 14pt;");

        int inCount = logic.getInputPins().size();
        int outCount = logic.getOutputPins().size();
        int maxPins = Math.max(inCount, outCount);

        label = new Label(name);
        label.setWrapText(true);
        label.setMaxWidth(WIDTH - 10);
        label.setLayoutX(5);
        label.setLayoutY(5);

        label.layoutBoundsProperty().addListener((obs, old, bounds) -> {
            double dynamicHeight = Math.max(40, TOP_PADDING + maxPins * PIN_SPACING);

            double totalHeight = Math.max(dynamicHeight, bounds.getHeight() + 10);

            ((Rectangle) getChildren().get(0)).setHeight(totalHeight);
        });

        double baseHeight = Math.max(60, TOP_PADDING + maxPins * PIN_SPACING);

        Rectangle body = new Rectangle(WIDTH, baseHeight);
        body.setFill(Paint.valueOf("lightgray"));
        body.setStroke(Paint.valueOf("black"));
        body.setStrokeWidth(2);

        getChildren().addAll(body, label);

        for (int i = 0; i < inCount; i++) {
            Pin pin = logic.getInputPins().get(i);
            PinNode pinNode = new PinNode(pin, "In " + i, i);
            pinNode.setSide(true);

            pinNode.setLayoutX(-SIDE_OFFSET);
            pinNode.setLayoutY(TOP_PADDING + i * PIN_SPACING);

            getChildren().add(pinNode);
        }

        for (int i = 0; i < outCount; i++) {
            Pin pin = logic.getOutputPins().get(i);
            PinNode pinNode = new PinNode(pin, "Out " + i, i);

            pinNode.setLayoutX(WIDTH + SIDE_OFFSET);
            pinNode.setLayoutY(TOP_PADDING + i * PIN_SPACING);

            getChildren().add(pinNode);
        }
    }

    public Gate getLogic() {
        return logic;
    }

    public void setName(String newName) {
        this.name = newName;
        this.logic.name = newName;
        this.label.setText(name);
    }

    @Override
    public String getName() {
        return name;
    }
}