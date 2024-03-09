package com.example.pericinprojektnizadatak.threads;

import com.example.pericinprojektnizadatak.enumerations.FilePathsEnum;
import com.example.pericinprojektnizadatak.model.Volunteer;
import com.example.pericinprojektnizadatak.model.interfaces.ActionInterface;
import com.example.pericinprojektnizadatak.model.organization.AnimalWelfare;
import com.example.pericinprojektnizadatak.model.organization.Charity;
import com.example.pericinprojektnizadatak.model.voluntaryAction.AnimalWelfareAction;
import com.example.pericinprojektnizadatak.model.voluntaryAction.CharityAction;
import com.example.pericinprojektnizadatak.utils.DatabaseUtils;
import com.example.pericinprojektnizadatak.utils.FileUtils;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AnimalWelfareActionsThread {
    private static final Logger logger = LoggerFactory.getLogger(CharityActionsThread.class);
    private ScheduledExecutorService scheduler;
    public AnimalWelfareActionsThread() {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.running = true;
    }

    private volatile boolean running;

    public void startThread(Stage primaryStage) {
        scheduler.scheduleAtFixedRate(this::deleteExpiredAnimalWelfareActions, 0, 30, TimeUnit.SECONDS);

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

    private void deleteExpiredAnimalWelfareActions() {
        List<AnimalWelfareAction> voluntaryActions = DatabaseUtils.getAllAnimalWelfareActionsFromDatabase();
        LocalDateTime now = LocalDateTime.now();

        for (AnimalWelfareAction action : voluntaryActions) {
            if (action.getEndingDate().isBefore(now)) {

                //updatenja sto se tice charity
                AnimalWelfare activeUser = DatabaseUtils.getAnimalWelfareFromEmail(FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(0));

                activeUser.addToTotal(action.getNumberOfSavedAnimals());

                Map<String,Integer> valuesToBeChanged = new HashMap<>();
                valuesToBeChanged.put("TOTAL_NUMBER_OF_SAVED_ANIMALS",activeUser.getTotalNumberOfSavedAnimals());
                DatabaseUtils.commitOrganizationChangesToDatabaseInteger(valuesToBeChanged);


                //za volontere
                List<Volunteer> volunteers = activeUser.getVolunteerList();
                volunteers.forEach(volunteer -> DatabaseUtils.incrementVolunteerOperation(volunteer));


                DatabaseUtils.deleteActionFromDatabase(action.getName(), ActionInterface.animalWelfareAction);
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
                logger.error("Error waiting for thread ending", e.getMessage());
            }
        }
        logger.info("Thread done executing");
    }
}
