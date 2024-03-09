package com.example.pericinprojektnizadatak.model.interfaces;

import com.example.pericinprojektnizadatak.exceptions.ActionWithSameNameException;
import com.example.pericinprojektnizadatak.model.voluntaryAction.AnimalWelfareAction;
import com.example.pericinprojektnizadatak.model.voluntaryAction.CharityAction;
import com.example.pericinprojektnizadatak.model.voluntaryAction.DisasterReliefAction;
import com.example.pericinprojektnizadatak.model.voluntaryAction.VoluntaryAction;
import com.example.pericinprojektnizadatak.utils.DatabaseUtils;

import java.util.List;

public sealed interface ActionInterface permits CharityAction, DisasterReliefAction, AnimalWelfareAction {

    public static final String charityAction = "charity_action";
    public static final String animalWelfareAction = "animal_welfare_action";
    public static final String disasterReliefAction = "disaster_relief_action";

    public static <T extends VoluntaryAction> void findIfActionWithSameNameExists(String name, List<T> actionList) throws ActionWithSameNameException {
        for (VoluntaryAction action : actionList) {
            if (action.getName().equals(name)) {
                throw new ActionWithSameNameException("Action with the same name exists");
            }
        }
    }
}
