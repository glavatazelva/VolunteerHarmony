package com.example.pericinprojektnizadatak.model.interfaces;


import com.example.pericinprojektnizadatak.enumerations.FilePathsEnum;
import com.example.pericinprojektnizadatak.model.Volunteer;
import com.example.pericinprojektnizadatak.model.generics.SuccessCalculator;
import com.example.pericinprojektnizadatak.model.organization.Charity;
import com.example.pericinprojektnizadatak.utils.DatabaseUtils;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;


public interface OrganizationMethods {
    void addToTotal(Integer increment);
    default void displayAllVolunteers(List<Volunteer> volunteerList){

        System.out.println("This organization's volunteers are:");
        for(int i = 0;i<volunteerList.size();i++){
            System.out.println(i+1+". "+volunteerList.get(i).toString());
        }

    }




}