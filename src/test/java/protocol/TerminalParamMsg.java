package protocol;

public class TerminalParamMsg {
    private int paramId;
    private int paramLength;
    private TerminalParam param;

    public int getParamId() {
        return paramId;
    }

    public void setParamId(int paramId) {
        this.paramId = paramId;
    }

    public int getParamLength() {
        return paramLength;
    }

    public void setParamLength(int paramLength) {
        this.paramLength = paramLength;
    }

    public TerminalParam getParam() {
        return param;
    }

    public void setParam(TerminalParam param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "[paramId=" + paramId + " paramLength=" + paramLength + " param=" + param +"]";
    }
}
