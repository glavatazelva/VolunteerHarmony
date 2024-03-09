package com.example.pericinprojektnizadatak.model.organization;

import com.example.pericinprojektnizadatak.model.Address;
import com.example.pericinprojektnizadatak.model.Volunteer;
import com.example.pericinprojektnizadatak.model.interfaces.OrganizationMethods;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public final class Charity extends Organization implements OrganizationMethods, Serializable {

    private BigDecimal totalFundsRaised;
    private Integer familiesCurrentlyAiding;

    public Charity(CharityBuilder charityBuilder){
        super();
    }
    public Charity(String name, String description, String email, String phone, Address address, Integer foundingYear, List<Volunteer> volunteerList ,Integer familiesCurrentlyAiding,BigDecimal totalFundsRaised) {
        super(name, description, email, phone, address, foundingYear,volunteerList);
        this.familiesCurrentlyAiding = familiesCurrentlyAiding;
        this.totalFundsRaised = totalFundsRaised;
    }

    public BigDecimal getTotalFundsRaised() {
        return totalFundsRaised;
    }

    public void setTotalFundsRaised(BigDecimal totalFundsRaised) {
        this.totalFundsRaised = totalFundsRaised;
    }

    public Integer getFamiliesCurrentlyAiding() {
        return familiesCurrentlyAiding;
    }

    public void setFamiliesCurrentlyAiding(Integer familiesCurrentlyAiding) {
        this.familiesCurrentlyAiding = familiesCurrentlyAiding;
    }

    @Override
    public void addToTotal(Integer increment) {
        familiesCurrentlyAiding +=increment;
    }

    public static class CharityBuilder {
        private String name;
        private String description;
        private String email;
        private String phone;
        private Address address;
        private Integer foundingYear;
        private Integer familiesCurrentlyAiding;
        private BigDecimal totalFundsRaised;

        public CharityBuilder(String name, String description, String email, String phone, Address address, Integer foundingYear) {
            this.name = name;
            this.description = description;
            this.email = email;
            this.phone = phone;
            this.address = address;
            this.foundingYear = foundingYear;
            this.familiesCurrentlyAiding = 0;
            this.totalFundsRaised = BigDecimal.valueOf(0);
        }

        public CharityBuilder familiesCurrentlyAiding(Integer familiesCurrentlyAiding) {
            this.familiesCurrentlyAiding = familiesCurrentlyAiding;
            return this;
        }

        public CharityBuilder totalFundsRaised(BigDecimal totalFundsRaised) {
            this.totalFundsRaised = totalFundsRaised;
            return this;
        }

        public Charity build() {
            return new Charity(this);
        }
    }

    @Override
    public String toString() {
        String returnString = super.toString();

        return getClass().getSimpleName()+"\n"+returnString
                +"\ncurrently aiding families: "+this.familiesCurrentlyAiding
                +"\ntotal funds raise: "+this.totalFundsRaised;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Charity other = (Charity) obj;
        return Objects.equals(getEmail(), other.getEmail());
    }

}
