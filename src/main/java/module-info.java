module com.biscuittaiger.budgettrackerx {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;


    opens com.biscuittaiger.budgettrackerx to javafx.fxml;
    exports com.biscuittaiger.budgettrackerx.App;
    exports com.biscuittaiger.budgettrackerx.View;
    exports com.biscuittaiger.budgettrackerx.Model;




}