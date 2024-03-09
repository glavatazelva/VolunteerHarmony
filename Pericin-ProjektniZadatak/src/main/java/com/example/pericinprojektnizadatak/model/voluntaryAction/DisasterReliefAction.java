package com.example.pericinprojektnizadatak.model.voluntaryAction;

import com.example.pericinprojektnizadatak.enumerations.CitiesEnum;
import com.example.pericinprojektnizadatak.enumerations.DisasterType;
import com.example.pericinprojektnizadatak.model.Volunteer;
import com.example.pericinprojektnizadatak.model.interfaces.ActionInterface;
import com.example.pericinprojektnizadatak.model.organization.DisasterRelief;

import java.time.LocalDateTime;
import java.util.List;

public final class DisasterReliefAction extends VoluntaryAction implements ActionInterface {

    private DisasterType occurredDisaster;
    private CitiesEnum cityAffected;
    private Integer estimatedNumberOfEndangeredPeople;
    private Boolean shelterSupportNeeded;

    public DisasterReliefAction(){}
    public DisasterReliefAction(String name, String description, LocalDateTime endingDate, DisasterType occuredDisaster, CitiesEnum cityAffected, Integer estimatedNumberOfEndangeredPeople, Boolean shelterSupportNeeded) {
        super(name, description,endingDate);
        this.occurredDisaster = occuredDisaster;
        this.cityAffected = cityAffected;
        this.estimatedNumberOfEndangeredPeople = estimatedNumberOfEndangeredPeople;
        this.shelterSupportNeeded = shelterSupportNeeded;
      //  this.foodSupplyNeeded = foodSupplyNeeded;
    }

    public DisasterType getOccuredDisaster() {
        return occurredDisaster;
    }

    public void setOccuredDisaster(DisasterType occuredDisaster) {
        this.occurredDisaster = occuredDisaster;
    }

    public CitiesEnum getCityAffected() {
        return cityAffected;
    }

    public void setCityAffected(CitiesEnum cityAffected) {
        this.cityAffected = cityAffected;
    }

    public Integer getEstimatedNumberOfEndangeredPeople() {
        return estimatedNumberOfEndangeredPeople;
    }

    public void setEstimatedNumberOfEndangeredPeople(Integer estimatedNumberOfEndangeredPeople) {
        this.estimatedNumberOfEndangeredPeople = estimatedNumberOfEndangeredPeople;
    }

    public boolean isShelterSupportNeeded() {
        return shelterSupportNeeded;
    }

    public void setShelterSupportNeeded(boolean shelterSupportNeeded) {
        this.shelterSupportNeeded = shelterSupportNeeded;
    }

}
