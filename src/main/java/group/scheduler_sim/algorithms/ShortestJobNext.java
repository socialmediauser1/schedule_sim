package group.scheduler_sim.algorithms;

import group.scheduler_sim.model.Process;
import group.scheduler_sim.model.ScheduledSlice;
import group.scheduler_sim.model.SimulationResult;

import java.util.*;

public class ShortestJobNext implements SchedulingAlgorithm {

    @Override
    public String getName() {
        return "Shortest Job Next";
    }

    @Override
    public SimulationResult simulate(List<Process> processes) {
        List<Process> jobs = new ArrayList<>(processes);
        jobs.sort(Comparator.comparingInt(Process::getArrivalTime));

        int time = 0;
        List<ScheduledSlice> timeline = new ArrayList<>();
        Map<String, Integer> firstStart = new HashMap<>();
        Map<String, Integer> completion = new HashMap<>();
        Set<String> scheduled = new HashSet<>();

        int n = jobs.size();
        while (scheduled.size() < n) {
            List<Process> available = new ArrayList<>();
            for (Process p : jobs) {
                if (!scheduled.contains(p.getId()) && p.getArrivalTime() <= time) {
                    available.add(p);
                }
            }
            if (available.isEmpty()) {
                int nextArrival = Integer.MAX_VALUE;
                for (Process p : jobs) {
                    if (!scheduled.contains(p.getId())) {
                        nextArrival = Math.min(nextArrival, p.getArrivalTime());
                    }
                }
                time = nextArrival;
                continue;
            }
            available.sort(Comparator
                    .comparingInt(Process::getBurstTime)
                    .thenComparingInt(Process::getArrivalTime)
                    .thenComparing(Process::getId));
            Process p = available.get(0);

            int start = Math.max(time, p.getArrivalTime());
            int end = start + p.getBurstTime();
            timeline.add(new ScheduledSlice(p.getId(), start, end));
            firstStart.putIfAbsent(p.getId(), start);
            completion.put(p.getId(), end);
            scheduled.add(p.getId());
            time = end;
        }

        double totalWaiting = 0;
        double totalTurnaround = 0;
        double totalResponse = 0;
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
