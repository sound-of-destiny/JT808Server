package protocol.up;

import cn.edu.sdu.JT808Server.protobuf.ServerData;
import cn.edu.sdu.JT808Server.protocol.MsgHeader;
import cn.edu.sdu.JT808Server.protocol.PackageData;

import java.util.Arrays;

public class TerminalMediaDataMsg extends PackageData {
    // 多媒体 ID
    private int mediaId;
    // 多媒体类型 0:图像;1:音频;2:视频
    private int mediaType;
    // 多媒体格式编码 0:JPEG;1:TIF;2:MP3;3:WAV;4:WMV
    private int mediaCode;
    // 事件项编码 0:平台下发指令;1:定时动作;2:抢劫报警触发;3:碰撞侧翻报警触发;其他保留
    private int eventCode;
    // 通道 ID
    private int channelId;
    // 位置信息汇报
    private ServerData.TerminalLocationMsg terminalLocationMsg;
    // 多媒体数据包
    private byte[] MediaData;

    public TerminalMediaDataMsg(){}

    public TerminalMediaDataMsg(PackageData packageData) {
        this.checkSum = packageData.getCheckSum();
        this.msgBody = packageData.getMsgBody();
        this.msgHeader = packageData.getMsgHeader();
    }

    @Override
    public String toString() {
        return "TerminalMediaDataMsg{" +
                "mediaId=" + mediaId +
                ", mediaType=" + mediaType +
                ", mediaCode=" + mediaCode +
                ", eventCode=" + eventCode +
                ", channelId=" + channelId +
                ", terminalLocationMsg=" + terminalLocationMsg +
                ", MediaData=" + Arrays.toString(MediaData) +
                '}';
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
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

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public ServerData.TerminalLocationMsg getTerminalLocationMsg() {
        return terminalLocationMsg;
    }

    public void setTerminalLocationMsg(ServerData.TerminalLocationMsg terminalLocationMsg) {
        this.terminalLocationMsg = terminalLocationMsg;
    }

    public byte[] getMediaData() {
        return MediaData;
    }

    public void setMediaData(byte[] mediaData) {
        MediaData = mediaData;
    }

    @Override
    public MsgHeader getMsgHeader() {
        return msgHeader;
    }

}
