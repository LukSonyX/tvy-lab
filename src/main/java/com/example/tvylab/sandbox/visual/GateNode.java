package com.example.tvylab.sandbox.visual;

import com.example.tvylab.sandbox.logic.Gate;
import com.example.tvylab.sandbox.logic.Pin;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class GateNode extends Pane implements LogicItem {
    Gate logic;
    String name;

    public GateNode(Gate logic) {
        this.logic = logic;
        this.name = logic.name;
        this.setStyle("-fx-font-size: 14pt;");
        setPickOnBounds(false);

        Rectangle body = new Rectangle(80, 60);
        body.setFill(Paint.valueOf("lightgray"));
        body.setStroke(Paint.valueOf("black"));
        body.setStrokeWidth(2);

        Text label = new Text(name);
        label.setLayoutX(15);
        label.setLayoutY(35);

        this.getChildren().addAll(body, label);

        int inCount = logic.getInputPins().size();
        for (int i = 0; i < inCount; i++) {
            Pin pin = logic.getInputPins().get(i);
            PinNode pinNode = new PinNode(pin, "In " + i);

            pinNode.setLayoutX(-5);
            pinNode.setLayoutY((55.0 / (inCount + 0.5)) * (i + 1));

            this.getChildren().add(pinNode);
        }

        int outCount = logic.getOutputPins().size();
        for (int i = 0; i < outCount; i++) {
            Pin pin = logic.getOutputPins().get(i);
            PinNode pinNode = new PinNode(pin, "Out " + i);

            pinNode.setLayoutX(85);
            pinNode.setLayoutY((60.0 / (outCount + 1)) * (i + 1));

            this.getChildren().add(pinNode);
        }
    }

    @Override
    public String getName() {
        return "";
    }
}
