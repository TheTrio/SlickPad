package com.company;


import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class SettingWindow {
    @FXML
    private JFXColorPicker ColorID;

    @FXML
    private JFXSlider Size;

    @FXML
    private JFXToggleButton Screen;

    @FXML
    private JFXToggleButton Save;

    @FXML
    private JFXTextField pathJdk;

    @FXML
    private JFXButton openerJava;

    @FXML
    private JFXTextField pathC;

    @FXML
    private JFXButton openerC;


    public static String color;
    String names[];
    public static int size;
    public static boolean bool_Screen;
    public static boolean bool_Notify;
    public static String StringPathJava;
    public static String StringPathC;
    public void initialize() {
        getData();
        String rule = names[3];
        rule = rule.substring(2,8);
        ColorID.setValue(Color.valueOf(rule));
        Size.setValue(Integer.parseInt(names[2]));
        if(names[0].equals("true")){
            System.out.println("Full Screen - True");
            bool_Screen = true;
            Screen.setText("Enabled");
            Screen.setSelected(true);
        }else {
            System.out.println("Full Screen - False");
            bool_Screen= false;
            Screen.setText("Disabled");
            Screen.setSelected(false);
        }

        if(names[1].equals("true")){
            System.out.println("Sound - true");
            Save.setText("Enabled");
            bool_Notify = true;
            Save.setSelected(true);
        }else {
            System.out.println("Sound - false");
            Save.setText("Disabled");
            bool_Notify= false;
            Save.setSelected(false);
        }
        Screen.setOnAction(e -> {
            if (Screen.isSelected()) {
                Screen.setText("Enabled");
            } else {
                Screen.setText("Disabled");
            }
        });

        Save.setOnAction(e-> {
            if(Save.isSelected()){
                Save.setText("Enabled");
            }else {
                Save.setText("Disabled");
            }
        });

        DirectoryChooser directoryChooser = new DirectoryChooser() ;

        pathJdk.setText(names[4]);
        pathC.setText(names[5]);
        openerJava.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e){
                File f =  directoryChooser.showDialog(openerJava.getScene().getWindow());
                pathJdk.setText(f.getAbsolutePath());
            }
        });

        openerC.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent e){
                File f =  directoryChooser.showDialog(openerJava.getScene().getWindow());
                pathC.setText(f.getAbsolutePath());
            }
        });


    }

    private void getData() {
        GetSetting getSetting = new GetSetting();
        getSetting.OpenFile();
        String values[] = getSetting.GiveSetting();
        getSetting.CloseFile();


        names = values;
    }

    public void GiveData(){
        javafx.scene.paint.Color c = ColorID.getValue();
        color = c.toString();
        size = (int) Size.getValue();
        bool_Screen = Screen.isSelected();
        bool_Notify = Save.isSelected();

        String s[] = {String.valueOf(bool_Screen), String.valueOf(bool_Notify), String.valueOf(size), String.valueOf(color), pathJdk.getText(), pathC.getText()};
        WriteSetting writeSetting = new WriteSetting();
        writeSetting.OpenFile();
        writeSetting.AddRecords(s);
        writeSetting.CloseFile();
        names = s;

    }

    public void CloseAction(){
        Main.closeWindow();
        StringPathC = pathC.getText();
        StringPathJava = pathJdk.getText();
    }

    public void SaveAction(){
        GiveData();
        StringPathC = pathC.getText();
        StringPathJava = pathJdk.getText();
        Main.closeWindow();
    }

    public void ApplyAction(){
        GiveData();
        StringPathC = pathC.getText();
        StringPathJava = pathJdk.getText();
    }




}
