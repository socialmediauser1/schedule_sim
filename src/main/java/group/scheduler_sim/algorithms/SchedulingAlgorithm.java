package group.scheduler_sim.algorithms;

import group.scheduler_sim.model.Process;
import group.scheduler_sim.model.SimulationResult;

import java.util.List;

/**
 * Scheduling algorithm interface.
 */
public interface SchedulingAlgorithm {
    String getName();

    /**
     * Run the algorithm producing a full timeline and metrics.
     * Implementations must not mutate the input list.
     */
    SimulationResult simulate(List<Process> processes);
}


