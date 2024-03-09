package com.example.pericinprojektnizadatak;

import com.example.pericinprojektnizadatak.enumerations.CitiesEnum;
import com.example.pericinprojektnizadatak.enumerations.FilePathsEnum;
import com.example.pericinprojektnizadatak.exceptions.AccountWithSameEmailException;
import com.example.pericinprojektnizadatak.exceptions.ProblemLoadingUserDetailsException;
import com.example.pericinprojektnizadatak.model.generics.Changes;
import com.example.pericinprojektnizadatak.model.generics.UserLoader;
import com.example.pericinprojektnizadatak.model.organization.AnimalWelfare;
import com.example.pericinprojektnizadatak.model.organization.Charity;
import com.example.pericinprojektnizadatak.model.organization.DisasterRelief;
import com.example.pericinprojektnizadatak.model.organization.Organization;
import com.example.pericinprojektnizadatak.model.records.OrganizationRecord;
import com.example.pericinprojektnizadatak.utils.DataChanges;
import com.example.pericinprojektnizadatak.utils.DatabaseUtils;
import com.example.pericinprojektnizadatak.utils.FileUtils;
import com.example.pericinprojektnizadatak.utils.OtherUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
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

public class OrganizationEditAccountController {
    private static final Logger logger = LoggerFactory.getLogger(OrganizationEditAccountController.class);

    @FXML
    private Label title;
    @FXML
    private Label username;
    @FXML
    private ImageView userPicture;
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

    FXMLLoader loader;

  //  private static  String typeOfAcc = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(1);
  //  private static  String activeUserEmail = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(0);


    @FXML
    public void initialize(){

        String typeOfAcc = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(1);
        String activeUserEmail = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(0);



        System.out.println(typeOfAcc);
        System.out.println(activeUserEmail);

        city.setItems(FXCollections.observableArrayList(Arrays.stream(CitiesEnum.values()).map(CitiesEnum::getCityName).toList()));
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, LocalDateTime.now().getYear(), 1);
        foundingYear.setValueFactory(valueFactory);

        Organization activeUser = null;

        if(typeOfAcc.equals(FilePathsEnum.CHARITY_FILE_PATH.getType())) {
            title.setTextFill(Color.rgb(255,0,0));
            activeUser = DatabaseUtils.getCharityFromEmail(activeUserEmail);
            userPicture.setImage(new Image(OrganizationRecord.charityPictureWebAddress));
            try {
                UserLoader<Charity> userLoader = new UserLoader<>();
                userLoader.checkIfUserWasLoadedCorrectly((Charity) activeUser);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("additionalOrganizationDetails/charityAdditionalDetails.fxml"));
                Pane loadedPane = loader.load();
                this.loader = loader;
                additionalOrganizationDetailsPane.getChildren().clear();
                additionalOrganizationDetailsPane.getChildren().add(loadedPane);
            } catch (ProblemLoadingUserDetailsException e) {
                logger.error(e + e.getMessage());
                OtherUtils.loadLoginScreen();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            loadExistingData(activeUser,FilePathsEnum.CHARITY_FILE_PATH);
        }
        else if(typeOfAcc.equals(FilePathsEnum.ANIMAL_WELFARE_FILE_PATH.getType())) {
            title.setTextFill(Color.rgb(76,191,76));
            activeUser = DatabaseUtils.getAnimalWelfareFromEmail(activeUserEmail);
            username.setTextFill(Color.rgb(76,191,76));
            userPicture.setImage(new Image(OrganizationRecord.animalWelfarePictureWebAddress));
            try {
                UserLoader<AnimalWelfare> userLoader = new UserLoader<>();
                userLoader.checkIfUserWasLoadedCorrectly((AnimalWelfare) activeUser);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("additionalOrganizationDetails/animalWelfareAdditionalDetails.fxml"));
                Pane loadedPane = loader.load();
                this.loader = loader;
                additionalOrganizationDetailsPane.getChildren().clear();
                additionalOrganizationDetailsPane.getChildren().add(loadedPane);
            } catch (ProblemLoadingUserDetailsException e) {
                logger.error(e + e.getMessage());
                OtherUtils.loadLoginScreen();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            loadExistingData(activeUser,FilePathsEnum.ANIMAL_WELFARE_FILE_PATH);
        }
        else if(typeOfAcc.equals(FilePathsEnum.DISASTER_RELIEF_FILE_PATH.getType())) {
            title.setTextFill(Color.rgb(59,15,110));
            activeUser = DatabaseUtils.getDisasterReliefFromEmail(activeUserEmail);
            username.setTextFill(Color.rgb(59,15,110));
            userPicture.setImage(new Image(OrganizationRecord.disasterReliefWebAddress));
            try {
                UserLoader<DisasterRelief> userLoader = new UserLoader<>();
                userLoader.checkIfUserWasLoadedCorrectly((DisasterRelief) activeUser);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("additionalOrganizationDetails/disasterReliefAdditionalDetails.fxml"));
                Pane loadedPane = loader.load();
                this.loader = loader;
                additionalOrganizationDetailsPane.getChildren().clear();
                additionalOrganizationDetailsPane.getChildren().add(loadedPane);
            } catch (ProblemLoadingUserDetailsException e) {
                logger.error(e + e.getMessage());
                OtherUtils.loadLoginScreen();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            loadExistingData(activeUser,FilePathsEnum.DISASTER_RELIEF_FILE_PATH);
        }



