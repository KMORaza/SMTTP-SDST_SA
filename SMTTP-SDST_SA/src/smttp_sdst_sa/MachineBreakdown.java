package smttp_sdst_sa;
public class MachineBreakdown {
    private int startTime;
    private int endTime;
    public MachineBreakdown(int startTime, int endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public int getStartTime() { return startTime; }
    public int getEndTime() { return endTime; }

    public boolean isDuringBreakdown(int time) {
        return time >= startTime && time < endTime;
    }
    @Override
    public String toString() {
        return "Breakdown [" + startTime + "-" + endTime + "]";
    }
}