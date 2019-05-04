package protocol.down;

public class VoiceRecord {
    // 录音命令 0:停止录音;0x01:开始录音
    private int voiceRecordCommand;
    // 录音时间 单位为秒(s), 0 表示一直录音
    private int voiceRecordTime;
    // 保存标志 0:实时上传;1:保存
    private int saveFlag;
    // 音频采样率 0:8K;1:11K;2:23K;3:32K;其他保留
    private int audioSamplingRate;

    @Override
    public String toString() {
        return "VoiceRecord{" +
                "voiceRecordCommand=" + voiceRecordCommand +
                ", voiceRecordTime=" + voiceRecordTime +
                ", saveFlag=" + saveFlag +
                ", audioSamplingRate=" + audioSamplingRate +
                '}';
    }

    public int getVoiceRecordCommand() {
        return voiceRecordCommand;
    }

    public void setVoiceRecordCommand(int voiceRecordCommand) {
        this.voiceRecordCommand = voiceRecordCommand;
    }

    public int getVoiceRecordTime() {
        return voiceRecordTime;
    }

    public void setVoiceRecordTime(int voiceRecordTime) {
        this.voiceRecordTime = voiceRecordTime;
    }

    public int getSaveFlag() {
        return saveFlag;
    }

    public void setSaveFlag(int saveFlag) {
        this.saveFlag = saveFlag;
    }

    public int getAudioSamplingRate() {
        return audioSamplingRate;
    }

    public void setAudioSamplingRate(int audioSamplingRate) {
        this.audioSamplingRate = audioSamplingRate;
    }
}
