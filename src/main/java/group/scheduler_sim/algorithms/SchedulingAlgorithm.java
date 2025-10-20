package group.scheduler_sim.algorithms;

import group.scheduler_sim.model.Process;
import group.scheduler_sim.model.SimulationResult;

import java.util.List;

public interface SchedulingAlgorithm {
    String getName();

    SimulationResult simulate(List<Process> processes);
}


