package protocol.down;

public class TextMsg {
    // 标志
    private int flag;
    // 文本信息
    private String textMsg;

    @Override
    public String toString() {
        return "TextMsg{" +
                "flag=" + flag +
                ", textMsg='" + textMsg + '\'' +
                '}';
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getTextMsg() {
        return textMsg;
    }

    public void setTextMsg(String textMsg) {
        this.textMsg = textMsg;
    }
}
