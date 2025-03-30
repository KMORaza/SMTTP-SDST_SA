package smttp_sdst_sa;
import java.util.List;
import java.util.Random;
public class SimulatedAnnealing {
    private double initialTemperature;
    private double coolingRate;
    private double minTemperature;
    private Random random;
    private int maxIterations;
    public SimulatedAnnealing(double initialTemperature, double coolingRate, 
                            double minTemperature, int maxIterations) {
        this.initialTemperature = initialTemperature;
        this.coolingRate = coolingRate;
        this.minTemperature = minTemperature;
        this.maxIterations = maxIterations;
        this.random = new Random();
    }
    public Schedule solve(List<Job> jobs, List<MachineBreakdown> breakdowns) {
        Schedule currentSolution = new Schedule(jobs, breakdowns);
        Schedule bestSolution = new Schedule(currentSolution);
        double temperature = initialTemperature;
        int iteration = 0;
        while (temperature > minTemperature && iteration < maxIterations) {
            Schedule newSolution = generateFeasibleNeighbor(currentSolution);
            if (!newSolution.isFeasible()) continue;
            double currentCost = currentSolution.getWeightedTardiness();
            double newCost = newSolution.getWeightedTardiness();
            double delta = newCost - currentCost;
            if (delta < 0 || acceptWorseSolution(delta, temperature)) {
                currentSolution = new Schedule(newSolution);
                if (newCost < bestSolution.getWeightedTardiness()) {
                    bestSolution = new Schedule(newSolution);
                }
            }
            temperature *= coolingRate;
            iteration++;
        }
        return bestSolution;
    }
    private Schedule generateFeasibleNeighbor(Schedule current) {
        Schedule neighbor = new Schedule(current);
        int attempts = 0;
        do {
            int i = random.nextInt(neighbor.getJobs().size());
            int j = random.nextInt(neighbor.getJobs().size());
            while (i == j) {
                j = random.nextInt(neighbor.getJobs().size());
            }
            neighbor.swapJobs(i, j);
            attempts++;
            if (attempts > 10) break; 
        } while (!neighbor.isFeasible());
        return neighbor;
    }
    private boolean acceptWorseSolution(double delta, double temperature) {
        if (delta <= 0) return true;
        double probability = Math.exp(-delta / temperature);
        return random.nextDouble() < probability;
    }
}