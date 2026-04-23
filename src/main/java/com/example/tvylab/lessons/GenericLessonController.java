package com.example.tvylab.lessons;

import com.example.tvylab.Launcher;
import com.example.tvylab.sandbox.logic.*;
import com.example.tvylab.sandbox.managers.WireManager;
import com.example.tvylab.sandbox.visual.GateNode;
import com.example.tvylab.sandbox.visual.PinNode;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.io.IOException;

public class GenericLessonController {

    @FXML private Label titleLabel;
    @FXML private VBox textContentBox;
    @FXML private VBox quizBox;
    @FXML private Pane simulationPane;

    private LessonData currentLesson;
    private ToggleGroup quizGroup;

    public void onBackPressed() throws IOException {
        Launcher.changeScene("learning-space.fxml");
    }

    public void initLesson(LessonData lessonData) {

        this.currentLesson = lessonData;
        titleLabel.setText(lessonData.title);

        for (String paragraph : lessonData.content) {
            Label lbl = new Label(paragraph);
            lbl.setWrapText(true);
            lbl.setMaxWidth(Double.MAX_VALUE);

            if (paragraph.contains("|")) {
                lbl.setStyle("-fx-font-family: 'Monospaced'; -fx-background-color: #f0f0f0;");
            }

            textContentBox.getChildren().add(lbl);
        }

        setupQuiz();
        setupSimulation(lessonData.simulationType);
    }

    private void setupQuiz() {
        Label questionLbl = new Label(currentLesson.quiz.question);
        questionLbl.setStyle("-fx-font-weight: bold;");
        quizBox.getChildren().add(questionLbl);

        quizGroup = new ToggleGroup();

        int index = 0;
        for (String optionText : currentLesson.quiz.options) {
            RadioButton rb = new RadioButton(optionText);
            rb.setToggleGroup(quizGroup);
            rb.setUserData(index);
            quizBox.getChildren().add(rb);
            index++;
        }

        Button checkBtn = new Button("Zkontrolovat");
        checkBtn.setOnAction(e -> onCheckAnswer());
        quizBox.getChildren().add(checkBtn);
    }

    private void onCheckAnswer() {
        Toggle selected = quizGroup.getSelectedToggle();
        if (selected == null) return;

        int selectedIndex = (int) selected.getUserData();

        if (selectedIndex == currentLesson.quiz.correctOptionIndex) {
            quizBox.setStyle("-fx-background-color: #d4edda; -fx-padding: 15; -fx-background-radius: 8;");
        } else {
            quizBox.setStyle("-fx-background-color: #f8d7da; -fx-padding: 15; -fx-background-radius: 8;");
        }
    }

    private void setupSimulation(String type) {

        simulationPane.getChildren().clear();

        Line ghostWire = new Line();
        simulationPane.getChildren().add(ghostWire);
        Gate tempGate = new BufferGate();
        WireManager wireManager = new WireManager(simulationPane, ghostWire);

        switch (type) {

            case "Input": {
                PinNode input = new PinNode(new Pin(PinType.SOURCE), "vstup", 0);

                input.setTextColor("black");

                input.setTranslateX(180);
                input.setTranslateY(150);

                input.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        input.getLogic().toggle();
                        tempGate.update();
                    }
                });

                simulationPane.getChildren().add(input);
                break;
            }

            case "AND_GATE_DEMO": {
                AndGate andLogic = new AndGate();
                GateNode andNode = new GateNode(andLogic);

                PinNode inA = new PinNode(new Pin(PinType.SOURCE), "A", 0);
                PinNode inB = new PinNode(new Pin(PinType.SOURCE), "B", 1);
                PinNode out = new PinNode(new Pin(PinType.INPUT), "Výstup", 0);

                inA.setTextColor("black");
                inB.setTextColor("black");
                out.setTextColor("black");

                andNode.setLayoutX(150);
                andNode.setLayoutY(100);

                inA.setLayoutX(145);
                inA.setLayoutY(120);
                inA.setSide(true);

                inB.setLayoutX(145);
                inB.setLayoutY(145);
                inB.setSide(true);

                out.setLayoutX(245);
                out.setLayoutY(120);

                inA.getLogic().connectTo(andLogic.getInputPins().get(0));
                inB.getLogic().connectTo(andLogic.getInputPins().get(1));
                andLogic.getOutputPins().get(0).connectTo(out.getLogic());

                inA.setOnMouseClicked(e -> inA.getLogic().toggle());
                inB.setOnMouseClicked(e -> inB.getLogic().toggle());

                simulationPane.getChildren().addAll(andNode, inA, inB, out);
                break;
            }

            case "NOT_BUFFER_DEMO": {
                Gate logic = currentLesson.title.contains("NOT")
                        ? new NotGate()
                        : new BufferGate();

                GateNode node = new GateNode(logic);

                PinNode in = new PinNode(new Pin(PinType.SOURCE), "Vstup", 0);
                PinNode out = new PinNode(new Pin(PinType.INPUT), "Výstup", 0);

                in.setTextColor("black");
                out.setTextColor("black");

                node.setLayoutX(150);
                node.setLayoutY(100);

                in.setLayoutX(145);
                in.setLayoutY(120);
                in.setSide(true);

                out.setLayoutX(245);
                out.setLayoutY(120);

                in.getLogic().connectTo(logic.getInputPins().get(0));
                logic.getOutputPins().get(0).connectTo(out.getLogic());

                in.setOnMouseClicked(e -> in.getLogic().toggle());

                simulationPane.getChildren().addAll(node, in, out);
                break;
            }

            case "NAND_GATE_DEMO": {
                NandGate logic = new NandGate();
                GateNode node = new GateNode(logic);

                PinNode inA = new PinNode(new Pin(PinType.SOURCE), "A", 0);
                PinNode inB = new PinNode(new Pin(PinType.SOURCE), "B", 1);
                PinNode out = new PinNode(new Pin(PinType.INPUT), "Výstup", 0);

                inA.setTextColor("black");
                inB.setTextColor("black");
                out.setTextColor("black");

                node.setLayoutX(150);
                node.setLayoutY(100);

                inA.setLayoutX(145);
                inA.setLayoutY(120);
                inA.setSide(true);

                inB.setLayoutX(145);
                inB.setLayoutY(145);
                inB.setSide(true);

                out.setLayoutX(245);
                out.setLayoutY(120);

                inA.getLogic().connectTo(logic.getInputPins().get(0));
                inB.getLogic().connectTo(logic.getInputPins().get(1));
                logic.getOutputPins().get(0).connectTo(out.getLogic());

                inA.setOnMouseClicked(e -> inA.getLogic().toggle());
                inB.setOnMouseClicked(e -> inB.getLogic().toggle());

                simulationPane.getChildren().addAll(node, inA, inB, out);
                break;
            }

            case "Signal": {
                PinNode source = new PinNode(new Pin(PinType.SOURCE), "Zdroj", 0);
                PinNode light = new PinNode(new Pin(PinType.INPUT), "Žárovka", 0);

                source.setTextColor("black");
                light.setTextColor("black");

                source.setLayoutX(100);
                source.setLayoutY(150);

                light.setLayoutX(280);
                light.setLayoutY(150);

                wireManager.forceConnect(source, light);

                source.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        source.getLogic().toggle();
                        light.getLogic().setState(source.getLogic().getState());
                    }
                });

                simulationPane.getChildren().addAll(source, light);
                break;
            }
        }
    }
}