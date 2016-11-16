package com.company;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class Controller {
    @FXML
    TextArea Text;

    public void initialize(){
        Text.setOnKeyPressed(e->{
            e.consume();
        });
    }
}
