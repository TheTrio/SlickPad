package com.company;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TryClass extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ListView<String> listView = new ListView<>();

        listView.getItems().add("");
        listView.getItems().add("Option 2");

        VBox vBox = new VBox();
        Label label = new Label("Right Click Here");
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(new StackPane(listView), 100, 100));

        label.setOnMousePressed(e -> {
            if (e.isSecondaryButtonDown()) {
                stage.setX(e.getScreenX());
                stage.setY(e.getScreenY());
                stage.show();
            } else if (stage.isShowing()) {
                stage.hide();
            }
        });
        vBox.getChildren().add(label);
        Scene newScene = new Scene(vBox, 300, 300);
        newScene.addEventFilter(MouseEvent.MOUSE_PRESSED, e ->
        {
            stage.hide();
        });
        primaryStage.setScene(newScene);
        primaryStage.show();
    }
}