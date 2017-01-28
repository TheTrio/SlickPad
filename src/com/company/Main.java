package com.company;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javax.swing.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Formatter;
import java.util.Scanner;

public class Main extends Application {


    private Stage window;
    public static String myText = "";
    private int myTab = 1;
    public static boolean bool_notify = false;
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
    private String val[];
    private Pane pane;
    private BorderPane bb;
    private TextArea textArea;


    public static void main(String args[]) {
        if (System.getProperty("os.name").startsWith("Windows")) {
            launch(args);
        } else {
            System.out.println("Sorry. SlickPad v3.0.0 is only compatible with Windows. Visit the website for more");
        }
    }

    public static String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    public static void writeText(String s) {
        text.setText(s);
    }

    public static void setColor(String color) {
        text.setStyle("-fx-text-fill: " + color);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        SettingWindows = new Stage();
        MenuBar mb = new MenuBar();
        window = primaryStage;
        makeSplash();
        text = new TextArea();
        text.setOnKeyTyped(e -> {
            if(!tabs.getSelectionModel().getSelectedItem().getText().startsWith("New File")) {
                System.out.println("True");
                //tabs.getSelectionModel().getSelectedItem().setStyle("-fx-background-color : #FF7043");
            }
        });
        HBox hb = new HBox();
        hb.setPadding(new Insets(5, 0, 0, 10));
        hb.setSpacing(1);
        //hb.getChildren().addAll(saveBut,saveAllBut, newBut, openBut,closeBut, supportBut);

        //New File Button
        Button newBut = new Button();
        newBut.setId("NewTab");
        newBut.setOnAction(e -> {
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

        saveBut.setOnAction(e -> {
            String s = text.getText();
            int f = getTab();
            //tabs.getSelectionModel().getSelectedItem().st
            if (tabs.getTabs().get(f).getText().startsWith("New File")) {

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
                try {
                    wr.OpenFile(text.getText(), file.getAbsolutePath());
                    wr.WriteFile();
                    wr.CloseFile();
                } catch (Exception eff){
                }
                temp = text.getText();
            } else {
                System.out.println(tabs.getSelectionModel().getSelectedItem().getId());
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

        closeBut.setOnAction(e -> {
            if (tabs.getTabs().get(tabs.getSelectionModel().getSelectedIndex()).getText().contains("*")) {

            } else {
                if (!(tabs.getTabs().size() == 1))
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

        cmdBut.setOnAction(e -> {
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

        progBut.setOnAction(e -> {
            ControllerForChooser.s = text.getText();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Chooser.fxml"));

            try {
                Parent root = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle("SlickPad");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        });
        //Run Java Program
        Button runBut = new Button();
        runBut.setBackground(Background.EMPTY);
        ImageView runBut_img = new ImageView(new Image(getClass().getResourceAsStream("Icons/FunJava.png")));
        runBut_img.setFitHeight(42);
        runBut_img.setFitWidth(42);
        runBut.setGraphic(runBut_img);
        hb.getChildren().addAll(runBut);

        runBut.setOnAction(e -> {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RunChooser.fxml"));
            ControllerForRun.m = this;
            if (tabs.getTabs().get(tabs.getSelectionModel().getSelectedIndex()).getId().contains("New File")) {
                bool_notify = true;
                myText = text.getText();
            }
            try {
                Parent root = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle("SlickPad");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });


        //Html Viewer

        Button htmlBut = new Button();
        htmlBut.setBackground(Background.EMPTY);
        ImageView htmlBut_img = new ImageView(new Image(getClass().getResourceAsStream("Icons/openHtml.png")));
        htmlBut_img.setFitHeight(42);
        htmlBut_img.setFitWidth(42);
        htmlBut.setGraphic(htmlBut_img);
        hb.getChildren().addAll(htmlBut);

        htmlBut.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (tabs.getTabs().get(tabs.getSelectionModel().getSelectedIndex()).getText().contains(".htm")) {
                    try {
                        java.awt.Desktop.getDesktop().browse((new File(tabs.getTabs().get(tabs.getSelectionModel().getSelectedIndex()).getId()).toURI()));
                    } catch (Exception error) {

                    }
                } else if (tabs.getTabs().get(tabs.getSelectionModel().getSelectedIndex()).getText().startsWith("New File")) {
                    WriteFile writeFile = new WriteFile();
                    try {
                        File f = new File("temp");
                        if (!f.exists()) {
                            f.mkdir();
                            System.out.println("Directory Made");
                        }
                        writeFile.OpenFile(text.getText(), "temp//Unsaved.html");
                        writeFile.WriteFile();
                        writeFile.CloseFile();
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }

                    try {
                        java.awt.Desktop.getDesktop().browse((new File("temp//Unsaved.html")).toURI());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        //Settings
        Button setBut = new Button();
        setBut.setBackground(Background.EMPTY);
        ImageView setBut_img = new ImageView(new Image(getClass().getResourceAsStream("Icons/setting.png")));
        setBut_img.setFitHeight(42);
        setBut_img.setFitWidth(42);
        setBut.setGraphic(setBut_img);
        hb.getChildren().addAll(setBut);

        setBut.setOnAction(e -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Setting.fxml"));

            try {
                Parent root = (Parent) fxmlLoader.load();

                SettingWindows = new Stage();
                SettingWindows.setTitle("Settings");
                SettingWindows.initModality(Modality.APPLICATION_MODAL);
                SettingWindows.setScene(new Scene(root));
                SettingWindows.setResizable(false);
                SettingWindows.show();

            } catch (IOException e1) {
                e1.printStackTrace();
            }

        });

        //Help

        Button helpBut = new Button();
        helpBut.setBackground(Background.EMPTY);
        ImageView helpBut_img = new ImageView(new Image(getClass().getResourceAsStream("Icons/help.png")));
        helpBut_img.setFitHeight(42);
        helpBut_img.setFitWidth(42);
        helpBut.setGraphic(helpBut_img);
        hb.getChildren().addAll(helpBut);

        helpBut.setOnAction(e -> {
            try {
                java.awt.Desktop.getDesktop().browse(new URI("https://github.com/TheTrio/SlickPad"));
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
        });


        bb = new BorderPane();
        VBox vb = new VBox();

        window.setTitle("SlickPad");
        editor = new Scene(bb, 628, 532);

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
                if (tabs.getTabs().size() == 1) {
                    tab.setClosable(false);
                }
                tabs.getTabs().add(tab);
                tabs.getSelectionModel().select(tab);

                if (tabs.getTabs().size() == 1) {
                    tab.setClosable(false);
                } else {
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
            String s = (System.getProperty("user.dir") + System.getProperty("file.separator") + "AutoSave\\");
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
        MenuItem run = new MenuItem("Compile And Run");

        JavaProgram.getItems().addAll(run);
        run.setOnAction(e -> {
            textArea.setText("");
            try {
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

                    } catch (Exception er) {
                    }
                    String spd = (tabs.getTabs().get(tabs.getSelectionModel().getSelectedIndex()).getId().replace(tabs.getTabs().get(tabs.getSelectionModel().getSelectedIndex()).getText(), ""));
                    System.out.println(spd);
                    Formatter formatter = new Formatter("Start.bat");
                    formatter.format("@echo off\n" + "\"" + SettingWindow.StringPathJava + "/bin/javac\" " + "\"" + tabs.getSelectionModel().getSelectedItem().getId() + "\"");

                    System.out.println(tabs.getTabs().get(tabs.getSelectionModel().getSelectedIndex()).getId());
                    formatter.close();

                    Formatter newFormatter = new Formatter("Execute.bat");
                    newFormatter.format("@echo off\n" + "\"" + SettingWindow.StringPathJava + "/bin/javac\" " + "\"" + tabs.getSelectionModel().getSelectedItem().getId() + "\"" + "\n" + "start cb_console_runner.exe " + "\"" + SettingWindow.StringPathJava + "\\bin\\java\" -cp \"" + tabs.getSelectionModel().getSelectedItem().getId().replace(tabs.getSelectionModel().getSelectedItem().getText(), "").replace("\\", "/") + "\" " + Input.className);
                    newFormatter.close();

                    Process p = Runtime.getRuntime().exec("cmd /c Start.bat");

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                    String as;
                    String errorString = "Your Program had the following errors \n";
                    boolean errorfound = false;

                    Parent scene = new FXMLLoader(getClass().getResource("Loading.fxml")).load();
                    Scene newScene = new Scene(scene, 366, 258);
                    Stage stage = new Stage();
                    stage.initStyle(StageStyle.UTILITY);
                    stage.setScene(newScene);
                    stage.show();

                    while ((as = bufferedReader.readLine()) != null) {
                        errorString = errorString + "\n" + as;
                        errorfound = true;
                    }

                    if (errorfound) {
                        ChangeText(errorString);
                        stage.hide();
                    } else {
                        Thread thread = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    Runtime.getRuntime().exec("cmd /c Execute.bat");
                                } catch (IOException e1) {
                                    System.out.println(e1);
                                }
                            }
                        };
                        thread.start();
                        thread.join();
                        stage.hide();
                    }


                } else {
                    JOptionPane.showMessageDialog(null, "Please Save Your File first");
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

        Edit.getItems().add(JavaProgram);
        Menu Prefer = new Menu("Preferences");
        Menu View = new Menu("View");
        CheckMenuItem QuickBar = new CheckMenuItem("QuickBar");
        QuickBar.setSelected(true);
        CheckMenuItem TerminalEmulator = new CheckMenuItem("Terminal Emulator");
        QuickBar.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (QuickBar.isSelected()) {
                vb.getChildren().add(1, hb);
            } else {
                vb.getChildren().remove(hb);
            }
        });
        TerminalEmulator.setSelected(true);
        TerminalEmulator.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (TerminalEmulator.isSelected()) {
                    bb.setBottom(pane);
                } else {
                    bb.setBottom(null);
                }
            }
        });

        View.getItems().addAll(QuickBar, TerminalEmulator);
        Prefer.getItems().add(View);
        MenuItem Setting = new MenuItem("Setting");
        Setting.setOnAction(e -> {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Setting.fxml"));
            try {
                Parent root1 = (Parent) fxmlLoader.load();
                SettingWindows.initModality(Modality.APPLICATION_MODAL);
                SettingWindows.setTitle("Settings");
                SettingWindows.setScene(new Scene(root1));
                SettingWindows.setResizable(false);
                SettingWindows.showAndWait();

            } catch (Exception er) {


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
            System.out.println(file.getAbsolutePath());

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
        Chooser.setOnAction(e -> {
            MakeWindow();
        });
        MenuItem red = new MenuItem("Red");
        red.setOnAction(e -> {
            color = "red";
            text.setStyle("-fx-text-inner-color: " + color + ";");
        });
        MenuItem blue = new MenuItem("Blue");
        blue.setOnAction(e -> {
            color = "blue";
            text.setStyle("-fx-text-inner-color: " + color + ";");
        });
        MenuItem pink = new MenuItem("Pink");
        pink.setOnAction(e -> {
            color = "pink";
            text.setStyle("-fx-text-inner-color: " + color + ";");
        });
        MenuItem green = new MenuItem("Green");
        green.setOnAction(e -> {
            color = "green";
            text.setStyle("-fx-text-inner-color: " + color + ";");
        });

        BasicColor.getItems().addAll(red, blue, pink, green);
        Color.getItems().addAll(BasicColor);

        Menu Font = new Menu("Font");
        CheckMenuItem Wrap = new CheckMenuItem("Word Wrap");
        Wrap.setOnAction(event -> {
            if (Wrap.isSelected()) {
                wrapSetting = true;
            } else {
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
        f12.setOnAction(e -> {
            font = 12;
            text.setFont(new Font(font));
        });

        f15.setOnAction(e -> {
            font = 15;
            text.setFont(new Font(font));
        });

        f20.setOnAction(e -> {
            font = 20;
            text.setFont(new Font(font));
        });

        f30.setOnAction(e -> {
            font = 30;
            text.setFont(new Font(font));
        });

        f45.setOnAction(e -> {
            font = 45;
            text.setFont(new Font(font));
        });
        MenuItem newFile = new MenuItem("New File");
        MenuItem openFile = new MenuItem("Open File");
        MenuItem ProgrammerWindow = new MenuItem("Programmer Window");
        ProgrammerWindow.setOnAction(e -> {
            Editor editor1 = new Editor(
            );
            editor1.code = text.getText();
            editor1.start();

        });

        openFile.setOnAction(e -> {
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


        newFile.setOnAction(e -> {
            text.setText("");
            newTab();
        });
        MenuItem saveFile = new MenuItem("Save As");
        MenuItem SaveNorm = new MenuItem("Save File");
        SaveNorm.setOnAction(e -> {
            String s = text.getText();
            int f = getTab();
            if (tabs.getTabs().get(f).getText().startsWith("New File")) {
                FileChooser fileChooser = new FileChooser();
                tabs.getSelectionModel().getSelectedItem().setStyle("-fx-background-color: green");
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
                try {
                    wr.OpenFile(text.getText(), file.getAbsolutePath());
                    wr.WriteFile();
                    wr.CloseFile();
                } catch (Exception eff) {

                }
                temp = text.getText();
            } else {
                tabs.getSelectionModel().getSelectedItem().setStyle("-fx-background-color: green");
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
        saveFile.setOnAction(e -> {
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
            try {
                wr.OpenFile(text.getText(), file.getAbsolutePath());
                wr.WriteFile();
                wr.CloseFile();
            } catch (Exception eff) {

            }

        });
        vb.getChildren().addAll(mb);
        vb.getChildren().add(hb);
        vb.getChildren().add(root);
        MenuItem exit = new MenuItem("Exit");
        MenuItem FullScreen = new MenuItem("Enable FullScreen");
        if (window.isFullScreen()) {
            FullScreen.setText("Disable FullScreen");
        }


        FullScreen.setOnAction(e -> {
            if (FullScreen.getText().equals("Enable FullScreen")) {
                FullScreen.setText("Disable FullScreen");
                window.setFullScreen(true);
            } else {
                FullScreen.setText("Enable FullScreen");
                window.setFullScreen(false);
            }
        });

        exit.setOnAction(e -> {
            System.exit(0);
        });
        FileMenu.getItems().addAll(openFile, SaveNorm, saveFile, FullScreen, ProgrammerWindow, exit);
        mb.getMenus().addAll(FileMenu, Edit, Prefer);

        bb.setTop(vb);

        text.setWrapText(wrapSetting);
        bb.setCenter(text);
        Pane bottom = new Pane();
        TextArea textArea = new TextArea();
        textArea.setMinHeight(20);
        bottom.getChildren().add(textArea);
        editor.getStylesheets().add(getClass().getResource("Error.css").toExternalForm());
        text.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent e) {
                Dragboard db = e.getDragboard();
                if (db.hasFiles()) {
                    e.acceptTransferModes(TransferMode.COPY);
                }
                e.consume();
            }
        });

        text.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                System.out.println("Hello");
                String file = null;
                success = true;
                String FileName = null;
                for (File f : db.getFiles()) {
                    file = f.getAbsolutePath();
                    FileName = f.getName();
                }
                ReadFile readFile = new ReadFile();
                readFile.OpenFile(file);
                readFile.GetFiles();
                System.out.println(readFile.GiveFiles());
                text.setText(readFile.GiveFiles());
                readFile.CloseFile();
                tabs.getSelectionModel().getSelectedItem().setText(FileName);
                tabs.getSelectionModel().getSelectedItem().setId(file);

            }
            e.setDropCompleted(success);
        });
        window.setScene(editor);
        MakePane();

        GetSetting getSetting = new GetSetting();
        getSetting.OpenFile();
        String values[] = getSetting.GiveSetting();
        getSetting.CloseFile();


        val = values;
        SettingWindow.StringPathJava = val[4];
        SettingWindow.StringPathC = val[5];
        text.setFont(new Font(Integer.parseInt(val[2])));
        if (val[0].equals("true")) {
            window.setFullScreen(true);
            FullScreen.setText("Disable FullScreen");

        } else {
            FullScreen.setText("Enable FullScreen");
        }
        text.setStyle("-fx-text-fill: #" + val[3].substring(2, 8));
        window.setOnCloseRequest(close -> {
            try {
                System.out.println(val[1]);
                if (val[1].equals("true")) {
                    Runtime.getRuntime().exec("cmd /c copy.bat");
                } else {
                    Runtime.getRuntime().exec("cmd /c end.bat");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            close.consume();
            System.exit(0);

        });

    }

    public void ChangeText(String errorString) {
        textArea.setText(errorString);
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

    private void MakePane() {
        pane = new Pane();

        textArea = new TextArea();
        textArea.setLayoutY(0);
        textArea.setLayoutX(0);
        textArea.setId("TextBox");
        textArea.setPrefWidth(window.getScene().getWidth());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        Button button = new Button();
        button.setId("MakeBut");
        pane.setId("OuterPane");

        ImageView imgView1 = new ImageView(new Image(getClass().getResource("down.png").toExternalForm()));
        imgView1.setPreserveRatio(true);
        imgView1.setFitHeight(20);

        ImageView imgView2 = new ImageView(new Image(getClass().getResource("up.png").toExternalForm()));
        imgView2.setPreserveRatio(true);
        imgView2.setFitHeight(20);
        button.setGraphic(imgView1);
        window.getScene().widthProperty().addListener((observable, oldValue, newValue) -> {
            textArea.setPrefWidth(newValue.doubleValue());
        });
        textArea.widthProperty().addListener((observable, oldValue, newValue) -> {
            button.setTranslateX(textArea.getWidth() / 2);
            button.setTranslateY(textArea.getLayoutY());
        });

        button.setOnAction(ewerwer -> {
            if (button.getGraphic() == imgView1) {
                button.setGraphic(imgView2);
                pane.getChildren().remove(textArea);
                button.toFront();
            } else {
                button.setGraphic(imgView1);
                pane.getChildren().add(textArea);
                button.toFront();
            }
        });
        pane.getChildren().add(button);
        pane.getChildren().add(textArea);
        bb.setBottom(pane);
        button.toFront();
        button.setTranslateX(textArea.getPrefWidth() / 2);
    }

    private void makeSplash() throws InterruptedException {
        Stage SplashStage = new Stage();
        VBox vBox = new VBox();
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("Splash.png")));
        vBox.getChildren().add(imageView);

        SplashStage.setScene(new Scene(vBox, 800, 509));
        SplashStage.initStyle(StageStyle.UNDECORATED);
        SplashStage.show();
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> {
            window.show();
            Region region = (Region) text.lookup(".content");
            region.setBackground(new Background(new BackgroundFill(Paint.valueOf("#292f38"), CornerRadii.EMPTY, Insets.EMPTY)));
            SplashStage.close();
        });
        pause.play();
    }

    public static void closeWindow() {
        SettingWindows.close();
    }

    private void newTab() {
        tab = new Tab("New File  " + myTab + ".txt");
        tab.setId(tab.getText());
        myTab++;
        if (tabs.getTabs().size() == 1) {
            tab.setClosable(false);
        }
        tabs.getTabs().add(tab);
        tabs.getSelectionModel().select(tab);

        if (tabs.getTabs().size() == 1) {
            tab.setClosable(false);
        } else {
            tab.setClosable(true);
        }

        text.setDisable(false);
    }

}

