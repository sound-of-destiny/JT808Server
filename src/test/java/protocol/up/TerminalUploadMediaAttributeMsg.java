package protocol.up;

import cn.edu.sdu.JT808Server.protocol.PackageData;

public class TerminalUploadMediaAttributeMsg extends PackageData {
    // 输入音频编码方式
    private int voiceCode;
    // 输入音频声道数
    private int voiceChannelNum;
    // 输入音频采样率
    private int voiceSamplingRate;
    // 输入音频采样位数
    private int voiceSamplingBit;
    // 音频帧长度
    private int voiceFrameLength;
    // 是否支持音频输出
    private int voiceOutput;
    // 视频编码方式
    private int videoCode;
    // 终端支持的最大音频物理通道数
    private int maxVoiceChannelNum;
    // 终端支持的最大视频物理通道数
    private int maxVideoChannelNum;

    @Override
    public String toString() {
        return "TerminalUploadMediaAttributeMsg{" +
                "voiceCode=" + voiceCode +
                ", voiceChannelNum=" + voiceChannelNum +
                ", voiceSamplingRate=" + voiceSamplingRate +
                ", voiceSamplingBit=" + voiceSamplingBit +
                ", voiceFrameLength=" + voiceFrameLength +
                ", voiceOutput=" + voiceOutput +
                ", videoCode=" + videoCode +
                ", maxVoiceChannelNum=" + maxVoiceChannelNum +
                ", maxVideoChannelNum=" + maxVideoChannelNum +
                '}';
    }

    public int getVoiceCode() {
        return voiceCode;
    }

    public void setVoiceCode(int voiceCode) {
        this.voiceCode = voiceCode;
    }

    public int getVoiceChannelNum() {
        return voiceChannelNum;
    }

    public void setVoiceChannelNum(int voiceChannelNum) {
        this.voiceChannelNum = voiceChannelNum;
    }

    public int getVoiceSamplingRate() {
        return voiceSamplingRate;
    }

    public void setVoiceSamplingRate(int voiceSamplingRate) {
        this.voiceSamplingRate = voiceSamplingRate;
    }

    public int getVoiceSamplingBit() {
        return voiceSamplingBit;
    }

    public void setVoiceSamplingBit(int voiceSamplingBit) {
        this.voiceSamplingBit = voiceSamplingBit;
    }

    public int getVoiceFrameLength() {
        return voiceFrameLength;
    }

    public void setVoiceFrameLength(int voiceFrameLength) {
        this.voiceFrameLength = voiceFrameLength;
    }

    public int getVoiceOutput() {
        return voiceOutput;
    }

    public void setVoiceOutput(int voiceOutput) {
        this.voiceOutput = voiceOutput;
    }

    public int getVideoCode() {
        return videoCode;
    }

    public void setVideoCode(int videoCode) {
        this.videoCode = videoCode;
    }

    public int getMaxVoiceChannelNum() {
        return maxVoiceChannelNum;
    }

    public void setMaxVoiceChannelNum(int maxVoiceChannelNum) {
        this.maxVoiceChannelNum = maxVoiceChannelNum;
    }

    public int getMaxVideoChannelNum() {
        return maxVideoChannelNum;
    }

    public void setMaxVideoChannelNum(int maxVideoChannelNum) {
        this.maxVideoChannelNum = maxVideoChannelNum;
    }

    public TerminalUploadMediaAttributeMsg() {}

    public TerminalUploadMediaAttributeMsg(PackageData packageData) {
        this.checkSum = packageData.getCheckSum();
        this.msgBody = packageData.getMsgBody();
        this.msgHeader = packageData.getMsgHeader();
    }


}
