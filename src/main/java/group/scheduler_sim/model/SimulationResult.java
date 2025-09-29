package group.scheduler_sim.model;

import java.util.Collections;
import java.util.List;

/**
 * Result of running a scheduling algorithm: Gantt slices and per-process metrics.
 */
public class SimulationResult {
    private final List<ScheduledSlice> timeline; // ordered by start time
    private final double averageWaitingTime;
    private final double averageTurnaroundTime;
    private final double averageResponseTime;

    public SimulationResult(List<ScheduledSlice> timeline,
                            double averageWaitingTime,
                            double averageTurnaroundTime,
                            double averageResponseTime) {
        this.timeline = List.copyOf(timeline);
        this.averageWaitingTime = averageWaitingTime;
        this.averageTurnaroundTime = averageTurnaroundTime;
        this.averageResponseTime = averageResponseTime;
    }

    public List<ScheduledSlice> getTimeline() {
        return Collections.unmodifiableList(timeline);
    }

    public double getAverageWaitingTime() {
        return averageWaitingTime;
    }

    public double getAverageTurnaroundTime() {
        return averageTurnaroundTime;
    }

    public double getAverageResponseTime() {
        return averageResponseTime;
    }
}


