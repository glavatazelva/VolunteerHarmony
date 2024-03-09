package com.example.pericinprojektnizadatak.model.organization;

import com.example.pericinprojektnizadatak.model.Address;
import com.example.pericinprojektnizadatak.model.Volunteer;
import com.example.pericinprojektnizadatak.model.voluntaryAction.VoluntaryAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Organization implements Serializable {

    private static final long serialVersionUID = -402135860403272140L;

    private String name;
    private String description;
    private List<Volunteer> volunteerList;


    private String email;
    private String phone;
    private Address address;

    private Integer foundingYear;




    public Organization(){}
    public Organization(String name, String description, String email, String phone, Address address, Integer foundingYear,List<Volunteer> volunteerList) {
        this.name = name;
        this.description = description;
        this.volunteerList = volunteerList;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.foundingYear = foundingYear;


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

    public List<Volunteer> getVolunteerList() {
        return volunteerList;
    }

    public void setVolunteerList(List<Volunteer> volunteerList) {
        this.volunteerList = volunteerList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Integer getFoundingYear() {
        return foundingYear;
    }

    public void setFoundingYear(Integer foundingYear) {
        this.foundingYear = foundingYear;
    }


    public void addVolunteerToList(Volunteer volunteerToBeAdded){
        this.volunteerList.add(volunteerToBeAdded);
    }




    @Override
    public String toString() {
        return "\nname: "+this.name
                +"\ndescription: "+this.description
                +"\nfounding year: "+this.foundingYear
                +"\ncity: "+this.address.getCity().getCityName()
                +"\naddress: "+this.address.getStreet()
                +"\nhouse number: "+this.address.getHouseNumber()
                +"\ncontact number:"+this.getPhone();
    }




}
