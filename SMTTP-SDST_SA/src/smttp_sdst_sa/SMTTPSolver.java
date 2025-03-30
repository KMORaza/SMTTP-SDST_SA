package smttp_sdst_sa;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class SMTTPSolver {
    private List<Job> jobs;
    private List<MachineBreakdown> breakdowns;
    private Random random;
    public SMTTPSolver(int numJobs) {
        this.jobs = new ArrayList<>();
        this.breakdowns = new ArrayList<>();
        this.random = new Random();
        initializeProblem(numJobs);
    }
    private void initializeProblem(int numJobs) {
        for (int i = 0; i < numJobs; i++) {
            int processingTime = random.nextInt(20) + 1;
            int dueDate = random.nextInt(50) + 20;
            int releaseTime = random.nextInt(10);
            double weight = random.nextDouble() * 2 + 1;
            jobs.add(new Job(i, processingTime, dueDate, releaseTime, weight, numJobs));
        }
        for (int i = 0; i < numJobs; i++) {
            for (int j = 0; j < numJobs; j++) {
                if (i != j) {
                    jobs.get(i).setSetupTime(j, random.nextInt(10) + 1);
                }
            }
        }
        for (int i = 0; i < numJobs / 2; i++) {
            int from = random.nextInt(numJobs);
            int to = random.nextInt(numJobs);
            if (from < to) jobs.get(to).addPredecessor(from);
        }
        int currentTime = 0;
        for (int i = 0; i < 2; i++) {
            currentTime += random.nextInt(20);
            int duration = random.nextInt(10) + 5;
            breakdowns.add(new MachineBreakdown(currentTime, currentTime + duration));
        }
    }
    public void solve() {
        double initialTemp = 1000.0;
        double coolingRate = 0.995;
        double minTemp = 1.0;
        int maxIterations = 10000;
        SimulatedAnnealing sa = new SimulatedAnnealing(initialTemp, coolingRate, minTemp, maxIterations);
        Schedule solution = sa.solve(jobs, breakdowns);
        System.out.println("Jobs:");
        for (Job job : jobs) {
            System.out.println(job + ", Preds=" + job.getPredecessors());
        }
        System.out.println("\nBreakdowns:");
        for (MachineBreakdown breakdown : breakdowns) {
            System.out.println(breakdown);
        }
        System.out.println("\nOptimal Schedule:");
        System.out.println(solution);
        ScheduleVisualizer visualizer = new ScheduleVisualizer(solution, breakdowns);
        visualizer.visualize();
    }
    public static void main(String[] args) {
        SMTTPSolver solver = new SMTTPSolver(10);
        solver.solve();
    }
}