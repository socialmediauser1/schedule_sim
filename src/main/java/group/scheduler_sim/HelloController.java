package group.scheduler_sim;

import group.scheduler_sim.algorithms.FirstComeFirstServed;
import group.scheduler_sim.algorithms.ShortestJobNext;
import group.scheduler_sim.algorithms.SchedulingAlgorithm;
import group.scheduler_sim.model.Process;
import group.scheduler_sim.model.ScheduledSlice;
import group.scheduler_sim.model.SimulationResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class HelloController {

    @FXML private ChoiceBox<String> algorithmChoice;
    @FXML private TextField idField;
    @FXML private TextField arrivalField;
    @FXML private TextField burstField;
    @FXML private TableView<Process> processTable;
    @FXML private TableColumn<Process, String> colId;
    @FXML private TableColumn<Process, Integer> colArrival;
    @FXML private TableColumn<Process, Integer> colBurst;
    @FXML private HBox gantt;
    @FXML private Label avgWaiting;
    @FXML private Label avgTurnaround;
    @FXML private Label avgResponse;

    private final ObservableList<Process> processes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        algorithmChoice.setItems(FXCollections.observableArrayList(
                "First Come First Served",
                "Shortest Job Next"
        ));
        algorithmChoice.getSelectionModel().selectFirst();

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colArrival.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        colBurst.setCellValueFactory(new PropertyValueFactory<>("burstTime"));
        processTable.setItems(processes);
    }

    @FXML
    public void onAddProcess() {
        String id = idField.getText().trim();
        String a = arrivalField.getText().trim();
        String b = burstField.getText().trim();
        if (id.isEmpty() || a.isEmpty() || b.isEmpty()) {
            return;
        }
        try {
            int arrival = Integer.parseInt(a);
            int burst = Integer.parseInt(b);
            processes.add(new Process(id, arrival, burst));
            idField.clear();
            arrivalField.clear();
            burstField.clear();
        } catch (NumberFormatException ignored) {
        }
    }

    @FXML
    public void onClearProcesses() {
        processes.clear();
        gantt.getChildren().clear();
        avgWaiting.setText("Avg Waiting: -");
        avgTurnaround.setText("Avg Turnaround: -");
        avgResponse.setText("Avg Response: -");
    }

    @FXML
    public void onRun() {
        SchedulingAlgorithm algorithm;
        String selected = algorithmChoice.getValue();
        if ("Shortest Job Next".equals(selected)) {
            algorithm = new ShortestJobNext();
        } else {
            algorithm = new FirstComeFirstServed();
        }
        SimulationResult result = algorithm.simulate(new ArrayList<>(processes));
        renderResult(result);
    }

    private void renderResult(SimulationResult result) {
        gantt.getChildren().clear();
        Map<String, String> colorFor = new LinkedHashMap<>();
        int colorIndex = 0;
        String[] palette = {"#4CAF50", "#2196F3", "#FFC107", "#E91E63", "#9C27B0", "#FF5722", "#795548"};
        for (Process p : processes) {
            colorFor.put(p.getId(), palette[colorIndex % palette.length]);
            colorIndex++;
        }
        for (ScheduledSlice s : result.getTimeline()) {
            javafx.scene.layout.Region block = new javafx.scene.layout.Region();
            int width = Math.max(10, (s.getEndTime() - s.getStartTime()) * 20);
            block.setPrefSize(width, 30);
            block.setStyle("-fx-background-color: " + colorFor.getOrDefault(s.getProcessId(), "#BDBDBD") + "; -fx-border-color: #333;");
            block.getStyleClass().add("gantt-block");
            Label label = new Label(s.getProcessId() + "(" + s.getStartTime() + "-" + s.getEndTime() + ")");
            label.setStyle("-fx-text-fill: white; -fx-font-size: 11px; -fx-padding: 2 4 2 4;");
            javafx.scene.layout.StackPane stack = new javafx.scene.layout.StackPane(block, label);
            gantt.getChildren().add(stack);
        }

        avgWaiting.setText(String.format("Avg Waiting: %.2f", result.getAverageWaitingTime()));
        avgTurnaround.setText(String.format("Avg Turnaround: %.2f", result.getAverageTurnaroundTime()));
        avgResponse.setText(String.format("Avg Response: %.2f", result.getAverageResponseTime()));
    }
}
