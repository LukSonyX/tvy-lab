package com.example.tvylab.sandbox.visual;

import com.example.tvylab.sandbox.logic.Pin;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class PinNode extends Pane implements LogicItem {

    private final Pin logic;
    private final ArrayList<Wire> wires = new ArrayList<>();
    private final Circle texture = new Circle(10);
    private final Label nameLabel;
    private final int order;
    private final String name;

    public PinNode(Pin logic, String name, int order) {
        this.logic = logic;
        this.name = name;
        this.order = order;

        texture.setFill(Paint.valueOf("black"));

        Circle hoverArea = new Circle(20);
        hoverArea.setFill(Color.TRANSPARENT);

        nameLabel = new Label(name);
        nameLabel.setVisible(false);
        nameLabel.layoutXProperty().bind(texture.radiusProperty().add(5));
        nameLabel.setLayoutY(-12);
        nameLabel.setTextFill(Paint.valueOf("white"));

        setPickOnBounds(false);
        setStyle("-fx-font-size: 14pt;");

        nameLabel.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                TextField field = new TextField(nameLabel.getText());

                field.setOnAction(ev -> {
                    nameLabel.setText(field.getText());
                    replace(field, nameLabel);
                });

                replace(nameLabel, field);
            }
        });

        hoverProperty().addListener((obs, oldVal, hovering) ->
                nameLabel.setVisible(hovering)
        );

        texture.fillProperty().bind(
                Bindings.when(logic.stateProperty())
                        .then(Paint.valueOf("#39ff14"))
                        .otherwise(Paint.valueOf("black"))
        );

        getChildren().addAll(texture, hoverArea, nameLabel);
    }

    private void replace(Node oldNode, Node newNode) {
        Pane parent = (Pane) oldNode.getParent();
        int index = parent.getChildren().indexOf(oldNode);
        parent.getChildren().set(index, newNode);
    }

    public Pin getLogic() {
        return logic;
    }

    public void setStroke(Paint paint) {
        texture.setStroke(paint);
    }

    public void setTextColor(String color) {
        nameLabel.setTextFill(Paint.valueOf(color));
    }

    @Override
    public String getName() {
        return name;
    }

    public void setStrokeWidth(int i) {
        texture.setStrokeWidth(i);
    }

    public ObservableValue<? extends Paint> fillProperty() {
        return texture.fillProperty();
    }

    public void addWire(Wire wire) {
        if (!wires.contains(wire)) {
            wires.add(wire);
        }
    }

    public void removeWire(Wire wire) {
        wires.remove(wire);
    }

    public ArrayList<Wire> getWires() {
        return wires;
    }

    public void setSide(boolean isLeft) {
        nameLabel.layoutXProperty().unbind();

        if (isLeft) {
            nameLabel.layoutXProperty().bind(
                    nameLabel.widthProperty().multiply(-1).subtract(texture.radiusProperty()).subtract(5)
            );
        } else {
            nameLabel.layoutXProperty().bind(
                    texture.radiusProperty().add(5)
            );
        }
    }

    public int getOrder() {
        return order;
    }
}