package com.example.pericinprojektnizadatak.threads;

import com.example.pericinprojektnizadatak.enumerations.FilePathsEnum;
import com.example.pericinprojektnizadatak.model.Volunteer;
import com.example.pericinprojektnizadatak.model.interfaces.ActionInterface;
import com.example.pericinprojektnizadatak.model.organization.Charity;
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

public class CharityActionsThread {

    private static final Logger logger = LoggerFactory.getLogger(CharityActionsThread.class);
    private ScheduledExecutorService scheduler;
    private volatile boolean running;


    public CharityActionsThread() {

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
        List<CharityAction> voluntaryActions = DatabaseUtils.getAllCharityActionsFromDatabase();
        LocalDateTime now = LocalDateTime.now();

        for (CharityAction action : voluntaryActions) {
            if (action.getEndingDate().isBefore(now)) {

                //updatenja sto se tice charity
                Charity activeUser = DatabaseUtils.getCharityFromEmail(FileUtils.readEveryLineFromFile(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath()).get(0));


                System.out.println(activeUser.getTotalFundsRaised());
                BigDecimal bigDecimalToBeUpdated = activeUser.getTotalFundsRaised().add(action.getFundsRaised());
                System.out.println(bigDecimalToBeUpdated);
                activeUser.addToTotal(action.getAidedPeople());

                Map<String,Integer> valuesToBeChanged = new HashMap<>();
                valuesToBeChanged.put("FAMILIES_CURRENTLY_AIDING", activeUser.getFamiliesCurrentlyAiding());
                DatabaseUtils.commitOrganizationChangesToDatabaseInteger(valuesToBeChanged);

                Map<String, BigDecimal> valuesToBeChangedDecimal = new HashMap<>();
                valuesToBeChangedDecimal.put("FOUNDS_RAISED",bigDecimalToBeUpdated);
                DatabaseUtils.commitOrganizationChangesToDatabaseBigDecimal(valuesToBeChangedDecimal);


                //za volontere
                List<Volunteer> volunteers = activeUser.getVolunteerList();
                volunteers.forEach(volunteer -> DatabaseUtils.incrementVolunteerOperation(volunteer));


                DatabaseUtils.deleteActionFromDatabase(action.getName(), ActionInterface.charityAction);
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
