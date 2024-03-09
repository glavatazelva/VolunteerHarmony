package com.example.pericinprojektnizadatak.model.voluntaryAction;

import com.example.pericinprojektnizadatak.model.Volunteer;
import com.example.pericinprojektnizadatak.model.interfaces.ActionInterface;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public final class CharityAction extends VoluntaryAction implements ActionInterface {

    private BigDecimal fundsRaised;
    private Integer aidedPeople;
    private String automaticDonationNumber;

    public CharityAction() {
    }

    public CharityAction(String name, String description, LocalDateTime endingDate, Integer aidedPeople,String automaticDonationNumber) {
        super(name, description, endingDate);
        this.automaticDonationNumber = automaticDonationNumber;
        this.aidedPeople = aidedPeople;
        this.fundsRaised = BigDecimal.valueOf(Math.random()*10000);
    }

    public CharityAction(String name, String description, LocalDateTime endingDate, Integer aidedPeople,String automaticDonationNumber,BigDecimal fundsRaised) {
        super(name, description, endingDate);
        this.automaticDonationNumber = automaticDonationNumber;
        this.aidedPeople = aidedPeople;
        this.fundsRaised = fundsRaised;
    }

    public BigDecimal getFundsRaised() {
        return fundsRaised;
    }

    public void setFundsRaised(BigDecimal fundsRaised) {
        this.fundsRaised = fundsRaised;
    }

    public Integer getAidedPeople() {
        return aidedPeople;
    }

    public void setAidedPeople(Integer aidedPeople) {
        this.aidedPeople = aidedPeople;
    }

    public String getAutomaticDonationNumber() {
        return automaticDonationNumber;
    }

    public void setAutomaticDonationNumber(String automaticDonationNumber) {
        this.automaticDonationNumber = automaticDonationNumber;
    }
}
