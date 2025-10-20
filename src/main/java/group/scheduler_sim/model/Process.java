package group.scheduler_sim.model;

import java.util.Objects;

public class Process {
    private final String id;
    private final int arrivalTime;
    private final int burstTime;

    public Process(String id, int arrivalTime, int burstTime) {
        if (arrivalTime < 0 || burstTime <= 0) {
            throw new IllegalArgumentException("Invalid arrival or burst time");
        }
        this.id = Objects.requireNonNull(id, "id");
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
    }

    public String getId() {
        return id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }
}


