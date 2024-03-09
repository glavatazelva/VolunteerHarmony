package com.example.pericinprojektnizadatak.enumerations;

public enum NationalityEnum {
    AUSTRALIA("Australia", "+61"),
    BRAZIL("Brazil", "+55"),
    CANADA("Canada", "+1"),
    CHINA("China", "+86"),
    CROATIA("Croatia", "+385"),
    FRANCE("France", "+33"),
    GERMANY("Germany", "+49"),
    INDIA("India", "+91"),
    ITALY("Italy", "+39"),
    JAPAN("Japan", "+81"),
    NETHERLANDS("Netherlands", "+31"),
    NORWAY("Norway", "+47"),
    POLAND("Poland", "+48"),
    SPAIN("Spain", "+34"),
    SWEDEN("Sweden", "+46"),
    SWITZERLAND("Switzerland", "+41"),
    UK("United Kingdom", "+44"),
    USA("United States of America", "+1");

    private final String coutryName;
    private final String callingNumber;

    private NationalityEnum(String coutryName, String callingNumber) {
        this.coutryName = coutryName;
        this.callingNumber = callingNumber;
    }

    public String getCoutryName() {
        return coutryName;
    }

    public String getCallingNumber() {
        return callingNumber;
    }
}
