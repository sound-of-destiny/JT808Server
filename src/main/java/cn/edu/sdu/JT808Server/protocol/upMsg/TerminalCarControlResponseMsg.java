package cn.edu.sdu.JT808Server.protocol.upMsg;

import cn.edu.sdu.JT808Server.protobuf.ServerData;
import cn.edu.sdu.JT808Server.protocol.MsgHeader;
import cn.edu.sdu.JT808Server.protocol.PackageData;

public class TerminalCarControlResponseMsg extends PackageData {
    private int replyFlowId;
    private ServerData.TerminalLocationMsg terminalLocationMsg;

    public int getReplyFlowId() {
        return replyFlowId;
    }

    public TerminalCarControlResponseMsg(){}

    public TerminalCarControlResponseMsg(PackageData packageData) {
        this.checkSum = packageData.getCheckSum();
        this.msgBody = packageData.getMsgBody();
        this.msgHeader = packageData.getMsgHeader();
    }

    @Override
    public String toString() {
        return "TerminalCarControlResponseMsg{" +
                "replyFlowId=" + replyFlowId +
                ", terminalLocationMsg=" + terminalLocationMsg +
                '}';
    }

    public void setReplyFlowId(int replyFlowId) {
        this.replyFlowId = replyFlowId;
    }

    public ServerData.TerminalLocationMsg getTerminalLocationMsg() {
        return terminalLocationMsg;
    }

    public void setTerminalLocationMsg(ServerData.TerminalLocationMsg terminalLocationMsg) {
        this.terminalLocationMsg = terminalLocationMsg;
    }

    public MsgHeader getMsgHeader() {
        return msgHeader;
    }
}
