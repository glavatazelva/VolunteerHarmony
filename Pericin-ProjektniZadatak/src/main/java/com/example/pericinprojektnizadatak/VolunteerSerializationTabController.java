package com.example.pericinprojektnizadatak;

import com.example.pericinprojektnizadatak.model.generics.Changes;
import com.example.pericinprojektnizadatak.utils.DataChanges;
import com.example.pericinprojektnizadatak.utils.FileUtils;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class VolunteerSerializationTabController {

    private static final Logger logger = LoggerFactory.getLogger(VolunteerSerializationTabController.class);

    @FXML
    private TableView<String> changesTable;
    @FXML
    private TableColumn<Changes,String> beforeColumn;
    @FXML
    private TableColumn<Changes,String> role;
    @FXML
    private TableColumn<Changes,String> afterColumn;
    @FXML
    private TableColumn<Changes,String> dateTimeColumn;

    public void initialize(){
        beforeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Changes,String>, ObservableValue<String>>(){
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Changes, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getBefore().toString());
            }
        });
        afterColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Changes,String>, ObservableValue<String>>(){
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Changes, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getAfter().toString());
            }
        });
        role.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Changes,String>, ObservableValue<String>>(){
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Changes, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getRole());
            }
        });
        dateTimeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Changes,String>, ObservableValue<String>>(){
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Changes, String> param) {
                LocalDateTime tempDate = param.getValue().getDateTimeOfChange();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy HH:mm:ss");
                String formmatedDate  = tempDate.format(formatter);
                return new ReadOnlyStringWrapper(formmatedDate);
            }
        });
       deserializeChanges();
    }

    public void deserializeChanges(){
   //     List<Changes> ozljedaChanges = new ArrayList<>();


        Optional<DataChanges> dat = FileUtils.readCurrentData();
        System.out.println(dat.get().getChangesList().size());

        ObservableList observableList = FXCollections.observableArrayList(dat.get().getChangesList());
        changesTable.setItems(observableList);
    }

    public void back(){
        FXMLLoader fxmlLoader = new FXMLLoader(VolunteerHomePageController.class.getResource("volunteerTabs/volunteerHomePage.fxml"));

        Scene scene = null;
        try{
            scene = new Scene(fxmlLoader.load(),800,650);
        }catch (IOException e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().setTitle("VolunteerHarmony");
        HelloApplication.getStage().show();
    }



}
