package com.company;

import javafx.application.Application;
import javafx.event.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Formatter;
import java.util.Scanner;

public class Main extends Application implements Runnable{

    private Stage window;
    private int myTab = 1;
    private String color = "black";
    private boolean wrapSetting = false;
    private Scene editor;
    private static TextArea text;
    private int font = 12;
    private Menu FileMenu;
    private static Stage SettingWindows;
    private String name = "";
    private String[] spd = new String[10];
    private Tab tab;
    private TabPane tabs;
    static String textString = "";
    private String temp;
    String val[];
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        MenuBar mb = new MenuBar();
        window = primaryStage;
        text = new TextArea();
        HBox hb = new HBox();
        GetSetting getSetting = new GetSetting();
        getSetting.OpenFile();
        String values[] = getSetting.GiveSetting();
        getSetting.CloseFile();

        val = values;
        hb.setPadding(new Insets(5, 0, 0, 10));
        hb.setSpacing(1);
        //hb.getChildren().addAll(saveBut,saveAllBut, newBut, openBut,closeBut, supportBut);

        //New File Button
        Button newBut = new Button();
        newBut.setOnAction(e-> {
            newTab();
        });
        newBut.setBackground(Background.EMPTY);
        ImageView img = new ImageView(new Image(getClass().getResourceAsStream("Icons/newFile.png")));
        img.setFitHeight(42);
        img.setFitWidth(42);
        newBut.setGraphic(img);
        hb.getChildren().addAll(newBut);

        //Save File Button

        Button saveBut = new Button();
        saveBut.setBackground(Background.EMPTY);
        ImageView saveBut_img = new ImageView(new Image(getClass().getResourceAsStream("Icons/SaveFile.png")));
        saveBut_img.setFitHeight(42);
        saveBut_img.setFitWidth(42);
        saveBut.setGraphic(saveBut_img);
        hb.getChildren().addAll(saveBut);

        saveBut.setOnAction(e ->{
            String s = text.getText();
            int f = getTab();
            if(tabs.getTabs().get(f).getText().startsWith("New File")){

                FileChooser fileChooser = new FileChooser();

                //Set extension filter
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All Files(*.*)", "*.*");
                fileChooser.getExtensionFilters().add(extFilter);

                //Show save file dialog
                File file = fileChooser.showSaveDialog(primaryStage);
                Path path = file.toPath();
                String String_path = path.getFileName().toString();
                int i = tabs.getSelectionModel().getSelectedIndex();
                tabs.getTabs().get(i).setText(String_path);
                name = file.getAbsolutePath();
                tabs.getTabs().get(i).setId(file.getAbsolutePath());
                WriteFile wr = new WriteFile();
                try{
                    wr.OpenFile(text.getText(), file.getAbsolutePath());
                    wr.WriteFile();
                    wr.CloseFile();
                }catch (Exception eff){

                }
                temp = text.getText();
            }else {
                File file = new File(tabs.getTabs().get(f).getId());
                WriteFile wr = new WriteFile();
                Path path = file.toPath();
                String String_path = path.getFileName().toString();
                int i = tabs.getSelectionModel().getSelectedIndex();
                tabs.getTabs().get(i).setText(String_path);
                tabs.getTabs().get(tabs.getSelectionModel().getSelectedIndex()).setId(file.getAbsolutePath());
                try {
                    wr.OpenFile(text.getText(), file.getAbsolutePath());
                    wr.WriteFile();
                    wr.CloseFile();
                } catch (Exception eff) {

                }
                temp = text.getText();
            }
        });

        //Close File Window Opener

        Button closeBut = new Button();
        closeBut.setBackground(Background.EMPTY);
        ImageView closeBut_img = new ImageView(new Image(getClass().getResourceAsStream("Icons/CloseFile.png")));
        closeBut_img.setFitHeight(42);
        closeBut_img.setFitWidth(42);
        closeBut.setGraphic(closeBut_img);
        hb.getChildren().addAll(closeBut);

        closeBut.setOnAction(e->{
            if(tabs.getTabs().get(tabs.getSelectionModel().getSelectedIndex()).getText().contains("*")){

            }else {
                if(!(tabs.getTabs().size()==1))
                    tabs.getTabs().remove(tabs.getSelectionModel().getSelectedIndex());
            }
        });

