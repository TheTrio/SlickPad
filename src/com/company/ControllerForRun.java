package com.company;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Formatter;


public class ControllerForRun implements Runnable{
    public void Java(ActionEvent e){
        if(Main.bool_notify==true){
            System.out.println("Here we gii");
            try {
                Thread thread = new Thread(this);
                thread.start();
                thread.join();
                Runtime.getRuntime().exec("cmd /c start Jrun.bat");
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }


        }
    }

    public void Html(ActionEvent actionEvent){

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

    @Override
    public void run() {
        Formatter x = null;
        try {
            x = new Formatter("temp.java");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        x.format("%s", Main.myText);
        x.close();
    }
}
