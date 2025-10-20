package group.scheduler_sim.model;

public class ScheduledSlice {
    private final String processId;
    private final int startTime;
    private final int endTime;

    public ScheduledSlice(String processId, int startTime, int endTime) {
        if (startTime < 0 || endTime <= startTime) {
            throw new IllegalArgumentException("Invalid slice times");
        }
        this.processId = processId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getProcessId() {
        return processId;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }
}


