package com.company;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.util.ResourceBundle;

public class AutoController implements Initializable {
    @FXML
    AnchorPane root;

    @FXML
    TextField input;
    private Stage theStage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String[] words = {
                "abstract", "assert", "boolean", "break", "byte",
                "case", "catch", "char", "class", "const",
                "continue", "default", "do", "double", "else",
                "enum", "extends", "final", "finally", "float",
                "for", "goto", "if", "implements", "import",
                "instanceof", "int", "interface", "long", "native",
                "new", "package", "private", "protected", "public",
                "return", "short", "static", "strictfp", "super",
                "switch", "synchronized", "this", "throw", "throws",
                "transient", "try", "void", "volatile", "while", "System"
        };
        String[] how = {
                "hi", "how", "How are you"
        };
        TextFields.bindAutoCompletion(input, words);
        input.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                System.out.println("hello");
                Node source = (Node) e.getSource();
                theStage = (Stage) source.getScene().getWindow();
                theStage.close();
            }
        });
    }
}
