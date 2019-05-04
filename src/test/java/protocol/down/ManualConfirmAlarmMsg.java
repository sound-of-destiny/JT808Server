package protocol.down;

public class ManualConfirmAlarmMsg {
    // 报警消息流水号
    private int alarmFlowId;
    // 人工确认报警类型
    private int manualConfirmAlarmType;

    @Override
    public String toString() {
        return "ManualConfirmAlarmMsg{" +
                "alarmFlowId=" + alarmFlowId +
                ", manualConfirmAlarmType=" + manualConfirmAlarmType +
                '}';
    }

    public int getAlarmFlowId() {
        return alarmFlowId;
    }

    public void setAlarmFlowId(int alarmFlowId) {
        this.alarmFlowId = alarmFlowId;
    }

    public int getManualConfirmAlarmType() {
        return manualConfirmAlarmType;
    }

    public void setManualConfirmAlarmType(int manualConfirmAlarmType) {
        this.manualConfirmAlarmType = manualConfirmAlarmType;
    }
}
