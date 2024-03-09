package com.example.pericinprojektnizadatak;

import com.example.pericinprojektnizadatak.enumerations.FilePathsEnum;
import com.example.pericinprojektnizadatak.enumerations.GenderEnum;
import com.example.pericinprojektnizadatak.enumerations.NationalityEnum;
import com.example.pericinprojektnizadatak.exceptions.ProblemLoadingUserDetailsException;
import com.example.pericinprojektnizadatak.model.Volunteer;
import com.example.pericinprojektnizadatak.model.generics.UserLoader;
import com.example.pericinprojektnizadatak.model.organization.AnimalWelfare;
import com.example.pericinprojektnizadatak.model.organization.Charity;
import com.example.pericinprojektnizadatak.model.organization.DisasterRelief;
import com.example.pericinprojektnizadatak.model.organization.Organization;
import com.example.pericinprojektnizadatak.model.records.OrganizationRecord;
import com.example.pericinprojektnizadatak.threads.GetVolunteersThread;
import com.example.pericinprojektnizadatak.utils.DatabaseUtils;
import com.example.pericinprojektnizadatak.utils.FileUtils;
import com.example.pericinprojektnizadatak.utils.OtherUtils;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class OrganizationAddNewVolunteerTabController {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationAddNewVolunteerTabController.class);
    @FXML
    private Label title;
    @FXML
    private Label username;
    @FXML
    private ImageView userPicture;
    @FXML
    private Spinner<Integer> fromAge;
    @FXML
    private Spinner<Integer> toAge;
    @FXML
    private ComboBox<String> gender;
    @FXML
    private ComboBox<String> nationality;
    @FXML
    private TableView<Volunteer> volunteerTableView;
    @FXML
    private TableColumn<Volunteer, Void> volunteerAddColumn;
    @FXML
    private TableColumn<Volunteer,String> volunteerNameColumn;
    @FXML
    private TableColumn<Volunteer,String> volunteerLastNameColumn;
    @FXML
    private TableColumn<Volunteer,String> volunteerAgeColumn;
    @FXML
    private TableColumn<Volunteer,String> volunteerGenderColumn;
    @FXML
    private TableColumn<Volunteer,String> volunteerNationalityColumn;
    @FXML
    private TableColumn<Volunteer,String> volunteerPhoneNumberColumn;
    @FXML
    private TableColumn<Volunteer,String> volunteerEmailColumn;
    @FXML
    private TableColumn<Volunteer,String> volunteerOperationsColumn;

    public void initialize() {

        String activeUserEmail = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(0);
        String typeOfAcc = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(1);

        Organization activeUser = null;

        if(typeOfAcc.equals(FilePathsEnum.CHARITY_FILE_PATH.getType())) {
            title.setTextFill(Color.rgb(255,0,0));
            activeUser = DatabaseUtils.getCharityFromEmail(activeUserEmail);
            userPicture.setImage(new Image(OrganizationRecord.charityPictureWebAddress));
            try {
                UserLoader<Charity> userLoader = new UserLoader<>();
                userLoader.checkIfUserWasLoadedCorrectly((Charity) activeUser);
            } catch (ProblemLoadingUserDetailsException e) {
                logger.error(e + e.getMessage());
                OtherUtils.loadLoginScreen();
            }
        }
        else if(typeOfAcc.equals(FilePathsEnum.ANIMAL_WELFARE_FILE_PATH.getType())) {
            title.setTextFill(Color.rgb(76,191,76));
            activeUser = DatabaseUtils.getAnimalWelfareFromEmail(activeUserEmail);
            username.setTextFill(Color.rgb(76,191,76));
            userPicture.setImage(new Image(OrganizationRecord.animalWelfarePictureWebAddress));
            try {
                UserLoader<AnimalWelfare> userLoader = new UserLoader<>();
                userLoader.checkIfUserWasLoadedCorrectly((AnimalWelfare) activeUser);
            } catch (ProblemLoadingUserDetailsException e) {
                logger.error(e + e.getMessage());
                OtherUtils.loadLoginScreen();
            }
        }
        else if(typeOfAcc.equals(FilePathsEnum.DISASTER_RELIEF_FILE_PATH.getType())) {
            title.setTextFill(Color.rgb(59,15,110));
            activeUser = DatabaseUtils.getDisasterReliefFromEmail(activeUserEmail);
            username.setTextFill(Color.rgb(59,15,110));
            userPicture.setImage(new Image(OrganizationRecord.disasterReliefWebAddress));
            try {
                UserLoader<DisasterRelief> userLoader = new UserLoader<>();
                userLoader.checkIfUserWasLoadedCorrectly((DisasterRelief) activeUser);
            } catch (ProblemLoadingUserDetailsException e) {
                logger.error(e + e.getMessage());
                OtherUtils.loadLoginScreen();
            }
        }



        assert activeUser != null;
        username.setText(activeUser.getName());

        SpinnerValueFactory<Integer> valueFactory1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(18,100, 1);
        SpinnerValueFactory<Integer> valueFactory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(18,100, 1);
        fromAge.setValueFactory(valueFactory1);
        toAge.setValueFactory(valueFactory2);

        Arrays.stream(GenderEnum.values())
                .map(GenderEnum::getGender)
                .forEach(item -> gender.getItems().add(item));

        Arrays.stream(NationalityEnum.values())
                .map(NationalityEnum::getCoutryName)
                .forEach(item -> nationality.getItems().add(item));


        volunteerNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Volunteer,String>, ObservableValue<String>>(){

            public ObservableValue<String> call(TableColumn.CellDataFeatures<Volunteer, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getPersonalInfo().getName());
            }
        });
        volunteerLastNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Volunteer,String>, ObservableValue<String>>(){
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Volunteer, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getPersonalInfo().getSurname());
            }
        });
        volunteerAgeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Volunteer,String>, ObservableValue<String>>(){
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Volunteer, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getPersonalInfo().getAge().toString());
            }
        });
        volunteerGenderColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Volunteer,String>, ObservableValue<String>>(){
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Volunteer, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getPersonalInfo().getGender().getGender());
            }
        });
        volunteerNationalityColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Volunteer,String>, ObservableValue<String>>(){
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Volunteer, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getPersonalInfo().getNationality().getCoutryName());
            }
        });
        volunteerPhoneNumberColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Volunteer,String>, ObservableValue<String>>(){
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Volunteer, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getPersonalInfo().getPhoneNumber());
            }
        });
        volunteerEmailColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Volunteer,String>, ObservableValue<String>>(){
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Volunteer, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getPersonalInfo().getEmail());
            }
        });
        volunteerOperationsColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Volunteer,String>, ObservableValue<String>>(){
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Volunteer, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getParticipatedOperations().toString());
            }
        });

        volunteerAddColumn.setCellFactory(param -> new TableCellWithPlusButton());

    }

    public static void addVolunteerToOrganization(String email){

        Long volunteerID = DatabaseUtils.getIDFromElement(email,FilePathsEnum.VOLUNTEER_FILE_PATH.getType());
        System.out.println(volunteerID);
        String activeUserEmail = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(0);
        System.out.println(activeUserEmail);
        String typeOfAcc = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(1);

        if(typeOfAcc.equals(FilePathsEnum.CHARITY_FILE_PATH.getType())){
            if(DatabaseUtils.updateVolunteerListByAdding(activeUserEmail,FilePathsEnum.CHARITY_FILE_PATH,volunteerID))
                OtherUtils.addedVolunteerToOrganization();
        }

        else if(typeOfAcc.equals(FilePathsEnum.DISASTER_RELIEF_FILE_PATH.getType())){
            if(DatabaseUtils.updateVolunteerListByAdding(activeUserEmail,FilePathsEnum.DISASTER_RELIEF_FILE_PATH,volunteerID))
                OtherUtils.addedVolunteerToOrganization();
        }

        else if(typeOfAcc.equals(FilePathsEnum.ANIMAL_WELFARE_FILE_PATH.getType())){
            if(DatabaseUtils.updateVolunteerListByAdding(activeUserEmail,FilePathsEnum.ANIMAL_WELFARE_FILE_PATH,volunteerID))
                OtherUtils.addedVolunteerToOrganization();
        }


    }



    public void sortVolunteers() throws SQLException, IOException {

        if(toAge.getValue().compareTo(fromAge.getValue()) < 0)
            OtherUtils.createAndDisplayAnAlert("'To' field can't be greater than 'From' field");

        Predicate<Volunteer> volunteerSortWithoutGender =
                volunteer -> volunteer.getPersonalInfo().getAge().compareTo(fromAge.getValue()) >= 0 &&
                        volunteer.getPersonalInfo().getAge().compareTo(toAge.getValue()) <= 0 &&
                        volunteer.getPersonalInfo().getNationality().getCoutryName().equals(nationality.getValue());


        Predicate<Volunteer> volunteerSortWithoutNationality =
                volunteer -> volunteer.getPersonalInfo().getAge().compareTo(fromAge.getValue()) >= 0 &&
                        volunteer.getPersonalInfo().getAge().compareTo(toAge.getValue()) <= 0 &&
                        volunteer.getPersonalInfo().getGender().getGender().equals(gender.getValue());

        Predicate<Volunteer> volunteerSortWithoutBoth =
                volunteer -> volunteer.getPersonalInfo().getAge().compareTo(fromAge.getValue()) >= 0 &&
                        volunteer.getPersonalInfo().getAge().compareTo(toAge.getValue()) <= 0;


        Predicate<Volunteer> volunteerSortWithAll =
                volunteer -> volunteer.getPersonalInfo().getAge().compareTo(fromAge.getValue()) >= 0 &&
                        volunteer.getPersonalInfo().getAge().compareTo(toAge.getValue()) <= 0 &&
                        volunteer.getPersonalInfo().getNationality().getCoutryName().equals(nationality.getValue()) &&
                        volunteer.getPersonalInfo().getGender().getGender().equals(gender.getValue());


        GetVolunteersThread getVolunteersThread = new GetVolunteersThread();
        Thread volunteersThread = new Thread(getVolunteersThread);
        volunteersThread.start();
        try{
            volunteersThread.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        List<Volunteer> volunteerList = getVolunteersThread.getAllVolunteersFromDatabaseThread();

        if(nationality.getValue() == null && gender.getValue() == null){
            volunteerList = volunteerList.stream()
                    .filter(volunteerSortWithoutBoth)
                    .collect(Collectors.toList());
        }
        else if(nationality.getValue() == null && !(gender.getValue().isEmpty())) {
            volunteerList = volunteerList.stream()
                    .filter(volunteerSortWithoutNationality)
                    .collect(Collectors.toList());
        }
        else if(!(nationality.getValue().isEmpty()) && gender.getValue() == null) {
            volunteerList = volunteerList.stream()
                    .filter(volunteerSortWithoutGender)
                    .collect(Collectors.toList());
        }
        else{
            volunteerList = volunteerList.stream()
                    .filter(volunteerSortWithAll)
                    .collect(Collectors.toList());
        }



       // volunteerList.stream().forEach(volunteer -> System.out.println(volunteer.getPersonalInfo().getName()));

        ObservableList<Volunteer> observableList = FXCollections.observableArrayList(volunteerList);
        volunteerTableView.setItems(observableList);
    }

    public void back(){
        String typeOfAcc = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(1);
        FXMLLoader fxmlLoader = null;
        if(typeOfAcc.equals(FilePathsEnum.CHARITY_FILE_PATH.getType())){
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("charityTabs/charityHomePage.fxml"));
        }
        else if(typeOfAcc.equals(FilePathsEnum.DISASTER_RELIEF_FILE_PATH.getType())){
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("disasterReliefTabs/disasterReliefHomePage.fxml"));
        }
        else if(typeOfAcc.equals(FilePathsEnum.ANIMAL_WELFARE_FILE_PATH.getType())){
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("animalWelfareTabs/animalWelfareHomePage.fxml"));
        }

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

}
