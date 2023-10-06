module nopointers {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;


    requires org.json;
    requires json.simple;
    requires MaterialFX;
    opens nopointers to javafx.fxml;
    exports nopointers;
}