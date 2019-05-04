package protocol.up;

import cn.edu.sdu.JT808Server.protocol.MsgHeader;
import cn.edu.sdu.JT808Server.protocol.PackageData;
import protocol.TerminalParam;

public class TerminalParamQueryResponseMsg extends PackageData {

    private int replyFlowId;
    private int replyParamNumber;

    private TerminalParam terminalParam;

    public TerminalParamQueryResponseMsg(PackageData packageData){
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

    public int getReplyParamNumber() {
        return replyParamNumber;
    }

    public void setReplyParamNumber(int replyParamNumber) {
        this.replyParamNumber = replyParamNumber;
    }

    public TerminalParam getTerminalParam() {
        return terminalParam;
    }

    public void setTerminalParam(TerminalParam terminalParam) {
        this.terminalParam = terminalParam;
    }

    public MsgHeader getMsgHeader() {
        return msgHeader;
    }

    @Override
    public String toString() {
        return "[replyFlowId=" + replyFlowId + " replyParamNumber" + replyParamNumber + "]";
    }
}
