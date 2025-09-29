module group.scheduler_sim {
    requires javafx.controls;
    requires javafx.fxml;


    opens group.scheduler_sim to javafx.fxml;
    opens group.scheduler_sim.model to javafx.base;
    exports group.scheduler_sim;
}