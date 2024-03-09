package com.example.pericinprojektnizadatak.model.organization;

import com.example.pericinprojektnizadatak.model.Address;
import com.example.pericinprojektnizadatak.model.Volunteer;
import com.example.pericinprojektnizadatak.model.interfaces.OrganizationMethods;

import com.example.pericinprojektnizadatak.model.organization.Organization;
import com.example.pericinprojektnizadatak.model.voluntaryAction.VoluntaryAction;
import javafx.scene.control.Label;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public final class AnimalWelfare extends Organization implements OrganizationMethods, Serializable {

    private static final long serialVersionUID = -2381899738790509406L;
    private Integer totalNumberOfSavedAnimals;
    private Boolean providesMedicalCare;
    private Boolean conductsRescueOperations;


    public AnimalWelfare(){
        super();
    }

    public AnimalWelfare(String name, String description, String email, String phone, Address address, Integer foundingYear,List<Volunteer> volunteerList,Integer totalNumberOfSavedAnimals, Boolean providesMedicalCare, Boolean conductsRescueOperations) {
        super(name, description, email, phone, address, foundingYear,volunteerList);
        this.totalNumberOfSavedAnimals = totalNumberOfSavedAnimals;
        this.providesMedicalCare = providesMedicalCare;
        this.conductsRescueOperations = conductsRescueOperations;
    }



    public Integer getTotalNumberOfSavedAnimals() {
        return totalNumberOfSavedAnimals;
    }

    public void setTotalNumberOfSavedAnimals(Integer totalNumberOfSavedAnimals) {
        this.totalNumberOfSavedAnimals = totalNumberOfSavedAnimals;
    }

    public Boolean getProvidesMedicalCare() {
        return providesMedicalCare;
    }

    public void setProvidesMedicalCare(Boolean providesMedicalCare) {
        this.providesMedicalCare = providesMedicalCare;
    }

    public Boolean getConductsRescueOperations() {
        return conductsRescueOperations;
    }

    public void setConductsRescueOperations(Boolean conductsRescueOperations) {
        this.conductsRescueOperations = conductsRescueOperations;
    }

    @Override
    public void addToTotal(Integer increment) {
        totalNumberOfSavedAnimals += increment;
    }




    @Override
    public String toString() {
        String returnString = super.toString();

        return getClass().getSimpleName()+"\n"+returnString
                +"\nsaved animals: "+this.totalNumberOfSavedAnimals
                +"\nmedical care: "+this.providesMedicalCare
                +"\nrescue operations: "+this.conductsRescueOperations;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AnimalWelfare other = (AnimalWelfare) obj;
        return Objects.equals(getEmail(), other.getEmail());
    }



}
