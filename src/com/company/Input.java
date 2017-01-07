package com.company;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Input {


    @FXML
    JFXTextField text;

    public static String className;

    public void DoSomething(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            String temp = text.getText().replace(".class", "");
            className = temp;
            text.setPromptText("Close this window");
            Stage stage = (Stage) text.getScene().getWindow();
            stage.close();
        }
    }

}
