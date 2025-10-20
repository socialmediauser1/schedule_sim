package group.scheduler_sim.engine;

import group.scheduler_sim.algorithms.SchedulingAlgorithm;
import group.scheduler_sim.model.Process;
import group.scheduler_sim.model.SimulationResult;

import java.util.List;

public class Simulator {
    private final SchedulingAlgorithm algorithm;

    public Simulator(SchedulingAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public SimulationResult run(List<Process> processes) {
        return algorithm.simulate(processes);
    }

    public String getAlgorithmName() {
        return algorithm.getName();
    }
}


