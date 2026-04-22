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
    Pin logic;
    private final ArrayList<Wire> wires = new ArrayList<>();
    Circle texture = new Circle(10, Paint.valueOf("black"));
    String name;
    Label nameLabel;
    private final int order;

    public PinNode(Pin logic, String name, int order) {
        this.getChildren().add(texture);
        this.name = name;
        this.logic = logic;
        this.order = order;
        nameLabel = new Label(name);
        setPickOnBounds(false);
        this.setStyle("-fx-font-size: 14pt;");

        Circle hoverArea = new Circle(20);
        hoverArea.setFill(Color.TRANSPARENT);

        nameLabel.setVisible(false);
        nameLabel.setLayoutY(-12);
        nameLabel.setLayoutX(texture.getLayoutX() + nameLabel.getWidth() + 15);
        nameLabel.setTextFill(Paint.valueOf("white"));

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



        hoverProperty().addListener((obs, oldVal, hovering) -> nameLabel.setVisible(hovering));

        texture.fillProperty().bind(Bindings.when(logic.isOnProperty())
                .then(Paint.valueOf("green"))
                .otherwise(Paint.valueOf("black")));

        getChildren().addAll(hoverArea, nameLabel);
    }

    private void replace(Node oldNode, Node newNode) {
        Pane parent = (Pane) oldNode.getParent();
        int index = parent.getChildren().indexOf(oldNode);
        parent.getChildren().set(index, newNode);
    }

    public Pin getLogic() {
        return this.logic;
    }

    public void setStroke(Paint paint) {
        texture.setStroke(paint);
    }

    public void setTextColor(String color) {
        nameLabel.setTextFill(Paint.valueOf(color));
    }

    @Override
    public String getName() {
        return this.name;
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
            if (logic.getParentGate() != null) {
                logic.getParentGate().update();
            }
        }
    }

    public void removeWire(Wire wire) {
        wires.remove(wire);
    }

    public ArrayList<Wire> getWires() {
        return wires;
    }

    public void setSide(boolean isLeft) {
        if (isLeft) {
            nameLabel.layoutXProperty().bind(nameLabel.widthProperty().multiply(-1).subtract(15));
        }
    }

    public int getOrder() {
        return order;
    }
}
