package com.company;


import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ThreadRunner implements Runnable {

    @Override
    public void run() {
        Stage SplashStage = new Stage();
        VBox vBox = new VBox();
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("Splash.jpg")));
        vBox.getChildren().add(imageView);

        SplashStage.setScene(new Scene(vBox, 600, 382));
        SplashStage.initStyle(StageStyle.UNDECORATED);
        SplashStage.show();
    }


}