        assert activeUser != null;
        username.setText(activeUser.getName());
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

    public void loadExistingData(Organization activeUser,FilePathsEnum filePathsEnum) {
        name.setText(activeUser.getName());
        description.setText(activeUser.getDescription());
        city.setValue(activeUser.getAddress().getCity().getCityName());
        foundingYear.getValueFactory().setValue(activeUser.getFoundingYear());
        addressName.setText(activeUser.getAddress().getStreet());
        houseNumber.setText(activeUser.getAddress().getHouseNumber());
        contactNumber.setText(activeUser.getPhone().replace("+385","").trim());
        email.setText(activeUser.getEmail());

        CharityAdditionalDetailsController charityAdditionalDetailsController;
        DisasterReliefAdditionalDetailsController disasterReliefAdditionalDetailsController;
        AnimalWelfareAdditionalDetailsController animalWelfareAdditionalDetailsController;

        switch (filePathsEnum) {
            case CHARITY_FILE_PATH -> {
                charityAdditionalDetailsController = loader.getController();
                Charity activeUser1 = (Charity)activeUser;
                charityAdditionalDetailsController.setFamiliesCurrentlyAiding(activeUser1.getFamiliesCurrentlyAiding().toString());
                break;
            }
            case DISASTER_RELIEF_FILE_PATH -> {
                disasterReliefAdditionalDetailsController = loader.getController();
                DisasterRelief activeUser1 = (DisasterRelief) activeUser;
                disasterReliefAdditionalDetailsController.setProvidesFoodSupply(activeUser1.getProvidesFoodSupply());
                break;
            }
            case ANIMAL_WELFARE_FILE_PATH -> {
                animalWelfareAdditionalDetailsController = loader.getController();
                AnimalWelfare activeUser1 = (AnimalWelfare) activeUser;
                animalWelfareAdditionalDetailsController.setConductsRescueOperations(activeUser1.getConductsRescueOperations());
                animalWelfareAdditionalDetailsController.setProvidesMedicalCare(activeUser1.getProvidesMedicalCare());
                break;
            }
        }


    }

    public void reset(){
        String typeOfAcc = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(1);
        String activeUserEmail = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(0);

        if(typeOfAcc.equals(FilePathsEnum.CHARITY_FILE_PATH.getType())) {
           Charity activeUser = DatabaseUtils.getCharityFromEmail(activeUserEmail);
           loadExistingData(activeUser,FilePathsEnum.CHARITY_FILE_PATH);
        }
        else if(typeOfAcc.equals(FilePathsEnum.ANIMAL_WELFARE_FILE_PATH.getType())) {
            AnimalWelfare activeUser = DatabaseUtils.getAnimalWelfareFromEmail(activeUserEmail);
            loadExistingData(activeUser,FilePathsEnum.ANIMAL_WELFARE_FILE_PATH);
        }
        else if(typeOfAcc.equals(FilePathsEnum.DISASTER_RELIEF_FILE_PATH.getType())) {
            DisasterRelief activeUser = DatabaseUtils.getDisasterReliefFromEmail(activeUserEmail);
            loadExistingData(activeUser,FilePathsEnum.DISASTER_RELIEF_FILE_PATH);
        }
        password.setText("");
    }

