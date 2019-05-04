package protocol.down;

public class TempLocationTrackMsg {
    // 时间间隔
    private int interval;
    // 位置跟踪有效期
    private int locationTrackValidTerm;

    @Override
    public String toString() {
        return "TempLocationTrackMsg{" +
                "interval=" + interval +
                ", locationTrackValidTerm=" + locationTrackValidTerm +
                '}';
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getLocationTrackValidTerm() {
        return locationTrackValidTerm;
    }

    public void setLocationTrackValidTerm(int locationTrackValidTerm) {
        this.locationTrackValidTerm = locationTrackValidTerm;
    }
}
