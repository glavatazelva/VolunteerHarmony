package com.example.pericinprojektnizadatak;

import com.example.pericinprojektnizadatak.enumerations.FilePathsEnum;
import com.example.pericinprojektnizadatak.exceptions.ProblemLoadingUserDetailsException;
import com.example.pericinprojektnizadatak.model.Volunteer;
import com.example.pericinprojektnizadatak.model.generics.SuccessCalculator;
import com.example.pericinprojektnizadatak.model.generics.UserLoader;
import com.example.pericinprojektnizadatak.model.records.EfficiencyRecord;
import com.example.pericinprojektnizadatak.threads.GetVolunteersThread;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class VolunteerHomePageController {

    private static final Logger logger = LoggerFactory.getLogger(VolunteerHomePageController.class);
    @FXML
    private Label username;
    @FXML
    private ImageView userPicture;
    @FXML
    private VBox notifications;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Label operations;
    @FXML
    private Label rank;
    @FXML
    private Label efficiency;
    @FXML
    private ImageView emoji;

    public void logout(){
        OtherUtils.loadLoginScreen();

    }

    public void initialize() throws SQLException, IOException {

        userPicture.setImage(new Image("https://upload.wikimedia.org/wikipedia/commons/thumb/1/12/User_icon_2.svg/1024px-User_icon_2.svg.png"));

        String activeUserEmail = FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(0);
        Volunteer activeUser = DatabaseUtils.getVolunteerFromEmail(activeUserEmail);


        try {
            UserLoader<Volunteer> userLoader = new UserLoader<>();
            userLoader.checkIfUserWasLoadedCorrectly(activeUser);
        }
        catch (ProblemLoadingUserDetailsException e){
            logger.error(e + e.getMessage());
            OtherUtils.loadLoginScreen();
        }

        assert activeUser != null;
        username.setText(activeUser.getPersonalInfo().getName()+" "+activeUser.getPersonalInfo().getSurname());

        if(username.getText().equals("Jin Woo Sung")) {
            username.setText("Shadow Monarch");
            username.setTextFill(Color.rgb(153, 51, 153));
        }


        addNotifications();
        scrollPane.setContent(notifications);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-border-color: #251fe7; -fx-border-width: 1px;");


        operations.setText(activeUser.getParticipatedOperations().toString());

        GetVolunteersThread getVolunteersThread = new GetVolunteersThread();
        Thread volunteersThread = new Thread(getVolunteersThread);
        volunteersThread.start();
        try{
            volunteersThread.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }

        rank.setText(new SuccessCalculator<Volunteer>(activeUser).calculateRank(getVolunteersThread.getAllVolunteersFromDatabaseThread()).toString());
        Double efficiencyValue = new SuccessCalculator<Volunteer>(activeUser).calculateEfficiency();
        efficiency.setText(String.format("%.2f",efficiencyValue));

        if(Double.parseDouble(efficiency.getText()) < 10){
            emoji.setImage(new Image(EfficiencyRecord.badEfficiency));
        }
        else if(Double.parseDouble(efficiency.getText()) < 20 && Double.parseDouble(efficiency.getText()) > 10){
            emoji.setImage(new Image(EfficiencyRecord.averageEfficiency));
        }
        else emoji.setImage((new Image(EfficiencyRecord.goodEfficiency)));
    }


    public void openViewChangesTab(){
        FXMLLoader fxmlLoader = new FXMLLoader(VolunteerHomePageController.class.getResource("volunteerTabs/volunteerSerializationTab.fxml"));

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

    public void openEditAccountTab(){
        FXMLLoader fxmlLoader = new FXMLLoader(VolunteerHomePageController.class.getResource("volunteerTabs/volunteerEditAccount.fxml"));

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


    public void refresh(){
        try {
            initialize();
        }
        catch (SQLException | IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public void addNotifications() throws SQLException, IOException {
        notifications.getChildren().clear();

        Long id = DatabaseUtils.getIDFromElement(FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(0),FilePathsEnum.VOLUNTEER_FILE_PATH.getType());
        System.out.println(id);
        String name = null;
        String description = null;
        LocalDateTime localDateTime = null;
        String organizationName = null;

        List<String> queries = new ArrayList<>();
        queries.add("SELECT ca.NAME AS action_name, ca.DESCRIPTION, ca.ENDING_DATE, c.NAME AS name" +
                " FROM CHARITY_ACTION ca" +
                " JOIN CHARITY c ON ca.CHARITY_ID = c.ID" +
                " WHERE c.VOLUNTEERS_ID LIKE '%param%';");

        queries.add("SELECT awa.NAME AS action_name, awa.DESCRIPTION, awa.ENDING_DATE, aw.NAME AS name" +
                " FROM ANIMAL_WELFARE_ACTION awa" +
                " JOIN ANIMAL_WELFARE aw ON awa.ANIMAL_WELFARE_ID = aw.ID" +
                " WHERE aw.VOLUNTEERS_ID LIKE '%param%';");

        queries.add("SELECT dra.NAME AS action_name, dra.DESCRIPTION, dra.ENDING_DATE, dr.NAME AS name" +
                " FROM DISASTER_RELIEF_ACTION dra" +
                " JOIN DISASTER_RELIEF dr ON dra.DISASTER_RELIEF_ID = dr.ID" +
                " WHERE dr.VOLUNTEERS_ID LIKE '%param%';");

        try (Connection connection = DatabaseUtils.connectToDatabase();
             Statement sqlstatement = connection.createStatement()) {

         for(String query : queries) {
            query = query.replace("param",id.toString());
             ResultSet resultSet = sqlstatement.executeQuery(query);

             while (resultSet.next()) {
                  name = resultSet.getString("ACTION_NAME");
                  description = resultSet.getString("DESCRIPTION");
                  localDateTime = resultSet.getTimestamp("ENDING_DATE").toLocalDateTime();
                  organizationName = resultSet.getString("NAME");

                 Label hosted = new Label(organizationName + " is inviting you to");
                 Label actionName = new Label(name);
                 Label actionDesc = new Label(description);
                 hosted.setStyle("-fx-font-weight: bold");
                 hosted.setTextFill(Color.rgb(25,21,231));
                 actionDesc.setStyle("-fx-font-style: italic;");
                 actionName.setStyle("-fx-font-weight: bold");

                 Label actionEndingDate = new Label("Ends on "+localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))+"\n");
                 notifications.getChildren().addAll(hosted,actionName,actionDesc,actionEndingDate);

                 Region spacer = new Region();
                 spacer.setPrefSize(Region.USE_PREF_SIZE, 10);
                 notifications.getChildren().add(spacer);

             }
         }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }






}
