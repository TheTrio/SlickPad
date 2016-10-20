package com.company;

import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.plaf.MenuItemUI;
import java.io.File;
import java.util.Set;


public class Main extends Application{

    private Stage window;
    private String color = "black";
    private boolean wrapSetting = false;
    private Scene editor;
    private TextArea text;
    private int font = 12;
    private Menu FileMenu;
    private String name = "";
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        MenuBar mb = new MenuBar();
        window = primaryStage;

        BorderPane bb = new BorderPane();
        VBox vb = new VBox();
        window.setTitle("SlickPad");
        editor = new Scene(bb,628,532);

        /*TabPane tabPane = new TabPane();

        BorderPane borderPane = new BorderPane();
        for (int i = 0; i < 5; i++) {
            Tab tab = new Tab();
            tab.setText("Tab" + i);
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER);
            tab.setContent(hbox);
            tabPane.getTabs().add(tab);
        }
*/
        // bind to take available space
        //bb.prefHeightProperty().bind(editor.heightProperty());
        //bb.prefWidthProperty().bind(editor.widthProperty());


        text = new TextArea();
        FileMenu = new Menu("File");
        Menu Edit = new Menu("Edit");
        Menu Prefer = new Menu("Preferences");
        MenuItem Setting = new MenuItem("Setting");
        Setting.setOnAction(e-> {
            SettingWindow std = new SettingWindow();
            std.MakeWindow();
        });
        Prefer.getItems().addAll(Setting);
        Menu Color = new Menu("Color");
        Menu BasicColor = new Menu("Default Colors");
        MenuItem Chooser = new MenuItem("Color Picker");
        Chooser.setOnAction(e-> {
            MakeWindow();
        });
        MenuItem red = new MenuItem("Red");
        red.setOnAction(e-> {
            color = "red";
            text.setStyle("-fx-text-inner-color: " + color + ";");
        });
        MenuItem blue = new MenuItem("Blue");
        blue.setOnAction(e-> {
            color = "blue";
            text.setStyle("-fx-text-inner-color: " + color + ";");
        });
        MenuItem pink = new MenuItem("Pink");
        pink.setOnAction(e-> {
            color = "pink";
            text.setStyle("-fx-text-inner-color: " + color + ";");
        });
        MenuItem green = new MenuItem("Green");
        green.setOnAction(e-> {
            color = "green";
            text.setStyle("-fx-text-inner-color: " + color + ";");
        });

        Menu generateColors = new Menu("Generate Colors");
        BasicColor.getItems().addAll(red, blue, pink, green);
        Color.getItems().addAll(BasicColor);

        Menu Font = new Menu("Font");
        CheckMenuItem Wrap = new CheckMenuItem("Word Wrap");
        Wrap.setOnAction(event -> {
            if(Wrap.isSelected()){
                wrapSetting = true;
            }else {
                wrapSetting = false;
            }
            text.setWrapText(wrapSetting);
        });
        MenuItem f12 = new MenuItem("12px");
        MenuItem f15 = new MenuItem("15px");
        MenuItem f20 = new MenuItem("20px");
        MenuItem f30 = new MenuItem("30px");
        MenuItem f45 = new MenuItem("45px");
        MenuItem custom = new MenuItem("Custom");
        window.setOnCloseRequest(e-> {
            if(name!=""){
                ReadFile readFile = new ReadFile();
                readFile.OpenFile(name);
                readFile.GetFiles();
                String s = readFile.GiveFiles();
                readFile.CloseFile();

                if(s.equals(text.getText())){

                }else {

                }
            }
        });
        custom.setOnAction(e-> {
            try{
                font = Integer.parseInt(JOptionPane.showInputDialog("Enter Font Value"));
                if(font > 100){
                    JOptionPane.showMessageDialog(null, "Calm Down Buddy!");
                    font = 30;
                }
            }catch (Exception ev){
                JOptionPane.showMessageDialog(null, "Enter A Number");
            }finally {
                text.setFont(new Font(font));
            }
        });
        Font.getItems().addAll(f12, f15, f20, f30, f45, custom);
        Edit.getItems().addAll(Font, Wrap, Color);
        Color.getItems().addAll(Chooser);
        f12.setOnAction(e-> {
            font = 12;
            text.setFont(new Font(font));
        });

