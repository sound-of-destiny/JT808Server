package cn.edu.sdu.JT808Server.protocol.downMsg;

public class TerminalRegisterResponseMsg {

    public static final byte success = 0;
    public static final byte car_already_registered = 1;
    public static final byte car_not_found = 2;
    public static final byte terminal_already_registered = 3;
    public static final byte terminal_not_found = 4;

    /*public TerminalRegisterResponseMsg(TerminalRegisterMsg packageData) {
        this();
        this.checkSum = packageData.getCheckSum();
        this.msgBody = packageData.getMsgBody();
        this.msgHeader = packageData.getMsgHeader();
    }*/

    // 应答流水号 byte[0-1] 对应的终端注册消息的流水号
    private int replyFlowId;

    /**
     * byte[2] 结果(BYTE) <br>
     * 0：成功<br>
     * 1：车辆已被注册<br>
     * 2：数据库中无该车辆<br>
     */
    private byte replyCode;

    // 鉴权码(STRING) byte[3-x] 只有在成功后才有该字段
    private String replyToken;

    public int getReplyFlowId() {
        return replyFlowId;
    }

    public void setReplyFlowId(int flowId) {
        this.replyFlowId = flowId;
    }

    public byte getReplyCode() {
        return replyCode;
    }

    public void setReplyCode(byte code) {
        this.replyCode = code;
    }

    public String getReplyToken() {
        return replyToken;
    }

    public void setReplyToken(String token) {
        this.replyToken = token;
    }

    /*public MsgHeader getMsgHeader() {
        return this.msgHeader;
    }*/

    @Override
    public String toString() {
        return "[replyFlowId=" + replyFlowId + ", replyCode=" + replyCode + ", replyToken=" + replyToken + "]";
    }

}
