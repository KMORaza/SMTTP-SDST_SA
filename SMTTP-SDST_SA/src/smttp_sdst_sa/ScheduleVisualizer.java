package smttp_sdst_sa;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import java.awt.*;
import java.util.List;
public class ScheduleVisualizer {
    private Schedule schedule;
    private List<MachineBreakdown> breakdowns;
    public ScheduleVisualizer(Schedule schedule, List<MachineBreakdown> breakdowns) {
        this.schedule = schedule;
        this.breakdowns = breakdowns;
    }
    public void visualize() {
        TaskSeriesCollection dataset = createDataset();
        JFreeChart chart = ChartFactory.createGanttChart(
            "SMTTP-SDST Schedule",
            "Jobs",
            "Time",
            dataset,
            true,  
            true, 
            false  
        );
        CategoryPlot plot = chart.getCategoryPlot();
        GanttRenderer renderer = (GanttRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(true);
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesPaint(1, Color.GRAY);
        renderer.setSeriesPaint(2, Color.RED);
        ChartFrame frame = new ChartFrame("Schedule Visualization", chart);
        frame.pack();
        frame.setVisible(true);
    }
    private TaskSeriesCollection createDataset() {
        TaskSeries jobSeries = new TaskSeries("Jobs");
        TaskSeries setupSeries = new TaskSeries("Setup Times");
        TaskSeries breakdownSeries = new TaskSeries("Breakdowns");
        int currentTime = 0;
        Job previousJob = null;
        for (Job job : schedule.getJobs()) {
            currentTime = Math.max(currentTime, job.getReleaseTime());
            if (previousJob != null) {
                int setupTime = previousJob.getSetupTime(job.getId());
                if (setupTime > 0) {
                    Task setupTask = new Task("Setup J" + job.getId(),
                        new SimpleTimePeriod(currentTime, currentTime + setupTime));
                    setupSeries.add(setupTask);
                }
                currentTime += setupTime;
            }
            int startTime = currentTime;
            for (MachineBreakdown breakdown : breakdowns) {
                if (breakdown.isDuringBreakdown(currentTime)) {
                    currentTime = breakdown.getEndTime();
                }
            }
            Task jobTask = new Task("J" + job.getId(),
                new SimpleTimePeriod(currentTime, currentTime + job.getProcessingTime()));
            jobSeries.add(jobTask);
            currentTime += job.getProcessingTime();
            previousJob = job;
        }
        for (MachineBreakdown breakdown : breakdowns) {
            Task breakdownTask = new Task("Breakdown",
                new SimpleTimePeriod(breakdown.getStartTime(), breakdown.getEndTime()));
            breakdownSeries.add(breakdownTask);
        }
        TaskSeriesCollection collection = new TaskSeriesCollection();
        collection.add(jobSeries);
        collection.add(setupSeries);
        collection.add(breakdownSeries);
        return collection;
    }
}