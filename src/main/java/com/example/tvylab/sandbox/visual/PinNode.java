package com.example.tvylab.sandbox.visual;

import com.example.tvylab.sandbox.logic.Pin;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.ArrayList;


public class PinNode extends Pane implements LogicItem {
    Pin logic;
    private ArrayList<Wire> wires = new ArrayList<>();
    Circle texture = new Circle(10, Paint.valueOf("black"));;
    String name;
    Label menuButton = new Label("+");
    Label nameLabel;
    private int order;

    public PinNode(Pin logic, String name, int order) {
        this.getChildren().add(texture);
        this.name = name;
        this.logic = logic;
        this.order = order;
        nameLabel = new Label(name);
        setPickOnBounds(false);
        this.setStyle("-fx-font-size: 14pt;");

        Tooltip.install(this, new Tooltip(name));

        Circle hoverArea = new Circle(20);
        hoverArea.setFill(Color.TRANSPARENT);

        //ContextMenu pinMenu = new ContextMenu();

        menuButton.setVisible(false);
        menuButton.setLayoutX(14);
        menuButton.setLayoutY(-27);
        menuButton.setTextFill(Paint.valueOf("white"));

        nameLabel.setMouseTransparent(true);
        nameLabel.setVisible(false);
        nameLabel.setLayoutY(-12);
        nameLabel.setLayoutX(texture.getLayoutX() + nameLabel.getWidth() + 15);
        nameLabel.setTextFill(Paint.valueOf("white"));



        hoverProperty().addListener((obs, oldVal, hovering) -> {
            menuButton.setVisible(hovering);
            nameLabel.setVisible(hovering);
        });

        texture.fillProperty().bind(Bindings.when(logic.isOnProperty())
                .then(Paint.valueOf("green"))
                .otherwise(Paint.valueOf("black")));

        getChildren().addAll(hoverArea, menuButton, nameLabel);
    }

    public Pin getLogic() {
        return this.logic;
    }

    public void setStroke(Paint paint) {
        texture.setStroke(paint);
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
            };
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
