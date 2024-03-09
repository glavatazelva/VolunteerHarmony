package com.example.pericinprojektnizadatak.threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetDisasterReliefActionsThread extends DatabaseThread implements Runnable{

        private static final Logger logger = LoggerFactory.getLogger(GetDisasterReliefActionsThread.class);
        @Override
        public void run() {
            super.getAllDisasterReliefActionsFromDatabaseThread();
        }

}
