package com.company;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;


public class ControllerForChooser {
    public static String s;

    public void Java(ActionEvent e) {
        Editor editor = new Editor();
        editor.setText(s);
        editor.start();
        Node source = (Node) e.getSource();
        Stage theStage = (Stage) source.getScene().getWindow();
        theStage.close();


    }

    public void Html(ActionEvent actionEvent) {
        EditorHtml editor = new EditorHtml();
        editor.setText(s);
        editor.start();
        Node source = (Node) actionEvent.getSource();
        Stage theStage = (Stage) source.getScene().getWindow();
        theStage.close();

    }

    public void C(ActionEvent event) {
        EditorC editor = new EditorC();
        editor.setText(s);
        editor.start();

        Node source = (Node) event.getSource();
        Stage theStage = (Stage) source.getScene().getWindow();
        theStage.close();
    }

    public void CPP(ActionEvent event) {
        EditorCPP editor = new EditorCPP();
        editor.setText(s);
        editor.start();

        Node source = (Node) event.getSource();
        Stage theStage = (Stage) source.getScene().getWindow();
        theStage.close();
    }
}
