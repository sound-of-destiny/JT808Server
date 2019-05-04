package cn.edu.sdu.JT808Server.protocol.upMsg;

import cn.edu.sdu.JT808Server.protocol.PackageData;

public class TerminalCommonResponseMsg extends PackageData {

    public enum replyCode {
        SUCCESS,
        FAILURE,
        MSG_ERROR,
        UNSUPPORTED
    }

    public static final int success = 0;
    public static final int failure = 1;
    public static final int msg_error = 2;
    public static final int unsupported = 3;

    // 应答流水号 byte[0-1] 对应的终端消息的流水号
    private int replyFlowId;

    // 应答ID byte[2-3] 对应的终端消息的ID
    private int replyId;

    /**
     * 0：成功∕确认<br>
     * 1：失败<br>
     * 2：消息有误<br>
     * 3：不支持<br>
     */
    private int replyCode;

    public TerminalCommonResponseMsg(PackageData packageData) {
        this.checkSum = packageData.getCheckSum();
        this.msgBody = packageData.getMsgBody();
        this.msgHeader = packageData.getMsgHeader();
    }

    

    public int getReplyFlowId() {
        return replyFlowId;
    }

    public void setReplyFlowId(int replyFlowId) {
        this.replyFlowId = replyFlowId;
    }

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public int getReplyCode() {
        return replyCode;
    }

    public void setReplyCode(int replyCode) {
        this.replyCode = replyCode;
    }

    @Override
    public String toString() {
        return "[replyFlowId=" + replyFlowId + ", replyId=" + replyId + ", replyCode=" + replyCode + "]";
    }
}
