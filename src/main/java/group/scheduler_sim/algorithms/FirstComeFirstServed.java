package group.scheduler_sim.algorithms;

import group.scheduler_sim.model.Process;
import group.scheduler_sim.model.ScheduledSlice;
import group.scheduler_sim.model.SimulationResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Non-preemptive First-Come-First-Served scheduling.
 */
public class FirstComeFirstServed implements SchedulingAlgorithm {

    @Override
    public String getName() {
        return "First Come First Served";
    }

    @Override
    public SimulationResult simulate(List<Process> processes) {
        List<Process> jobs = new ArrayList<>(processes);
        jobs.sort(Comparator.comparingInt(Process::getArrivalTime));

        int time = 0;
        List<ScheduledSlice> timeline = new ArrayList<>();
        Map<String, Integer> firstStart = new HashMap<>();
        Map<String, Integer> completion = new HashMap<>();

        for (Process p : jobs) {
            if (time < p.getArrivalTime()) {
                time = p.getArrivalTime();
            }
            int start = time;
            int end = start + p.getBurstTime();
            timeline.add(new ScheduledSlice(p.getId(), start, end));
            firstStart.putIfAbsent(p.getId(), start);
            completion.put(p.getId(), end);
            time = end;
        }

        // Compute metrics
        double totalWaiting = 0;
        double totalTurnaround = 0;
        double totalResponse = 0;
        int n = jobs.size();
        for (Process p : jobs) {
            int comp = completion.get(p.getId());
            int start = firstStart.get(p.getId());
            int turnaround = comp - p.getArrivalTime();
            int waiting = turnaround - p.getBurstTime();
            int response = start - p.getArrivalTime();
            totalWaiting += waiting;
            totalTurnaround += turnaround;
            totalResponse += response;
        }

        return new SimulationResult(
                timeline,
                n == 0 ? 0 : totalWaiting / n,
                n == 0 ? 0 : totalTurnaround / n,
                n == 0 ? 0 : totalResponse / n
        );
    }
}


