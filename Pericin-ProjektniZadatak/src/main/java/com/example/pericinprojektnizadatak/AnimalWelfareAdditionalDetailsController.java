package com.example.pericinprojektnizadatak;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

public class AnimalWelfareAdditionalDetailsController {

    @FXML
    private CheckBox providesMedicalCare;
    @FXML
    private CheckBox conductsRescueOperations;

    public CheckBox getProvidesMedicalCare() {
        return providesMedicalCare;
    }

    public CheckBox getConductsRescueOperations() {
        return conductsRescueOperations;
    }

    public void setProvidesMedicalCare(Boolean bool) {
        if(bool) {
            providesMedicalCare.setSelected(true);
        }
    }

    public void setConductsRescueOperations(Boolean bool) {
        if(bool) {
            conductsRescueOperations.setSelected(true);
        }

    }
}