        f15.setOnAction(e-> {
            font = 15;
            text.setFont(new Font(font));
        });

        f20.setOnAction(e-> {
            font = 20;
            text.setFont(new Font(font));
        });

        f30.setOnAction(e-> {
            font = 30;
            text.setFont(new Font(font));
        });

        f45.setOnAction(e-> {
            font = 45;
            text.setFont(new Font(font));
        });



        MenuItem newFile = new MenuItem("New File");
        MenuItem openFile = new MenuItem("Open File");

        openFile.setOnAction(e->{
            FileChooser fileChooser = new FileChooser();


            //Set extension filter
            //FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All files (*.*)", "*.*");
            //fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File file = fileChooser.showOpenDialog(primaryStage);
            name = file.getAbsolutePath();
            ReadFile readFile = new ReadFile();
            readFile.OpenFile(file.getAbsolutePath());
            readFile.GetFiles();
            text.setText(readFile.GiveFiles());
            readFile.CloseFile();

        });

        newFile.setOnAction(e->{
            text.setText("");
        });
        MenuItem saveFile = new MenuItem("Save As");
        MenuItem SaveNorm = new MenuItem("Save File");
        SaveNorm.setOnAction(e->{
            String s = text.getText();
            if(name==""){
                FileChooser fileChooser = new FileChooser();

                //Set extension filter
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(extFilter);

                //Show save file dialog
                File file = fileChooser.showSaveDialog(primaryStage);
                name = file.getAbsolutePath();
                WriteFile wr = new WriteFile();
                try{
                    wr.OpenFile(text.getText(), file.getAbsolutePath());
                    wr.WriteFile();
                    wr.CloseFile();
                }catch (Exception eff){

                }
            }else {
                File file = new File(name);
                WriteFile wr = new WriteFile();
                try {
                    wr.OpenFile(text.getText(), file.getAbsolutePath());
                    wr.WriteFile();
                    wr.CloseFile();
                } catch (Exception eff) {

                }
            }
        });
        saveFile.setOnAction(e-> {
            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File file = fileChooser.showSaveDialog(primaryStage);
            name = file.getAbsolutePath();
            WriteFile wr = new WriteFile();
            try{
                wr.OpenFile(text.getText(), file.getAbsolutePath());
                wr.WriteFile();
                wr.CloseFile();
            }catch (Exception eff){

            }

        });

        vb.getChildren().addAll(mb);
        MenuItem exit = new MenuItem("Exit");
        MenuItem FullScreen = new MenuItem("Enable FullScreen");
        FullScreen.setOnAction(e-> {
            if(FullScreen.getText().equals("Enable FullScreen")) {
                FullScreen.setText("Disable FullScreen");
                window.setFullScreen(true);
            }else {
                FullScreen.setText("Enable FullScreen");
                window.setFullScreen(false);
            }
        });

        exit.setOnAction(e-> {
           window.close();
        });
        FileMenu.getItems().addAll(openFile,SaveNorm,saveFile, newFile,FullScreen, exit);
        mb.getMenus().addAll(FileMenu, Edit, Prefer);

        text.setStyle("-fx-text-inner-color: " + color + ";");
        text.setFont(new Font(font));
        bb.setTop(vb);

        text.setWrapText(wrapSetting);
        bb.setCenter(text);
        window.setScene(editor);
        window.show();
        }

    public void MakeWindow() {

        Stage stage;

        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("ColorPicker");
        Scene scene = new Scene(new HBox(20), 400, 100);
        HBox box = (HBox) scene.getRoot();
        box.setPadding(new javafx.geometry.Insets(5, 5, 5, 5));

        final ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue(javafx.scene.paint.Color.CORAL);
        box.setAlignment(Pos.CENTER);


        colorPicker.setOnAction(new EventHandler() {
            public void handle(javafx.event.Event t) {
                Color c = colorPicker.getValue();
                color = toRGBCode(c);
                text.setStyle("-fx-text-inner-color: " + color + ";");
            }
        });

        box.getChildren().addAll(colorPicker);

        stage.setScene(scene);
        stage.show();
    }
    public static String toRGBCode( Color color )
    {
        return String.format( "#%02X%02X%02X",
                (int)( color.getRed() * 255 ),
                (int)( color.getGreen() * 255 ),
                (int)( color.getBlue() * 255 ) );
    }

    }

