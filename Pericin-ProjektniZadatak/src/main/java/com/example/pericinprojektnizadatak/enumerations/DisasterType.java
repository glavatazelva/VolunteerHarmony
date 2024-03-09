package com.example.pericinprojektnizadatak.enumerations;

public enum DisasterType {

    FIRE("Fire"),
    EARTHQUAKE("Earthquake"),
    FLOOD("Flood");

    private final String disasterType;

    private DisasterType(String disasterType) {
        this.disasterType = disasterType;

    }

    public String getDisasterType() {
        return disasterType;
    }

}
