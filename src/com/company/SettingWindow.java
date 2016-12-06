package com.company;


import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;

public class SettingWindow {
    @FXML
    private JFXColorPicker ColorID;

    @FXML
    private JFXSlider Size;

    @FXML
    private JFXToggleButton Screen;

    @FXML
    private JFXToggleButton Save;


    public static String color;
    String names[];
    public static int size;
    public static boolean bool_Screen;
    public static boolean bool_Notify;
    private GetSetting gr;
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

        String s[] = {String.valueOf(bool_Screen), String.valueOf(bool_Notify), String.valueOf(size), String.valueOf(color)};
        WriteSetting writeSetting = new WriteSetting();
        writeSetting.OpenFile();
        writeSetting.AddRecords(s);
        writeSetting.CloseFile();
        names = s;

    }

    public void CloseAction(){
        Main.closeWindow();
    }

    public void SaveAction(){
        GiveData();
        Main.closeWindow();
    }

    public void ApplyAction(){
        GiveData();
    }




}
