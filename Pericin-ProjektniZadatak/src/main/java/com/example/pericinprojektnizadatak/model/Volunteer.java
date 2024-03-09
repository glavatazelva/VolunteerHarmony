package com.example.pericinprojektnizadatak.model;


import java.io.Serializable;
import java.util.Objects;

public final class Volunteer implements Serializable{

    private PersonalInformation personalInfo;
    private Integer participatedOperations;

    public Volunteer(){}

    public Volunteer(PersonalInformation personalInfo, Integer participatedOperations) {
        this.personalInfo = personalInfo;
        this.participatedOperations = participatedOperations;

    }

    public PersonalInformation getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(PersonalInformation personalInfo) {
        this.personalInfo = personalInfo;
    }

    public Integer getParticipatedOperations() {
        return participatedOperations;
    }

    public void setParticipatedOperations(Integer participatedOperations) {
        this.participatedOperations = participatedOperations;
    }

    public static class VolunteerBuilder {
        private PersonalInformation personalInfo;
        private Integer participatedOperations;

        public VolunteerBuilder withPersonalInfo(PersonalInformation personalInfo) {
            this.personalInfo = personalInfo;
            return this;
        }

        public VolunteerBuilder withParticipatedOperations(Integer participatedOperations) {
            this.participatedOperations = participatedOperations;
            return this;
        }

        public Volunteer build() {
            return new Volunteer(personalInfo, participatedOperations);
        }
    }

    @Override
    public String toString() {
        return "name: "+this.personalInfo.getName()+" "+this.personalInfo.getSurname()
                +"\nnationality: "+this.personalInfo.getNationality().getCoutryName()
                +"\ngender: "+this.personalInfo.getGender().getGender()
                +"\nphone number "+this.personalInfo.getPhoneNumber()
                +"\nemail "+this.personalInfo.getEmail();
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Volunteer other = (Volunteer) obj;
        return Objects.equals(personalInfo.getEmail(), other.personalInfo.getEmail());
    }


}