        //Open File
        Button openFileBut = new Button();
        openFileBut.setBackground(Background.EMPTY);
        ImageView openFileBut_img = new ImageView(new Image(getClass().getResourceAsStream("Icons/openFile.png")));
        openFileBut_img.setFitHeight(42);
        openFileBut_img.setFitWidth(42);
        openFileBut.setGraphic(openFileBut_img);
        hb.getChildren().addAll(openFileBut);


        openFileBut.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();


            //Set extension filter
            //FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All files (*.*)", "*.*");
            //fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File file = fileChooser.showOpenDialog(primaryStage);
            name = file.getAbsolutePath();

            Path path = file.toPath();
            String parth = path.getFileName().toString();
            setTab(parth, path.toString());

            ReadFile readFile = new ReadFile();
            readFile.OpenFile(file.getAbsolutePath());
            readFile.GetFiles();
            text.setText(readFile.GiveFiles());
            temp = readFile.GiveFiles();


            readFile.CloseFile();

        });

        //CMD Window

        Button cmdBut = new Button();
        cmdBut.setBackground(Background.EMPTY);
        ImageView cmdBut_img = new ImageView(new Image(getClass().getResourceAsStream("Icons/cmd.png")));
        cmdBut_img.setFitHeight(42);
        cmdBut_img.setFitWidth(42);
        cmdBut.setGraphic(cmdBut_img);
        hb.getChildren().addAll(cmdBut);

        cmdBut.setOnAction(e-> {
            try {
                Runtime.getRuntime().exec("cmd /c start");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        //Programming Window

        Button progBut = new Button();
        progBut.setBackground(Background.EMPTY);
        ImageView progBut_img = new ImageView(new Image(getClass().getResourceAsStream("Icons/programming.png")));
        progBut_img.setFitHeight(42);
        progBut_img.setFitWidth(42);
        progBut.setGraphic(progBut_img);
        hb.getChildren().addAll(progBut);

        progBut.setOnAction(e-> {
            Editor editor1 = new Editor();
            editor1.code = text.getText();
            editor1.start();
        });

        //Find Button

        Button findBut = new Button();
        findBut.setBackground(Background.EMPTY);
        ImageView findBut_img = new ImageView(new Image(getClass().getResourceAsStream("Icons/find.png")));
        findBut_img.setFitHeight(42);
        findBut_img.setFitWidth(42);
        findBut.setGraphic(findBut_img);
        hb.getChildren().addAll(findBut);

        //Run Java Program
        Button runBut = new Button();
        runBut.setBackground(Background.EMPTY);
        ImageView runBut_img = new ImageView(new Image(getClass().getResourceAsStream("Icons/FunJava.png")));
        runBut_img.setFitHeight(42);
        runBut_img.setFitWidth(42);
        runBut.setGraphic(runBut_img);
        hb.getChildren().addAll(runBut);

        //Html Viewer

        Button htmlBut = new Button();
        htmlBut.setBackground(Background.EMPTY);
        ImageView htmlBut_img = new ImageView(new Image(getClass().getResourceAsStream("Icons/openHtml.png")));
        htmlBut_img.setFitHeight(42);
        htmlBut_img.setFitWidth(42);
        htmlBut.setGraphic(htmlBut_img);
        hb.getChildren().addAll(htmlBut);

        //Settings
        Button setBut = new Button();
        setBut.setBackground(Background.EMPTY);
        ImageView setBut_img = new ImageView(new Image(getClass().getResourceAsStream("Icons/setting.png")));
        setBut_img.setFitHeight(42);
        setBut_img.setFitWidth(42);
        setBut.setGraphic(setBut_img);
        hb.getChildren().addAll(setBut);

        //Help

        Button helpBut = new Button();
        helpBut.setBackground(Background.EMPTY);
        ImageView helpBut_img = new ImageView(new Image(getClass().getResourceAsStream("Icons/help.png")));
        helpBut_img.setFitHeight(42);
        helpBut_img.setFitWidth(42);
        helpBut.setGraphic(helpBut_img);
        hb.getChildren().addAll(helpBut);

        if(val[0].equals("true")){
            window.setFullScreen(true);
        }

        BorderPane bb = new BorderPane();
        VBox vb = new VBox();

        window.setTitle("SlickPad");
        editor = new Scene(bb,628,532);
        text.setOnKeyTyped(e-> {
            if(tabs.getTabs().get(tabs.getSelectionModel().getSelectedIndex()).getText().contains("*")){

            }else
                tabs.getTabs().get(tabs.getSelectionModel().getSelectedIndex()).setText(tabs.getTabs().get(tabs.getSelectionModel().getSelectedIndex()).getText() + "*");
        });
        final AnchorPane root = new AnchorPane();
        tabs = new TabPane();
        final Button addButton = new Button("+");

        AnchorPane.setTopAnchor(tabs, 5.0);
        AnchorPane.setLeftAnchor(tabs, 5.0);
        AnchorPane.setRightAnchor(tabs, 5.0);
        AnchorPane.setTopAnchor(addButton, 10.0);
        AnchorPane.setRightAnchor(addButton, 10.0);
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tab = new Tab("New File  " + myTab + ".txt");
                tab.setId(tab.getText());
                myTab++;
                if(tabs.getTabs().size()==1){
                    tab.setClosable(false);
                }
                tabs.getTabs().add(tab);
                tabs.getSelectionModel().select(tab);

                if(tabs.getTabs().size()==1){
                    tab.setClosable(false);
                }else {
                    tab.setClosable(true);
                }
                text.setText("");
            }
        });


        root.getChildren().addAll(tabs, addButton);
        newTab();

        FileMenu = new Menu("File");
        MenuItem recover = new MenuItem("Recover Unsaved Files");

        recover.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            String s = (System.getProperty("user.dir") + System.getProperty("file.separator")+ "AutoSave\\");
            System.out.println(s);
            fileChooser.setInitialDirectory(new File(s));
            File file = fileChooser.showOpenDialog(primaryStage);
            name = file.getAbsolutePath();

            Path path = file.toPath();
            String parth = path.getFileName().toString();
            setTab(parth, path.toString());

            ReadFile readFile = new ReadFile();
            readFile.OpenFile(file.getAbsolutePath());
            readFile.GetFiles();
            text.setText(readFile.GiveFiles());
            temp = readFile.GiveFiles();



            readFile.CloseFile();

        });
        FileMenu.getItems().add(recover);

        Menu Edit = new Menu("Edit");
        Menu JavaProgram = new Menu("Java Program");
        MenuItem run = new MenuItem("Compile");
        MenuItem exec = new MenuItem("Execute");

        JavaProgram.getItems().addAll(run, exec);
        run.setOnAction(e-> {
            try {

                /*if(tabs.getTabs().get(tabs.getSelectionModel().getSelectedIndex()).getText().contains("*")){
                    Stage stg = new Stage();
                    VBox vBox = new VBox();
                    Scene scene = new Scene(vBox, 400,400);

                }
                */
                if (tabs.getTabs().get(tabs.getSelectionModel().getSelectedIndex()).getText().endsWith(".java")) {
                    Scanner s = new Scanner(System.in);
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Popup.fxml"));

                    try {
                        Parent root1 = (Parent) fxmlLoader.load();
                        Stage Pop;
                        Pop = new Stage();
                        Pop.initModality(Modality.APPLICATION_MODAL);
                        Pop.setTitle("Enter Value");
                        Pop.setScene(new Scene(root1));
                        Pop.setResizable(false);
                        Pop.showAndWait();

                    }catch (Exception er){


                    }
                    String spd = (tabs.getTabs().get(tabs.getSelectionModel().getSelectedIndex()).getId().replace(tabs.getTabs().get(tabs.getSelectionModel().getSelectedIndex()).getText(), ""));
                    System.out.println(spd);
                    Formatter formatter = new Formatter("Start.bat");
                    formatter.format("@echo off \n"+ spd.substring(0, 2) + "\n cd " + spd + "\n java " + Input.className + "\n pause > nul \n exit");


                    formatter.close();
                    String className = Input.className;
                    Runtime.getRuntime().exec("cmd /c start javac " + tabs.getTabs().get(tabs.getSelectionModel().getSelectedIndex()).getId());


                }

            } catch (Exception error) {
                System.out.println(error);
            }

            //Code for Terminal. Do your editing as needed.

            /*



            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("CMD.fxml"));
            try {
                Parent root1 = (Parent) fxmlLoader.load();
                SettingWindows = new Stage();
                SettingWindows.initModality(Modality.APPLICATION_MODAL);
                SettingWindows.setTitle("Settings");
                SettingWindows.setScene(new Scene(root1));
                SettingWindows.setResizable(false);
                SettingWindows.showAndWait();
            } catch (Exception error) {

            }

*/
        });

        exec.setOnAction(e-> {
            try{
                Runtime.getRuntime().exec("cmd /c start Start.bat");
            }catch (Exception error){

            }
        });
        Edit.getItems().add(JavaProgram);
        Menu Prefer = new Menu("Preferences");
        MenuItem Setting = new MenuItem("Setting");
        Setting.setOnAction(e-> {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Setting.fxml"));
                try {
                    Parent root1 = (Parent) fxmlLoader.load();
                    SettingWindows = new Stage();
                    SettingWindows.initModality(Modality.APPLICATION_MODAL);
                    SettingWindows.setTitle("Settings");
                    SettingWindows.setScene(new Scene(root1));
                    SettingWindows.setResizable(false);
                    SettingWindows.showAndWait();

                }catch (Exception er){


                }
        });


        Prefer.getItems().addAll(Setting);
        Menu Color = new Menu("Color");
        Menu BasicColor = new Menu("Default Colors");
        tabs.getSelectionModel().selectedItemProperty().addListener((o, old, ne) -> {

            /*try {
                    if (old.getId().startsWith("Default")) {
                        old.setId("Default" + text.getText());
                    }
                if(old.getId().replace("Default", "").equals("")){
                    tabs.getTabs().remove(old);
                }
                    if (ne.getId().startsWith("Default")) {
                        if (ne.getId().equals("Default")) {

                        } else {
                            text.setText(ne.getId().replaceAll("Default", ""));
                        }
                    }
                    if (ne.getText().startsWith("New File")) {

                    } else {
                        ReadFile readFile = new ReadFile();
                        readFile.OpenFile(ne.getId());
                        readFile.GetFiles();
                        readFile.CloseFile();
                        text.setText(readFile.GiveFiles());
                    }


                }catch(NullPointerException e){
                    //First Time Run
                }
*/
            WriteFile writeFile = new WriteFile();
            try {
                writeFile.OpenFile(text.getText(), old.getText());
                writeFile.WriteFile();
                writeFile.CloseFile();
            } catch (Exception error) {

            }


            File file = new File(ne.getId());

            if (file.exists()) {
                ReadFile readFile = new ReadFile();
                readFile.OpenFile(file.getAbsolutePath());
                readFile.GetFiles();
                readFile.CloseFile();
                text.setText(readFile.GiveFiles());
            } else {
                text.setText("");
            }


        });

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

        text.setStyle("-fx-text-fill: #" + val[3].substring(2,8));
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
        /*custom.setOnAction(e-> {
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
        */

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
        MenuItem ProgrammerWindow = new MenuItem("Programmer Window");
        ProgrammerWindow.setOnAction(e-> {
            Editor editor1 = new Editor();
            editor1.code = text.getText();
            editor1.start();

        });

        openFile.setOnAction(e->{
            FileChooser fileChooser = new FileChooser();


            //Set extension filter
            //FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All files (*.*)", "*.*");
            //fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File file = fileChooser.showOpenDialog(primaryStage);
            name = file.getAbsolutePath();

            Path path = file.toPath();
            String parth = path.getFileName().toString();
            setTab(parth, path.toString());

            ReadFile readFile = new ReadFile();
            readFile.OpenFile(file.getAbsolutePath());
            readFile.GetFiles();
            text.setText(readFile.GiveFiles());
            temp = readFile.GiveFiles();



            readFile.CloseFile();

        });


        newFile.setOnAction(e->{
            text.setText("");
            newTab();
        });
        MenuItem saveFile = new MenuItem("Save As");
        MenuItem SaveNorm = new MenuItem("Save File");
        SaveNorm.setOnAction(e->{
            String s = text.getText();
            int f = getTab();
            if(tabs.getTabs().get(f).getText().startsWith("New File")){

                FileChooser fileChooser = new FileChooser();

                //Set extension filter
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All Files(*.*)", "*.*");
                fileChooser.getExtensionFilters().add(extFilter);

                //Show save file dialog
                File file = fileChooser.showSaveDialog(primaryStage);
                Path path = file.toPath();
                String String_path = path.getFileName().toString();
                int i = tabs.getSelectionModel().getSelectedIndex();
                tabs.getTabs().get(i).setText(String_path);
                name = file.getAbsolutePath();
                tabs.getTabs().get(i).setId(file.getAbsolutePath());
                WriteFile wr = new WriteFile();
                try{
                    wr.OpenFile(text.getText(), file.getAbsolutePath());
                    wr.WriteFile();
                    wr.CloseFile();
                }catch (Exception eff){

                }
                temp = text.getText();
            }else {
                File file = new File(tabs.getTabs().get(f).getId());
                WriteFile wr = new WriteFile();
                Path path = file.toPath();
                String String_path = path.getFileName().toString();
                int i = tabs.getSelectionModel().getSelectedIndex();
                tabs.getTabs().get(i).setText(String_path);
                tabs.getTabs().get(tabs.getSelectionModel().getSelectedIndex()).setId(file.getAbsolutePath());
                try {
                    wr.OpenFile(text.getText(), file.getAbsolutePath());
                    wr.WriteFile();
                    wr.CloseFile();
                } catch (Exception eff) {

                }
                temp = text.getText();
            }
        });
        saveFile.setOnAction(e-> {
            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All Files (*.*)", "*.*");
            fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File file = fileChooser.showSaveDialog(primaryStage);
            name = file.getAbsolutePath();

            Path path = file.toPath();
            String filename = path.getFileName().toString();
            tabs.getTabs().get(tabs.getSelectionModel().getSelectedIndex()).setText(filename);
            tabs.getTabs().get(tabs.getSelectionModel().getSelectedIndex()).setId(file.getAbsolutePath());

            WriteFile wr = new WriteFile();
            try{
                wr.OpenFile(text.getText(), file.getAbsolutePath());
                wr.WriteFile();
                wr.CloseFile();
            }catch (Exception eff){

            }

        });
        vb.getChildren().addAll(mb);
        vb.getChildren().add(hb);

        vb.getChildren().add(root);
        MenuItem exit = new MenuItem("Exit");
        MenuItem FullScreen = new MenuItem("Enable FullScreen");
        if(window.isFullScreen()){
            FullScreen.setText("Disable FullScreen");
        }

        window.setOnCloseRequest(close-> {
            try {
                System.out.println(val[1]);
                if(val[1].equals("true")){
                    Runtime.getRuntime().exec("cmd /c copy.bat");
                }else {
                    Runtime.getRuntime().exec("cmd /c end.bat");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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
        FileMenu.getItems().addAll(openFile,SaveNorm,saveFile,FullScreen,ProgrammerWindow, exit);
        mb.getMenus().addAll(FileMenu, Edit, Prefer);
        text.setFont(new Font(Integer.parseInt(val[2])));
        bb.setTop(vb);

        text.setWrapText(wrapSetting);
        bb.setCenter(text);
        window.setScene(editor);
        window.show();
        Region region = ( Region ) text.lookup( ".content" );
        region.setBackground( new Background( new BackgroundFill( Paint.valueOf("#292f38"), CornerRadii.EMPTY, Insets.EMPTY ) ) );

        // Or you can set it by setStyle()
        region.setStyle( "-fx-background-color: #292f38" );
        }

    private void newTab() {
        tab = new Tab("New File  " + myTab + ".txt");
        tab.setId(tab.getText());
        myTab++;
        if(tabs.getTabs().size()==1){
            tab.setClosable(false);
        }
        tabs.getTabs().add(tab);
        tabs.getSelectionModel().select(tab);

        if(tabs.getTabs().size()==1){
            tab.setClosable(false);
        }else {
            tab.setClosable(true);
        }

        text.setDisable(false);
    }

    private int getTab() {
        return tabs.getSelectionModel().getSelectedIndex();
    }

    private void setTab(String parth, String URL) {
        int f = tabs.getSelectionModel().getSelectedIndex();
        tabs.getTabs().get(f).setText(parth);
        tabs.getTabs().get(f).setId(URL);
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
    public static String toRGBCode( Color color)
    {
        return String.format( "#%02X%02X%02X",
                (int)( color.getRed() * 255 ),
                (int)( color.getGreen() * 255 ),
                (int)( color.getBlue() * 255 ) );
    }


    public static void writeText(String s){
        text.setText(s);
    }

    public static void closeWindow() {
        SettingWindows.close();
    }

    public static void setColor(String color){
        text.setStyle("-fx-text-fill: " + color);

    }
    @Override
    public void run() {

        if(new File(spd + Input.className + ".class").exists()){

            try{
                System.out.println("Well Done");
            Runtime.getRuntime().exec("cmd /c start Start.bat");
            }catch (Exception e){

            }
        }
    }
}

