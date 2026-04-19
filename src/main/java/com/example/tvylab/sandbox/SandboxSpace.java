package com.example.tvylab.sandbox;

import com.example.tvylab.LanguageChanger;
import com.example.tvylab.Launcher;
import com.example.tvylab.settings.Settings;
import com.example.tvylab.settings.SettingsController;
import com.example.tvylab.sandbox.logic.*;
import com.example.tvylab.sandbox.visual.GateNode;
import com.example.tvylab.sandbox.visual.LogicItem;
import com.example.tvylab.sandbox.visual.PinNode;
import com.example.tvylab.sandbox.visual.Wire;
import com.example.tvylab.settings.SettingsManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SandboxSpace {

    public void onBackPressed() throws IOException { Launcher.changeScene("main-menu.fxml"); }

    @FXML
    private Pane sandboxPane;
    private Settings settings;
    Line ghostWire = new Line();
    boolean wasDragged = false;
    private Pane zoomGroup;
    private Map<String, ComboBox<String>> categoryBoxes = new HashMap<>();

    private double scale_factor = 1.035;

    private double lastMouseX;
    private double lastMouseY;

    LogicItem selectedItem;
    PinNode connectFrom;
    private int pinCount;

    @FXML
    private ComboBox<String> pins;
    @FXML
    private ComboBox<String> gates;
    @FXML
    private VBox menuBox;
    @FXML
    private CheckBox deleteToggle;
    @FXML
    private VBox gateSpace;
    @FXML
    private Button saveBtn;
    @FXML
    private Button backBtn;

    @FXML
    public void initialize() {
        settings = SettingsManager.load();
        saveBtn.setText(LanguageChanger.get("save"));
        backBtn.setText(LanguageChanger.get("back"));
        deleteToggle.setText(LanguageChanger.get("delete"));

        sandboxPane.setStyle("-fx-background-color: gray;");
        menuBox.getStyleClass().add("sandbox");

        zoomGroup = new Pane();
        zoomGroup.setPrefSize(5000, 5000);
        zoomGroup.setManaged(false);

        sandboxPane.getChildren().add(zoomGroup);

        ghostWire.setVisible(false);
        ghostWire.setStroke(Paint.valueOf("red"));
        ghostWire.setStrokeWidth(5);
        zoomGroup.getChildren().add(ghostWire);

        pins.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText("IO");
            }
        });

        pins.setPromptText("IO");
        pins.getItems().addAll("1-" + LanguageChanger.get("input"), "1-" + LanguageChanger.get("output"));
        pins.setOnAction(e -> onComboboxSelect(pins));

        gates.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(LanguageChanger.get("gates"));
            }
        });

        createComboboxFromFolders(new File(settings.gatesDir));

        gates.setPromptText(LanguageChanger.get("gates"));
        gates.getItems().addAll("NAND", "AND", "NOT", "BUFFER");
        gates.setOnAction(e -> onComboboxSelect(gates));

        sandboxPane.setOnMousePressed(this::handleMousePressed);
        sandboxPane.setOnMouseClicked(this::handleMouseClick);
        sandboxPane.setOnMouseDragged(this::handleMouseDrag);
        sandboxPane.setOnMouseMoved(this::handleMouseMove);
        sandboxPane.setOnScroll(this::handleMouseScroll);
    }

    private void createComboboxFromFolders(File root) {
        File[] folders = root.listFiles();
        if (folders == null) return;

        for (File folder : folders) {
            String category = folder.getName();
            ComboBox<String> combo;

            if (categoryBoxes.containsKey(category)) {
                combo = categoryBoxes.get(category);
                combo.getItems().clear();
            } else {
                combo = new ComboBox<>();
                combo.setMaxWidth(Double.MAX_VALUE);
                combo.setPrefWidth(Double.MAX_VALUE);
                combo.setPromptText(category);
                categoryBoxes.put(category, combo);

                Button deleteBtn = new Button("X");
                deleteBtn.setOnAction(e -> onDeleteCategory(category, folder));

                HBox row = new HBox(5, combo, deleteBtn);
                row.setMaxWidth(Double.MAX_VALUE);

                HBox.setHgrow(combo, javafx.scene.layout.Priority.ALWAYS);
                combo.setPrefWidth(0);
                combo.setMaxWidth(Double.MAX_VALUE);
                gateSpace.getChildren().add(row);
            }

            File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
            if (files != null) {
                for (File f : files) {
                    combo.getItems().add(f.getName().replace(".json", ""));
                }
            }

            combo.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(category);
                }
            });

            combo.setOnAction(e -> onCustomGateSelected(combo, category));
        }
    }

    private void onDeleteCategory(String category, File folder) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(LanguageChanger.get("folder_delete_title"));
        alert.setHeaderText(LanguageChanger.get("delete") + " \"" + category.toUpperCase() + "\" " + LanguageChanger.get("folder"));
        alert.setContentText(LanguageChanger.get("folder_delete_content"));

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                deleteFolder(folder);
                gateSpace.getChildren().remove(categoryBoxes.get(category).getParent());
                categoryBoxes.remove(category);
            }
        });
    }

    private void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) f.delete();
        }
        folder.delete();
    }

    private void onCustomGateSelected(ComboBox<String> combobox, String category) {
        String gateName = combobox.getSelectionModel().getSelectedItem();
        if (gateName == null) return;

        try {
            File file = new File(new File(settings.gatesDir, category), gateName + ".json");
            GateDefinition def = GateManager.load(file.getAbsolutePath());
            CustomGate gate = new CustomGate(def.name, def.inputs, def.outputs, def.table);
            selectedItem = new GateNode(gate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> combobox.getSelectionModel().clearSelection());
    }

    private Point2D toZoomLocal(double sceneX, double sceneY) {
        return zoomGroup.sceneToLocal(sceneX, sceneY);
    }

    private void handleMousePressed(MouseEvent e) {
        if (e.getButton() == MouseButton.MIDDLE) {
            lastMouseX = e.getX();
            lastMouseY = e.getY();
        }
    }

    private void handleMouseClick(MouseEvent e) {
        if (wasDragged) {
            wasDragged = false;
            return;
        }

        Point2D local = toZoomLocal(e.getSceneX(), e.getSceneY());
        Node item = findTopItem((Node) e.getTarget());

        if (e.getButton() == MouseButton.PRIMARY) {
            if (deleteToggle.isSelected()) {
                deleteNode(item);
            }
            if (item instanceof PinNode inputPin) {
                inputPin.getLogic().toggle();
                for (int k = 0; k < 5; k++) {
                    updateAllGates();
                }
            } else if (selectedItem instanceof Node node) {
                node.setLayoutX(local.getX());
                node.setLayoutY(local.getY());
                zoomGroup.getChildren().add(node);
                selectedItem = null;
            }
        }

        if (e.getButton() == MouseButton.SECONDARY) {
            PinNode connectTo = findPin((Node) e.getTarget());

            if (connectTo != null) {
                connectTo.setStroke(Paint.valueOf("orange"));
                connectTo.setStrokeWidth(5);

                if (connectFrom == null) {
                    connectFrom = connectTo;
                    Point2D p = getPinPosition(connectFrom);
                    ghostWire.setStartX(p.getX());
                    ghostWire.setStartY(p.getY());
                    ghostWire.setEndX(local.getX());
                    ghostWire.setEndY(local.getY());
                    ghostWire.setVisible(true);
                } else {
                    ghostWire.setVisible(false);
                    if (connectFrom.getLogic().connectTo(connectTo.getLogic())) {
                        generateWire(connectFrom, connectTo);
                    }
                    connectFrom.setStroke(null);
                    connectTo.setStroke(null);
                    connectFrom = null;
                }
            } else {
                ghostWire.setVisible(false);
                if (connectFrom != null) {
                    connectFrom.setStroke(null);
                    connectFrom = null;
                }
            }
        }
    }

    private void handleMouseDrag(MouseEvent e) {
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

    private void handleMouseMove(MouseEvent e) {
        if (ghostWire != null && ghostWire.isVisible()) {
            Point2D local = toZoomLocal(e.getSceneX(), e.getSceneY());
            ghostWire.setEndX(local.getX());
            ghostWire.setEndY(local.getY());
        }
    }

    private void handleMouseScroll(ScrollEvent e) {
        double scale = e.getDeltaY() > 0 ? scale_factor : 1 / scale_factor;
        if (zoomGroup.getScaleY() * scale > 3.0) return;
        if (zoomGroup.getScaleY() * scale < 0.4) return;

        zoomGroup.setScaleX(zoomGroup.getScaleX() * scale);
        zoomGroup.setScaleY(zoomGroup.getScaleY() * scale);

        e.consume();
    }

    private Node findTopItem(Node node) {
        if (node == sandboxPane || node == zoomGroup) return null;
        while (node != null && node.getParent() != zoomGroup) {
            node = node.getParent();
        }
        return node;
    }

    private PinNode findPin(Node node) {
        while (node != null && !(node instanceof PinNode)) {
            node = node.getParent();
        }
        return (PinNode) node;
    }

    private boolean isSandboxEmpty() {
        for (Node node : zoomGroup.getChildren()) {
            if (node instanceof PinNode || node instanceof GateNode) {
                return false;
            }
        }
        return true;
    }

    private void clearSandbox() {
        zoomGroup.getChildren().clear();
        zoomGroup.getChildren().add(ghostWire);
    }

    public void onSavePressed() {
        if (isSandboxEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(LanguageChanger.get("empty_sandbox"));
            alert.setHeaderText(LanguageChanger.get("empty_sandbox_header"));
            alert.showAndWait();
            return;
        }
        Optional<Pair<String, String>> input = showSaveDialog();
        if (input.isEmpty()) return;

        String gateName = input.get().getKey();
        String category = input.get().getValue();

        GateDefinition def = createGateDefinition(gateName);
        saveGate(def, category);
        clearSandbox();
        loadGate(gateName, category);
        createComboboxFromFolders(new File(settings.gatesDir));
    }

    private Optional<Pair<String, String>> showSaveDialog() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle(LanguageChanger.get("save") + " " + LanguageChanger.get("gate"));

        ButtonType saveButton = new ButtonType(LanguageChanger.get("save"), ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        TextField nameField = new TextField();
        nameField.setPromptText(LanguageChanger.get("name") + " " + LanguageChanger.get("gates"));

        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(categoryBoxes.keySet());
        categoryBox.setEditable(true);
        categoryBox.setValue("Gates");

        VBox content = new VBox(10, new Label(LanguageChanger.get("name") + " " + LanguageChanger.get("gates") + ":"), nameField, new Label(LanguageChanger.get("category") + ":"), categoryBox);
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(button -> {
            if (button == saveButton) {
                String name = nameField.getText();
                if (name == null || name.isBlank()) return null;
                return new Pair<>(name, categoryBox.getValue());
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private GateDefinition createGateDefinition(String gateName) {
        List<PinNode> inputs = new ArrayList<>();
        List<PinNode> outputs = new ArrayList<>();

        for (Node node : zoomGroup.getChildren()) {
            if (node instanceof PinNode pinNode) {
                if (pinNode.getLogic().isInput()) inputs.add(pinNode);
                else outputs.add(pinNode);
            }
        }

        inputs.sort(Comparator.comparingInt(PinNode::getOrder));
        outputs.sort(Comparator.comparingInt(PinNode::getOrder));

        int inputCount = inputs.size();
        int combinations = 1 << inputCount;

        Map<String, List<Boolean>> table = new HashMap<>();

        for (int i = 0; i < combinations; i++) {
            StringBuilder key = new StringBuilder();

            for (int j = 0; j < inputCount; j++) {
                boolean value = (i & (1 << (inputCount - 1 - j))) != 0;
                inputs.get(j).getLogic().setState(value);
                key.append(value ? "1" : "0");
            }

            for (int k = 0; k < 5; k++) updateAllGates();

            List<Boolean> resultRow = new ArrayList<>();
            for (PinNode out : outputs) {
                resultRow.add(out.getLogic().getState());
            }

            table.put(key.toString(), resultRow);
        }

        return new GateDefinition(gateName, inputCount, outputs.size(), table);
    }

    private void saveGate(GateDefinition def, String category) {
        try {
            File categoryDir = new File(settings.gatesDir, category);
            if (!categoryDir.exists()) categoryDir.mkdirs();

            File file = new File(categoryDir, def.name + ".json");
            GateManager.save(def, file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateAllGates() {
        for (Node node : zoomGroup.getChildren()) {
            if (node instanceof GateNode gateNode) {
                gateNode.getLogic().update();
            }
        }
    }

    public void loadGate(String gateName, String category) {
        try {
            File file = new File(new File(settings.gatesDir, category), gateName + ".json");
            GateDefinition def = GateManager.load(file.getAbsolutePath());
            CustomGate gate = new CustomGate(def.name, def.inputs, def.outputs, def.table);
            GateNode node = new GateNode(gate);
            zoomGroup.getChildren().add(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onComboboxSelect(ComboBox<String> comboBox) {
        String selection = comboBox.getSelectionModel().getSelectedItem();
        if (selection == null) return;

        if (selection.equals("1-Vstup")) selection = "1-Input";
        if (selection.equals("1-Výstup")) selection = "1-Output";

        selectedItem = switch (selection) {
            case "1-Input"  -> new PinNode(new Pin(true), LanguageChanger.get("input"), pinCount++);
            case "1-Output" -> new PinNode(new Pin(false), LanguageChanger.get("output"), pinCount++);
            case "NAND" -> new GateNode(new NandGate());
            case "AND" -> new GateNode(new AndGate());
            case "NOT" -> new GateNode(new NotGate());
            case "BUFFER" -> new GateNode(new BufferGate());
            default -> null;
        };

        Platform.runLater(() -> comboBox.getSelectionModel().clearSelection());
    }

    private void generateWire(PinNode start, PinNode end) {
        Wire wire = new Wire(start, end);
        start.addWire(wire);
        end.addWire(wire);
        start.localToSceneTransformProperty().addListener((obs, o, n) -> wire.update());
        end.localToSceneTransformProperty().addListener((obs, o, n) -> wire.update());
        zoomGroup.getChildren().add(0, wire);
        wire.update();
    }

    private void deleteNode(Node toDelete) {
        if (toDelete instanceof Wire wire) {
            wire.disconnect();
            zoomGroup.getChildren().remove(wire);
        }
        if (toDelete instanceof PinNode pin) {
            for (Wire wire : new ArrayList<>(pin.getWires())) {
                wire.disconnect();
                zoomGroup.getChildren().remove(wire);
            }
            pin.getLogic().disconnectAll();
            zoomGroup.getChildren().remove(pin);
        }
        if (toDelete instanceof GateNode gate) {
            for (Pin pin : gate.getLogic().getInputPins()) pin.disconnectAll();
            for (Pin pin : gate.getLogic().getOutputPins()) pin.disconnectAll();
            for (Node node : new ArrayList<>(zoomGroup.getChildren())) {
                if (node instanceof Wire wire) {
                    if (wire.getConnectedFrom().getParent() == gate || wire.getConnectedTo().getParent() == gate) {
                        wire.disconnect();
                        zoomGroup.getChildren().remove(wire);
                    }
                }
            }
            zoomGroup.getChildren().remove(gate);
        }
    }

    private Point2D getPinPosition(PinNode pin) {
        Point2D scene = pin.localToScene(0, 0);
        return zoomGroup.sceneToLocal(scene);
    }
}