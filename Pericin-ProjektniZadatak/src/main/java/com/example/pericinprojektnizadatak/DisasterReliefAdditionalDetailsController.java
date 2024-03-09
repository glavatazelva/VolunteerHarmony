package com.example.pericinprojektnizadatak;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class DisasterReliefAdditionalDetailsController {

    @FXML
    private CheckBox providesFoodSupply;

    public CheckBox getProvidesFoodSupply() {
        return providesFoodSupply;
    }
    public void setProvidesFoodSupply(Boolean bool){
        if(bool) {
            providesFoodSupply.setSelected(true);
        }

    }
}
