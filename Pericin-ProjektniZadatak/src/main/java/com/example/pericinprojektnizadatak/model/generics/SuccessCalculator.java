package com.example.pericinprojektnizadatak.model.generics;

import com.example.pericinprojektnizadatak.model.Volunteer;

import com.example.pericinprojektnizadatak.model.comparators.TComparator;

import com.example.pericinprojektnizadatak.model.organization.AnimalWelfare;
import com.example.pericinprojektnizadatak.model.organization.Charity;
import com.example.pericinprojektnizadatak.model.organization.DisasterRelief;
import com.example.pericinprojektnizadatak.model.records.EfficiencyRecord;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class SuccessCalculator <T> {

    private T element;

    public SuccessCalculator(){}

    public SuccessCalculator(T element){
        this.element = element;
    }

    public Integer calculateRank(List<T> competitors) {
        Integer rank = 1;

        Set<T> sortedSet = new TreeSet<>(new TComparator<>());
        sortedSet.addAll(competitors);

        for(T temp:sortedSet){
            System.out.println(temp);
            if(temp.equals(element)) return rank;
            else rank++;
        }


        return rank;
    }

    public Double calculateEfficiency(){
        Double efficiency = 0.0;

        if(element instanceof Volunteer){
            efficiency = ((Volunteer)element).getParticipatedOperations()  * EfficiencyRecord.ageCoefficient;
        }
        else if(element instanceof Charity){
            efficiency = ((Charity)element).getFamiliesCurrentlyAiding() / 100 * EfficiencyRecord.aidingFamiliesCoefficient;
            if(((Charity)element).getTotalFundsRaised().compareTo(BigDecimal.valueOf(100000.0)) > 0){
                efficiency += EfficiencyRecord.fundsBonus;
            }
        }
        else if(element instanceof AnimalWelfare){
            efficiency = ((AnimalWelfare)element).getTotalNumberOfSavedAnimals() / 100 * EfficiencyRecord.aidingAnimalsCoefficient;
        }
        else if(element instanceof DisasterRelief){
            efficiency = ((DisasterRelief)element).getTotalNumberOfAidedPeople() / 100 * EfficiencyRecord.aidingFamiliesCoefficient;
        }

        return efficiency;
    }
}
