package com.example.pericinprojektnizadatak;

import com.example.pericinprojektnizadatak.enumerations.FilePathsEnum;
import com.example.pericinprojektnizadatak.enumerations.GenderEnum;
import com.example.pericinprojektnizadatak.enumerations.NationalityEnum;
import com.example.pericinprojektnizadatak.exceptions.AccountWithSameEmailException;
import com.example.pericinprojektnizadatak.model.PersonalInformation;
import com.example.pericinprojektnizadatak.model.Volunteer;
import com.example.pericinprojektnizadatak.utils.DatabaseUtils;
import com.example.pericinprojektnizadatak.utils.FileUtils;
import com.example.pericinprojektnizadatak.utils.OtherUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VolunteerFormController {

    private static final Logger logger = LoggerFactory.getLogger(VolunteerFormController.class);
    @FXML
    private TextField name;
    @FXML
    private TextField surname;
    @FXML
    private Spinner<Integer> age;
    @FXML
    private ComboBox<String> gender;
    @FXML
    private ComboBox<String> nationality;
    @FXML
    private TextField phoneNumber;
    @FXML
    private Label callingNumber;
    @FXML
    private TextField email;
    @FXML
    private TextField password;

    @FXML
    public void initialize(){


        for(NationalityEnum nationalityEnum : NationalityEnum.values()){
            nationality.getItems().add(nationalityEnum.getCoutryName());
        }

        for(GenderEnum genderEnum : GenderEnum.values()){
            gender.getItems().add(genderEnum.getGender());
        }

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(18, 100, 1);
        age.setValueFactory(valueFactory);

    }


    public void changeCallingNumberDependingOnCountry(){
        for(NationalityEnum nationalityEnum:NationalityEnum.values()){
            if(nationalityEnum.getCoutryName().compareTo(nationality.getValue()) == 0)
                callingNumber.setText(nationalityEnum.getCallingNumber());
        }

    }


    public void createVolunteer(){
        Pattern pattern1 = Pattern.compile("[^a-zA-Z]");
        Pattern pattern2 = Pattern.compile("[^0-9]");
        List<String> listOfUnfilledFields = new ArrayList<>();

        Matcher matcher = pattern1.matcher(name.getText());
        if (matcher.find()) {
            listOfUnfilledFields.add("name");

        }
        matcher.reset(name.getText());


        matcher = pattern1.matcher(surname.getText());
        if (matcher.find()) {
            listOfUnfilledFields.add("surname");
        }
        matcher.reset(surname.getText());

        matcher = pattern2.matcher(phoneNumber.getText());
        if (matcher.find()) {
            listOfUnfilledFields.add("phone number");
        }
        matcher.reset(phoneNumber.getText());

        if(name.getText().isEmpty()) listOfUnfilledFields.add("name");
        if(surname.getText().isEmpty()) listOfUnfilledFields.add("last name");
        if(gender.getValue() == null) listOfUnfilledFields.add("gender");
        if(nationality.getValue() == null) listOfUnfilledFields.add("nationality");
        if(phoneNumber.getText().isEmpty()) listOfUnfilledFields.add("phone number");
        if(email.getText().isEmpty() || !(email.getText().contains("@"))) listOfUnfilledFields.add("email");
        if(password.getText().isEmpty()) listOfUnfilledFields.add("password");

        if(!listOfUnfilledFields.isEmpty()){
            OtherUtils.createAndDisplayAnAlert(listOfUnfilledFields);
            return;
        }



      //  PersonalInformation personalInformation = new PersonalInformation(name.getText(),surname.getText(), age.getValue(), GenderEnum.valueOf(gender.getValue().toUpperCase()),NationalityEnum.valueOf(nationality.getValue().toUpperCase()),phoneNumber.getText(), email.getText());
        PersonalInformation personalInformation = new PersonalInformation.PersonalInformationBuilder()
                .withName(name.getText())
                .withSurname(surname.getText())
                .withAge(age.getValue())
                .withGender(GenderEnum.valueOf(gender.getValue().toUpperCase()))
                .withNationality(NationalityEnum.valueOf(nationality.getValue().toUpperCase()))
                .withPhoneNumber(phoneNumber.getText())
                .withEmail(email.getText())
                .build();
        Volunteer volunteer = new Volunteer.VolunteerBuilder().withPersonalInfo(personalInformation).withParticipatedOperations(0).build();

        boolean foundVolunteerWithSameEmail = false;
        try {
            OtherUtils.findIfAnotherAccountWithSameEmailExist(email.getText());

        } catch (AccountWithSameEmailException e) {
            logger.error(e.getMessage());
          OtherUtils.createAndDisplayAnAlert(e.getMessage());
          foundVolunteerWithSameEmail = true;
        } catch (SQLException | IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        if(foundVolunteerWithSameEmail) return;

        DatabaseUtils.addNewVolunteer(volunteer);
        FileUtils.writeHashToFile(FileUtils.hashSHA256(password.getText()),email.getText(), FilePathsEnum.VOLUNTEER_FILE_PATH);

        OtherUtils.createdAccountAlert();
        OtherUtils.loadLoginScreen();


    }



}
