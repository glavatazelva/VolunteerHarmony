package com.example.pericinprojektnizadatak.utils;

import com.example.pericinprojektnizadatak.model.generics.Changes;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataChanges implements Serializable {
    List<Changes> changesList = new ArrayList<>();

    public DataChanges(List<Changes> changesList) {
        this.changesList = changesList;
    }
    public DataChanges() {

    }
    public List<Changes> getChangesList() {
        return changesList;
    }
    public void addChange(Changes temp){
        changesList.add(temp);
    }
    public static Optional<DataChanges> readFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("E:/Faks/3. semestar/Java/Volonterske_akcije_Pericin/Pericin-ProjektniZadatak/files/serialization.bin"))) {
            DataChanges dataWrapper = (DataChanges) in.readObject();
            return Optional.of(dataWrapper);
        } catch (IOException | ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    public void writeToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("E:/Faks/3. semestar/Java/Volonterske_akcije_Pericin/Pericin-ProjektniZadatak/files/serialization.bin"))) {
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}