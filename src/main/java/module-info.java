module nopointers {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    requires java.sql;


    requires org.json;
    requires json.simple;

    opens nopointers to javafx.fxml;
    exports nopointers;
}