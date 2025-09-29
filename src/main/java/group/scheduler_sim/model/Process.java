package group.scheduler_sim.model;

import java.util.Objects;

/**
 * Represents a single CPU-bound process/job arriving to the ready queue.
 */
public class Process {
    private final String id;
    private final int arrivalTime; // time when process arrives
    private final int burstTime;   // total CPU time required

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


