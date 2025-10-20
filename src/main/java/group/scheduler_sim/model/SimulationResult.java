package group.scheduler_sim.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimulationResult {
    private final List<ScheduledSlice> timeline;
    private final double averageWaitingTime;
    private final double averageTurnaroundTime;
    private final double averageResponseTime;

    public SimulationResult(List<ScheduledSlice> timeline,
                            double averageWaitingTime,
                            double averageTurnaroundTime,
                            double averageResponseTime) {
        this.timeline = Collections.unmodifiableList(new ArrayList<>(timeline));
        this.averageWaitingTime = averageWaitingTime;
        this.averageTurnaroundTime = averageTurnaroundTime;
        this.averageResponseTime = averageResponseTime;
    }

    public List<ScheduledSlice> getTimeline() {
        return timeline;
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


