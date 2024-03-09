package com.example.pericinprojektnizadatak.model;

import com.example.pericinprojektnizadatak.enumerations.GenderEnum;
import com.example.pericinprojektnizadatak.enumerations.NationalityEnum;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public class PersonalInformation implements Serializable {

    private String name;
    private String surname;
    private Integer age;
    private GenderEnum gender;
    private NationalityEnum nationality;
    private String phoneNumber;
    private String email;

    public PersonalInformation(String name, String surname,Integer age,GenderEnum gender, NationalityEnum nationality, String phoneNumber, String email) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.gender = gender;
        this.nationality = nationality;

        this.phoneNumber = nationality.getCallingNumber() + " " + phoneNumber;

        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public NationalityEnum getNationality() {
        return nationality;
    }

    public void setNationality(NationalityEnum nationality) {
        this.nationality = nationality;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static class PersonalInformationBuilder {
        private String name;
        private String surname;
        private Integer age;
        private GenderEnum gender;
        private NationalityEnum nationality;
        private String phoneNumber;
        private String email;

        public PersonalInformationBuilder() {
        }

        public PersonalInformationBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public PersonalInformationBuilder withSurname(String surname) {
            this.surname = surname;
            return this;
        }

        public PersonalInformationBuilder withAge(Integer age) {
            this.age = age;
            return this;
        }

        public PersonalInformationBuilder withGender(GenderEnum gender) {
            this.gender = gender;
            return this;
        }

        public PersonalInformationBuilder withNationality(NationalityEnum nationality) {
            this.nationality = nationality;
            return this;
        }

        public PersonalInformationBuilder withPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public PersonalInformationBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public PersonalInformation build() {
            return new PersonalInformation(name, surname, age, gender, nationality, phoneNumber, email);
        }
    }
}
