package com.company;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.Formatter;
import java.util.Scanner;


public class ControllerForRun implements Runnable {
    public static Main m;
    public void Java(ActionEvent e) {
        if (Main.bool_notify == true) {

            try {
                Formatter form = new Formatter("temp.java");
                form.format("%s", Main.myText);
                form.close();
                Thread thread = new Thread(this);
                thread.start();
                thread.join();
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
                String className = Input.className;
                Formatter formatter = new Formatter("Jrun.bat");
                formatter.format("@echo off\n" + "\"" + SettingWindow.StringPathJava + "\\bin\\javac\"" + " temp.java\n" + "start cb_console_runner.exe java " + className);
                formatter.close();
                Process p = Runtime.getRuntime().exec("cmd /c \"" + SettingWindow.StringPathJava + "\\bin\\javac\"" + " temp.java");
                System.out.println("cmd /c \"" + SettingWindow.StringPathJava + "\\bin\\javac\"" + " temp.java");
                BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(p.getErrorStream()));
                String as;
                String errorString = "Your Program had the following errors \n";
                boolean errorfound = false;
                while ((as = bufferedReader.readLine()) != null) {
                    errorString = errorString + "\n" + as;
                    errorfound = true;
                }
                System.out.println(errorfound);
                if(errorfound){
                    m.ChangeText(errorString);
                }else {
                    m.ChangeText("");
                    Thread thread1 = new Thread(){
                        public void run(){
                            try {
                                Runtime.getRuntime().exec("cmd /c  Jrun.bat");
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    };
                    thread1.start();
                    thread1.join();
                }
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }


        }else {

        }
    }

    public void Html(ActionEvent actionEvent) {

    }

    public void C(ActionEvent event) {

        try {
            Formatter form = new Formatter("temp.c");
            form.format("%s", Main.myText);
            form.close();

            Runtime.getRuntime().exec("cmd /c Crun.bat");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void CPP(ActionEvent event) {
        try {
            Formatter form = new Formatter("temp.cpp");
            form.format("%s", Main.myText);
            form.close();
            Process p = Runtime.getRuntime().exec("cmd /c CPrun.bat");
            p.waitFor();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
