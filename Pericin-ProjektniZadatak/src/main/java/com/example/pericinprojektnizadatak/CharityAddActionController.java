package com.example.pericinprojektnizadatak;

import com.example.pericinprojektnizadatak.enumerations.FilePathsEnum;
import com.example.pericinprojektnizadatak.exceptions.ActionWithSameNameException;
import com.example.pericinprojektnizadatak.model.Volunteer;
import com.example.pericinprojektnizadatak.model.interfaces.ActionInterface;
import com.example.pericinprojektnizadatak.model.organization.Charity;
import com.example.pericinprojektnizadatak.model.records.OrganizationRecord;
import com.example.pericinprojektnizadatak.model.voluntaryAction.CharityAction;
import com.example.pericinprojektnizadatak.threads.GetCharityActionsThread;
import com.example.pericinprojektnizadatak.utils.DatabaseUtils;
import com.example.pericinprojektnizadatak.utils.FileUtils;
import com.example.pericinprojektnizadatak.utils.OtherUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharityAddActionController {

    private static final Logger logger = LoggerFactory.getLogger(CharityAddActionController.class);

    @FXML
    private Label username;
    @FXML
    private ImageView userPicture;
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
    private TextField donationNumber;
    @FXML
    private TextField aidingPeople;

    public void initialize(){
        Charity activeUser = DatabaseUtils.getCharityFromEmail(FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(0));

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0);
        hours.setValueFactory(valueFactory);

        SpinnerValueFactory<Integer> valueFactory1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        SpinnerValueFactory<Integer> valueFactory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        minutes.setValueFactory(valueFactory1);
        seconds.setValueFactory(valueFactory2);

        userPicture.setImage(new Image(OrganizationRecord.charityPictureWebAddress));
        username.setText(activeUser.getName());
    }

    public void reset(){
        name.setText("");
        description.setText("");
        endingDate.setValue(null);
        hours.getValueFactory().setValue(0);
        minutes.getValueFactory().setValue(0);
        seconds.getValueFactory().setValue(0);
        donationNumber.setText("");
        aidingPeople.setText("");

    }

    public void back(){

            FXMLLoader fxmlLoader = null;
                fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("charityTabs/charityHomePage.fxml"));


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

    public void addNewAction(){
        String activeEmail = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(0);
        String typeOfAcc = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(1);

        List<String> unfilledFields = new ArrayList<>();
        Pattern pattern2 = Pattern.compile("[^0-9]");
        Matcher matcher;
        if(name.getText().isEmpty()) unfilledFields.add("name");
        if(description.getText().isEmpty())unfilledFields.add("description");


        matcher = pattern2.matcher(donationNumber.getText());
        if(donationNumber.getText().isEmpty() || matcher.find())  unfilledFields.add("donation number");
        matcher.reset(donationNumber.getText());

        matcher = pattern2.matcher(aidingPeople.getText());
        if(aidingPeople.getText().isEmpty() || matcher.find()) unfilledFields.add("aiding people");
        matcher.reset(aidingPeople.getText());

        if(endingDate.getValue() == null) unfilledFields.add("ending date");


        //datum sam prije inicijalizirao da mogu provjeriti jel prije sadasnjeg
        LocalDateTime actionTime = endingDate.getValue().atStartOfDay()
                .plusHours(hours.getValue())
                .plusMinutes(minutes.getValue())
                .plusSeconds(seconds.getValue());

        if(actionTime.isBefore(LocalDateTime.now())) unfilledFields.add("ending date");

        if(!unfilledFields.isEmpty()){ OtherUtils.createAndDisplayAnAlert(unfilledFields);
            return;
        }

        GetCharityActionsThread getCharityActionsThread = new GetCharityActionsThread();
        Thread charityActionsThread = new Thread(getCharityActionsThread);
        charityActionsThread.start();
        try{
            charityActionsThread.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        try{
            ActionInterface.findIfActionWithSameNameExists(name.getText(),getCharityActionsThread.getAllCharityActionsFromDatabaseThread());

        } catch (ActionWithSameNameException e) {
            logger.error(e.getMessage());
            OtherUtils.createAndDisplayAnAlert(e.getMessage());
            return;
        }


        String actionName = name.getText();
        String actionDesc = description.getText();


        String actionDonationNumber = "+385 "+donationNumber.getText();
        String actionAidingPeople = aidingPeople.getText();

        List<Volunteer> volunteerList = new ArrayList<>();

        CharityAction charityAction = new CharityAction(actionName,actionDesc,actionTime,Integer.valueOf(actionAidingPeople),actionDonationNumber);

        DatabaseUtils.addNewCharityAction(charityAction,DatabaseUtils.getIDFromElement(activeEmail,typeOfAcc));

        OtherUtils.addedAction(name.getText());

    }



}


