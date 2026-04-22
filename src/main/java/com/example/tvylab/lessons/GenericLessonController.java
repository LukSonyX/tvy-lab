package com.example.tvylab.lessons;

import com.example.tvylab.Launcher;
import com.example.tvylab.sandbox.logic.*;
import com.example.tvylab.sandbox.managers.WireManager;
import com.example.tvylab.sandbox.visual.GateNode;
import com.example.tvylab.sandbox.visual.PinNode;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
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
        WireManager wireManager = new WireManager(simulationPane, ghostWire);

        switch (type) {
            case "Input":
                PinNode input = new PinNode(new Pin(true), "vstup", 0);
                PinNode output = new PinNode(new Pin(false), "výstup", 1);
                Gate tempGate = new BufferGate();
                output.setTextColor("black");
                output.setTranslateX(240);
                output.setTranslateY(200);

                output.getLogic().setParentGate(tempGate);

                input.setTextColor("black");
                input.setTranslateY(200);
                input.setTranslateX(140);
                input.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        input.getLogic().toggle();
                    }
                    else if (e.getButton() == MouseButton.SECONDARY) {
                        wireManager.handleRightClick(input, new Point2D(200, 140));
                    }
                });

                output.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.SECONDARY) {
                        wireManager.handleRightClick(output, new Point2D(240, 200));
                    }
                });
                simulationPane.getChildren().addAll(input, output);
                break;
            case "AND_GATE_DEMO":
                AndGate andLogic = new AndGate();
                GateNode andNode = new GateNode(andLogic);

                PinNode inA = new PinNode(new Pin(true), "A", 0);
                PinNode inB = new PinNode(new Pin(true), "B", 1);
                PinNode out = new PinNode(new Pin(false), "Výstup", 0);

                andNode.setLayoutX(150);
                andNode.setLayoutY(100);
                inA.setLayoutX(145);
                inA.setLayoutY(120);
                inA.setTextColor("black");
                inA.setSide(true);
                inB.setLayoutX(145);
                inB.setLayoutY(145);
                inB.setTextColor("black");
                inB.setSide(true);
                out.setLayoutX(245);
                out.setLayoutY(120);
                out.setTextColor("black");

                inA.getLogic().connectTo(andLogic.getInputPins().get(0));
                inB.getLogic().connectTo(andLogic.getInputPins().get(1));
                andLogic.getOutputPins().get(0).connectTo(out.getLogic());

                inA.setOnMouseClicked(e -> { inA.getLogic().toggle(); andLogic.update(); });
                inB.setOnMouseClicked(e -> { inB.getLogic().toggle(); andLogic.update(); });

                simulationPane.getChildren().addAll(andNode, inA, inB, out);
                break;

            case "NOT_BUFFER_DEMO":
                Gate singleGateLogic = currentLesson.title.contains("NOT") ?
                        new NotGate() :
                        new BufferGate();

                GateNode singleNode = new GateNode(singleGateLogic);
                PinNode sIn = new PinNode(new Pin(true), "Vstup", 0);
                PinNode sOut = new PinNode(new Pin(false), "Výstup", 0);

                sIn.setTextColor("black");
                sOut.setTextColor("black");
                singleNode.setLayoutX(150);
                singleNode.setLayoutY(100);
                sIn.setLayoutX(145);
                sIn.setLayoutY(120);
                sIn.setSide(true);
                sOut.setLayoutX(245);
                sOut.setLayoutY(120);

                sIn.getLogic().connectTo(singleGateLogic.getInputPins().get(0));
                singleGateLogic.getOutputPins().get(0).connectTo(sOut.getLogic());

                sIn.setOnMouseClicked(e -> { sIn.getLogic().toggle(); singleGateLogic.update(); });
                simulationPane.getChildren().addAll(singleNode, sIn, sOut);
                break;

            case "NAND_GATE_DEMO":
                NandGate nandLogic = new NandGate();
                GateNode nandNode = new GateNode(nandLogic);

                PinNode nInA = new PinNode(new Pin(true), "A", 0);
                PinNode nInB = new PinNode(new Pin(true), "B", 1);
                PinNode nOut = new PinNode(new Pin(false), "Výstup", 0);

                nInA.setTextColor("black");
                nInB.setTextColor("black");
                nOut.setTextColor("black");
                nandNode.setLayoutX(150);
                nandNode.setLayoutY(100);
                nInA.setLayoutX(145);
                nInA.setLayoutY(120);
                nInA.setSide(true);
                nInB.setLayoutX(145);
                nInB.setLayoutY(145);
                nInB.setSide(true);
                nOut.setLayoutX(245);
                nOut.setLayoutY(120);

                nInA.getLogic().connectTo(nandLogic.getInputPins().get(0));
                nInB.getLogic().connectTo(nandLogic.getInputPins().get(1));
                nandLogic.getOutputPins().get(0).connectTo(nOut.getLogic());

                nInA.setOnMouseClicked(e -> { nInA.getLogic().toggle(); nandLogic.update(); });
                nInB.setOnMouseClicked(e -> { nInB.getLogic().toggle(); nandLogic.update(); });

                simulationPane.getChildren().addAll(nandNode, nInA, nInB, nOut);
                break;

            case "Signal":
                PinNode signalIn = new PinNode(new Pin(true), "Zdroj", 0);
                PinNode lightOut = new PinNode(new Pin(false), "Žárovka", 0);
                signalIn.setTextColor("black");
                lightOut.setTextColor("black");
                Gate wireBuffer = new BufferGate();
                wireManager.forceConnect(signalIn, lightOut);

                signalIn.setLayoutX(100);
                signalIn.setLayoutY(150);
                lightOut.setLayoutX(280);
                lightOut.setLayoutY(150);

                lightOut.getLogic().setParentGate(wireBuffer);
                signalIn.getLogic().connectTo(lightOut.getLogic());

                signalIn.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        signalIn.getLogic().toggle();
                        wireBuffer.update();
                    }
                });

                simulationPane.getChildren().addAll(signalIn, lightOut);
                break;
        }
    }
}