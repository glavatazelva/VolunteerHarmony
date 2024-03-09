package com.example.pericinprojektnizadatak.model;

import com.example.pericinprojektnizadatak.enumerations.CitiesEnum;

import java.io.Serializable;
import java.util.Objects;

/**
 * Klasa za kreiranje objekta koji se ponaša kao adresa
 */
public class Address implements Serializable{

    private Long id;
    private CitiesEnum city;
    private String street;
    private  String houseNumber;


    public Address(){}


    public Address(String street, String houseNumber, CitiesEnum city) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.city = city;

    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public CitiesEnum getCity() {
        return city;
    }

    public void setCity(CitiesEnum city) {
        this.city = city;
    }




    public static class Builder {


        private String street;
        private String houseNumber;
        private CitiesEnum city;



        public Builder() {
        }

        public Builder street(String street) {
            this.street = street;
            return this;
        }

        public Builder houseNumber(String houseNumber) {
            this.houseNumber = houseNumber;
            return this;
        }

        public Builder city(CitiesEnum city) {
            this.city = city;
            return this;
        }

        public Address build() {
            return new Address(street, houseNumber, city);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address address)) return false;
        return Objects.equals(getStreet(), address.getStreet()) && Objects.equals(getHouseNumber(), address.getHouseNumber()) && Objects.equals(getCity(), address.getCity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStreet(), getHouseNumber(), getCity());
    }
}
