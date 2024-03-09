package com.example.pericinprojektnizadatak.enumerations;

public enum AnimalTypeEnum {

    DOG("Dog"),
    CAT("Cat"),
    BIRD("Bird"),
    OTHER("Other");

    private final String animalType;

    private AnimalTypeEnum(String animalType) {
        this.animalType = animalType;

    }

    public String getAnimalType() {
        return animalType;
    }

}
