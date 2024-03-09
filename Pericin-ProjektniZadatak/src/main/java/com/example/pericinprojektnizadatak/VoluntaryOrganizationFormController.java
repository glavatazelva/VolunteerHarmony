package com.example.pericinprojektnizadatak;

import com.example.pericinprojektnizadatak.enumerations.CitiesEnum;
import com.example.pericinprojektnizadatak.enumerations.FilePathsEnum;
import com.example.pericinprojektnizadatak.exceptions.AccountWithSameEmailException;
import com.example.pericinprojektnizadatak.model.Address;
import com.example.pericinprojektnizadatak.model.organization.AnimalWelfare;
import com.example.pericinprojektnizadatak.model.organization.Charity;
import com.example.pericinprojektnizadatak.model.organization.DisasterRelief;
import com.example.pericinprojektnizadatak.utils.DatabaseUtils;
import com.example.pericinprojektnizadatak.utils.FileUtils;
import com.example.pericinprojektnizadatak.utils.OtherUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VoluntaryOrganizationFormController {

    @FXML
    private TextField name;
    @FXML
    private TextArea description;
    @FXML
    private Spinner<Integer> foundingYear;
    @FXML
    private ComboBox<String> city;
    @FXML
    private TextField addressName;
    @FXML
    private TextField houseNumber;
    @FXML
    private TextField contactNumber;
    @FXML
    private TextField email;
    @FXML
    private TextField password;

    @FXML
    public StackPane additionalOrganizationDetailsPane;

  /*
    @FXML
    private RadioButton radioButton1;
    @FXML
    private RadioButton radioButton2;
    */

    @FXML
    private ComboBox<String> typesOfOrganization;

    FXMLLoader loader;

    private static final Logger logger = LoggerFactory.getLogger(VoluntaryOrganizationFormController.class);

    @FXML
    public void initialize(){



        city.setItems(FXCollections.observableArrayList(Arrays.stream(CitiesEnum.values()).map(CitiesEnum::getCityName).toList()));
        typesOfOrganization.getItems().addAll("Charity", "Disaster Relief", "Animal Welfare");
       // radioButton1.setVisible(false);
      //  radioButton2.setVisible(false);

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, LocalDateTime.now().getYear(), 1);
        foundingYear.setValueFactory(valueFactory);


    }

    public String getTypeOfOrganization(){
        return typesOfOrganization.getValue();
    }

    public void showAdditionalOrganizationDetails() {
        if (typesOfOrganization.getValue().compareTo("Animal Welfare") == 0){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("additionalOrganizationDetails/animalWelfareAdditionalDetails.fxml"));
                Pane loadedPane = loader.load();
                this.loader = loader;
                additionalOrganizationDetailsPane.getChildren().clear();
                additionalOrganizationDetailsPane.getChildren().add(loadedPane);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if (typesOfOrganization.getValue().compareTo("Disaster Relief") == 0){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("additionalOrganizationDetails/disasterReliefAdditionalDetails.fxml"));
                Pane loadedPane = loader.load();
                this.loader = loader;
                additionalOrganizationDetailsPane.getChildren().clear();
                additionalOrganizationDetailsPane.getChildren().add(loadedPane);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        else if (typesOfOrganization.getValue().compareTo("Charity") == 0){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("additionalOrganizationDetails/charityAdditionalDetails.fxml"));
                Pane loadedPane = loader.load();
                this.loader = loader;
                additionalOrganizationDetailsPane.getChildren().clear();
                additionalOrganizationDetailsPane.getChildren().add(loadedPane);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }


    public void organizationCreationDependingOnType(){

        if(typesOfOrganization.getValue() == null){
            OtherUtils.createAndDisplayAnAlert("Odaberite tip organizacije");
            return;
        }

        String selectedType = typesOfOrganization.getValue();

        switch (selectedType) {
            case "Charity":
                createCharity();
                break;
            case "Animal Welfare":
                createAnimalWelfare();
                break;
            case "Disaster Relief":
                createDisasterRelief();
                break;
            default:
        }

    }

    public void createCharity(){
        List<String> listOfUnfilledFields = new ArrayList<>();
        CharityAdditionalDetailsController controller = loader.getController();

        Pattern pattern1 = Pattern.compile("[^a-zA-Z]");
        Pattern pattern2 = Pattern.compile("[^0-9]");
        Matcher matcher = pattern2.matcher(contactNumber.getText());
        if(matcher.find()){
            listOfUnfilledFields.add("contact number");
        }
        matcher.reset(contactNumber.getText());

        if(name.getText().isEmpty()) listOfUnfilledFields.add("name");
        if(description.getText().isEmpty()) listOfUnfilledFields.add("description");
        if(email.getText().isEmpty() ||!email.getText().contains("@")) listOfUnfilledFields.add("email");
        if(city.getValue() == null) listOfUnfilledFields.add("city");
        if(addressName.getText().isEmpty()) listOfUnfilledFields.add("phone number");
        if(houseNumber.getText().isEmpty()) listOfUnfilledFields.add("house number");
        if(contactNumber.getText().isEmpty()) listOfUnfilledFields.add("contact number");
        if(password.getText().isEmpty()) listOfUnfilledFields.add("password");

        matcher = pattern2.matcher(controller.getNumberOfFamilies());
        if(matcher.find()){
            listOfUnfilledFields.add("number of currently aiding families");
        }
        matcher.reset(controller.getNumberOfFamilies());
        if(controller.getNumberOfFamilies().isEmpty())listOfUnfilledFields.add("number of currently aiding families");


        if(!listOfUnfilledFields.isEmpty()){
            OtherUtils.createAndDisplayAnAlert(listOfUnfilledFields);
            return;
        }

        try {
            OtherUtils.findIfAnotherAccountWithSameEmailExist(email.getText());

        } catch (AccountWithSameEmailException e) {
            OtherUtils.createAndDisplayAnAlert(e.getMessage());
            return;
        } catch (SQLException | IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        Address address = new Address.Builder()
                .street(addressName.getText())
                .houseNumber(houseNumber.getText())
                .city(CitiesEnum.valueOf(city.getValue().toUpperCase()))
                .build();


        Charity charity = new Charity(name.getText(),
                description.getText(),
                email.getText(),
                contactNumber.getText(),
                address,
                foundingYear.getValue(),
                new ArrayList<>()
                ,Integer.parseInt(controller.getNumberOfFamilies())
                ,BigDecimal.valueOf(0));

        DatabaseUtils.addNewCharity(charity);
        FileUtils.writeHashToFile(FileUtils.hashSHA256(password.getText()),email.getText(), FilePathsEnum.CHARITY_FILE_PATH);

        OtherUtils.createdAccountAlert();
        OtherUtils.loadLoginScreen();


    }

    public void createAnimalWelfare(){
        List<String> listOfUnfilledFields = new ArrayList<>();

        Pattern pattern1 = Pattern.compile("[^a-zA-Z]");
        Pattern pattern2 = Pattern.compile("[^0-9]");
        Matcher matcher = pattern2.matcher(contactNumber.getText());
        if(matcher.find()){
            listOfUnfilledFields.add("contact number");
        }
        matcher.reset(contactNumber.getText());

        AnimalWelfareAdditionalDetailsController controller = loader.getController();

        Boolean providesMedicalCare = controller.getProvidesMedicalCare().isSelected();
        Boolean conductsRescueOperations = controller.getConductsRescueOperations().isSelected();


        if(name.getText().isEmpty()) listOfUnfilledFields.add("name");
        if(description.getText().isEmpty()) listOfUnfilledFields.add("description");
        if(email.getText().isEmpty() || !email.getText().contains("@")) listOfUnfilledFields.add("email");
        if(city.getValue() == null) listOfUnfilledFields.add("city");
        if(addressName.getText().isEmpty()) listOfUnfilledFields.add("phone number");
        if(houseNumber.getText().isEmpty()) listOfUnfilledFields.add("house number");
        if(contactNumber.getText().isEmpty()) listOfUnfilledFields.add("contact number");
        if(password.getText().isEmpty()) listOfUnfilledFields.add("password");



        if(!listOfUnfilledFields.isEmpty()){
            OtherUtils.createAndDisplayAnAlert(listOfUnfilledFields);
            return;
        }

        try {
            OtherUtils.findIfAnotherAccountWithSameEmailExist(email.getText());

        } catch (AccountWithSameEmailException e) {
            OtherUtils.createAndDisplayAnAlert(e.getMessage());
            return;
        } catch (SQLException | IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        Address address = new Address.Builder()
                .street(addressName.getText())
                .houseNumber(houseNumber.getText())
                .city(CitiesEnum.valueOf(city.getValue().toUpperCase()))
                .build();


        AnimalWelfare animalWelfare = new AnimalWelfare(name.getText(),
                description.getText(),
                email.getText(),
                contactNumber.getText(),
                address,
                foundingYear.getValue()
                ,new ArrayList<>()
                ,0
                ,providesMedicalCare
                ,conductsRescueOperations);

        DatabaseUtils.addNewAnimalWelfare(animalWelfare);
        FileUtils.writeHashToFile(FileUtils.hashSHA256(password.getText()),email.getText(), FilePathsEnum.ANIMAL_WELFARE_FILE_PATH);

        OtherUtils.createdAccountAlert();
        OtherUtils.loadLoginScreen();


    }

    public void createDisasterRelief(){
        List<String> listOfUnfilledFields = new ArrayList<>();

        Pattern pattern1 = Pattern.compile("[^a-zA-Z]");
        Pattern pattern2 = Pattern.compile("[^0-9]");
        Matcher matcher = pattern2.matcher(contactNumber.getText());
        if(matcher.find()){
            listOfUnfilledFields.add("contact number");
        }
        matcher.reset(contactNumber.getText());

        DisasterReliefAdditionalDetailsController controller = loader.getController();

        Boolean providesFoodSupply = controller.getProvidesFoodSupply().isSelected();


        if(name.getText().isEmpty()) listOfUnfilledFields.add("name");
        if(description.getText().isEmpty()) listOfUnfilledFields.add("description");
        if(email.getText().isEmpty()|| !email.getText().contains("@")) listOfUnfilledFields.add("email");
        if(city.getValue() == null) listOfUnfilledFields.add("city");
        if(addressName.getText().isEmpty()) listOfUnfilledFields.add("phone number");
        if(houseNumber.getText().isEmpty()) listOfUnfilledFields.add("house number");
        if(contactNumber.getText().isEmpty()) listOfUnfilledFields.add("contact number");
        if(password.getText().isEmpty()) listOfUnfilledFields.add("password");



        if(!listOfUnfilledFields.isEmpty()){
            OtherUtils.createAndDisplayAnAlert(listOfUnfilledFields);
            return;
        }

        try {
            OtherUtils.findIfAnotherAccountWithSameEmailExist(email.getText());

        } catch (AccountWithSameEmailException e) {
            OtherUtils.createAndDisplayAnAlert(e.getMessage());
            return;
        } catch (SQLException | IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        Address address = new Address.Builder()
                .street(addressName.getText())
                .houseNumber(houseNumber.getText())
                .city(CitiesEnum.valueOf(city.getValue().toUpperCase()))
                .build();


        DisasterRelief disasterRelief = new DisasterRelief(name.getText(),
                description.getText(),
                email.getText(),
                contactNumber.getText(),
                address,
                foundingYear.getValue(),
                new ArrayList<>(),
                providesFoodSupply,
                0
                );

        DatabaseUtils.addNewDisasterRelief(disasterRelief);
        FileUtils.writeHashToFile(FileUtils.hashSHA256(password.getText()),email.getText(), FilePathsEnum.DISASTER_RELIEF_FILE_PATH);

        OtherUtils.createdAccountAlert();
        OtherUtils.loadLoginScreen();


    }
}
