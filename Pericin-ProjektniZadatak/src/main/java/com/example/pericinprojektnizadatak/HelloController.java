package com.example.pericinprojektnizadatak;

import com.example.pericinprojektnizadatak.enumerations.FilePathsEnum;
import com.example.pericinprojektnizadatak.model.Volunteer;
import com.example.pericinprojektnizadatak.model.organization.AnimalWelfare;
import com.example.pericinprojektnizadatak.model.organization.Charity;
import com.example.pericinprojektnizadatak.model.organization.DisasterRelief;
import com.example.pericinprojektnizadatak.threads.*;
import com.example.pericinprojektnizadatak.utils.FileUtils;
import com.example.pericinprojektnizadatak.utils.OtherUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HelloController {

@FXML
private TextField email;

@FXML
private TextField password;

@FXML
private Label incorrectPassword;

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    public void openRegisterWindow(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register.fxml"));
        Scene scene = null;
        try{
            scene = new Scene(fxmlLoader.load(),800,650);
        }catch (IOException e){
            e.printStackTrace();
        }
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().setTitle("VolunteerHarmony");
        HelloApplication.getStage().show();
    }

    public void login() throws SQLException, IOException {

        List<String> listOfUnfilledFields = new ArrayList<>();
        if(email.getText().isEmpty()) listOfUnfilledFields.add("username");
        if(password.getText().isEmpty())listOfUnfilledFields.add("password");

        FilePathsEnum filePathsEnum = null;


        if(!listOfUnfilledFields.isEmpty()){
            OtherUtils.createAndDisplayAnAlert(listOfUnfilledFields);
            return;
        }
        FXMLLoader fxmlLoader = null;


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
                .filter(obj -> obj.getPersonalInfo().getEmail().equals(email.getText()))
                .toList();

        for(Volunteer temp : volunteerList) System.out.println(temp.getPersonalInfo().getEmail());

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
                .filter(obj -> obj.getEmail().equals(email.getText()))
                .toList();

        for(Charity temp : charityList) System.out.println(temp.getEmail());

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
                .filter(obj -> obj.getEmail().equals(email.getText()))
                .toList();

        for(AnimalWelfare temp : animalWelfareList) System.out.println(temp.getEmail());

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
                .filter(obj -> obj.getEmail().equals(email.getText()))
                .toList();

        for(DisasterRelief temp : disasterReliefList) System.out.println(temp.getEmail());

        if(volunteerList.isEmpty() && charityList.isEmpty() && animalWelfareList.isEmpty() && disasterReliefList.isEmpty()){
            OtherUtils.createAndDisplayAnAlert("Account doesn't exist!");

        }

        if(!volunteerList.isEmpty()){
            boolean loginConfirmed = false;
            loginConfirmed = FileUtils.successfulLogin(email.getText(),password.getText(), FilePathsEnum.VOLUNTEER_FILE_PATH);

            if(loginConfirmed) {
                fxmlLoader = new FXMLLoader(VolunteerHomePageController.class.getResource("volunteerTabs/volunteerHomePage.fxml"));
                filePathsEnum = FilePathsEnum.VOLUNTEER_FILE_PATH;
            }
        }
        else if(!charityList.isEmpty()){
            boolean loginConfirmed = false;
            loginConfirmed = FileUtils.successfulLogin(email.getText(),password.getText(), FilePathsEnum.CHARITY_FILE_PATH);

            if(loginConfirmed) {
                fxmlLoader = new FXMLLoader(CharityHomePageController.class.getResource("charityTabs/charityHomePage.fxml"));
                filePathsEnum = FilePathsEnum.CHARITY_FILE_PATH;
            }

        }
        else if(!animalWelfareList.isEmpty()){
            boolean loginConfirmed = false;
            loginConfirmed = FileUtils.successfulLogin(email.getText(),password.getText(), FilePathsEnum.ANIMAL_WELFARE_FILE_PATH);

            if(loginConfirmed) {
                fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("animalWelfareTabs/animalWelfareHomePage.fxml"));
                filePathsEnum = FilePathsEnum.ANIMAL_WELFARE_FILE_PATH;
            }
        }
        else if(!disasterReliefList.isEmpty()){
            boolean loginConfirmed = false;
            loginConfirmed = FileUtils.successfulLogin(email.getText(),password.getText(), FilePathsEnum.DISASTER_RELIEF_FILE_PATH);

            if(loginConfirmed) {
                fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("disasterReliefTabs/disasterReliefHomePage.fxml"));
                filePathsEnum = FilePathsEnum.DISASTER_RELIEF_FILE_PATH;
            }
        }

        /*
        try {
            if (FileUtils.successfulLogin(email.getText(), password.getText(), FilePathsEnum.VOLUNTEER_FILE_PATH)) {
                fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("volunteerTabs/charityHomePage.fxml"));
            } else if (FileUtils.successfulLogin(email.getText(), password.getText(), FilePathsEnum.CHARITY_FILE_PATH)) {
                fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("volunteerTabs/charityHomePage.fxml"));
            }
        /*
        else if (FileUtils.successfulLogin(email.getText(),password.getText(), FilePathsEnum.DISASTER_RELIEF_FILE_PATH)){
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("volunteerTabs/charityHomePage.fxml"));
        }
        else if (FileUtils.successfulLogin(email.getText(),password.getText(), FilePathsEnum.ANIMAL_WELFARE_FILE_PATH)){
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("volunteerTabs/charityHomePage.fxml"));
        }
        */

        /*
        }
        catch (AccountNotFoundException e){
            OtherUtils.createAndDisplayAnAlert(e.getMessage());
        }
        */

        if(fxmlLoader == null){
            incorrectPassword.setText("Incorrect Password");
            return;
        }

        FileUtils.writeActiveUserToFile(email.getText(),filePathsEnum);


      //  else incorrectPassword.setText("Incorrect password!");

        Scene scene = null;
            try{
                scene = new Scene(fxmlLoader.load(),800,650);
            }catch (IOException e){
                logger.error(e.getMessage());
                e.printStackTrace();
            }
            HelloApplication.getStage().setScene(scene);
            HelloApplication.getStage().setTitle("VolunteerHarmony");
            HelloApplication.getStage().show();




    }
}