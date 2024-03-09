package com.example.pericinprojektnizadatak;

import com.example.pericinprojektnizadatak.enumerations.AnimalTypeEnum;
import com.example.pericinprojektnizadatak.enumerations.CitiesEnum;
import com.example.pericinprojektnizadatak.enumerations.FilePathsEnum;
import com.example.pericinprojektnizadatak.exceptions.ActionWithSameNameException;
import com.example.pericinprojektnizadatak.model.interfaces.ActionInterface;
import com.example.pericinprojektnizadatak.model.organization.AnimalWelfare;
import com.example.pericinprojektnizadatak.model.records.OrganizationRecord;
import com.example.pericinprojektnizadatak.model.voluntaryAction.AnimalWelfareAction;
import com.example.pericinprojektnizadatak.threads.GetAnimalWelfareActionsThread;
import com.example.pericinprojektnizadatak.utils.DatabaseUtils;
import com.example.pericinprojektnizadatak.utils.FileUtils;
import com.example.pericinprojektnizadatak.utils.OtherUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnimalWelfareAddActionController {

    @FXML
    private Label username;
    @FXML
    private ImageView userPicture;
    @FXML
    private ComboBox<String> typeOfAnimal;
    @FXML
    private ComboBox<String> city;
    @FXML
    private TextField name;
    @FXML
    private TextArea description;
    @FXML
    private DatePicker endingDate;
    @FXML
    private Spinner<Integer> hours;
    @FXML
    private Spinner<Integer> minutes;
    @FXML
    private Spinner<Integer> seconds;
    @FXML
    private TextField savedAnimals;
    private static final Logger logger = LoggerFactory.getLogger(AnimalWelfareAddActionController.class);

    public void initialize(){
        AnimalWelfare activeUser = DatabaseUtils.getAnimalWelfareFromEmail(FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(0));

        userPicture.setImage(new Image(OrganizationRecord.animalWelfarePictureWebAddress));
        username.setTextFill(Color.rgb(76,191,76));
        username.setText(activeUser.getName());

        for(AnimalTypeEnum animalTypeEnum : AnimalTypeEnum.values()){
            typeOfAnimal.getItems().add(animalTypeEnum.getAnimalType());
        }

        for(CitiesEnum citiesEnum:CitiesEnum.values()){
            city.getItems().add(citiesEnum.getCityName());
        }

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0);
        hours.setValueFactory(valueFactory);

        SpinnerValueFactory<Integer> valueFactory1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        SpinnerValueFactory<Integer> valueFactory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        minutes.setValueFactory(valueFactory1);
        seconds.setValueFactory(valueFactory2);
    }
    public void reset(){
        name.setText("");
        description.setText("");
        endingDate.setValue(null);
        hours.getValueFactory().setValue(0);
        minutes.getValueFactory().setValue(0);
        seconds.getValueFactory().setValue(0);
        typeOfAnimal.setValue(null);
        city.setValue(null);
        savedAnimals.setText("");

    }

    public void back(){

        FXMLLoader fxmlLoader = null;
        fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("animalWelfareTabs/animalWelfareHomePage.fxml"));


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

    public void addNewAction() {
        String activeEmail = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(0);
        String typeOfAcc = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(1);

        List<String> unfilledFields = new ArrayList<>();
        Pattern pattern2 = Pattern.compile("[^0-9]");
        Matcher matcher;
        if (name.getText().isEmpty()) unfilledFields.add("name");
        if (description.getText().isEmpty()) unfilledFields.add("description");

        if(city.getValue() == null) unfilledFields.add("city");
        if(typeOfAnimal.getValue() == null) unfilledFields.add("type of animal");

        matcher = pattern2.matcher(savedAnimals.getText());
        if (savedAnimals.getText().isEmpty() || matcher.find()) unfilledFields.add("saved animals");
        matcher.reset(savedAnimals.getText());

        if(endingDate.getValue() == null) unfilledFields.add("ending date");


        //datum sam prije inicijalizirao da mogu provjeriti jel prije sadasnjeg
        LocalDateTime actionTime = endingDate.getValue().atStartOfDay()
                .plusHours(hours.getValue())
                .plusMinutes(minutes.getValue())
                .plusSeconds(seconds.getValue());

        if(actionTime.isBefore(LocalDateTime.now())) unfilledFields.add("ending date");

        if(!unfilledFields.isEmpty()){
            OtherUtils.createAndDisplayAnAlert(unfilledFields);
            return;
        }

        GetAnimalWelfareActionsThread getAnimalWelfareActionsThread = new GetAnimalWelfareActionsThread();
        Thread animalWelfareActionsThread = new Thread(getAnimalWelfareActionsThread);
        animalWelfareActionsThread.start();
        try{
            animalWelfareActionsThread.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        try{
            ActionInterface.findIfActionWithSameNameExists(name.getText(),getAnimalWelfareActionsThread.getAllAnimalWelfareActionsFromDatabaseThread());

        } catch (ActionWithSameNameException e) {
            logger.error(e.getMessage());
            OtherUtils.createAndDisplayAnAlert(e.getMessage());
            return;
        }

        String actionName = name.getText();
        String actionDesc = description.getText();
        String actionSavedAnimals = savedAnimals.getText();

        AnimalWelfareAction action = new AnimalWelfareAction(actionName,actionDesc,actionTime,Integer.valueOf(actionSavedAnimals),AnimalTypeEnum.valueOf(typeOfAnimal.getValue().toUpperCase()),CitiesEnum.valueOf(city.getValue().toUpperCase()));
        DatabaseUtils.addNewAnimalWelfareAction(action,DatabaseUtils.getIDFromElement(activeEmail,typeOfAcc));

        OtherUtils.addedAction(name.getText());
    }
}
