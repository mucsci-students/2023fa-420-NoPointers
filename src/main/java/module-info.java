module nopointers {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;

    requires org.json;
    requires json.simple;
    requires MaterialFX;
    requires org.jline;
    requires com.google.gson;
    opens nopointers to com.google.gson, javafx.fxml;
    exports nopointers;
}