package com.example.tvylab.sandbox.managers;

import com.example.tvylab.LanguageChanger;
import com.example.tvylab.sandbox.GateDefinition;
import com.example.tvylab.sandbox.GateManager;
import com.example.tvylab.sandbox.logic.CustomGate;
import com.example.tvylab.sandbox.visual.GateNode;
import com.example.tvylab.sandbox.visual.PinNode;
import com.example.tvylab.settings.Settings;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SandboxGateManager {
    private final Pane zoomGroup;
    private final Settings settings;

    public SandboxGateManager(Pane zoomGroup, Settings settings) {
        this.zoomGroup = zoomGroup;
        this.settings = settings;
    }

    public void updateAllGates() {
        for (Node node : zoomGroup.getChildren()) {
            if (node instanceof GateNode gateNode) {
                gateNode.getLogic().update();
            }
        }
    }

    public GateDefinition createDefinition(String gateName) {
        List<PinNode> inputs = new ArrayList<>();
        List<PinNode> outputs = new ArrayList<>();

        for (Node node : zoomGroup.getChildren()) {
            if (node instanceof PinNode pin) {
                if (pin.getLogic().isInput()) inputs.add(pin);
                else outputs.add(pin);
            }
        }

        inputs.sort(Comparator.comparingInt(PinNode::getOrder));
        outputs.sort(Comparator.comparingInt(PinNode::getOrder));

        int inputCount = inputs.size();
        Map<String, List<Boolean>> table = new HashMap<>();

        for (int i = 0; i < (1 << inputCount); i++) {
            StringBuilder key = new StringBuilder();
            for (int j = 0; j < inputCount; j++) {
                boolean value = (i & (1 << (inputCount - 1 - j))) != 0;
                inputs.get(j).getLogic().setState(value);
                key.append(value ? "1" : "0");
            }
            for (int k = 0; k < 5; k++) updateAllGates();
            List<Boolean> row = new ArrayList<>();
            for (PinNode out : outputs) row.add(out.getLogic().getState());
            table.put(key.toString(), row);
        }

        return new GateDefinition(gateName, inputCount, outputs.size(), table);
    }

    public void save(GateDefinition def, String category) throws IOException {
        File dir = new File(settings.gatesDir, category);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(LanguageChanger.get("folder_creation_error"));
                alert.setContentText(LanguageChanger.get("folder_creation_error_context"));
                alert.showAndWait();
            }
        }
        GateManager.save(def, new File(dir, def.name + ".json").getAbsolutePath());
    }

    public GateNode load(String gateName, String category) throws Exception {
        File file = new File(new File(settings.gatesDir, category), gateName + ".json");
        GateDefinition def = GateManager.load(file.getAbsolutePath());
        return new GateNode(new CustomGate(def.name, def.inputs, def.outputs, def.table));
    }
}
