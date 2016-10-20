package com.company;


import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;

public class SettingWindow {
    public void MakeWindow(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        window.setTitle("Settings");

        BorderPane hbox = new BorderPane();

        VBox vb1 = new VBox();
        VBox vb2 = new VBox();

        vb1.setPadding(new Insets(20,20,20,20));
        vb1.setSpacing(20);

        Label Size = new Label("Default Size");

        vb1.getChildren().addAll(Size);
        vb2.setPadding(new Insets(20,20,20,20));
        vb2.setSpacing(20);

        TextField Size_t = new TextField();

        hbox.setLeft(vb1);
        hbox.setRight(vb2);


        ScrollPane scrollPane = new ScrollPane(hbox);
        scrollPane.setFitToHeight(true);


        Scene scene = new Scene(scrollPane, 400, 400);
        window.setScene(scene);
        window.show();

    }
}