    public void updateOrganizationDetails(){
        Pattern pattern1 = Pattern.compile("[^a-zA-Z]");
        Pattern pattern2 = Pattern.compile("[^0-9]");
        Matcher matcher;

        CharityAdditionalDetailsController charityAdditionalDetailsController = null;
        DisasterReliefAdditionalDetailsController disasterReliefAdditionalDetailsController = null;
        AnimalWelfareAdditionalDetailsController animalWelfareAdditionalDetailsController = null;

        String typeOfAcc = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(1);
        String activeUserEmail = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(0);

        Organization activeUser = null;
        if(typeOfAcc.equals(FilePathsEnum.CHARITY_FILE_PATH.getType())) {
            activeUser = DatabaseUtils.getCharityFromEmail(activeUserEmail);
            charityAdditionalDetailsController = loader.getController();
        }
        else if(typeOfAcc.equals(FilePathsEnum.ANIMAL_WELFARE_FILE_PATH.getType())) {
            activeUser = DatabaseUtils.getAnimalWelfareFromEmail(activeUserEmail);
            animalWelfareAdditionalDetailsController = loader.getController();
        }
        else if(typeOfAcc.equals(FilePathsEnum.DISASTER_RELIEF_FILE_PATH.getType())) {
            activeUser = DatabaseUtils.getDisasterReliefFromEmail(activeUserEmail);
            disasterReliefAdditionalDetailsController = loader.getController();
        }




        //OVDI KRECE
        //LUDNICA

        //STRING DIO
        Map<String,String> mapWhatToChangeThenValueString = new HashMap<>();
        Map<String,Integer> mapWhatToChangeThenValueInteger = new HashMap<>();
        Map<String,Boolean> mapWhatToChangeThenValueBoolean = new HashMap<>();

   //     Set<String> changedElementsList = new HashSet<>();
        List<String> unfilledField = new ArrayList<>();
        String contact_number = "+385 " + contactNumber.getText();
        System.out.println(contact_number + " "+contactNumber.getText());


        if(name.getText().compareTo(activeUser.getName())!=0){
            if(!name.getText().isEmpty())
                mapWhatToChangeThenValueString.put("name",name.getText());
            else unfilledField.add("name");
        }
        if(description.getText().compareTo(activeUser.getDescription())!=0){
            if(!description.getText().isEmpty())
                mapWhatToChangeThenValueString.put("description",description.getText());
            else unfilledField.add("description");
        }
        if(city.getValue().compareTo(activeUser.getAddress().getCity().getCityName())!=0)  mapWhatToChangeThenValueString.put("city",city.getValue());

        if(addressName.getText().compareTo(activeUser.getAddress().getStreet())!=0){
            if(!addressName.getText().isEmpty())
                mapWhatToChangeThenValueString.put("street",addressName.getText());
            else unfilledField.add("street");
        }
        if(houseNumber.getText().compareTo(activeUser.getAddress().getHouseNumber())!=0){
            if(!houseNumber.getText().isEmpty())
                mapWhatToChangeThenValueString.put("house_number",houseNumber.getText());
            else unfilledField.add("house_number");
        }
        //ovdje sam zamijenio radi matchera
        if(contact_number.compareTo(activeUser.getPhone()) != 0){

            matcher = pattern2.matcher(contactNumber.getText());

            if(contact_number.compareTo("+385 ") == 0 || matcher.find())
                unfilledField.add("contact_phone");
            else mapWhatToChangeThenValueString.put("contact_phone",contact_number);

            matcher.reset(contactNumber.getText());
        }


        if(email.getText().compareTo(activeUser.getEmail())!=0){
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



       //INTEGER DIO
        if(foundingYear.getValue().compareTo(activeUser.getFoundingYear()) != 0) mapWhatToChangeThenValueInteger.put("founding_year",foundingYear.getValue());
        if (typeOfAcc.equals(FilePathsEnum.CHARITY_FILE_PATH.getType())) {
            Charity activeUser1 = (Charity) activeUser;
            if (charityAdditionalDetailsController.getNumberOfFamilies().isEmpty()) {
                unfilledField.add("number of currently aiding families");
            } else if (!charityAdditionalDetailsController.getNumberOfFamilies().equals(activeUser1.getFamiliesCurrentlyAiding().toString())) {
                matcher = pattern2.matcher(charityAdditionalDetailsController.getNumberOfFamilies());
                if (matcher.find()) {
                    unfilledField.add("number of currently aiding families");
                } else {
                    mapWhatToChangeThenValueInteger.put("families_currently_aiding", Integer.valueOf(charityAdditionalDetailsController.getNumberOfFamilies()));
                }
            }
        }


        //BOOLEAN DIO
        else if(typeOfAcc.equals(FilePathsEnum.ANIMAL_WELFARE_FILE_PATH.getType())) {
            AnimalWelfare activeUser1 = (AnimalWelfare) activeUser;
            if(activeUser1.getProvidesMedicalCare().compareTo(animalWelfareAdditionalDetailsController.getProvidesMedicalCare().isSelected()) != 0){
                mapWhatToChangeThenValueBoolean.put("provides_medical_care",animalWelfareAdditionalDetailsController.getProvidesMedicalCare().isSelected());
            }

            if(activeUser1.getConductsRescueOperations().compareTo(animalWelfareAdditionalDetailsController.getConductsRescueOperations().isSelected()) != 0){
                mapWhatToChangeThenValueBoolean.put("conducts_rescue_operations",animalWelfareAdditionalDetailsController.getConductsRescueOperations().isSelected());
            }

        }
        else if(typeOfAcc.equals(FilePathsEnum.DISASTER_RELIEF_FILE_PATH.getType())) {
            DisasterRelief activeUser1 = (DisasterRelief) activeUser;
            if(activeUser1.getProvidesFoodSupply().compareTo(disasterReliefAdditionalDetailsController.getProvidesFoodSupply().isSelected()) != 0){
                mapWhatToChangeThenValueBoolean.put("provides_food_supply",disasterReliefAdditionalDetailsController.getProvidesFoodSupply().isSelected());
            }
        }



        if(!unfilledField.isEmpty()){
            OtherUtils.createAndDisplayAnAlert(unfilledField);
            return;
        }

        if (!password.getText().isEmpty()) {
            Long id = DatabaseUtils.getIDFromElement(activeUserEmail, typeOfAcc);
            List<String> listOfPasswords = FileUtils.readEveryLineFromFile(FilePathsEnum.valueOf(typeOfAcc.toUpperCase() + "_FILE_PATH").getFilePath());

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

            FileUtils.writeLinesToFile(updatedList,FilePathsEnum.valueOf(typeOfAcc.toUpperCase() + "_FILE_PATH").getFilePath());
            OtherUtils.updatedPassword();

        }

        System.out.println(mapWhatToChangeThenValueBoolean);
        System.out.println(mapWhatToChangeThenValueString);
        System.out.println(mapWhatToChangeThenValueInteger);
        if(mapWhatToChangeThenValueBoolean.isEmpty() && mapWhatToChangeThenValueInteger.isEmpty() && mapWhatToChangeThenValueString.isEmpty()){
            if (password.getText().isEmpty()) OtherUtils.createAndDisplayAnAlert("No changes done!");
            return;
        }

        if(!mapWhatToChangeThenValueString.isEmpty()){
            DatabaseUtils.commitOrganizationChangesToDatabaseString(mapWhatToChangeThenValueString);
           // OtherUtils.updatedAccountDetails();
        }
        if(!mapWhatToChangeThenValueInteger.isEmpty()){
            DatabaseUtils.commitOrganizationChangesToDatabaseInteger(mapWhatToChangeThenValueInteger);
          //  OtherUtils.updatedAccountDetails();
        }
        if(!mapWhatToChangeThenValueBoolean.isEmpty()){
            DatabaseUtils.commitOrganizationChangesToDatabaseBoolean(mapWhatToChangeThenValueBoolean);
          //  OtherUtils.updatedAccountDetails();
        }
        OtherUtils.updatedAccountDetails();



        if (typeOfAcc.equals(FilePathsEnum.CHARITY_FILE_PATH.getType())) {
            FileUtils.writeActiveUserToFile(email.getText(),FilePathsEnum.CHARITY_FILE_PATH);

        }
        else if(typeOfAcc.equals(FilePathsEnum.ANIMAL_WELFARE_FILE_PATH.getType())) {
            FileUtils.writeActiveUserToFile(email.getText(),FilePathsEnum.ANIMAL_WELFARE_FILE_PATH);
        }
        else if(typeOfAcc.equals(FilePathsEnum.DISASTER_RELIEF_FILE_PATH.getType())) {
            FileUtils.writeActiveUserToFile(email.getText(),FilePathsEnum.DISASTER_RELIEF_FILE_PATH);
        }

        String newActiveUserEmail = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(0);

        Organization newUser = null;
        if(typeOfAcc.equals(FilePathsEnum.CHARITY_FILE_PATH.getType())) {
            newUser = DatabaseUtils.getCharityFromEmail(newActiveUserEmail);
        }
        else if(typeOfAcc.equals(FilePathsEnum.ANIMAL_WELFARE_FILE_PATH.getType())) {
            newUser = DatabaseUtils.getAnimalWelfareFromEmail(newActiveUserEmail);
        }
        else if(typeOfAcc.equals(FilePathsEnum.DISASTER_RELIEF_FILE_PATH.getType())) {
            newUser = DatabaseUtils.getDisasterReliefFromEmail(newActiveUserEmail);
        }



        Optional<DataChanges> dataChangeStoreUtil = FileUtils.readCurrentData();
         //DataChanges dataChanges = new DataChanges();

        Changes change = new Changes(typeOfAcc, LocalDateTime.now(),activeUser,newUser);

        dataChangeStoreUtil.get().addChange(change);
        // dataChanges.addChange(change);


        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("E:/Faks/3. semestar/Java/Volonterske_akcije_Pericin/Pericin-ProjektniZadatak/files/serialization.bin"))) {
            oos.writeObject(dataChangeStoreUtil.get());
            //oos.writeObject(dataChanges);

            oos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        loadExistingData(newUser,FilePathsEnum.valueOf(typeOfAcc.toUpperCase()+"_FILE_PATH"));
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
