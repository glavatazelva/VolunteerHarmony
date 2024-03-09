package com.example.pericinprojektnizadatak.threads;

import com.example.pericinprojektnizadatak.model.organization.Charity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class GetCharitiesThread extends DatabaseThread implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(GetCharitiesThread.class);

    public GetCharitiesThread() {
    }


    @Override
    public void run() {
        try {
           super.getAllCharitiesFromDatabaseThread();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
