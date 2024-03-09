package com.example.pericinprojektnizadatak;

import com.example.pericinprojektnizadatak.utils.OtherUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class RegisterController {

    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @FXML
    private ComboBox<String> chosenAccountType;

    @FXML
    private StackPane contentPane;



    @FXML
    public void initialize(){

        chosenAccountType.getItems().addAll("Voluntary organization","Volunteer");

    }

    public void back(){
        OtherUtils.loadLoginScreen();
    }

    public void displayRegisterForm(){
        if(chosenAccountType.getValue().compareTo("Volunteer") == 0){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("volunteerForm.fxml"));
                Pane loadedPane = loader.load();

                contentPane.getChildren().clear();
                contentPane.getChildren().add(loadedPane);
            } catch (IOException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }


        }
        else if(chosenAccountType.getValue().compareTo("Voluntary organization") == 0){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("voluntaryOrganizationForm.fxml"));
                Pane loadedPane = loader.load();

                contentPane.getChildren().clear();
                contentPane.getChildren().add(loadedPane);
            } catch (IOException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }

    }

    public String returnAccountCreationType(){
        return chosenAccountType.getValue();
    }

}
