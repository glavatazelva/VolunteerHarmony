package com.example.pericinprojektnizadatak;

import com.example.pericinprojektnizadatak.enumerations.FilePathsEnum;
import com.example.pericinprojektnizadatak.exceptions.ProblemLoadingUserDetailsException;
import com.example.pericinprojektnizadatak.model.generics.SuccessCalculator;
import com.example.pericinprojektnizadatak.model.generics.UserLoader;
import com.example.pericinprojektnizadatak.model.organization.AnimalWelfare;
import com.example.pericinprojektnizadatak.model.records.OrganizationRecord;
import com.example.pericinprojektnizadatak.model.voluntaryAction.AnimalWelfareAction;
import com.example.pericinprojektnizadatak.model.voluntaryAction.VoluntaryAction;
import com.example.pericinprojektnizadatak.threads.AnimalWelfareActionsThread;
import com.example.pericinprojektnizadatak.threads.GetAnimalWelfaresThread;
import com.example.pericinprojektnizadatak.utils.DatabaseUtils;
import com.example.pericinprojektnizadatak.utils.FileUtils;
import com.example.pericinprojektnizadatak.utils.OtherUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AnimalWelfareHomePageController {
    private static final Logger logger = LoggerFactory.getLogger(AnimalWelfareHomePageController.class);
    @FXML
    private Label username;
    @FXML
    private ImageView userPicture;
    @FXML
    private Label rescueMissions;
    @FXML
    private Label savedAnimals;
    @FXML
    private Label medicalCare;
    @FXML
    private VBox notifications;
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label efficiency;
    @FXML
    private Label rank;

    private AnimalWelfareActionsThread thread;
    public void logout(){

        if (thread != null) {
            thread.stopThread();
        }
        OtherUtils.loadLoginScreen();

    }
    public void initialize() {

        thread = new AnimalWelfareActionsThread();
        thread.startThread(HelloApplication.primaryStage);

        userPicture.setImage(new Image(OrganizationRecord.animalWelfarePictureWebAddress));

        String activeUserEmail = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(0);
        AnimalWelfare activeUser = DatabaseUtils.getAnimalWelfareFromEmail(activeUserEmail);


        try {
            UserLoader<AnimalWelfare> userLoader = new UserLoader<>();
            userLoader.checkIfUserWasLoadedCorrectly(activeUser);
        } catch (
                ProblemLoadingUserDetailsException e) {
            logger.error(e + e.getMessage());
            OtherUtils.loadLoginScreen();
        }

        assert activeUser != null;
        username.setText(activeUser.getName());

        rescueMissions.setText(activeUser.getConductsRescueOperations().toString());
        medicalCare.setText(activeUser.getProvidesMedicalCare().toString());
        savedAnimals.setText(activeUser.getTotalNumberOfSavedAnimals().toString());

        addNotifications();
        scrollPane.setContent(notifications);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-border-color: #76bf76; -fx-border-width: 1px;");

        GetAnimalWelfaresThread getAnimalWelfaresThread = new GetAnimalWelfaresThread();
        Thread animalWelfareThread = new Thread(getAnimalWelfaresThread);
        animalWelfareThread.start();
        try{
            animalWelfareThread.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }

        rank.setText(new SuccessCalculator<AnimalWelfare>(activeUser).calculateRank(getAnimalWelfaresThread.getAllAnimalWelfaresFromDatabaseThread()).toString());
        Double efficiencyValue = new SuccessCalculator<AnimalWelfare>(activeUser).calculateEfficiency();
        efficiency.setText(String.format("%.2f",efficiencyValue));
    }

    public void openAddVolunteerTab(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("organizationTabs/organizationAddNewVolunteerTab.fxml"));
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

    public void openRemoveVolunteerTab(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("organizationTabs/organizationRemoveVolunteerTab.fxml"));
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

    public void openEditAccountTab(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("organizationTabs/organizationEditAccount.fxml"));
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

    public void openAddActionTab(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("animalWelfareTabs/animalWelfareAddAction.fxml"));
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

    public void addNotifications(){
        notifications.getChildren().clear();
        Long id = DatabaseUtils.getIDFromElement(FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(0),FilePathsEnum.ANIMAL_WELFARE_FILE_PATH.getType());
        List<VoluntaryAction> foundActions = DatabaseUtils.getActionsByOrganizationID(new AnimalWelfareAction(),id);


        for(VoluntaryAction action:foundActions){
            Label actionName = new Label(action.getName());
            Label actionDesc = new Label(action.getDescription());
            actionDesc.setStyle("-fx-font-style: italic");
            actionName.setStyle("-fx-font-weight: bold");

            Label actionEndingDate = new Label("Ends on "+action.getEndingDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));

            notifications.getChildren().add(actionName);
            notifications.getChildren().add(actionDesc);
            notifications.getChildren().add(actionEndingDate);
        }



    }

    public void refresh(){
        initialize();
    }


}
