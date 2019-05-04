package cn.edu.sdu.JT808Server.protocol.upMsg;

import cn.edu.sdu.JT808Server.protocol.MsgHeader;
import cn.edu.sdu.JT808Server.protocol.PackageData;

public class TerminalMediaEventMsg extends PackageData {
    // 多媒体数据 ID
    private int mediaDataId;
    // 多媒体类型 0:图像;1:音频;2:视频
    private int mediaType;
    // 多媒体格式编码 0:JPEG;1:TIF;2:MP3;3:WAV;4:WMV
    private int mediaCode;
    // 事件项编码 0:平台下发指令;1:定时动作;2:抢劫报警触发;3:碰撞侧翻报警触发;
    // 4:门开拍照;5:门关拍照;6:车门由开变关,时速从<20 公里到超过 20 公里;7:定距拍照
    private int eventCode;
    // 通道 ID
    private int channelId;

    public TerminalMediaEventMsg(){}

    public TerminalMediaEventMsg(PackageData packageData) {
        this.checkSum = packageData.getCheckSum();
        this.msgBody = packageData.getMsgBody();
        this.msgHeader = packageData.getMsgHeader();
    }

    @Override
    public String toString() {
        return "TerminalMediaEventMsg{" +
                "mediaDataId=" + mediaDataId +
                ", mediaType=" + mediaType +
                ", mediaCode=" + mediaCode +
                ", eventCode=" + eventCode +
                ", channelId=" + channelId +
                '}';
    }

    public int getMediaDataId() {
        return mediaDataId;
    }

    public void setMediaDataId(int mediaDataId) {
        this.mediaDataId = mediaDataId;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public int getMediaCode() {
        return mediaCode;
    }

    public void setMediaCode(int mediaCode) {
        this.mediaCode = mediaCode;
    }

    public int getEventCode() {
        return eventCode;
    }

    public void setEventCode(int eventCode) {
        this.eventCode = eventCode;
    }

    public int getChannelIdId() {
        return channelId;
    }

    public void setChannelIdId(int passagewayId) {
        this.channelId = channelId;
    }

    @Override
    public MsgHeader getMsgHeader() {
        return msgHeader;
    }
}
