package group.scheduler_sim;

import group.scheduler_sim.algorithms.FirstComeFirstServed;
import group.scheduler_sim.algorithms.ShortestJobNext;
import group.scheduler_sim.algorithms.ShortestRemainingTime;
import group.scheduler_sim.algorithms.RoundRobin;
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
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class HelloController {

    @FXML private ChoiceBox<String> algorithmChoice;
    @FXML private Spinner<Integer> timeQuantumSpinner;
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
                "Shortest Job Next",
                "Shortest Remaining Time",
                "Round Robin"
        ));
        algorithmChoice.getSelectionModel().selectFirst();

        // Initialize time quantum spinner
        SpinnerValueFactory.IntegerSpinnerValueFactory quantumFactory = 
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 2);
        timeQuantumSpinner.setValueFactory(quantumFactory);

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
        
        switch (selected) {
            case "Shortest Job Next":
                algorithm = new ShortestJobNext();
                break;
            case "Shortest Remaining Time":
                algorithm = new ShortestRemainingTime();
                break;
            case "Round Robin":
                int timeQuantum = timeQuantumSpinner.getValue();
                algorithm = new RoundRobin(timeQuantum);
                break;
            case "First Come First Served":
            default:
                algorithm = new FirstComeFirstServed();
                break;
        }
        
        SimulationResult result = algorithm.simulate(new ArrayList<>(processes));
        renderResult(result);
    }

    private void renderResult(SimulationResult result) {
        gantt.getChildren().clear();
        Map<String, String> colorFor = new LinkedHashMap<>();
        int colorIndex = 0;
        String[] palette = {
            "#3498db", "#e74c3c", "#2ecc71", "#f39c12", 
            "#9b59b6", "#1abc9c", "#34495e", "#e67e22"
        };
        
        // Assign colors to processes
        for (Process p : processes) {
            colorFor.put(p.getId(), palette[colorIndex % palette.length]);
            colorIndex++;
        }
        
        // Render timeline blocks
        for (ScheduledSlice s : result.getTimeline()) {
            javafx.scene.layout.Region block = new javafx.scene.layout.Region();
            int duration = s.getEndTime() - s.getStartTime();
            int width = Math.max(40, duration * 25);
            block.setPrefSize(width, 35);
            
            String color = colorFor.getOrDefault(s.getProcessId(), "#95a5a6");
            block.setStyle(String.format(
                "-fx-background-color: %s; " +
                "-fx-border-color: #333333; " +
                "-fx-border-width: 1;",
                color
            ));
            block.getStyleClass().add("gantt-block");

            StackPane stack = getStackPane(s, block);
            gantt.getChildren().add(stack);
        }

        avgWaiting.setText(String.format("Avg Waiting Time: %.2f", result.getAverageWaitingTime()));
        avgTurnaround.setText(String.format("Avg Turnaround Time: %.2f", result.getAverageTurnaroundTime()));
        avgResponse.setText(String.format("Avg Response Time: %.2f", result.getAverageResponseTime()));

        addProcessLegend(colorFor);
    }

    private static StackPane getStackPane(ScheduledSlice s, Region block) {
        Label label = new Label(String.format("%s\n[%d-%d]", 
            s.getProcessId(), s.getStartTime(), s.getEndTime()));
        label.setStyle(
            "-fx-text-fill: white; " +
            "-fx-font-size: 10px; " +
            "-fx-alignment: center; " +
            "-fx-text-alignment: center;"
        );

        StackPane stack = new StackPane(block, label);
        stack.setAlignment(Pos.CENTER);
        return stack;
    }

    private void addProcessLegend(Map<String, String> colorFor) {
        if (!gantt.getChildren().isEmpty()) {
            javafx.scene.layout.HBox legend = new javafx.scene.layout.HBox(10);
            legend.setAlignment(Pos.CENTER_LEFT);
            legend.setStyle("-fx-padding: 10 0 0 0;");
            
            Label legendLabel = new Label("Process Colors: ");
            legendLabel.setStyle("-fx-font-weight: normal; -fx-text-fill: #000000;");
            legend.getChildren().add(legendLabel);
            
            for (Map.Entry<String, String> entry : colorFor.entrySet()) {
                javafx.scene.layout.Region colorBox = new javafx.scene.layout.Region();
                colorBox.setPrefSize(16, 16);
                colorBox.setStyle(String.format(
                    "-fx-background-color: %s; " +
                    "-fx-border-color: #333333; " +
                    "-fx-border-width: 1;",
                    entry.getValue()
                ));
                
                Label processLabel = new Label(entry.getKey());
                processLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #000000;");
                
                javafx.scene.layout.HBox legendItem = new javafx.scene.layout.HBox(5);
                legendItem.setAlignment(Pos.CENTER_LEFT);
                legendItem.getChildren().addAll(colorBox, processLabel);
                legend.getChildren().add(legendItem);
            }

             if (gantt.getParent() instanceof javafx.scene.layout.VBox parent) {
                 if (parent.getChildren().size() > 2) {
                     parent.getChildren().remove(parent.getChildren().size() - 1);
                 }
                 parent.getChildren().add(legend);
            }
        }
    }
}
