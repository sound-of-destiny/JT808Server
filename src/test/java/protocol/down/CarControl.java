package protocol.down;

public class CarControl {
    // 控制标志
    private int controlFlag;

    @Override
    public String toString() {
        return "CarControl{" +
                "controlFlag=" + controlFlag +
                '}';
    }

    public int setControlFlag() {
        return controlFlag;
    }

    public void setControlFlag(int controlFlag) {
        this.controlFlag = controlFlag;
    }
}
