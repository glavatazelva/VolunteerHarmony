package com.example.pericinprojektnizadatak.enumerations;

public enum FilePathsEnum {
    CHARITY_FILE_PATH("Charity", "E:/Faks/3. semestar/Java/Volonterske_akcije_Pericin/Pericin-ProjektniZadatak/files/charitiesPasswords.txt"),
    ANIMAL_WELFARE_FILE_PATH("Animal_welfare", "E:/Faks/3. semestar/Java/Volonterske_akcije_Pericin/Pericin-ProjektniZadatak/files/animalWelfaresPasswords.txt"),
    DISASTER_RELIEF_FILE_PATH("Disaster_relief", "E:/Faks/3. semestar/Java/Volonterske_akcije_Pericin/Pericin-ProjektniZadatak/files/disasterReliefsPasswords.txt"),
    VOLUNTEER_FILE_PATH("Volunteer", "E:/Faks/3. semestar/Java/Volonterske_akcije_Pericin/Pericin-ProjektniZadatak/files/volunteerPasswords.txt"),
    ACTIVE_USER_FILE_PATH("ActiveUser","E:/Faks/3. semestar/Java/Volonterske_akcije_Pericin/Pericin-ProjektniZadatak/files/currentUser.txt");

    private final String type;
    private final String filePath;

    private FilePathsEnum(String type,String filePath){
        this.type = type;
        this.filePath = filePath;
    }

    public String getType() {
        return type;
    }

    public String getFilePath() {
        return filePath;
    }
}
