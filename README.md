
## Prerequisites

- **Java JDK 17 or later** (Recommended: JDK 21)
- **JavaFX SDK 17 or later**
- An IDE like IntelliJ IDEA, Eclipse, or VS Code.

## Installation & Setup

1.  **Clone the Repository**
    ```bash
    git clone <your-github-repo-url>
    cd schedule_sim
    ```

2.  **Configure your IDE for JavaFX**
    - **IntelliJ IDEA**:
      - Go to `File > Project Structure > Libraries`.
      - Add the `lib` folder from your JavaFX SDK.
      - In `Run > Edit Configurations` for the `Launcher` class, add the following to VM options:
        ```
        --module-path /path/to/javafx-sdk-21/lib --add-modules javafx.controls,javafx.fxml
        ```
        *(Replace the path with your actual JavaFX SDK path)*.

    - **VS Code**:
      - Install the "Extension Pack for Java" and "JavaFX Support" extensions.
      - Configure your `settings.json` to point to your JavaFX SDK.

    - **Command Line**:
      ```bash
      # Compile (example, adjust paths as needed)
      javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -d out src/main/java/group/scheduler_sim/**/*.java src/main/java/group/scheduler_sim/*.java

      # Run
      java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -cp out group.scheduler_sim.Launcher
      ```

## How to Run

1.  Ensure all prerequisites and setup steps are complete.
2.  Run the `Launcher.java` file from your IDE, or execute the JAR from the command line.
3.  The application window will open.

## Usage

1.  **Add Processes**: Use the table in the UI to input Process ID, Arrival Time, and Burst Time for each job.
2.  **Select Algorithm**: Choose an algorithm from the dropdown menu. For Round Robin, a time quantum spinner will appear.
3.  **Run Simulation**: Click the "Run Simulation" button.
4.  **View Results**: The Gantt chart will display the scheduling timeline, and the metrics panel will show the average Waiting, Turnaround, and Response times.

## Project Structure
schedule_sim/
└── src/main/java/group/scheduler_sim/
├── HelloApplication.java # Main JavaFX application class
├── Launcher.java # Launcher with main() method
├── HelloController.java # Main UI controller
├── algorithms/ # Scheduling algorithm implementations
│ ├── SchedulingAlgorithm.java # Interface
│ ├── FirstComeFirstServed.java
│ ├── ShortestJobNext.java
│ ├── ShortestRemainingTime.java
│ └── RoundRobin.java
├── engine/
│ └── Simulator.java # Simulation coordinator
└── model/ # Core data classes
├── Process.java
├── ScheduledSlice.java
└── SimulationResult.java

src/main/resources/group/scheduler_sim/
├── hello-view.fxml # UI layout definition
└── styles.css # Application stylesheet
