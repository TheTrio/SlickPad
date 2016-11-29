package com.company;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.Window;


public class ControllerForChooser {
    public void Java(ActionEvent e){
        Editor editor = new Editor();
        editor.start();
        Node source = (Node) e.getSource();
        Stage theStage = (Stage)source.getScene().getWindow();
        theStage.close();


    }

    public void Html(ActionEvent actionEvent){
        EditorHtml editor = new EditorHtml();
        editor.start();
        Node source = (Node) actionEvent.getSource();
        Stage theStage = (Stage)source.getScene().getWindow();
        theStage.close();

    }

    public void C(ActionEvent event){
        EditorC editor = new EditorC();
        editor.start();

        Node source = (Node) event.getSource();
        Stage theStage = (Stage)source.getScene().getWindow();
        theStage.close();
    }

    public void CPP(ActionEvent event){
        EditorCPP editor = new EditorCPP();
        editor.start();

        Node source = (Node) event.getSource();
        Stage theStage = (Stage)source.getScene().getWindow();
        theStage.close();
    }
}
