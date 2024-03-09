package com.example.pericinprojektnizadatak;

import com.example.pericinprojektnizadatak.model.Volunteer;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

public class TableCellWithMinusButton extends TableCell<Volunteer, Void> {
    private final Button button;

    public TableCellWithMinusButton() {
        this.button = new Button("-");
        this.button.setOnAction(event -> {

            OrganizationRemoveVolunteerController.removeVolunteerToOrganization(getTableView().getItems().get(getIndex()).getPersonalInfo().getEmail());

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