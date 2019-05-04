package cn.edu.sdu.JT808Server.protocol.upMsg;

import cn.edu.sdu.JT808Server.protocol.MsgHeader;
import cn.edu.sdu.JT808Server.protocol.PackageData;

public class TerminalMessageRequestMsg extends PackageData {
    // 信息类型
    private int messageType;
    // 点播/取消标志
    private int messageFlag;

    public int getMessageType() {
        return messageType;
    }

    public TerminalMessageRequestMsg(){}

    public TerminalMessageRequestMsg(PackageData packageData) {
        this.checkSum = packageData.getCheckSum();
        this.msgBody = packageData.getMsgBody();
        this.msgHeader = packageData.getMsgHeader();
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getMessageFlag() {
        return messageFlag;
    }

    public void setMessageFlag(int messageFlag) {
        this.messageFlag = messageFlag;
    }

    @Override
    public MsgHeader getMsgHeader() {
        return msgHeader;
    }
}
