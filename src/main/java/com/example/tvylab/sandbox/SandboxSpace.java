package com.example.tvylab.sandbox;

import com.example.tvylab.LanguageChanger;
import com.example.tvylab.Launcher;
import com.example.tvylab.sandbox.managers.InteractionManager;
import com.example.tvylab.sandbox.managers.NodeManager;
import com.example.tvylab.sandbox.managers.SandboxGateManager;
import com.example.tvylab.sandbox.managers.WireManager;
import com.example.tvylab.settings.Settings;
import com.example.tvylab.sandbox.logic.*;
import com.example.tvylab.sandbox.visual.GateNode;
import com.example.tvylab.sandbox.visual.PinNode;
import com.example.tvylab.settings.SettingsManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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


    @FXML
    private Pane sandboxPane;
    private Settings settings;
    private final Line ghostWire = new Line();
    private InteractionManager interactionManager;
    private Pane zoomGroup;
    private final Map<String, ComboBox<String>> categoryBoxes = new HashMap<>();

    private final double scale_factor = 1.035;
    WireManager wireManager;
    SandboxGateManager sandboxGateManager;
    NodeManager nodeManager;
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
        gates.setPromptText(LanguageChanger.get("gates"));
        gates.getItems().addAll("NAND", "AND", "NOT", "BUFFER");
        gates.setOnAction(e -> onComboboxSelect(gates));

        ghostWire.setVisible(false);
        ghostWire.setStroke(Paint.valueOf("red"));
        ghostWire.setStrokeWidth(5);

        interactionManager = new InteractionManager(zoomGroup, sandboxPane);
        wireManager = new WireManager(zoomGroup, ghostWire);
        sandboxGateManager = new SandboxGateManager(zoomGroup, settings);
        nodeManager = new NodeManager(zoomGroup);

        createComboboxFromFolders(new File(settings.gatesDir));
        zoomGroup.getChildren().add(ghostWire);

        sandboxPane.setOnMousePressed(interactionManager::handleMousePressed);
        sandboxPane.setOnMouseClicked(this::handleMouseClick);
        sandboxPane.setOnMouseDragged(interactionManager::handleMouseDrag);
        sandboxPane.setOnMouseMoved(this::handleMouseMove);
        sandboxPane.setOnScroll(e -> interactionManager.handleMouseScroll(e, scale_factor));
    }

    public void onBackPressed() throws IOException {
        Launcher.changeScene("main-menu.fxml");
    }

    public void onSavePressed() throws Exception {
        if (nodeManager.isSandboxEmpty()) {
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

        GateDefinition def = sandboxGateManager.createDefinition(gateName);
        sandboxGateManager.save(def, category);
        nodeManager.clearSandbox(ghostWire);
        zoomGroup.getChildren().add(sandboxGateManager.load(gateName, category));
        createComboboxFromFolders(new File(settings.gatesDir));
    }

    public void onComboboxSelect(ComboBox<String> comboBox) {
        String selection = comboBox.getSelectionModel().getSelectedItem();
        if (selection == null) return;

        if (selection.equals("1-Vstup")) selection = "1-Input";
        if (selection.equals("1-Výstup")) selection = "1-Output";

        nodeManager.setSelected(switch (selection) {
            case "1-Input"  -> new PinNode(new Pin(true), LanguageChanger.get("input"), pinCount++);
            case "1-Output" -> new PinNode(new Pin(false), LanguageChanger.get("output"), pinCount++);
            case "NAND" -> new GateNode(new NandGate());
            case "AND" -> new GateNode(new AndGate());
            case "NOT" -> new GateNode(new NotGate());
            case "BUFFER" -> new GateNode(new BufferGate());
            default -> null;
        });

        Platform.runLater(() -> comboBox.getSelectionModel().clearSelection());
    }

    private void onCustomGateSelected(ComboBox<String> combobox, String category) {
        String gateName = combobox.getSelectionModel().getSelectedItem();
        if (gateName == null) return;

        try {
            File file = new File(new File(settings.gatesDir, category), gateName + ".json");
            GateDefinition def = GateManager.load(file.getAbsolutePath());
            CustomGate gate = new CustomGate(def.name, def.inputs, def.outputs, def.table);
            nodeManager.setSelected(new GateNode(gate));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> combobox.getSelectionModel().clearSelection());
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
            for (File f : files) {
                if (!f.delete()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(LanguageChanger.get("file_deletion_error"));
                    alert.setContentText(LanguageChanger.get("file_deletion_error_context"));
                    alert.showAndWait();
                }
            }
        }
        if (!folder.delete()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(LanguageChanger.get("folder_deletion_error"));
            alert.setContentText(LanguageChanger.get("folder_deletion_error_context"));
            alert.showAndWait();
        }
    }

    private void handleMouseClick(MouseEvent e) {
        if (interactionManager.wasDragged()) {
            interactionManager.setDragged(false);
            return;
        }

        Point2D local = interactionManager.toZoomLocal(e.getSceneX(), e.getSceneY());
        Node item = interactionManager.findTopItem((Node) e.getTarget());

        if (e.getButton() == MouseButton.PRIMARY) {
            if (deleteToggle.isSelected()) {
                nodeManager.delete(item);
            }

            if (item instanceof PinNode inputPin) {
                inputPin.getLogic().toggle();
                for (int k = 0; k < 5; k++) {
                    nodeManager.updateAllGates();
                }
            }
            nodeManager.tryPlace(local);
        }

        if (e.getButton() == MouseButton.SECONDARY) {
            PinNode pin = findPin((Node) e.getTarget());
            wireManager.handleRightClick(pin, local);
        }
    }

    private void handleMouseMove(MouseEvent e) {
        Point2D local = toZoomLocal(e.getSceneX(), e.getSceneY());
        wireManager.updateGhostWire(local);
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

    private PinNode findPin(Node node) {
        while (node != null && !(node instanceof PinNode)) {
            node = node.getParent();
        }
        return (PinNode) node;
    }

    private Point2D toZoomLocal(double sceneX, double sceneY) {
        return zoomGroup.sceneToLocal(sceneX, sceneY);
    }
}