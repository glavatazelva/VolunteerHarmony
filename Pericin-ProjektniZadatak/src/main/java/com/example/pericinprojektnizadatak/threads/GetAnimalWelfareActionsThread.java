package com.example.pericinprojektnizadatak.threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetAnimalWelfareActionsThread extends DatabaseThread implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(GetAnimalWelfareActionsThread.class);
    @Override
    public void run() {
        super.getAllAnimalWelfareActionsFromDatabaseThread();
    }
}
