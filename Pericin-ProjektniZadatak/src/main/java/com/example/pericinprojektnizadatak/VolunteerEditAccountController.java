package com.example.pericinprojektnizadatak;

import com.example.pericinprojektnizadatak.enumerations.FilePathsEnum;
import com.example.pericinprojektnizadatak.enumerations.GenderEnum;
import com.example.pericinprojektnizadatak.enumerations.NationalityEnum;
import com.example.pericinprojektnizadatak.exceptions.AccountWithSameEmailException;
import com.example.pericinprojektnizadatak.model.Volunteer;
import com.example.pericinprojektnizadatak.model.generics.Changes;
import com.example.pericinprojektnizadatak.utils.DataChanges;
import com.example.pericinprojektnizadatak.utils.DatabaseUtils;
import com.example.pericinprojektnizadatak.utils.FileUtils;
import com.example.pericinprojektnizadatak.utils.OtherUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class VolunteerEditAccountController {

    private static final long serialVersionUID =-4533594597239637556L;

    @FXML
    private TextField name;
    @FXML
    private TextField surname;
    @FXML
    private ComboBox<String> nationality;
    @FXML
    private ComboBox<String> gender;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField email;
    @FXML
    private TextField password;
    @FXML
    private Label callingNumber;
    @FXML
    private Label username;
    @FXML
    private ImageView userPicture;


    private static final Logger logger = LoggerFactory.getLogger(VolunteerEditAccountController.class);


    public void initialize(){
        for(NationalityEnum nationalityEnum:NationalityEnum.values()){
            nationality.getItems().add(nationalityEnum.getCoutryName());
        }

        for(GenderEnum genderEnum:GenderEnum.values()){
            gender.getItems().add(genderEnum.getGender());
        }

        Volunteer activeUser = DatabaseUtils.getVolunteerFromEmail(FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(0));
        loadExistingData(activeUser);

        username.setText(activeUser.getPersonalInfo().getName() + " "+activeUser.getPersonalInfo().getSurname());
        userPicture.setImage(new Image("https://upload.wikimedia.org/wikipedia/commons/thumb/1/12/User_icon_2.svg/1024px-User_icon_2.svg.png"));
        callingNumber.setText(activeUser.getPersonalInfo().getNationality().getCallingNumber());
    }


    public void back(){
        FXMLLoader fxmlLoader = new FXMLLoader(VolunteerHomePageController.class.getResource("volunteerTabs/volunteerHomePage.fxml"));

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


    public void reset(){

        loadExistingData(Objects.requireNonNull(DatabaseUtils.getVolunteerFromEmail(FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(0))));
        password.setText("");
    }

    public void loadExistingData(Volunteer activeUser) {
        name.setText(activeUser.getPersonalInfo().getName());
        surname.setText(activeUser.getPersonalInfo().getSurname());
        nationality.setValue(activeUser.getPersonalInfo().getNationality().getCoutryName());
        gender.setValue(activeUser.getPersonalInfo().getGender().getGender());
        phoneNumber.setText(activeUser.getPersonalInfo().getPhoneNumber().replace(activeUser.getPersonalInfo().getNationality().getCallingNumber(),"").trim());
        email.setText(activeUser.getPersonalInfo().getEmail());

    }

    public void changeCallingNumberDependingOnCountry(){
       callingNumber.setText(NationalityEnum.valueOf(nationality.getValue().toUpperCase()).getCallingNumber());
    }


    public void changeAccountSettings() {

        Pattern pattern1 = Pattern.compile("[^a-zA-Z]");
        Pattern pattern2 = Pattern.compile("[^0-9]");
        Matcher matcher;

        String typeOfAcc = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(1);
        String activeUserEmail = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(0);
        Volunteer activeUser = DatabaseUtils.getVolunteerFromEmail(activeUserEmail);

        Map<String, String> mapWhatToChangeThenValueString = new HashMap<>();
        Map<String, Integer> mapWhatToChangeThenValueInteger = new HashMap<>();
        Map<String, Boolean> mapWhatToChangeThenValueBoolean = new HashMap<>();

        List<String> unfilledField = new ArrayList<>();
        String contact_number = NationalityEnum.valueOf(nationality.getValue().toUpperCase()).getCallingNumber()+" "+ phoneNumber.getText();
        System.out.println(contact_number + " " + phoneNumber.getText());


        if (name.getText().compareTo(activeUser.getPersonalInfo().getName()) != 0) {
            if (!name.getText().isEmpty())
                mapWhatToChangeThenValueString.put("name", name.getText());
            else unfilledField.add("name");
        }
        if (surname.getText().compareTo(activeUser.getPersonalInfo().getSurname()) != 0) {
            if (!surname.getText().isEmpty())
                mapWhatToChangeThenValueString.put("surname", surname.getText());
            else unfilledField.add("surname");
        }
        if (nationality.getValue().compareTo(activeUser.getPersonalInfo().getNationality().getCoutryName()) != 0)
            mapWhatToChangeThenValueString.put("nationality", nationality.getValue());
        if (gender.getValue().compareTo(activeUser.getPersonalInfo().getGender().getGender()) != 0)
            mapWhatToChangeThenValueString.put("gender", gender.getValue());


        if (email.getText().compareTo(activeUser.getPersonalInfo().getEmail()) != 0) {
            if (!email.getText().isEmpty())
                mapWhatToChangeThenValueString.put("email", email.getText());
            else unfilledField.add("email");
        }
        System.out.println("njegov broj"+activeUser.getPersonalInfo().getPhoneNumber());
        if (contact_number.compareTo(activeUser.getPersonalInfo().getPhoneNumber()) != 0) {

            matcher = pattern2.matcher(phoneNumber.getText());

            if (contact_number.compareTo(NationalityEnum.valueOf(nationality.getValue().toUpperCase()).getCallingNumber() + " ") == 0 || matcher.find())
                unfilledField.add("phone_number");
            else mapWhatToChangeThenValueString.put("phone_number", contact_number);

            matcher.reset(phoneNumber.getText());
        }
        if(email.getText().compareTo(activeUser.getPersonalInfo().getEmail())!=0){
            if(!email.getText().isEmpty() && email.getText().contains("@")) {
                try {
                    OtherUtils.findIfAnotherAccountWithSameEmailExist(email.getText());

                } catch (AccountWithSameEmailException e) {
                    OtherUtils.createAndDisplayAnAlert(e.getMessage());
                    logger.error(e.getMessage());
                    return;
                } catch (SQLException | IOException e) {
                    logger.error(e.getMessage());
                    throw new RuntimeException(e);
                }
                mapWhatToChangeThenValueString.put("email",email.getText());
            }
            else unfilledField.add("email");
        }

        if (!unfilledField.isEmpty()) {
            OtherUtils.createAndDisplayAnAlert(unfilledField);
            return;
        }

        if (!password.getText().isEmpty()) {
            Long id = DatabaseUtils.getIDFromElement(activeUserEmail, typeOfAcc);
            List<String> listOfPasswords = FileUtils.readEveryLineFromFile(FilePathsEnum.VOLUNTEER_FILE_PATH.getFilePath());

            List<String> updatedList = listOfPasswords.stream()
                    .map(line -> {
                        String[] splitStrings = line.split("\\s+");
                        Long currentId = Long.parseLong(splitStrings[0]);
                        if (currentId.equals(id)) {
                            splitStrings[1] = FileUtils.hashSHA256(password.getText());
                            return String.join(" ", splitStrings);
                        }

                        return line;
                    })
                    .collect(Collectors.toList());
            FileUtils.writeLinesToFile(updatedList,FilePathsEnum.VOLUNTEER_FILE_PATH.getFilePath());
            OtherUtils.updatedPassword();
        }

        System.out.println(mapWhatToChangeThenValueString);
        if(mapWhatToChangeThenValueString.isEmpty()){
            if (password.getText().isEmpty()) OtherUtils.createAndDisplayAnAlert("No changes done!");
            return;
        }
        else{
            DatabaseUtils.commitVolunteerChangesToDatabaseString(mapWhatToChangeThenValueString);
            OtherUtils.updatedAccountDetails();
        }
        FileUtils.writeActiveUserToFile(email.getText(),FilePathsEnum.VOLUNTEER_FILE_PATH);

        String newActiveUserEmail = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(0);
        Volunteer newUser = DatabaseUtils.getVolunteerFromEmail(newActiveUserEmail);


        Optional<DataChanges> dataChangeStoreUtil = FileUtils.readCurrentData();
       // DataChanges dataChanges = new DataChanges();

        Changes change = new Changes(typeOfAcc, LocalDateTime.now(),activeUser,newUser);

        dataChangeStoreUtil.get().addChange(change);
       // dataChanges.addChange(change);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("E:/Faks/3. semestar/Java/Volonterske_akcije_Pericin/Pericin-ProjektniZadatak/files/serialization.bin"))) {
            //oos.writeObject(dataChanges);
            oos.writeObject((dataChangeStoreUtil.get()));
            oos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }


        loadExistingData(Objects.requireNonNull(newUser));


    }

    public void deleteAccount(){
        boolean confirmed = OtherUtils.showConfirmationDialog("Deleting account!", "Are you sure you want to delete your account?");
        if (confirmed) {
            String typeOfAcc = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(1);
            String activeUserEmail = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(0);
            Long id = DatabaseUtils.getIDFromElement(activeUserEmail, typeOfAcc);
            DatabaseUtils.deleteAccountDatabase(id,typeOfAcc);


            List<String> newPasswords = FileUtils.deletingAnAccountFromPasswordFile(FileUtils.readEveryLineFromFile(FilePathsEnum.valueOf(typeOfAcc.toUpperCase()+"_FILE_PATH").getFilePath()),id);

            FileUtils.writeLinesToFile(newPasswords,FilePathsEnum.valueOf(typeOfAcc.toUpperCase()+"_FILE_PATH").getFilePath());
            OtherUtils.loadLoginScreen();
        } else {
            return;
        }

    }


}
