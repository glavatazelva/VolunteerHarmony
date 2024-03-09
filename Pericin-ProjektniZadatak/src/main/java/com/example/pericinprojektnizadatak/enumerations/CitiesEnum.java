package com.example.pericinprojektnizadatak.enumerations;

public enum CitiesEnum {
    BJELOVAR("Bjelovar", "43000"),
    DUBROVNIK("Dubrovnik", "20000"),
    KARLOVAC("Karlovac", "47000"),
    KOPRIVNICA("Koprivnica", "48000"),
    OSIJEK("Osijek", "31000"),
    POREC("Porec", "52440"),
    PULA("Pula", "52100"),
    RIJEKA("Rijeka", "51000"),
    SIBENIK("Sibenik", "22000"),
    SISAK("Sisak", "44000"),
    SPLIT("Split", "21000"),
    VARAZDIN("Varazdin", "42000"),
    VUKOVAR("Vukovar", "32000"),
    ZAGREB("Zagreb", "10000"),
    ZADAR("Zadar", "23000");

    private final String cityName;
    private final String postalCode;

    private CitiesEnum(String cityName, String postalCode) {
        this.cityName = cityName;
        this.postalCode = postalCode;
    }

    public String getCityName() {
        return cityName;
    }

    public String getPostalCode() {
        return postalCode;
    }
}
