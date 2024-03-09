package com.example.pericinprojektnizadatak.threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

public class GetDisasterReliefsThread extends DatabaseThread implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(GetDisasterReliefsThread.class);

    @Override
    public void run() {
        super.getAllDisasterReliefsFromDatabaseThread();
    }
}
