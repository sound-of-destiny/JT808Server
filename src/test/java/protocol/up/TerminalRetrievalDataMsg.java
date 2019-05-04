package protocol.up;

import cn.edu.sdu.JT808Server.protobuf.ServerData;

public class TerminalRetrievalDataMsg {
    // 多媒体 ID
    private int mediaId;
    // 多媒体类型 0:图像;1:音频;2:视频
    private int mediaType;
    // 事件项编码 0:平台下发指令;1:定时动作;2:抢劫报警触发;3:碰撞侧翻报警触发;其他保留
    private int eventCode;
    // 通道 ID
    private int passagewayId;
    // 位置信息汇报
    private ServerData.TerminalLocationMsg terminalLocationMsg;

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

    public int getEventCode() {
        return eventCode;
    }

    public void setEventCode(int eventCode) {
        this.eventCode = eventCode;
    }

    public int getPassagewayId() {
        return passagewayId;
    }

    public void setPassagewayId(int passagewayId) {
        this.passagewayId = passagewayId;
    }

    public ServerData.TerminalLocationMsg getTerminalLocationMsg() {
        return terminalLocationMsg;
    }

    public void setTerminalLocationMsg(ServerData.TerminalLocationMsg terminalLocationMsg) {
        this.terminalLocationMsg = terminalLocationMsg;
    }
}
