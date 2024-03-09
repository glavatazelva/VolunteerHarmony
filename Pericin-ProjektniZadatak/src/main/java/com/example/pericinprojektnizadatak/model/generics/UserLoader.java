package com.example.pericinprojektnizadatak.model.generics;

import com.example.pericinprojektnizadatak.enumerations.FilePathsEnum;
import com.example.pericinprojektnizadatak.exceptions.ProblemLoadingUserDetailsException;
import com.example.pericinprojektnizadatak.model.organization.Organization;
import com.example.pericinprojektnizadatak.utils.DatabaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserLoader<T>{
    public static final Logger logger = LoggerFactory.getLogger(UserLoader.class);
    public void checkIfUserWasLoadedCorrectly(T user) throws ProblemLoadingUserDetailsException {
        if (user != null) {
            System.out.println("User loaded successfully:\n" + user);
        } else {
            throw new ProblemLoadingUserDetailsException("Problem loading "+user.getClass().getSimpleName()+" account");
        }
    }
/*
    public static <T extends Organization> List<T> getAllOrganizationsFromDatabase() {
        List<T> organizations = new ArrayList<>();
        List<String> tableNames = new ArrayList<>();
        tableNames.add(FilePathsEnum.CHARITY_FILE_PATH.getType());
        tableNames.add(FilePathsEnum.ANIMAL_WELFARE_FILE_PATH.getType());
        tableNames.add(FilePathsEnum.DISASTER_RELIEF_FILE_PATH.getType());

        try (Connection connection = DatabaseUtils.connectToDatabase();
             Statement sqlStatement = connection.createStatement()) {

            for (String tableName : tableNames) {
                try (ResultSet itemsResultSet = sqlStatement.executeQuery("SELECT * FROM " + tableName)) {
                    while (itemsResultSet.next()) {
                        T newOrganization = DatabaseUtils.getOrganizationFromResultSet(itemsResultSet);
                        organizations.add(newOrganization);
                    }
                }
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace(); // Handle or log the exception as needed
        }

        return organizations;
    }

 */

}
