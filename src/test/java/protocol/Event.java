package protocol;

public class Event {
    // 事件 ID
    private int eventId;
    // 事件内容长度
    private int eventLength;
    // 事件内容
    private String event;

    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", eventLength=" + eventLength +
                ", event='" + event + '\'' +
                '}';
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getEventLength() {
        return eventLength;
    }

    public void setEventLength(int eventLength) {
        this.eventLength = eventLength;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
    private String terminal;
}
