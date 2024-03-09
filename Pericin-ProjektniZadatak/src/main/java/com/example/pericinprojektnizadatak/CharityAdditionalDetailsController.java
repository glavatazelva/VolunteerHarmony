package com.example.pericinprojektnizadatak;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CharityAdditionalDetailsController {



    @FXML
    private TextField familiesCurrentlyAiding;

    public String getNumberOfFamilies(){
       return familiesCurrentlyAiding.getText();
    }

    public void setFamiliesCurrentlyAiding(String text){
        familiesCurrentlyAiding.setText(text);
    }

}
