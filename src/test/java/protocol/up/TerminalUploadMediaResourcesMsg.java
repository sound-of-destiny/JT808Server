package protocol.up;

import cn.edu.sdu.JT808Server.protocol.PackageData;
import protocol.MediaResources;

import java.util.ArrayList;

public class TerminalUploadMediaResourcesMsg extends PackageData {
    // 流水号
    private int replyFlowId;
    // 音视频资源总数
    private int mediaResourcesNum;
    // 音视频资源列表
    private ArrayList<MediaResources> mediaResourcesList;

    @Override
    public String toString() {
        return "TerminalUploadMediaResourcesMsg{" +
                "replyFlowId=" + replyFlowId +
                ", mediaResourcesNum=" + mediaResourcesNum +
                ", mediaResourcesList=" + mediaResourcesList +
                '}';
    }

    public int getReplyFlowId() {
        return replyFlowId;
    }

    public void setReplyFlowId(int replyFlowId) {
        this.replyFlowId = replyFlowId;
    }

    public int getMediaResourcesNum() {
        return mediaResourcesNum;
    }

    public void setMediaResourcesNum(int mediaResourcesNum) {
        this.mediaResourcesNum = mediaResourcesNum;
    }

    public ArrayList<MediaResources> getMediaResourcesList() {
        return mediaResourcesList;
    }

    public void setMediaResourcesList(ArrayList<MediaResources> mediaResourcesList) {
        this.mediaResourcesList = mediaResourcesList;
    }

    public TerminalUploadMediaResourcesMsg() {}

    public TerminalUploadMediaResourcesMsg(PackageData packageData) {
        this.checkSum = packageData.getCheckSum();
        this.msgBody = packageData.getMsgBody();
        this.msgHeader = packageData.getMsgHeader();
    }
}
