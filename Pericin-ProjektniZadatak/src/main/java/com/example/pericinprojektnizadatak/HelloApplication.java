package com.example.pericinprojektnizadatak;

import com.example.pericinprojektnizadatak.threads.CharityActionsThread;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloApplication extends Application {

    private static final Logger logger = LoggerFactory.getLogger(HelloApplication.class);
    public static Stage primaryStage;
    public static Stage getStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage stage)  {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));


        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 650);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);

        }
        stage.setTitle("VolunteerHarmony");
        stage.setScene(scene);
        primaryStage = stage;

        stage.show();
        //System.out.println("trenutni direktorij: " + System.getProperty("user.dir"));
    }

    @Override
    public void stop() {


    }


    public static void main(String[] args) {
        launch();
    }
}