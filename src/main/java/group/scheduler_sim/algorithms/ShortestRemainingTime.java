package group.scheduler_sim.algorithms;

import group.scheduler_sim.model.Process;
import group.scheduler_sim.model.ScheduledSlice;
import group.scheduler_sim.model.SimulationResult;

import java.util.*;

public class ShortestRemainingTime implements SchedulingAlgorithm {

    @Override
    public String getName() {
        return "Shortest Remaining Time";
    }

    @Override
    public SimulationResult simulate(List<Process> processes) {
        if (processes.isEmpty()) {
            return new SimulationResult(new ArrayList<>(), 0, 0, 0);
        }

        List<ProcessWithRemaining> processesWithRemaining = new ArrayList<>();
        for (Process p : processes) {
            processesWithRemaining.add(new ProcessWithRemaining(p));
        }

        processesWithRemaining.sort(Comparator.comparingInt(p -> p.process.getArrivalTime()));

        List<ScheduledSlice> timeline = new ArrayList<>();
        Map<String, Integer> firstStart = new HashMap<>();
        Map<String, Integer> completion = new HashMap<>();
        PriorityQueue<ProcessWithRemaining> readyQueue = new PriorityQueue<>(
                Comparator.comparingInt((ProcessWithRemaining p) -> p.remainingTime)
                        .thenComparingInt(p -> p.process.getArrivalTime())
                        .thenComparing(p -> p.process.getId())
        );

        int currentTime = 0;
        ProcessWithRemaining currentProcess = null;
        int processIndex = 0;

        while (true) {
            while (processIndex < processesWithRemaining.size()) {
                ProcessWithRemaining p = processesWithRemaining.get(processIndex);
                if (p.process.getArrivalTime() <= currentTime) {
                    readyQueue.offer(p);
                    processIndex++;
                } else {
                    break;
                }
            }

            if (currentProcess != null && !readyQueue.isEmpty()) {
                ProcessWithRemaining shortest = readyQueue.peek();
                if (shortest.remainingTime < currentProcess.remainingTime) {
                    if (currentTime > currentProcess.lastStartTime) {
                        timeline.add(new ScheduledSlice(currentProcess.process.getId(), 
                                currentProcess.lastStartTime, currentTime));
                    }
                    readyQueue.offer(currentProcess);
                    currentProcess = readyQueue.poll();
                    currentProcess.lastStartTime = currentTime;
                    firstStart.putIfAbsent(currentProcess.process.getId(), currentTime);
                }
            }

            if (currentProcess == null && !readyQueue.isEmpty()) {
                currentProcess = readyQueue.poll();
                currentProcess.lastStartTime = currentTime;
                firstStart.putIfAbsent(currentProcess.process.getId(), currentTime);
            }

            if (currentProcess == null && processIndex < processesWithRemaining.size()) {
                currentTime = processesWithRemaining.get(processIndex).process.getArrivalTime();
                continue;
            }

            if (currentProcess == null) {
                break;
            }

            currentProcess.remainingTime--;
            currentTime++;

            if (currentProcess.remainingTime == 0) {
                timeline.add(new ScheduledSlice(currentProcess.process.getId(), 
                        currentProcess.lastStartTime, currentTime));
                completion.put(currentProcess.process.getId(), currentTime);
                currentProcess = null;
            }
        }

        double totalWaiting = 0;
        double totalTurnaround = 0;
        double totalResponse = 0;
        int n = processes.size();
        
        for (Process p : processes) {
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

    private static class ProcessWithRemaining {
        final Process process;
        int remainingTime;
        int lastStartTime;

        ProcessWithRemaining(Process process) {
            this.process = process;
            this.remainingTime = process.getBurstTime();
            this.lastStartTime = -1;
        }
    }
}
