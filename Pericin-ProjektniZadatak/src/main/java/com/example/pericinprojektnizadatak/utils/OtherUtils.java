package com.example.pericinprojektnizadatak.utils;

import com.example.pericinprojektnizadatak.HelloApplication;
import com.example.pericinprojektnizadatak.exceptions.AccountWithSameEmailException;
import com.example.pericinprojektnizadatak.model.Volunteer;
import com.example.pericinprojektnizadatak.model.organization.AnimalWelfare;
import com.example.pericinprojektnizadatak.model.organization.Charity;
import com.example.pericinprojektnizadatak.model.organization.DisasterRelief;
import com.example.pericinprojektnizadatak.threads.GetAnimalWelfaresThread;
import com.example.pericinprojektnizadatak.threads.GetCharitiesThread;
import com.example.pericinprojektnizadatak.threads.GetDisasterReliefsThread;
import com.example.pericinprojektnizadatak.threads.GetVolunteersThread;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class OtherUtils {
    private static final Logger logger = LoggerFactory.getLogger(OtherUtils.class);
    public static void createAndDisplayAnAlert(List<String> listOfUnfilledFields){

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText("Invalid data input");

        StringBuilder errorMessage = new StringBuilder();

        for(String string:listOfUnfilledFields){
            errorMessage.append("The field '").append(string).append("' is not filled correctly\n");
        }

        alert.setContentText(errorMessage.toString());
        alert.showAndWait();

    }

    public static void createAndDisplayAnAlert(String error){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error!");
        alert.setHeaderText(error);
        alert.showAndWait();

    }

    public static void createdAccountAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Account successfully created!");
        alert.setHeaderText("You will be redirected to the login screen");
        alert.showAndWait();

    }

    public static void addedAction(String name){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Action successfully added!");
        alert.setHeaderText("Action "+name+" has started");
        alert.showAndWait();

    }

    public static void addedVolunteerToOrganization(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Volunteer successfully added!");
        alert.setHeaderText("Volunteer was successfully added to your organization!");
        alert.showAndWait();

    }

    public static void removedVolunteerToOrganization(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Volunteer successfully removed!");
        alert.setHeaderText("Volunteer was successfully removed to your organization!");
        alert.showAndWait();

    }

    public static void updatedAccountDetails(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Account details successfully updated");
        alert.setHeaderText("New values should be displayed on screen!");
        alert.showAndWait();

    }



    public static void updatedPassword(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Password successfully changed");
        alert.setHeaderText("Try logging in again =)");
        alert.showAndWait();

    }

    public static boolean showConfirmationDialog(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText("Confirmation");
        alert.setContentText(message);
        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
        return result == ButtonType.OK;
    }




    public static void loadLoginScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 650);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        Stage titleScreen = HelloApplication.getStage();
        titleScreen.setScene(scene);
        titleScreen.show();
    }

    public static void findIfAnotherAccountWithSameEmailExist(String email) throws SQLException, IOException {

        int brojac = 0;

        GetVolunteersThread getVolunteersThread = new GetVolunteersThread();
        Thread volunteersThread = new Thread(getVolunteersThread);
        volunteersThread.start();
        try{
            volunteersThread.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }

        List<Volunteer> volunteerList = getVolunteersThread.getAllVolunteersFromDatabaseThread()
                .stream()
                .filter(obj -> obj.getPersonalInfo().getEmail().equals(email))
                .toList();

        GetCharitiesThread getCharitiesThread = new GetCharitiesThread();
        Thread charityThread = new Thread(getCharitiesThread);
        charityThread.start();
        try{
            charityThread.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }

        List<Charity> charityList = getCharitiesThread.getAllCharitiesFromDatabaseThread()
                .stream()
                .filter(obj -> obj.getEmail().equals(email))
                .toList();

        GetAnimalWelfaresThread getAnimalWelfaresThread = new GetAnimalWelfaresThread();
        Thread animalWelfareThread = new Thread(getAnimalWelfaresThread);
        animalWelfareThread.start();
        try{
            animalWelfareThread.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }

        List<AnimalWelfare> animalWelfareList = getAnimalWelfaresThread.getAllAnimalWelfaresFromDatabaseThread()
                .stream()
                .filter(obj -> obj.getEmail().equals(email))
                .toList();

        GetDisasterReliefsThread getDisasterReliefsThread = new GetDisasterReliefsThread();
        Thread disasterReliefThread = new Thread(getDisasterReliefsThread);
        disasterReliefThread.start();
        try{
            disasterReliefThread.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }

        List<DisasterRelief> disasterReliefList = getDisasterReliefsThread.getAllDisasterReliefsFromDatabaseThread()
                .stream()
                .filter(obj -> obj.getEmail().equals(email))
                .toList();


        if(!volunteerList.isEmpty()) brojac++;
        if(!charityList.isEmpty()) brojac++;
        if(!animalWelfareList.isEmpty()) brojac++;
        if(!disasterReliefList.isEmpty()) brojac++;

        if(brojac > 0) throw new AccountWithSameEmailException("Email is already in use!");


    }
}

