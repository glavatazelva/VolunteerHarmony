package com.example.pericinprojektnizadatak.threads;
import com.example.pericinprojektnizadatak.model.Volunteer;
import com.example.pericinprojektnizadatak.model.organization.AnimalWelfare;
import com.example.pericinprojektnizadatak.model.organization.Charity;
import com.example.pericinprojektnizadatak.model.organization.DisasterRelief;
import com.example.pericinprojektnizadatak.model.voluntaryAction.AnimalWelfareAction;
import com.example.pericinprojektnizadatak.model.voluntaryAction.CharityAction;
import com.example.pericinprojektnizadatak.model.voluntaryAction.DisasterReliefAction;
import com.example.pericinprojektnizadatak.utils.DatabaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class DatabaseThread {
    public boolean canAccess = false;
    private static final Logger logger = LoggerFactory.getLogger(DatabaseThread.class);
    public synchronized List<Charity> getAllCharitiesFromDatabaseThread() throws SQLException, IOException {
        List<Charity> organizationList;
        while(canAccess) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }

        canAccess = true;
        organizationList = DatabaseUtils.getAllCharitiesFromDatabase();
        canAccess = false;

        notifyAll();
        return organizationList;
    }

    public synchronized List<Volunteer> getAllVolunteersFromDatabaseThread(){
        List<Volunteer> volunteerList;
        while(canAccess) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }

        canAccess = true;
        volunteerList = DatabaseUtils.getAllVolunteersFromDatabase();
        canAccess = false;

        notifyAll();
        return volunteerList;
    }

    public synchronized List<AnimalWelfare> getAllAnimalWelfaresFromDatabaseThread(){
        List<AnimalWelfare> organizationList;
        while(canAccess) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }

        canAccess = true;
        organizationList = DatabaseUtils.getAllAnimalWelfaresFromDatabase();
        canAccess = false;

        notifyAll();
        return organizationList;
    }

    public synchronized List<DisasterRelief> getAllDisasterReliefsFromDatabaseThread(){
        List<DisasterRelief> organizationList;
        while(canAccess) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }

        canAccess = true;
        organizationList = DatabaseUtils.getAllDisasterReliefsFromDatabase();
        canAccess = false;

        notifyAll();
        return organizationList;
    }

    public synchronized List<CharityAction> getAllCharityActionsFromDatabaseThread() {
        List<CharityAction> organizationList;
        while(canAccess) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }

        canAccess = true;
        organizationList = DatabaseUtils.getAllCharityActionsFromDatabase();
        canAccess = false;

        notifyAll();
        return organizationList;
    }

    public synchronized List<AnimalWelfareAction> getAllAnimalWelfareActionsFromDatabaseThread() {
        List<AnimalWelfareAction> organizationList;
        while(canAccess) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }

        canAccess = true;
        organizationList = DatabaseUtils.getAllAnimalWelfareActionsFromDatabase();
        canAccess = false;

        notifyAll();
        return organizationList;
    }

    public synchronized List<DisasterReliefAction> getAllDisasterReliefActionsFromDatabaseThread() {
        List<DisasterReliefAction> organizationList;
        while(canAccess) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }

        canAccess = true;
        organizationList = DatabaseUtils.getAllDisasterReliefActionsFromDatabase();
        canAccess = false;

        notifyAll();
        return organizationList;
    }

}