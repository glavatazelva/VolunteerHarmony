package com.example.pericinprojektnizadatak.model.voluntaryAction;

import com.example.pericinprojektnizadatak.enumerations.AnimalTypeEnum;
import com.example.pericinprojektnizadatak.enumerations.CitiesEnum;
import com.example.pericinprojektnizadatak.model.interfaces.ActionInterface;

import java.time.LocalDateTime;
import java.util.List;

public final class AnimalWelfareAction extends VoluntaryAction implements ActionInterface {

    private Integer numberOfSavedAnimals;
    private AnimalTypeEnum listOfInvolvedAnimal;
    private  CitiesEnum cityWhereActionIsHeld;

    public AnimalWelfareAction(){}
    public AnimalWelfareAction(String name, String description, LocalDateTime endingDate, Integer numberOfSavedAnimals, AnimalTypeEnum listOfInvolvedAnimals, CitiesEnum cityWhereActionIsHeld) {
        super(name, description, endingDate);
        this.numberOfSavedAnimals = numberOfSavedAnimals;
        this.listOfInvolvedAnimal = listOfInvolvedAnimals;
        this.cityWhereActionIsHeld = cityWhereActionIsHeld;
    }

    public Integer getNumberOfSavedAnimals() {
        return numberOfSavedAnimals;
    }

    public void setNumberOfSavedAnimals(Integer numberOfSavedAnimals) {
        this.numberOfSavedAnimals = numberOfSavedAnimals;
    }

    public AnimalTypeEnum getListOfInvolvedAnimal() {
        return listOfInvolvedAnimal;
    }

    public void setListOfInvolvedAnimal(AnimalTypeEnum listOfInvolvedAnimal) {
        this.listOfInvolvedAnimal = listOfInvolvedAnimal;
    }

    public CitiesEnum getCityWhereActionIsHeld() {
        return cityWhereActionIsHeld;
    }

    public void setCityWhereActionIsHeld(CitiesEnum cityWhereActionIsHeld) {
        this.cityWhereActionIsHeld = cityWhereActionIsHeld;
    }
}
