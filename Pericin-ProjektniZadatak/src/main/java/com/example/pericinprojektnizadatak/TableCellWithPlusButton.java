package com.example.pericinprojektnizadatak;

import com.example.pericinprojektnizadatak.model.Volunteer;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

public class TableCellWithPlusButton extends TableCell<Volunteer, Void> {
    private final Button button;

    public TableCellWithPlusButton() {
        this.button = new Button("+");
        this.button.setOnAction(event -> {

            OrganizationAddNewVolunteerTabController.addVolunteerToOrganization(getTableView().getItems().get(getIndex()).getPersonalInfo().getEmail());

        });
        setGraphic(button);
    }

    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(button);
        }
    }
}