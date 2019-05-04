package protocol.up;

import cn.edu.sdu.JT808Server.protobuf.ServerData;
import cn.edu.sdu.JT808Server.protocol.MsgHeader;
import cn.edu.sdu.JT808Server.protocol.PackageData;

import java.util.ArrayList;

public class TerminalBulkLocationMsg extends PackageData {
    private int dataNum;
    private int dataType;

    private ArrayList<ServerData.TerminalLocationMsg> terminalLocationMsg;

    public TerminalBulkLocationMsg() {}

    public TerminalBulkLocationMsg(PackageData packageData) {
        this.checkSum = packageData.getCheckSum();
        this.msgBody = packageData.getMsgBody();
        this.msgHeader = packageData.getMsgHeader();
    }

    @Override
    public String toString() {
        return "TerminalBulkLocationMsg{" +
                "dataNum=" + dataNum +
                ", dataType=" + dataType +
                ", terminalLocationMsg=" + terminalLocationMsg +
                '}';
    }

    public int getDataNum() {
        return dataNum;
    }

    public void setDataNum(int dataNum) {
        this.dataNum = dataNum;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public ArrayList<ServerData.TerminalLocationMsg> getTerminalLocationMsg() {
        return terminalLocationMsg;
    }

    public void setTerminalLocationMsg(ArrayList<ServerData.TerminalLocationMsg> terminalLocationMsg) {
        this.terminalLocationMsg = terminalLocationMsg;
    }

    @Override
    public MsgHeader getMsgHeader() {
        return msgHeader;
    }

}
