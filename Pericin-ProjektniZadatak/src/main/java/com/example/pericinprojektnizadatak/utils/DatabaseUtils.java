package com.example.pericinprojektnizadatak.utils;

import com.example.pericinprojektnizadatak.enumerations.*;
import com.example.pericinprojektnizadatak.model.Address;
import com.example.pericinprojektnizadatak.model.PersonalInformation;
import com.example.pericinprojektnizadatak.model.Volunteer;
import com.example.pericinprojektnizadatak.model.organization.AnimalWelfare;
import com.example.pericinprojektnizadatak.model.organization.Charity;
import com.example.pericinprojektnizadatak.model.organization.DisasterRelief;
import com.example.pericinprojektnizadatak.model.voluntaryAction.AnimalWelfareAction;
import com.example.pericinprojektnizadatak.model.voluntaryAction.CharityAction;
import com.example.pericinprojektnizadatak.model.voluntaryAction.DisasterReliefAction;
import com.example.pericinprojektnizadatak.model.voluntaryAction.VoluntaryAction;
import javafx.fxml.FXMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DatabaseUtils {
//resetanje ID u bazi ALTER TABLE VOLUNTEER ALTER COLUMN ID RESTART WITH 1;
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtils.class);
    private static FXMLLoader loader;
    private static final String DATABASE_FILE = "E:/Faks/3. semestar/Java/Volonterske_akcije_Pericin/Pericin-ProjektniZadatak/files/database.properties";

    public static synchronized Connection connectToDatabase(){

        Properties svojstva = new Properties();
        try {
            svojstva.load(new FileReader(DATABASE_FILE));
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
        String urlBazePodataka = svojstva.getProperty("url");
        String korisnickoIme = svojstva.getProperty("username");
        String lozinka = svojstva.getProperty("password");
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(urlBazePodataka, korisnickoIme,lozinka);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }


        return connection;
    }

    public static void addNewVolunteer(Volunteer volunteer){

        String addVolunteerQuery = "INSERT INTO VOLUNTEER (name, surname, age, gender, nationality, phone_number, email, participated_operations) " +
                "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(addVolunteerQuery)) {

            preparedStatement.setString(1, volunteer.getPersonalInfo().getName());
            preparedStatement.setString(2, volunteer.getPersonalInfo().getSurname());
            preparedStatement.setInt(3, volunteer.getPersonalInfo().getAge());
            preparedStatement.setString(4, volunteer.getPersonalInfo().getGender().getGender());
            preparedStatement.setString(5, volunteer.getPersonalInfo().getNationality().getCoutryName());
            preparedStatement.setString(6, volunteer.getPersonalInfo().getPhoneNumber());
            preparedStatement.setString(7, volunteer.getPersonalInfo().getEmail());
            preparedStatement.setInt(8, volunteer.getParticipatedOperations());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            String message = "An error occurred while adding a new Volunteer!";
           logger.error(e.getMessage() + message);
        }

    }



    public static List<Volunteer> getAllVolunteersFromDatabase(){
        List<Volunteer> volunteerList = new ArrayList<>();

        try (Connection connection = connectToDatabase();
             Statement sqlStatement = connection.createStatement();
             ResultSet itemsResultSet = sqlStatement.executeQuery("SELECT * FROM VOLUNTEER")) {

            while (itemsResultSet.next()) {
                Volunteer newVolunteer = getVolunteerFromResultSet(itemsResultSet);
                volunteerList.add(newVolunteer);
            }



        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }




        return volunteerList;
    }


    public static List<Charity> getAllCharitiesFromDatabase(){
        List<Charity> charityList = new ArrayList<>();

        try (Connection connection = connectToDatabase();
             Statement sqlStatement = connection.createStatement();
             ResultSet itemsResultSet = sqlStatement.executeQuery("SELECT * FROM CHARITY")) {

            while (itemsResultSet.next()) {
                Charity newCharity = getCharityFromResultSet(itemsResultSet);
                charityList.add(newCharity);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return charityList;
    }

    public static List<AnimalWelfare> getAllAnimalWelfaresFromDatabase(){
        List<AnimalWelfare> animalWelfares = new ArrayList<>();

        try (Connection connection = connectToDatabase();
             Statement sqlStatement = connection.createStatement();
             ResultSet itemsResultSet = sqlStatement.executeQuery("SELECT * FROM ANIMAL_WELFARE")) {

            while (itemsResultSet.next()) {
                AnimalWelfare newWelfare = getAnimalWelfareFromResultSet(itemsResultSet);
                animalWelfares.add(newWelfare);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return animalWelfares;
    }


    public static List<DisasterRelief> getAllDisasterReliefsFromDatabase(){
        List<DisasterRelief> disasterReliefs = new ArrayList<>();

        try (Connection connection = connectToDatabase();
             Statement sqlStatement = connection.createStatement();
             ResultSet itemsResultSet = sqlStatement.executeQuery("SELECT * FROM DISASTER_RELIEF")) {

            while (itemsResultSet.next()) {
                DisasterRelief newRelief = getDisasterReliefFromResultSet(itemsResultSet);
                disasterReliefs.add(newRelief);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return disasterReliefs;
    }


    public static Volunteer getVolunteerFromEmail(String email){
        List<Volunteer> volunteerList = getAllVolunteersFromDatabase();

        for(Volunteer tempVolunteer:volunteerList){
            if(email.compareTo(tempVolunteer.getPersonalInfo().getEmail()) == 0){

                return tempVolunteer;
            }
        }

       return null;

    }



    public static Charity getCharityFromEmail(String email){
        List<Charity> charityList = getAllCharitiesFromDatabase();

        for(Charity tempCharity:charityList){
            if(email.compareTo(tempCharity.getEmail()) == 0){

                return tempCharity;
            }
        }

        return null;

    }
/*
    public static Long getIdFromOrganization(String email,FilePathsEnum filePathsEnum){
        String query = "SELECT * FROM "+filePathsEnum.getType().toUpperCase()+" WHERE EMAIL = '"+email+"'";
        Long id = null;

        try(Connection connection = connectToDatabase();
            Statement sqlStatement = connection.createStatement();
            ResultSet resultSet = sqlStatement.executeQuery(query)){

            while(resultSet.next()){
                id = resultSet.getLong("ID");
            }


        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return id;
    }
*/


    public static AnimalWelfare getAnimalWelfareFromEmail(String email){
        List<AnimalWelfare> animalWelfareList = getAllAnimalWelfaresFromDatabase();

        for(AnimalWelfare tempWelfare:animalWelfareList){
            if(email.compareTo(tempWelfare.getEmail()) == 0){

                return tempWelfare;
            }
        }

        return null;

    }

    public static DisasterRelief getDisasterReliefFromEmail(String email){
        List<DisasterRelief> disasterReliefList = getAllDisasterReliefsFromDatabase();

        for(DisasterRelief tempDisaster:disasterReliefList){
            if(email.compareTo(tempDisaster.getEmail()) == 0){

                return tempDisaster;
            }
        }

        return null;

    }


    public static long getIDFromElement(String email,String whatType){


        String query = "SELECT * FROM " + whatType.toUpperCase();

        long foundID = 0;

        try (Connection connection = connectToDatabase();
             Statement sqlStatement = connection.createStatement();

             ResultSet itemsResultSet = sqlStatement.executeQuery(query)) {

            while (itemsResultSet.next()) {

                if(whatType.compareTo(FilePathsEnum.VOLUNTEER_FILE_PATH.getType()) == 0){
                    Volunteer newVolunteer = getVolunteerFromResultSet(itemsResultSet);
                    if (newVolunteer.getPersonalInfo().getEmail().compareTo(email) == 0) {
                        foundID = itemsResultSet.getLong("ID");
                    }
                }
                else if(whatType.compareTo(FilePathsEnum.CHARITY_FILE_PATH.getType()) == 0){

                        Charity newCharity = getCharityFromResultSet(itemsResultSet);
                        if(newCharity.getEmail().compareTo(email) == 0)
                            foundID = itemsResultSet.getLong("ID");

                   // else if(voluntaryOrganizationType.compareTo("Disaster Relief") == 0) query = "SELECT * FROM DISASTER_RELIEF";
                   // else query =  "SELECT * FROM ANIMAL_WELFARE";
                }

                else if(whatType.compareTo(FilePathsEnum.ANIMAL_WELFARE_FILE_PATH.getType()) == 0){

                    AnimalWelfare newAnimalWelfare = getAnimalWelfareFromResultSet(itemsResultSet);
                    if(newAnimalWelfare.getEmail().compareTo(email) == 0)
                        foundID = itemsResultSet.getLong("ID");
                }

                else if(whatType.compareTo(FilePathsEnum.DISASTER_RELIEF_FILE_PATH.getType()) == 0){

                    DisasterRelief newDisasterRelief = getDisasterReliefFromResultSet(itemsResultSet);
                    if(newDisasterRelief.getEmail().compareTo(email) == 0)
                        foundID = itemsResultSet.getLong("ID");
                }



            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }


        return foundID;
    }



    public static Volunteer getVolunteerFromResultSet(ResultSet volunteerResultSet){


        String volunteerName = null;
        String volunteerSurname = null;
        Integer volunteerAge = null;
        String volunteerGender = null;
        String volunteerNationality = null;
        String volunteerPhoneNumber = null;
        String volunteerEmail = null;
        Integer volunteerParticipatedOperations = null;

        try {
            volunteerName = volunteerResultSet.getString("NAME");
            volunteerSurname = volunteerResultSet.getString("SURNAME");
            volunteerAge = volunteerResultSet.getInt("AGE");

            volunteerGender = volunteerResultSet.getString("GENDER");
            volunteerNationality = volunteerResultSet.getString("NATIONALITY");
            volunteerPhoneNumber = volunteerResultSet.getString("PHONE_NUMBER");
            volunteerEmail = volunteerResultSet.getString("EMAIL");
            volunteerParticipatedOperations = volunteerResultSet.getInt("PARTICIPATED_OPERATIONS");
        } catch (SQLException e) {
            logger.error(e.getMessage()+"Error while loading Volunteer from ResultSet");
            throw new RuntimeException(e);
        }


        GenderEnum genderEnum = GenderEnum.valueOf(volunteerGender.toUpperCase());
        NationalityEnum nationalityEnum = NationalityEnum.valueOf(volunteerNationality.toUpperCase());
        if(volunteerPhoneNumber.contains(NationalityEnum.valueOf(volunteerNationality.toUpperCase()).getCallingNumber())){

            volunteerPhoneNumber = volunteerPhoneNumber.replace(NationalityEnum.valueOf(volunteerNationality.toUpperCase()).getCallingNumber(), "").trim();
        }

       // PersonalInformation personalInformation = new PersonalInformation(volunteerName,volunteerSurname,volunteerAge,genderEnum,nationalityEnum,volunteerPhoneNumber,volunteerEmail);
        PersonalInformation personalInformation = new PersonalInformation.PersonalInformationBuilder()
                .withName(volunteerName)
                .withSurname(volunteerSurname)
                .withAge(volunteerAge)
                .withGender(genderEnum)
                .withNationality(nationalityEnum)
                .withPhoneNumber(volunteerPhoneNumber)
                .withEmail(volunteerEmail)
                .build();

        return new Volunteer.VolunteerBuilder().withPersonalInfo(personalInformation).withParticipatedOperations(volunteerParticipatedOperations).build();

    }

    public static CharityAction getCharityActionFromResultSet(ResultSet resultSet){
        String actionName = null;
        String actionDescription = null;
        LocalDateTime endingDate = null;
        String donationNumber = null;
        Integer aidingPeople = null;
        BigDecimal fundsRaised = null;
        String volunteerEmail = null;
        Integer volunteerParticipatedOperations = null;

        try {
            actionName = resultSet.getString("NAME");
            actionDescription = resultSet.getString("DESCRIPTION");
            endingDate = resultSet.getTimestamp("ENDING_DATE").toLocalDateTime();

            donationNumber = resultSet.getString("DONATION_NUMBER");
            aidingPeople = resultSet.getInt("AIDING_PEOPLE");
            fundsRaised = resultSet.getBigDecimal("FUNDS_RAISED");

        } catch (SQLException e) {
            logger.error(e.getMessage()+"Error while loading CharityAction from ResultSet");
            throw new RuntimeException(e);
        }

        return new CharityAction(actionName,actionDescription,endingDate,aidingPeople,donationNumber,fundsRaised);
    }

    public static AnimalWelfareAction getAnimalWelfareActionFromResultSet(ResultSet resultSet){
        String actionName = null;
        String actionDescription = null;
        LocalDateTime endingDate = null;
        CitiesEnum city = null;

        Integer savedAnimals = null;
        AnimalTypeEnum typeOfSavedAnimals = null;


        try {
            actionName = resultSet.getString("NAME");
            actionDescription = resultSet.getString("DESCRIPTION");
            endingDate = resultSet.getTimestamp("ENDING_DATE").toLocalDateTime();
            city = CitiesEnum.valueOf(resultSet.getString("CITY").toUpperCase());
            savedAnimals = resultSet.getInt("NUMBER_OF_SAVED_ANIMALS");
            typeOfSavedAnimals = AnimalTypeEnum.valueOf(resultSet.getString("TYPE_OF_INVOLVED_ANIMAL").toUpperCase());

        } catch (SQLException e) {
            logger.error(e.getMessage()+"Error while loading AnimalWelfareAction from ResultSet");
            throw new RuntimeException(e);
        }

        return new AnimalWelfareAction(actionName,actionDescription,endingDate,savedAnimals,typeOfSavedAnimals,city);
    }

    public static DisasterReliefAction getDisasterReliefActionFromResultSet(ResultSet resultSet){
        String actionName = null;
        String actionDescription = null;
        LocalDateTime endingDate = null;
        CitiesEnum city = null;

        Integer endangeredLives = null;
        DisasterType occurredDisaster = null;
        Boolean shelterNeeded = null;


        try {
            actionName = resultSet.getString("NAME");
            actionDescription = resultSet.getString("DESCRIPTION");
            endingDate = resultSet.getTimestamp("ENDING_DATE").toLocalDateTime();
            city = CitiesEnum.valueOf(resultSet.getString("CITY").toUpperCase());
            endangeredLives = resultSet.getInt("ESTIMATED_NUMBER_OF_ENDANGERED_PEOPLE");
            occurredDisaster = DisasterType.valueOf(resultSet.getString("OCCURRED_DISASTER").toUpperCase());
            shelterNeeded = Boolean.parseBoolean(resultSet.getString("SHELTER_NEEDED"));

        } catch (SQLException e) {
            logger.error(e.getMessage()+"Error while loading DisasterReliefAction from ResultSet");
            throw new RuntimeException(e);
        }

        return new DisasterReliefAction(actionName,actionDescription,endingDate,occurredDisaster,city,endangeredLives,shelterNeeded);
    }

    public static void addNewCharity(Charity charity){
        String addCharityQuery = "INSERT INTO CHARITY " +
            "(NAME, DESCRIPTION, EMAIL, CONTACT_PHONE, STREET, HOUSE_NUMBER, CITY, FOUNDING_YEAR, FAMILIES_CURRENTLY_AIDING, FOUNDS_RAISED, VOLUNTEERS_ID) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(addCharityQuery)) {

            preparedStatement.setString(1, charity.getName());
            preparedStatement.setString(2, charity.getDescription());
            preparedStatement.setString(3, charity.getEmail());
            preparedStatement.setString(4, "+385 "+charity.getPhone());
            preparedStatement.setString(5, charity.getAddress().getStreet());
            preparedStatement.setString(6, charity.getAddress().getHouseNumber());
            preparedStatement.setString(7, charity.getAddress().getCity().getCityName());
            preparedStatement.setInt(8, charity.getFoundingYear());
            preparedStatement.setInt(9, charity.getFamiliesCurrentlyAiding());
            preparedStatement.setLong(10,0);
            preparedStatement.setString(11,"0");

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.error(e.getMessage());
            String message = "An error occurred while adding a new Charity!";
            System.err.println(e);
        }



    }

    public static void addNewAnimalWelfare(AnimalWelfare animalWelfare){
        String addAnimalWelfareQuery = "INSERT INTO ANIMAL_WELFARE " +
                "(NAME, DESCRIPTION, EMAIL, CONTACT_PHONE, STREET, HOUSE_NUMBER, CITY, FOUNDING_YEAR, TOTAL_NUMBER_OF_SAVED_ANIMALS, PROVIDES_MEDICAL_CARE,CONDUCTS_RESCUE_OPERATIONS, VOLUNTEERS_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(addAnimalWelfareQuery)) {

            preparedStatement.setString(1, animalWelfare.getName());
            preparedStatement.setString(2, animalWelfare.getDescription());
            preparedStatement.setString(3, animalWelfare.getEmail());
            preparedStatement.setString(4, "+385 "+animalWelfare.getPhone());
            preparedStatement.setString(5, animalWelfare.getAddress().getStreet());
            preparedStatement.setString(6, animalWelfare.getAddress().getHouseNumber());
            preparedStatement.setString(7, animalWelfare.getAddress().getCity().getCityName());
            preparedStatement.setInt(8, animalWelfare.getFoundingYear());
            preparedStatement.setInt(9, animalWelfare.getTotalNumberOfSavedAnimals());
            preparedStatement.setBoolean(10, animalWelfare.getProvidesMedicalCare());
            preparedStatement.setBoolean(11,animalWelfare.getConductsRescueOperations());
            preparedStatement.setString(12,"0");

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            String message = "An error occurred while adding a new AnimalWelfare!";
            logger.error(e.getMessage() + message);

        }



    }

    public static void addNewDisasterRelief(DisasterRelief disasterRelief){
        String addAnimalWelfareQuery = "INSERT INTO DISASTER_RELIEF " +
                "(NAME, DESCRIPTION, EMAIL, CONTACT_PHONE, STREET, HOUSE_NUMBER, CITY, FOUNDING_YEAR, TOTAL_NUMBER_OF_AIDED_PEOPLE, PROVIDES_FOOD_SUPPLY,VOLUNTEERS_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(addAnimalWelfareQuery)) {

            preparedStatement.setString(1, disasterRelief.getName());
            preparedStatement.setString(2, disasterRelief.getDescription());
            preparedStatement.setString(3, disasterRelief.getEmail());
            preparedStatement.setString(4, "+385 "+disasterRelief.getPhone());
            preparedStatement.setString(5, disasterRelief.getAddress().getStreet());
            preparedStatement.setString(6, disasterRelief.getAddress().getHouseNumber());
            preparedStatement.setString(7, disasterRelief.getAddress().getCity().getCityName());
            preparedStatement.setInt(8, disasterRelief.getFoundingYear());
            preparedStatement.setInt(9, disasterRelief.getTotalNumberOfAidedPeople());
            preparedStatement.setBoolean(10, disasterRelief.getProvidesFoodSupply());

            preparedStatement.setString(11,"0");

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.error(e.getMessage());
            String message = "An error occurred while adding a new DisasterRelief!";
            System.err.println(e);
        }



    }


    public static Charity getCharityFromResultSet(ResultSet organizationResultSet){


        String organizationName = null;
        String organizationDescription = null;
        String organizationEmail = null;
        String organizationNumber = null;
        String organizationStreet = null;
        String orgaizationHouseNumber = null;
        CitiesEnum organizationCity = null;
        Integer organizationFoundingYear = null;

        List<Volunteer> volunteerList  = new ArrayList<>();

        Integer familiesCurrentlyAiding = null;
        BigDecimal fundsRaised = null;
        String[] stringVolunteerIDs = null;

        try {
            organizationName = organizationResultSet.getString("NAME");
            organizationDescription = organizationResultSet.getString("DESCRIPTION");
            organizationEmail = organizationResultSet.getString("EMAIL");
            organizationNumber = organizationResultSet.getString("CONTACT_PHONE");
            organizationStreet = organizationResultSet.getString("STREET");
            orgaizationHouseNumber = organizationResultSet.getString("HOUSE_NUMBER");
            organizationCity = CitiesEnum.valueOf(organizationResultSet.getString("CITY").toUpperCase());
            organizationFoundingYear = organizationResultSet.getInt("FOUNDING_YEAR");

            familiesCurrentlyAiding = organizationResultSet.getInt("FAMILIES_CURRENTLY_AIDING");
            fundsRaised = organizationResultSet.getBigDecimal("FOUNDS_RAISED");

            stringVolunteerIDs = organizationResultSet.getString("VOLUNTEERS_ID").split(", ");


        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        //volunteerList = OrganizationMethods.getVolonteerListFromString(stringVolunteerIDs);


        for(String volunteerID:stringVolunteerIDs){
            String volunteerIDQuery = "SELECT * FROM VOLUNTEER WHERE ID = "+volunteerID;

           try(Connection connection = connectToDatabase();
               Statement sqlStatement = connection.createStatement();
               ResultSet itemsResultSet = sqlStatement.executeQuery(volunteerIDQuery)){

               while(itemsResultSet.next()){
                   Volunteer volunteer = getVolunteerFromResultSet(itemsResultSet);
                   volunteerList.add(volunteer);
               }

            } catch (SQLException e) {
               logger.error(e.getMessage());
               throw new RuntimeException(e);
           }


        }




        Address address = new Address.Builder()
                .street(organizationStreet)
                .city(organizationCity)
                .houseNumber(orgaizationHouseNumber)
                .build();


        Charity returnCharity = new Charity(organizationName,organizationDescription,organizationEmail,organizationNumber, address,organizationFoundingYear,volunteerList,familiesCurrentlyAiding,fundsRaised);
        returnCharity.displayAllVolunteers(returnCharity.getVolunteerList());

        return returnCharity;
    }

    public static AnimalWelfare getAnimalWelfareFromResultSet(ResultSet organizationResultSet){


        String organizationName = null;
        String organizationDescription = null;
        String organizationEmail = null;
        String organizationNumber = null;
        String organizationStreet = null;
        String orgaizationHouseNumber = null;
        CitiesEnum organizationCity = null;
        Integer organizationFoundingYear = null;
        Integer totalNumberOfSavedAnimals = null;
        Boolean providesMedicalCare = null;
        Boolean conductsRescueOperations = null;

        String[] stringVolunteerIDs = null;
        List<Volunteer> volunteerList = new ArrayList<>();

        try {
            organizationName = organizationResultSet.getString("NAME");
            organizationDescription = organizationResultSet.getString("DESCRIPTION");
            organizationEmail = organizationResultSet.getString("EMAIL");
            organizationNumber = organizationResultSet.getString("CONTACT_PHONE");
            organizationStreet = organizationResultSet.getString("STREET");
            orgaizationHouseNumber = organizationResultSet.getString("HOUSE_NUMBER");
            organizationCity = CitiesEnum.valueOf(organizationResultSet.getString("CITY").toUpperCase());
            organizationFoundingYear = organizationResultSet.getInt("FOUNDING_YEAR");

            providesMedicalCare = organizationResultSet.getBoolean("PROVIDES_MEDICAL_CARE");
            conductsRescueOperations = organizationResultSet.getBoolean("CONDUCTS_RESCUE_OPERATIONS");
            totalNumberOfSavedAnimals = organizationResultSet.getInt("TOTAL_NUMBER_OF_SAVED_ANIMALS");
            stringVolunteerIDs = organizationResultSet.getString("VOLUNTEERS_ID").split(", ");
            stringVolunteerIDs = Arrays.stream(stringVolunteerIDs)
                    .sorted()
                    .toArray(String[]::new);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        for(String volunteerID:stringVolunteerIDs){
            String volunteerIDQuery = "SELECT * FROM VOLUNTEER WHERE ID = "+volunteerID;

            try(Connection connection = connectToDatabase();
                Statement sqlStatement = connection.createStatement();
                ResultSet itemsResultSet = sqlStatement.executeQuery(volunteerIDQuery)){

                while(itemsResultSet.next()){
                    Volunteer volunteer = getVolunteerFromResultSet(itemsResultSet);
                    volunteerList.add(volunteer);
                }

            } catch (SQLException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }


        }



        Address address = new Address.Builder()
                .street(organizationStreet)
                .city(organizationCity)
                .houseNumber(orgaizationHouseNumber)
                .build();


        return new AnimalWelfare(organizationName,organizationDescription,organizationEmail,organizationNumber, address,organizationFoundingYear,volunteerList,totalNumberOfSavedAnimals,providesMedicalCare,conductsRescueOperations);
    }

    public static DisasterRelief getDisasterReliefFromResultSet(ResultSet organizationResultSet){


        String organizationName = null;
        String organizationDescription = null;
        String organizationEmail = null;
        String organizationNumber = null;
        String organizationStreet = null;
        String orgaizationHouseNumber = null;
        CitiesEnum organizationCity = null;
        Integer organizationFoundingYear = null;
        Integer totalNumberOfAidedPeople = null;
        Boolean providesFoodSupply = null;
        String[] stringVolunteerIDs = null;
        List<Volunteer> volunteerList = new ArrayList<>();

        try {
            organizationName = organizationResultSet.getString("NAME");
            organizationDescription = organizationResultSet.getString("DESCRIPTION");
            organizationEmail = organizationResultSet.getString("EMAIL");
            organizationNumber = organizationResultSet.getString("CONTACT_PHONE");
            organizationStreet = organizationResultSet.getString("STREET");
            orgaizationHouseNumber = organizationResultSet.getString("HOUSE_NUMBER");
            organizationCity = CitiesEnum.valueOf(organizationResultSet.getString("CITY").toUpperCase());
            organizationFoundingYear = organizationResultSet.getInt("FOUNDING_YEAR");

            totalNumberOfAidedPeople = organizationResultSet.getInt("TOTAL_NUMBER_OF_AIDED_PEOPLE");
            providesFoodSupply = organizationResultSet.getBoolean("PROVIDES_FOOD_SUPPLY");

            stringVolunteerIDs = organizationResultSet.getString("VOLUNTEERS_ID").split(", ");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for(String volunteerID:stringVolunteerIDs){
            String volunteerIDQuery = "SELECT * FROM VOLUNTEER WHERE ID = "+volunteerID;

            try(Connection connection = connectToDatabase();
                Statement sqlStatement = connection.createStatement();
                ResultSet itemsResultSet = sqlStatement.executeQuery(volunteerIDQuery)){

                while(itemsResultSet.next()){
                    Volunteer volunteer = getVolunteerFromResultSet(itemsResultSet);
                    volunteerList.add(volunteer);
                }

            } catch (SQLException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }


        }


        Address address = new Address.Builder()
                .street(organizationStreet)
                .city(organizationCity)
                .houseNumber(orgaizationHouseNumber)
                .build();


        return new DisasterRelief(organizationName,organizationDescription,organizationEmail,organizationNumber, address,organizationFoundingYear,volunteerList,providesFoodSupply,totalNumberOfAidedPeople);
    }



    public static boolean updateVolunteerListByAdding(String email, FilePathsEnum filePathsEnum, Long id){

        String volunteerIDString = null;

        String query = "SELECT VOLUNTEERS_ID FROM " +filePathsEnum.getType().toUpperCase()+" WHERE EMAIL = '"+email+"'";
        System.out.println(query);


           try(Connection connection = connectToDatabase();
               Statement sqlStatement = connection.createStatement();
               ResultSet itemsResultSet = sqlStatement.executeQuery(query)){


               if (itemsResultSet.next()) {
                   volunteerIDString = itemsResultSet.getString("VOLUNTEERS_ID").trim();
                   System.out.println("Volunteers ID string: " + volunteerIDString);

               }

               if(volunteerIDString.contains(String.valueOf(id))){
                   OtherUtils.createAndDisplayAnAlert("You already have this volunteer added!");
                   return false;
               }

               if(volunteerIDString.compareTo("0") == 0){
                   volunteerIDString = String.valueOf(id);
               }
               else volunteerIDString = volunteerIDString + ", "+ id;
               System.out.println(volunteerIDString);




               String update = "UPDATE " + filePathsEnum.getType().toUpperCase()+
                       " SET VOLUNTEERS_ID = '"+volunteerIDString+
                       "' WHERE EMAIL = '"+email+"'";
               sqlStatement.executeUpdate(update);

           } catch (SQLException e) {
               logger.error(e.getMessage());
               throw new RuntimeException(e);
           }
        return true;
    }

    public static boolean updateVolunteerListByRemoving(String email, FilePathsEnum filePathsEnum, Long id){

        String volunteerIDString = null;

        String query = "SELECT VOLUNTEERS_ID FROM " +filePathsEnum.getType().toUpperCase()+" WHERE EMAIL = '"+email+"'";
        System.out.println(query);


        try(Connection connection = connectToDatabase();
            Statement sqlStatement = connection.createStatement();
            ResultSet itemsResultSet = sqlStatement.executeQuery(query)){


            if (itemsResultSet.next()) {
                volunteerIDString = itemsResultSet.getString("VOLUNTEERS_ID").trim();
                System.out.println("Volunteers ID string: " + volunteerIDString);



            }

            if(volunteerIDString.contains(String.valueOf(id) + ", ")){

                volunteerIDString = volunteerIDString.replace(String.valueOf(id) + ", ","");
            }
           else if(volunteerIDString.contains(String.valueOf(id))){

                volunteerIDString = volunteerIDString.replace(String.valueOf(id),"");
            }

           if(volunteerIDString.equals("")) volunteerIDString = "0";

           if(volunteerIDString.endsWith(" ")){
               volunteerIDString = volunteerIDString.substring(0, volunteerIDString.length() - 1);
            }

            if (volunteerIDString.endsWith(",")) {

                volunteerIDString = volunteerIDString.substring(0, volunteerIDString.length() - 1);
            }


            System.out.println("VOLONTERI "+volunteerIDString);
            String update = "UPDATE " + filePathsEnum.getType().toUpperCase()+
                    " SET VOLUNTEERS_ID = '"+volunteerIDString+
                    "' WHERE EMAIL = '"+email+"'";



            sqlStatement.executeUpdate(update);

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return true;
    }

    public static void commitVolunteerChangesToDatabaseString(Map<String, String> elementsToBeUpdated){
        String typeOfAcc = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(1);
        String activeUserEmail = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(0);
        long id = getIDFromElement(activeUserEmail,typeOfAcc);


        try(Connection connection = connectToDatabase();
            Statement sqlStatement = connection.createStatement()){

            for(String key : elementsToBeUpdated.keySet()){
                String value = elementsToBeUpdated.get(key);
                String query = "UPDATE " + typeOfAcc.toUpperCase() +
                        " SET " + key + " = '" +value+"' WHERE ID = "+id;

                sqlStatement.executeUpdate(query);
            }


        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }



    }
    public static void commitOrganizationChangesToDatabaseString(Map<String, String> elementsToBeUpdated){
        String typeOfAcc = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(1);
        String activeUserEmail = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(0);
        long id = getIDFromElement(activeUserEmail,typeOfAcc);


        try(Connection connection = connectToDatabase();
            Statement sqlStatement = connection.createStatement()){

            for(String key : elementsToBeUpdated.keySet()){
                String value = elementsToBeUpdated.get(key);
                String query = "UPDATE " + typeOfAcc.toUpperCase() +
                                " SET " + key + " = '" +value+"' WHERE ID = "+id;

                sqlStatement.executeUpdate(query);
            }


        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }



    }

    public static void commitOrganizationChangesToDatabaseInteger(Map<String, Integer> elementsToBeUpdated){
        String typeOfAcc = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(1);
        String activeUserEmail = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(0);
        long id = getIDFromElement(activeUserEmail,typeOfAcc);


        try(Connection connection = connectToDatabase();
            Statement sqlStatement = connection.createStatement()){

            for(String key : elementsToBeUpdated.keySet()){
                Integer value = elementsToBeUpdated.get(key);
                String query = "UPDATE " + typeOfAcc.toUpperCase() +
                        " SET " + key + " = " +value+" WHERE ID = "+id;

                sqlStatement.executeUpdate(query);
            }


        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }




    }
    public static void commitOrganizationChangesToDatabaseBoolean(Map<String, Boolean> elementsToBeUpdated){
        String typeOfAcc = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(1);
        String activeUserEmail = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(0);
        long id = getIDFromElement(activeUserEmail,typeOfAcc);


        try(Connection connection = connectToDatabase();
            Statement sqlStatement = connection.createStatement()){

            for(String key : elementsToBeUpdated.keySet()){
                Boolean value = elementsToBeUpdated.get(key);
                String query = "UPDATE " + typeOfAcc.toUpperCase() +
                        " SET " + key + " = " +value.toString().toUpperCase()+" WHERE ID = "+id;

                sqlStatement.executeUpdate(query);
            }


        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }




    }

    public static void commitOrganizationChangesToDatabaseBigDecimal(Map<String, BigDecimal> elementsToBeUpdated){
        String typeOfAcc = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(1);
        String activeUserEmail = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(0);
        long id = getIDFromElement(activeUserEmail,typeOfAcc);


        try(Connection connection = connectToDatabase();
            Statement sqlStatement = connection.createStatement()){

            for(String key : elementsToBeUpdated.keySet()){
                BigDecimal value = elementsToBeUpdated.get(key);
                String query = "UPDATE " + typeOfAcc.toUpperCase() +
                        " SET " + key + " = " +value.toString().toUpperCase()+" WHERE ID = "+id;

                sqlStatement.executeUpdate(query);
            }


        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }




    }

    public static void deleteAccountDatabase(Long id, String typeOfAcc){

        try(Connection connection = connectToDatabase();
            Statement sqlStatement = connection.createStatement()){


                String query = "DELETE FROM "+typeOfAcc.toUpperCase()+" WHERE ID = "+id;
                sqlStatement.executeUpdate(query);



        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public static void addNewCharityAction(CharityAction charityAction,Long charityId){

        String sql = "INSERT INTO CHARITY_ACTION (NAME, DESCRIPTION, ENDING_DATE, DONATION_NUMBER, AIDING_PEOPLE, FUNDS_RAISED,CHARITY_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?,?)";

        try (Connection connection = DatabaseUtils.connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, charityAction.getName());
            statement.setString(2, charityAction.getDescription());
            statement.setTimestamp(3, Timestamp.valueOf(charityAction.getEndingDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            statement.setString(4, charityAction.getAutomaticDonationNumber());
            statement.setInt(5, charityAction.getAidedPeople());
            statement.setBigDecimal(6, charityAction.getFundsRaised());
            statement.setLong(7, charityId);

            statement.executeUpdate();

            System.out.println("Charity action inserted successfully.");
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }




    }


    public static void addNewAnimalWelfareAction(AnimalWelfareAction action, Long charityId){

        String sql = "INSERT INTO ANIMAL_WELFARE_ACTION (NAME, DESCRIPTION, ENDING_DATE, CITY, NUMBER_OF_SAVED_ANIMALS, TYPE_OF_INVOLVED_ANIMAL,ANIMAL_WELFARE_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?,?)";

        try (Connection connection = DatabaseUtils.connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, action.getName());
            statement.setString(2, action.getDescription());
            statement.setTimestamp(3, Timestamp.valueOf(action.getEndingDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            statement.setString(4, action.getCityWhereActionIsHeld().getCityName());
            statement.setInt(5, action.getNumberOfSavedAnimals());
            statement.setString(6, action.getListOfInvolvedAnimal().getAnimalType());
            statement.setLong(7, charityId);

            statement.executeUpdate();

            System.out.println("Animal welfare action inserted successfully.");
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }




    }

    public static void addNewDisasterReliefAction(DisasterReliefAction action, Long charityId){

        String sql = "INSERT INTO DISASTER_RELIEF_ACTION (NAME, DESCRIPTION, ENDING_DATE, CITY, OCCURRED_DISASTER, ESTIMATED_NUMBER_OF_ENDANGERED_PEOPLE,SHELTER_NEEDED,DISASTER_RELIEF_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?,?,?)";

        try (Connection connection = DatabaseUtils.connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, action.getName());
            statement.setString(2, action.getDescription());
            statement.setTimestamp(3, Timestamp.valueOf(action.getEndingDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            statement.setString(4, action.getCityAffected().getCityName());
            statement.setString(5,action.getOccuredDisaster().getDisasterType());
            statement.setInt(6, action.getEstimatedNumberOfEndangeredPeople());
            statement.setBoolean(7, action.isShelterSupportNeeded());
            statement.setLong(8, charityId);


            statement.executeUpdate();

            System.out.println("Animal welfare action inserted successfully.");
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }




    }
    public static List<CharityAction> getAllCharityActionsFromDatabase(){
        List<CharityAction> actionList = new ArrayList<>();

        try (Connection connection = connectToDatabase();
             Statement sqlStatement = connection.createStatement();
             ResultSet itemsResultSet = sqlStatement.executeQuery("SELECT * FROM CHARITY_ACTION")) {

            while (itemsResultSet.next()) {
                CharityAction action = getCharityActionFromResultSet(itemsResultSet);
                actionList.add(action);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return actionList;
    }

    public static List<AnimalWelfareAction> getAllAnimalWelfareActionsFromDatabase(){
        List<AnimalWelfareAction> actionList = new ArrayList<>();

        try (Connection connection = connectToDatabase();
             Statement sqlStatement = connection.createStatement();
             ResultSet itemsResultSet = sqlStatement.executeQuery("SELECT * FROM ANIMAL_WELFARE_ACTION")) {

            while (itemsResultSet.next()) {
                AnimalWelfareAction action = getAnimalWelfareActionFromResultSet(itemsResultSet);
                actionList.add(action);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return actionList;
    }
    public static List<DisasterReliefAction> getAllDisasterReliefActionsFromDatabase(){
        List<DisasterReliefAction> actionList = new ArrayList<>();
        try (Connection connection = connectToDatabase();
             Statement sqlStatement = connection.createStatement();
             ResultSet itemsResultSet = sqlStatement.executeQuery("SELECT * FROM DISASTER_RELIEF_ACTION")) {

            while (itemsResultSet.next()) {
                DisasterReliefAction action = getDisasterReliefActionFromResultSet(itemsResultSet);
                actionList.add(action);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return actionList;
    }

    public static void deleteActionFromDatabase(String name,String whatType){


        try(Connection connection = connectToDatabase();
            Statement sqlStatement = connection.createStatement()){


            String query = "DELETE FROM "+whatType.toUpperCase()+" WHERE NAME = '"+name+"'";
            sqlStatement.executeUpdate(query);



        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public static CharityAction getCharityActionByName(String name) {


        List<CharityAction> actionList = getAllCharityActionsFromDatabase();

        Optional<CharityAction> optionalAction= actionList.stream()
                    .filter(action -> action.getName().compareTo(name) == 0)
                    .findFirst();

        return optionalAction.get();
    }


    public static void incrementVolunteerOperation(Volunteer volunteer){
        try(Connection connection = connectToDatabase();
            Statement sqlStatement = connection.createStatement()){


            String query = "UPDATE VOLUNTEER SET PARTICIPATED_OPERATIONS = PARTICIPATED_OPERATIONS + 1 WHERE EMAIL = '"+volunteer.getPersonalInfo().getEmail()+"'";
            sqlStatement.executeUpdate(query);

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static List<VoluntaryAction> getActionsByOrganizationID(VoluntaryAction action, Long currentOrganizaionId) {
        List<VoluntaryAction> list = new ArrayList<>();
        ResultSet resultSet = null;
        String query = null;
        try (Connection connection = connectToDatabase();
             Statement sqlstatement = connection.createStatement()) {

            if (action instanceof CharityAction) {
                query = "SELECT * FROM CHARITY_ACTION WHERE CHARITY_ID = " + currentOrganizaionId;
                resultSet = sqlstatement.executeQuery(query);
                while (resultSet.next()) {
                    CharityAction charityAction = getCharityActionFromResultSet(resultSet);
                    list.add(charityAction);
                }
            } else if (action instanceof AnimalWelfareAction) {
                query = "SELECT * FROM ANIMAL_WELFARE_ACTION WHERE ANIMAL_WELFARE_ID = " + currentOrganizaionId;
                resultSet = sqlstatement.executeQuery(query);
                while (resultSet.next()) {
                    AnimalWelfareAction animalWelfareAction = getAnimalWelfareActionFromResultSet(resultSet);
                    list.add(animalWelfareAction);
                }
            } else if (action instanceof DisasterReliefAction) {
                query = "SELECT * FROM DISASTER_RELIEF_ACTION WHERE DISASTER_RELIEF_ID = " + currentOrganizaionId;
                resultSet = sqlstatement.executeQuery(query);
                while (resultSet.next()) {
                    DisasterReliefAction disasterReliefAction = getDisasterReliefActionFromResultSet(resultSet);
                    list.add(disasterReliefAction);
                }
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return list;
    }

}
