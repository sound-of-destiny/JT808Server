package protocol.up;

import cn.edu.sdu.JT808Server.protocol.MsgHeader;
import cn.edu.sdu.JT808Server.protocol.PackageData;

import java.util.ArrayList;

public class TerminalCameraPhotoResponseMsg extends PackageData {
    // 应答流水号 对应平台摄像头立即拍摄命令的消息流水号
    private int replyFlowId;
    // 结果 0:成功;1:失败;2:通道不支持 以下字段在结果=0 时才有效
    private int result;
    // 多媒体 ID 个数 拍摄成功的多媒体个数
    private int mediaIdNum;
    // 多媒体 ID 列表
    private ArrayList<Integer> mediaIdList;

    public TerminalCameraPhotoResponseMsg(){}

    public TerminalCameraPhotoResponseMsg(PackageData packageData) {
        this.checkSum = packageData.getCheckSum();
        this.msgBody = packageData.getMsgBody();
        this.msgHeader = packageData.getMsgHeader();
    }

    @Override
    public String toString() {
        return "TerminalCameraPhotoResponseMsg{" +
                "replyFlowId=" + replyFlowId +
                ", result=" + result +
                ", mediaIdNum=" + mediaIdNum +
                ", mediaIdList=" + mediaIdList +
                '}';
    }

    public int getReplyFlowId() {
        return replyFlowId;
    }

    public void setReplyFlowId(int replyFlowId) {
        this.replyFlowId = replyFlowId;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getMediaIdNum() {
        return mediaIdNum;
    }

    public void setMediaIdNum(int mediaIdNum) {
        this.mediaIdNum = mediaIdNum;
    }

    public ArrayList<Integer> getMediaIdList() {
        return mediaIdList;
    }

    public void setMediaIdList(ArrayList<Integer> mediaIdList) {
        this.mediaIdList = mediaIdList;
    }

    @Override
    public MsgHeader getMsgHeader() {
        return msgHeader;
    }

}
