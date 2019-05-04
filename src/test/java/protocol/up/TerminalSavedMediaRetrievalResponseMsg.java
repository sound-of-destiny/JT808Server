package protocol.up;

import cn.edu.sdu.JT808Server.protocol.MsgHeader;
import cn.edu.sdu.JT808Server.protocol.PackageData;

import java.util.ArrayList;

public class TerminalSavedMediaRetrievalResponseMsg extends PackageData {
    // 应答流水号 对应的多媒体数据检索消息的流水号
    private int replyFlowId;
    // 多媒体数据总项数 满足检索条件的多媒体数据总项数
    private int mediaRetrievalNum;
    // 检索项
    private ArrayList<TerminalRetrievalDataMsg> terminalRetrievalDataMsgs;

    public TerminalSavedMediaRetrievalResponseMsg(){}

    public TerminalSavedMediaRetrievalResponseMsg(PackageData packageData) {
        this.checkSum = packageData.getCheckSum();
        this.msgBody = packageData.getMsgBody();
        this.msgHeader = packageData.getMsgHeader();
    }

    @Override
    public String toString() {
        return "TerminalSavedMediaRetrievalResponseMsg{" +
                "replyFlowId=" + replyFlowId +
                ", mediaRetrievalNum=" + mediaRetrievalNum +
                ", terminalRetrievalDataMsgs=" + terminalRetrievalDataMsgs +
                '}';
    }

    public int getReplyFlowId() {
        return replyFlowId;
    }

    public void setReplyFlowId(int replyFlowId) {
        this.replyFlowId = replyFlowId;
    }

    public int getMediaRetrievalNum() {
        return mediaRetrievalNum;
    }

    public void setMediaRetrievalNum(int mediaRetrievalNum) {
        this.mediaRetrievalNum = mediaRetrievalNum;
    }

    public ArrayList<TerminalRetrievalDataMsg> getTerminalRetrievalDataMsgs() {
        return terminalRetrievalDataMsgs;
    }

    public void setTerminalRetrievalDataMsgs(ArrayList<TerminalRetrievalDataMsg> terminalRetrievalDataMsgs) {
        this.terminalRetrievalDataMsgs = terminalRetrievalDataMsgs;
    }

    @Override
    public MsgHeader getMsgHeader() {
        return msgHeader;
    }

}
