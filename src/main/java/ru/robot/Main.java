package ru.robot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("scene.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setTitle("JavaFX and Gradle");
        stage.setScene(scene);
        stage.show();
        //note


    }


    public static void main(String[] args) {
        var a = 10;
        var b = a * -1;
        System.out.println(b);
        launch(args);
    }



}