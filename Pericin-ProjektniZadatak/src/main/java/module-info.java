module com.example.pericinprojektnizadatak {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;


    opens com.example.pericinprojektnizadatak to javafx.fxml;
    exports com.example.pericinprojektnizadatak;

}