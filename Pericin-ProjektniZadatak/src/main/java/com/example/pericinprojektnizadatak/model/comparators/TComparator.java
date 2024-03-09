package com.example.pericinprojektnizadatak.model.comparators;

import com.example.pericinprojektnizadatak.model.Volunteer;
import com.example.pericinprojektnizadatak.model.organization.AnimalWelfare;
import com.example.pericinprojektnizadatak.model.organization.Charity;
import com.example.pericinprojektnizadatak.model.organization.DisasterRelief;

import java.util.Comparator;

public class TComparator<T> implements Comparator<T> {

    @Override
    public int compare(T o1, T o2) {
        if(o1 instanceof Volunteer){
            if(((Volunteer)o1).getParticipatedOperations() > ((Volunteer)o2).getParticipatedOperations())
                return -1;
            else if(((Volunteer)o1).getParticipatedOperations() < ((Volunteer)o2).getParticipatedOperations())
                return 1;
            else return ((Volunteer)o2).getPersonalInfo().getAge().compareTo(((Volunteer)o1).getPersonalInfo().getAge());
        }
        else if(o1 instanceof Charity){
            if(((Charity)o1).getFamiliesCurrentlyAiding()>((Charity)o2).getFamiliesCurrentlyAiding())
                return -1;

            else if(((Charity)o1).getFamiliesCurrentlyAiding()<((Charity)o2).getFamiliesCurrentlyAiding())
                return 1;
            else return ((Charity)o2).getTotalFundsRaised().compareTo(((Charity)o1).getTotalFundsRaised());
        }

        else if(o1 instanceof AnimalWelfare){
            if(((AnimalWelfare)o1).getTotalNumberOfSavedAnimals()>((AnimalWelfare)o2).getTotalNumberOfSavedAnimals())
                return -1;
            else if (((AnimalWelfare)o1).getTotalNumberOfSavedAnimals()<((AnimalWelfare)o2).getTotalNumberOfSavedAnimals()) {
                return 1;
            }
            else return ((AnimalWelfare)o2).getFoundingYear().compareTo(((AnimalWelfare)o2).getFoundingYear());
        }
        else if(o1 instanceof DisasterRelief){
            if(((DisasterRelief)o1).getTotalNumberOfAidedPeople()>((DisasterRelief)o2).getTotalNumberOfAidedPeople())
                return -1;
            else if (((DisasterRelief)o1).getTotalNumberOfAidedPeople()<((DisasterRelief)o2).getTotalNumberOfAidedPeople()) {
                return 1;
            }
            else return ((DisasterRelief)o2).getFoundingYear().compareTo(((DisasterRelief)o2).getFoundingYear());
        }

        return -1;
    }
}
