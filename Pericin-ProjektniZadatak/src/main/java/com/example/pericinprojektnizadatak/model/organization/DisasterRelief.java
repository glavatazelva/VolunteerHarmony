package com.example.pericinprojektnizadatak.model.organization;

import com.example.pericinprojektnizadatak.model.Address;
import com.example.pericinprojektnizadatak.model.Volunteer;

import com.example.pericinprojektnizadatak.model.interfaces.OrganizationMethods;

import com.example.pericinprojektnizadatak.model.voluntaryAction.VoluntaryAction;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public final class DisasterRelief extends Organization implements OrganizationMethods, Serializable{

    private Integer totalNumberOfAidedPeople;
    private Boolean providesFoodSupply;

    public DisasterRelief(){
        super();
    }
    public DisasterRelief(String name, String description, String email, String phone, Address address, Integer foundingYear,List<Volunteer>volunteerList, Boolean providesFoodSupply,Integer totalNumberOfAidedPeople) {
        super(name, description, email,phone, address, foundingYear,volunteerList);
        this.providesFoodSupply = providesFoodSupply;
        this.totalNumberOfAidedPeople = totalNumberOfAidedPeople;
    }

    public Integer getTotalNumberOfAidedPeople() {
        return totalNumberOfAidedPeople;
    }

    public void setTotalNumberOfAidedPeople(Integer totalNumberOfAidedPeople) {
        this.totalNumberOfAidedPeople = totalNumberOfAidedPeople;
    }

    public Boolean getProvidesFoodSupply() {
        return providesFoodSupply;
    }

    public void setProvidesFoodSupply(Boolean providesFoodSupply) {
        this.providesFoodSupply = providesFoodSupply;
    }

    @Override
    public void addToTotal(Integer increment) {
        totalNumberOfAidedPeople+=increment;
    }

    @Override
    public String toString() {
        String returnString = super.toString();

        return getClass().getSimpleName()+"\n"+returnString
                +"\nnumber of aided people: "+this.totalNumberOfAidedPeople
                +"\nprovides food supply: "+this.providesFoodSupply;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DisasterRelief other = (DisasterRelief) obj;
        return Objects.equals(getEmail(), other.getEmail());
    }

}
