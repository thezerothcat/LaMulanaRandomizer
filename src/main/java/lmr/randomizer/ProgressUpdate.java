package lmr.randomizer;

public class ProgressUpdate {
    private long time;
    private int attempt;

    public ProgressUpdate(long time, int attempt) {
        this.time = time;
        this.attempt = attempt;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getAttempt() {
        return attempt;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }
}
