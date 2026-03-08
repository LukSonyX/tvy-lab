package com.example.tvylab.sandbox.visual;

import com.example.tvylab.sandbox.logic.Pin;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;


public class PinNode extends Circle implements LogicItem {
    Pin logic;
    String name;

    public PinNode(Pin logic, String name) {
        super(15, Paint.valueOf("black"));
        this.name = name;
        this.logic = logic;
        setPickOnBounds(false);

        Tooltip.install(this, new Tooltip(name));


        this.fillProperty().bind(Bindings.when(logic.isOnProperty())
                .then(Paint.valueOf("green"))
                .otherwise(Paint.valueOf("black")));

    }

    public Pin getLogic() {
        return this.logic;
    }


    @Override
    public String getName() {
        return this.name;
    }
}
