package com.example.pericinprojektnizadatak.threads;

import com.example.pericinprojektnizadatak.enumerations.FilePathsEnum;
import com.example.pericinprojektnizadatak.model.Volunteer;
import com.example.pericinprojektnizadatak.model.interfaces.ActionInterface;
import com.example.pericinprojektnizadatak.model.organization.DisasterRelief;
import com.example.pericinprojektnizadatak.model.voluntaryAction.DisasterReliefAction;
import com.example.pericinprojektnizadatak.utils.DatabaseUtils;
import com.example.pericinprojektnizadatak.utils.FileUtils;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DisasterReliefActionsThread {

    private static final Logger logger = LoggerFactory.getLogger(DisasterReliefActionsThread.class);
    private ScheduledExecutorService scheduler;
    private volatile boolean running;


    public DisasterReliefActionsThread() {

        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.running = true;

    }

    public void startThread(Stage primaryStage) {
        scheduler.scheduleAtFixedRate(this::deleteExpiredVoluntaryActions, 0, 30, TimeUnit.SECONDS);

        primaryStage.setOnCloseRequest(event -> stopThread());
        /*
        primaryStage.setOnCloseRequest(event -> {
            if (scheduler != null) {
                scheduler.shutdown();
                logger.info("Thread done executing");
            }
        });
        */

    }

    private void deleteExpiredVoluntaryActions() {
        List<DisasterReliefAction> voluntaryActions = DatabaseUtils.getAllDisasterReliefActionsFromDatabase();
        LocalDateTime now = LocalDateTime.now();

        for (DisasterReliefAction action : voluntaryActions) {
            if (action.getEndingDate().isBefore(now)) {

                //updatenja sto se tice charity
                DisasterRelief activeUser = DatabaseUtils.getDisasterReliefFromEmail(FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(0));

                activeUser.addToTotal(action.getEstimatedNumberOfEndangeredPeople());;
                Map<String,Integer> valuesToBeChanged = new HashMap<>();
                valuesToBeChanged.put("TOTAL_NUMBER_OF_AIDED_PEOPLE", activeUser.getTotalNumberOfAidedPeople());
                DatabaseUtils.commitOrganizationChangesToDatabaseInteger(valuesToBeChanged);

                //za volontere
                List<Volunteer> volunteers = activeUser.getVolunteerList();
                volunteers.forEach(volunteer -> DatabaseUtils.incrementVolunteerOperation(volunteer));


                DatabaseUtils.deleteActionFromDatabase(action.getName(), ActionInterface.disasterReliefAction);
            }
        }
    }

    public void stopThread() {
        running = false;
        if (scheduler != null) {
            scheduler.shutdown();
            try {
                scheduler.awaitTermination(1, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                logger.error("Error waiting for thread termination: {}", e.getMessage());
            }
        }
        logger.info("Thread done executing");
    }

}
