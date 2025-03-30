package smttp_sdst_sa;
import java.util.HashSet;
import java.util.Set;
public class Job {
    private int id;
    private int processingTime;
    private int dueDate;
    private int releaseTime;
    private double weight;  
    private int[][] setupTimes;
    private Set<Integer> predecessors;  
    public Job(int id, int processingTime, int dueDate, int releaseTime, double weight, int numJobs) {
        this.id = id;
        this.processingTime = processingTime;
        this.dueDate = dueDate;
        this.releaseTime = releaseTime;
        this.weight = weight;
        this.setupTimes = new int[numJobs][numJobs];
        this.predecessors = new HashSet<>();
    }
    public int getId() { return id; }
    public int getProcessingTime() { return processingTime; }
    public int getDueDate() { return dueDate; }
    public int getReleaseTime() { return releaseTime; }
    public double getWeight() { return weight; }
    public int getSetupTime(int toJobId) { return setupTimes[id][toJobId]; }
    public Set<Integer> getPredecessors() { return new HashSet<>(predecessors); }
    public void setSetupTime(int toJobId, int time) {
        setupTimes[id][toJobId] = time;
    }
    public void addPredecessor(int predId) {
        predecessors.add(predId);
    }
    @Override
    public String toString() {
        return "Job " + id + " (p=" + processingTime + ", d=" + dueDate + 
               ", r=" + releaseTime + ", w=" + weight + ")";
    }
}