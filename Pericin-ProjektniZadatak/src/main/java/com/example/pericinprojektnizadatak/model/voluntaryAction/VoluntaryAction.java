package com.example.pericinprojektnizadatak.model.voluntaryAction;

import com.example.pericinprojektnizadatak.enumerations.CitiesEnum;
import com.example.pericinprojektnizadatak.model.Volunteer;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public abstract class VoluntaryAction {

private String name;
private String description;
private LocalDateTime endingDate;


    public VoluntaryAction(String name, String description,LocalDateTime endingDate) {
        this.name = name;
        this.description = description;
        this.endingDate = endingDate;

    }

    public VoluntaryAction(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public LocalDateTime getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(LocalDateTime endingDate) {
        this.endingDate = endingDate;
    }

}
