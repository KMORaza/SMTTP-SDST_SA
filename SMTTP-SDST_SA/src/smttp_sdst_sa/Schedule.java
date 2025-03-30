package smttp_sdst_sa;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class Schedule {
    private List<Job> jobs;
    private List<MachineBreakdown> breakdowns;
    private double weightedTardiness;
    public Schedule(List<Job> jobs, List<MachineBreakdown> breakdowns) {
        this.jobs = new ArrayList<>(jobs);
        this.breakdowns = new ArrayList<>(breakdowns);
        calculateWeightedTardiness();
    }
    public Schedule(Schedule other) {
        this.jobs = new ArrayList<>(other.jobs);
        this.breakdowns = new ArrayList<>(other.breakdowns);
        this.weightedTardiness = other.weightedTardiness;
    }
    public void calculateWeightedTardiness() {
        weightedTardiness = 0;
        int currentTime = 0;
        Job previousJob = null;
        for (Job job : jobs) {
            currentTime = Math.max(currentTime, job.getReleaseTime());
            if (previousJob != null) {
                currentTime += previousJob.getSetupTime(job.getId());
            }
            currentTime = skipBreakdowns(currentTime);
            currentTime += job.getProcessingTime();
            int tardiness = Math.max(0, currentTime - job.getDueDate());
            weightedTardiness += tardiness * job.getWeight();
            previousJob = job;
        }
    }
    private int skipBreakdowns(int time) {
        for (MachineBreakdown breakdown : breakdowns) {
            if (breakdown.isDuringBreakdown(time)) {
                time = breakdown.getEndTime();
            }
        }
        return time;
    }
    public boolean isFeasible() {
        for (int i = 0; i < jobs.size(); i++) {
            Job job = jobs.get(i);
            for (int predId : job.getPredecessors()) {
                boolean found = false;
                for (int j = 0; j < i; j++) {
                    if (jobs.get(j).getId() == predId) {
                        found = true;
                        break;
                    }
                }
                if (!found) return false;
            }
        }
        return true;
    }
    public void swapJobs(int i, int j) {
        Collections.swap(jobs, i, j);
        calculateWeightedTardiness();
    }
    public double getWeightedTardiness() {
        return weightedTardiness;
    }
    public List<Job> getJobs() {
        return new ArrayList<>(jobs);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Schedule (Weighted Tardiness=").append(String.format("%.2f", weightedTardiness)).append("): ");
        for (Job job : jobs) {
            sb.append(job.getId()).append(" ");
        }
        return sb.toString();
    }
}