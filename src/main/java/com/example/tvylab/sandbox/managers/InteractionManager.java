package com.example.tvylab.sandbox.managers;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class InteractionManager {
    private final Pane zoomGroup;
    private final Pane sandboxPane;

    private boolean wasDragged;
    private double lastMouseX;
    private double lastMouseY;

    public InteractionManager(Pane zoomGroup, Pane sandboxPane) {
        this.zoomGroup = zoomGroup;
        this.sandboxPane = sandboxPane;
    }

    public void handleMousePressed(MouseEvent e) {
        if (e.getButton() == MouseButton.MIDDLE) {
            lastMouseX = e.getX();
            lastMouseY = e.getY();
        }
    }

    public void handleMouseDrag(MouseEvent e) {
        if (e.getButton() == MouseButton.MIDDLE) {
            double deltaX = e.getX() - lastMouseX;
            double deltaY = e.getY() - lastMouseY;
            zoomGroup.setLayoutX(zoomGroup.getLayoutX() + deltaX);
            zoomGroup.setLayoutY(zoomGroup.getLayoutY() + deltaY);
            lastMouseX = e.getX();
            lastMouseY = e.getY();
            return;
        }

        Node item = findTopItem((Node) e.getTarget());
        if (item instanceof Line) return;

        if (e.getButton() == MouseButton.PRIMARY && item != null) {
            wasDragged = true;
            Point2D local = toZoomLocal(e.getSceneX(), e.getSceneY());
            item.setLayoutX(local.getX());
            item.setLayoutY(local.getY());
            e.consume();
        }
    }

    public void handleMouseScroll(ScrollEvent e, double scaleFactor) {
        double scale = e.getDeltaY() > 0 ? scaleFactor : 1 / scaleFactor;
        if (zoomGroup.getScaleY() * scale > 3.0) return;
        if (zoomGroup.getScaleY() * scale < 0.4) return;
        zoomGroup.setScaleX(zoomGroup.getScaleX() * scale);
        zoomGroup.setScaleY(zoomGroup.getScaleY() * scale);
        e.consume();
    }

    public Point2D toZoomLocal(double sceneX, double sceneY) {
        return zoomGroup.sceneToLocal(sceneX, sceneY);
    }

    public Node findTopItem(Node node) {
        if (node == sandboxPane || node == zoomGroup) return null;
        while (node != null && node.getParent() != zoomGroup) {
            node = node.getParent();
        }
        return node;
    }

    public boolean wasDragged() { return wasDragged; }
    public void setDragged(boolean val) { wasDragged = val; }
}